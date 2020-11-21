(ns alterego.core
  (:gen-class)
  (:require [alterego.www.core :as www]))

(defn -main
  [& args]
  (www/start))
