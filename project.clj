(defproject alterego "0.1.0-SNAPSHOT"
  :description "My web- & gopherspace."
  :url "https://github.com/20centaurifux"
  :license {:name "AGPLv3"
            :url "https://www.gnu.org/licenses/agpl-3.0"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring/ring-core "1.9.5"]
                 [ring/ring-jetty-adapter "1.9.5"]
                 [compojure "1.7.0"]
                 [hiccup "1.0.5"]
                 [markdown-clj "1.11.2"]
                 [org.clojure/core.cache "1.0.225"]
                 [zcfux/confick "0.1.2"]
                 [zcfux/goophi "0.1.1"]
                 [org.clojure/data.json "2.4.0"]
                 [aleph "0.5.0-rc2"]
                 [toucan "1.18.0"]
                 [org.xerial/sqlite-jdbc "3.36.0.3"]
                 [org.clojure/core.async "1.5.648"]]
  :plugins [[lein-asset-minifier "0.4.6"]]
  :main ^:skip-aot alterego.core
  :profiles {:uberjar {:aot :all}}
  :minify-assets [[:js {:source ["node_modules/jquery/dist/jquery.js"
                                 "node_modules/lightbox2/dist/js/lightbox.js"
                                 "files/www-public/pageinit.js"]
                        :target "files/www-public/www.min.js"}]
                  [:css {:source ["node_modules/lightbox2/dist/css/lightbox.css"
                                  "files/www-public/base.css"]
                         :target "files/www-public/www.min.css"}]])
