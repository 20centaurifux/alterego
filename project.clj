(defproject alterego "0.1.0-SNAPSHOT"
  :description "My personal gopherspace and HTTP proxy."
  :url "gopher://dixieflatline.de"
  :license {:name "AGPLv3"
            :url "https://www.gnu.org/licenses/agpl-3.0"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [aleph "0.8.1"]
                 [hiccup "2.0.0-RC3"]
                 [de.dixieflatline/confick "0.2.0-SNAPSHOT"]
                 [de.dixieflatline/goophi "0.2.0-SNAPSHOT"]
                 [integrant "0.13.1"]
                 [ring/ring-core "1.13.0"]]
  :main ^:skip-aot alterego.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :plugins [[dev.weavejester/lein-cljfmt "0.13.0"]])
