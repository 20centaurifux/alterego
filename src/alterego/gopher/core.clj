(ns alterego.gopher.core
  (:gen-class)
  (:require [goophi.core :refer [info]]
            [goophi.routing :refer [->routes]]
            [goophi.response :refer [menu-entity]]
            [goophi.fs :refer [get-contents]]
            [goophi.redirect :as html]
            [goophi.tcp :refer [->gopher-handler]]
            [alterego.log :refer [log-request!]]
            [confick.core :as config]
            [aleph.tcp :as tcp]))

(defn- log
  [request]
  (log-request! (:remote-addr request) :gopher (:path request))
  request)

(defonce ^:private routes
  (->routes
   ("URL\\:*"
    [:as req]
    (if-let [url (html/selector->url (:path req))]
      (html/redirect url)
      (menu-entity (info "Not found."))))
   ("*"
    [:as req]
    (config/bind [^:required base-dir [:goophi :directory]]
      (get-contents base-dir (:path req))))))

(defonce ^:private app
  (comp routes log))

(defn start
  []
  (config/bind [^:required port [:goophi :binding :port]]
    (tcp/start-server (->gopher-handler app)
                      {:port port})))
