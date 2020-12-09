(ns alterego.storage.core
  (:gen-class))

(defprotocol RequestLog
  (append-request! [this address protocol path]))

(defmulti ->request-log
  #(-> %
       java.net.URI.
       .getScheme))
