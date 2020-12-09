(ns alterego.storage.sqlite
  (:gen-class)
  (:require [alterego.storage.core :refer [RequestLog ->request-log]]
            [toucan.db :as db]
            [toucan.models :as models])
  (:import [java.security MessageDigest]))

(models/defmodel Protocol :protocol
  models/IModel
  (types [this]
         {:scheme :keyword}))

(def protocol->id
  (memoize
   (fn [p]
     (db/select-one-field :id Protocol :scheme p))))

(def id->protocol
  (memoize
   (fn [id]
     (db/select-one-field :scheme Protocol :id id))))

(models/add-type! :protocol
                  :in  protocol->id
                  :out id->protocol)

(defn anonymize [string]
  (let [digest (.digest (MessageDigest/getInstance "SHA-256")
                        (.getBytes string "UTF-8"))]
    (apply str (map (partial format "%02x") digest))))

(models/add-type! :anonymized
                  :in anonymize
                  :out str)

(models/defmodel Request :request
  models/IModel
  (types [_]
         {:protocol_id :protocol
          :address :anonymized}))

(deftype SQLiteStorage
         [connection]
  RequestLog
  (append-request!
    [this address protocol path]
    (binding [db/*db-connection* connection]
      (db/insert! Request {:address address :protocol_id protocol :path path}))))

(defn- ->connection
  [path]
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname path})

(defn ^:private uri->path
  [uri]
  (if (clojure.string/starts-with? uri "sqlite://")
    (.getPath (java.net.URI. uri))
    (subs uri 7)))

(defmethod ->request-log "sqlite"
  [uri]
  (-> uri
      uri->path
      ->connection
      SQLiteStorage.))
