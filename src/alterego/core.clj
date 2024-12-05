(ns alterego.core
  (:gen-class)
  (:require [aleph.http :as http]
            [aleph.tcp :as tcp]
            [alterego.gopher.core :as gopher]
            [alterego.www.core :as www]
            [clojure.spec.alpha :as s]
            [clojure.tools.logging :as log]
            [confick.core :as c]
            [integrant.core :as ig])
  (:import java.lang.Runtime)
  (:use [alterego.specs]))

(defn- ->InetSocketAddress
  [{:keys [address port] :as binding}]
  {:pre [(s/valid? :alterego.specs/binding binding)]}
  (java.net.InetSocketAddress. address port))

;;;; components

;;; gopher

(def gopher-components
  {:gopher/binding (c/lookup [:gopher :binding])
   :gopher/file-handler (c/lookup [:gopher :opts] :default {})
   :gopher/handler (ig/ref :gopher/file-handler)
   :gopher/server {:binding (ig/ref :gopher/binding)
                   :handler (ig/ref :gopher/handler)}})

(defmethod ig/init-key :gopher/binding
  [_ opts]
  (->InetSocketAddress opts))

(defmethod ig/init-key :gopher/file-handler
  [_ opts]
  {:pre [(s/valid? map? opts)]}
  (gopher/file-handler opts))

(defn- wrap-gopher-logger
  [handler]
  (fn [req]
    (log/infof "Gopher request: %s" req)
    (handler req)))

(defmethod ig/init-key :gopher/handler
  [_ file-handler]
  (-> (gopher/router file-handler)
      wrap-gopher-logger))

(defmethod ig/init-key :gopher/server
  [_ {:keys [binding handler]}]
  (log/infof "Starting Gopher server with binding %s" binding)
  (tcp/start-server (gopher/tcp-handler handler) {:socket-address binding}))

(defmethod ig/halt-key! :gopher/server
  [_ server]
  (log/info "Stopping Gopher server.")
  (.close server))

;;; http

(def http-components
  {:http/binding (c/lookup [:http :binding])
   :http/handler {:opts (c/lookup [:http :opts])
                  :gopher-handler (ig/ref :gopher/file-handler)}
   :http/server {:binding (ig/ref :http/binding)
                 :handler (ig/ref :http/handler)}})

(defmethod ig/init-key :http/binding
  [_ opts]
  (->InetSocketAddress opts))

(defn- wrap-www-logger
  [handler]
  (fn [req]
    (log/infof "HTTP request: %s" (select-keys req [:protocol
                                                    :remote-addr
                                                    :host
                                                    :headers
                                                    :user-agent
                                                    :query-string
                                                    :scheme
                                                    :request-method
                                                    :uri]))
    (handler req)))

(defmethod ig/init-key :http/handler
  [_ {:keys [opts gopher-handler]}]
  (-> (www/handler opts gopher-handler)
      wrap-www-logger))

(defmethod ig/init-key :http/server
  [_ {:keys [binding handler]}]
  (log/infof "Starting HTTP server with binding %s" binding)
  (http/start-server handler {:socket-address binding
                              :idle-timeout 500}))

(defmethod ig/halt-key! :http/server
  [_ server]
  (log/info "Stopping HTTP server.")
  (.close server))

;;;; main

(defn shutdown-thread
  [system]
  (Thread.
   (reify Runnable
     (run [_]
       (log/info "Shutting down alterego.")
       (ig/halt! system)))))

(defn -main
  []
  (log/info "Starting alterego.")
  (let [system (ig/init (merge gopher-components http-components))]
    (.addShutdownHook (Runtime/getRuntime)
                      (shutdown-thread system))))