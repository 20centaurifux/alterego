(ns alterego.www.core
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.adapter.jetty :as jetty]
            [alterego.www.page :as page]
            [alterego.log :refer [log-request!]]
            [confick.core :as config]))

(defroutes app-routes
  (GET
    "/"
    []
    (page/index))
  (GET
    "/project/:name"
    [name]
    (page/project name))
  (GET
    "/ip"
    {:as req}
    (page/ip req))
  (GET
    "/imprint"
    []
    (page/from-markdown "/imprint" "Imprint" "imprint.md"))
  (route/not-found (page/not-found)))

(defn- resolve-ip
  [req]
  (if-let [ips (get-in req [:headers "x-forwarded-for"])]
    (-> ips
        (clojure.string/split #",")
        first)
    (:remote-addr req)))

(defn- wrap-client-ip
  [handler]
  (fn [request]
    (-> (assoc request :client-ip (resolve-ip request))
        handler)))

(defn- wrap-log
  [handler]
  (fn [request]
    (log-request! (:client-ip request) (:scheme request) (:uri request))
    (handler request)))

(defonce app
  (config/bind [^:required public-dir [:www :public-dir]]
    (-> app-routes
        (wrap-file public-dir)
        wrap-params
        wrap-log
        wrap-client-ip)))

(defn start
  []
  (config/bind [^{:default 3000} port [:www :binding :port]]
    (jetty/run-jetty #'app {:port port :join? false})))
