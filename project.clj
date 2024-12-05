(defproject alterego "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
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