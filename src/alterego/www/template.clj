(ns alterego.www.template
  (:gen-class)
  (:use hiccup.page
        hiccup.element
        markdown.core))

(defn head
  "Creates a head section with necessary CSS & Javascript links."
  [title & {:keys [header] :or {header []}}]
  [:head
   [:title title]
   (include-css "/www.min.css")
   (include-js "/www.min.js")
   [:link {:rel "shortcut icon" :href "fav.ico"}]
   header])

(defn site
  "Creates a website with the default head section & the given body."
  [title body & {:keys [header]}]
  (html5
   {:lang "en"}
   (head title :header header)
   body))

(defn- wrap-body
  [body]
  [:body [:div#content.content body]])

(defn subsite-from-md
  "Creates a website displaying the specified markdown string as HTML.
   A 'Go home' link is shown at the very top."
  [title md]
  (site
   title
   (wrap-body
    [:div
     (link-to {:class "gohome"} "/" "Go home")
     (md-to-html-string md)])))

(defn github-project
  "Creates a website displaying the specified markdown string as HTML.
   A 'Go home' link is shown at the very top. The top-left corner
   provides a Github link."
  [title project-name github-account md]
  (site
   title
   (wrap-body
    [:div
     (link-to
      {:target "_blank"}
      (str "https://github.com/" github-account "/" project-name)
      (image {:class "forkme"} "/forkme.png" "forkme"))
     (link-to {:class "gohome"} "/" "Go home")
     (md-to-html-string md)])))

(defn- project-list-item
  [m]
  [:li
   {:class "keywords"}
   (link-to
    {:class "project"}
    (:link m)
    (:name m))
   (str " - " (:description m))
   [:br]
   [:span
    {:class "keywords"}
    (clojure.string/join ", " (:tags m))]])

(defn- render-page-parts
  [parts]
  (map
   #(case (:type %)
       ;; render HTML from markdown:
      :markdown [:div
                 (when-not (get % :no-title?)
                   [:h2 (:title %)])
                 (md-to-html-string (:md %))]
       ;; generate project list:
      :projects [:div
                 [:h2 (:title %)]
                 [:p (:subtitle %)]
                 [:ul (map project-list-item (:project-list %))]])
   parts))

(defn index
  "Builds an index page from the elements found in the index
   collection. An element may contain markdown or a list of
   projects."
  [index]
  (site
   "Home"
   (wrap-body
    [:div
     [:h1 "Home"]
     (image {:class "logo"} "/logo.png" "me")
     [:span {:style "clear:both;"}]
     (render-page-parts index)])))
