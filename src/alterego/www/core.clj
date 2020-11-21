(ns alterego.www.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.adapter.jetty :as jetty]
            [alterego.www.page :as page]
            [confick.core :as config]))

(defroutes app-routes
  (GET "/"
       []
       (page/index))
  (GET "/project/:name"
       [name]
       (page/project name))
  (GET "/ip"
       {:as req}
       (page/ip req))
  (GET "/imprint"
       []
       (page/from-markdown "/imprint" "Imprint" "imprint.md"))
  (route/not-found "404 page"))

(defonce app
  (config/bind [^:required public-dir [:www :public-dir]]
    (-> app-routes
        (wrap-file public-dir)
        wrap-params)))

(defonce server
  (config/bind [^{:default 3000} port [:www :port]]
    (jetty/run-jetty #'app {:port port :join? false})))

(defn start
  []
  (.start server))

(defn stop
  []
  (.stop server))
