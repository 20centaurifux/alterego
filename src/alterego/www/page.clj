(ns alterego.www.page
  (:gen-class)
  (:require [confick.core :as config]
            [clojure.core.cache :as cache]
            [alterego.www.template :as tpl]
            [clojure.data.json :as json]
            [ring.util.response :as r])
  (:import java.io.FileNotFoundException))

(defonce ^:private page-cache
  (config/bind [^{:default 5000} ttl [:www :cache-millis]]
    (atom (cache/ttl-cache-factory {} :ttl ttl))))

(defmacro ^:private defpage
  [name bindings & body]
  `(defn ~name
     ~bindings
     (let [k# (clojure.string/join ":"
                                   (cons ~(str name) ~bindings))]
       (if-let [page# (cache/lookup @page-cache k#)]
         page#
         (let [content# (do ~@body)]
           (swap! page-cache assoc k# content#)
           content#)))))

(defn- join-path
  [col]
  (str
   (apply clojure.java.io/file col)))

(defn- read-file
  [& parts]
  (config/bind [^:required base-dir [:www :private-dir]]
    (slurp (join-path (cons base-dir parts)))))

(defpage not-found
  []
  (tpl/subsite-from-md
   "Page not found"
   (read-file "md" "404.md")))

(defn- page-parts-from-json
  [filename]
  (map #(case (:type %)
          "markdown" (merge
                      {:type :markdown :md (read-file "md/" (:md %))}
                      (select-keys % [:title :no-title?]))
          "projects" (update % :type keyword))
       (json/read-str (read-file filename) :key-fn keyword)))

(defpage index
  []
  (tpl/index (page-parts-from-json "index.json")))

(defn- project-filename
  [project-name]
  (-> project-name
      clojure.string/lower-case
      (str ".md")))

(defpage project
  [project-name]
  (config/bind [^:required github-account [:github :account]]
    (try
      (let [md (read-file "md"
                          "projects"
                           (project-filename project-name))]
        (tpl/github-project (str "Project: " project-name)
                             project-name
                             github-account
                             md))
      (catch FileNotFoundException _ (not-found)))))

(defpage from-markdown
  [location title filename]
  (tpl/subsite-from-md title (read-file "md" filename)))

(defn ip
  [req]
  (-> (r/response (:client-ip req))
      (r/header "Content-Type" "text/plain; charset=utf-8")))
