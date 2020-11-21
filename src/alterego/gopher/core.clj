(ns alterego.gopher.core
  (:require [goophi.core :refer [info]]
            [goophi.routing :refer [defroutes]]
            [goophi.response :refer [menu-entity]]
            [goophi.fs :refer [get-contents]]
            [goophi.redirect :as html]
            [goophi.tcp :refer [->gopher-handler]]
            [confick.core :as config]
            [aleph.tcp :as tcp]))

(defroutes routes
  ("URL\\:*"
    [:as req]
    (if-let [url (html/selector->url (:path req))]
      (html/redirect url)
      (menu-entity (info "Not found."))))
  ("*"
    [:as req]
    (config/bind [^:required base-dir [:goophi :directory]]
      (get-contents base-dir (:path req)))))

(defn start
  []
  (config/bind [^:required port [:goophi :port]]
    (tcp/start-server
      (->gopher-handler routes)
      {:port port})))
