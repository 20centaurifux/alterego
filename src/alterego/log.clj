(ns alterego.log
  (:gen-class)
  (:require [alterego.storage.core :refer [->request-log append-request!]]
            [confick.core :refer [bind]]
            [clojure.core.async :refer [chan dropping-buffer go-loop <! >!!]]))

(defn- load-storage-ns
  [uri]
  (->> uri
    java.net.URI.
    .getScheme
    (str "alterego.storage.")
    symbol
    use))

(defn- ->request-log-channel
  []
  (bind [^:required uri [:storage :requests]]
    (load-storage-ns uri)
    (let [ch (chan (dropping-buffer 64))
          writer (->request-log uri)]
      (go-loop []
        (let [m (<! ch)]
          (append-request! writer (:address m) (:protocol m) (:path m))
        (recur)))
      ch)))

(defonce ^:private request-log (->request-log-channel))

(defn log-request!
  [address protocol path]
  (>!! request-log
       {:address address
        :protocol protocol
        :path path}))
