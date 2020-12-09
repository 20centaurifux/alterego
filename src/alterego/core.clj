(ns alterego.core
  (:gen-class)
  (:require [alterego.www.core :as www]
            [alterego.gopher.core :as gopher]))

(defn -main
  []
  (www/start)
  (gopher/start))
