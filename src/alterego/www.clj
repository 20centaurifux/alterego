(ns alterego.www
  (:require [alterego.html :as html]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [ring.middleware.file :as f]
            [ring.util.mime-type :as mime])
  (:use [alterego.specs]))

(s/def ::asset-dir :alterego.specs/filled-string)

(s/def ::opts (s/keys :req-un [::asset-dir]))

(defn- method-not-allowed
  [_]
  {:status 405
   :headers {"Content-Type" "text/plain; charset=utf-8"},
   :body "Method Not Allowed."})

(defn- not-found
  [_]
  {:status 404
   :headers {"Content-Type" "text/plain; charset=utf-8"},
   :body "Not found."})

(defn- convert-textfile-entity
  [stream]
  (with-open [rdr (io/reader stream)]
    (transduce (comp (take-while #(not= % "."))
                     (map #(cond-> %
                             (.startsWith % "..") (subs 1)))
                     (interpose \newline))
               str
               (line-seq rdr))))

(defn- convert-document
  [stream]
  (cond-> stream
    (instance? goophi.textfileentity.TextfileEntityInputStream stream)
    convert-textfile-entity))

(defn- document-handler
  [uri response]
  (let [mime-type (mime/ext-mime-type uri)]
    {:status 200
     :headers {"Content-Type" mime-type}
     :body (convert-document response)}))

(defn- gophermap-handler
  [uri response]
  (let [gophermap (slurp response)]
    (.close response)
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (html/gophermap->html "Sebastian's Gopherspace (HTTP Gateway)" uri gophermap)}))

(defn- has-file-extension?
  [name]
  (some? (re-matches #"(?i).*[^/]\.([a-z0-9]+)$" name)))

(defn- gopher->http
  [uri response]
  (if (has-file-extension? uri)
    (document-handler uri response)
    (gophermap-handler uri response)))

(defn- gopher
  [gopher-handler]
  (fn [{:keys [uri] :as req}]
    (let [response (gopher-handler {:path uri})]
      (if response
        (gopher->http uri response)
        (not-found req)))))

(defn- wrap-get-method
  [handler]
  (fn [{:keys [request-method] :as req}]
    (if (#{:get} request-method)
      (handler req)
      (method-not-allowed req))))

(defn handler
  [{:keys [asset-dir] :as opts} gopher-handler]
  {:pre [(s/valid? ::opts opts)]}
  (-> (gopher gopher-handler)
      (f/wrap-file asset-dir)
      wrap-get-method))
