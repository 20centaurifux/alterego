(ns alterego.gopher.core
  (:require [aleph.http :as http]
            [clojure.spec.alpha :as s]
            [goophi.core :refer [info]]
            [goophi.fs :as fs]
            [goophi.redirect :as html]
            [goophi.response :as rsp]
            [goophi.routing :as r]
            [goophi.tcp :as tcp])
  (:use [alterego.specs]))

(s/def ::base-dir :alterego.specs/filled-string)

(s/def ::opts (s/keys :req-un [:alterego.specs/hostname
                               :alterego.specs/port
                               ::base-dir]))

(defn file-handler
  [{:keys [base-dir hostname port] :as opts}]
  {:pre [(s/valid? ::opts opts)]}
  (fn [req]
    (fs/get-contents base-dir
                     (:path req)
                     :hostname hostname
                     :port port)))

(defn- http-proxy
  [uri]
  (try
    (-> @(http/get uri)
        :body)
    (catch Exception _ (rsp/menu-entity (info "Not found.")))))

(defn router
  [handler]
  (r/routes
   ("URL\\:*"
    [:as req]
    (if-let [url (html/selector->url (:path req))]
      (html/redirect url)
      (rsp/menu-entity (info "Not Found."))))
   ("/github$/:repo/archive/:ref"
    [repo ref]
    (http-proxy (str "https://github.com/20centaurifux/"
                     repo
                     "/archive/refs/heads/"
                     ref
                     ".zip")))
   ("*"
    [:as req]
    (or (handler req)
        (rsp/menu-entity (info "Not Found."))))))

(defn tcp-handler
  [handler]
  (tcp/aleph-handler handler))