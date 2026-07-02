(defproject alterego "0.1.0-SNAPSHOT"
  :description "My personal gopherspace and HTTP proxy."
  :url "gopher://dixieflatline.de"
  :license {:name "AGPLv3"
            :url "https://www.gnu.org/licenses/agpl-3.0"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [aleph "0.9.9"]
                 [hiccup "2.0.0"]
                 [de.dixieflatline/confick "0.2.0-SNAPSHOT"]
                 [de.dixieflatline/goophi "0.2.1-SNAPSHOT"]
                 [integrant "1.0.1"]
                 [ring/ring-core "1.15.5"]]
  :main ^:skip-aot alterego.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :plugins [[dev.weavejester/lein-cljfmt "0.16.4"]])
