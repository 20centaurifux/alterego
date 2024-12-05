(ns alterego.www.html
  (:require [clojure.string :as str]
            [hiccup2.core :as h]))

(defn- ->page
  [head body]
  (h/html
   (h/raw "<!DOCTYPE html>")
   [:html  {:lang "en"}
    head
    body]))

(defn- ->head
  [title]
  [:head
   [:title title]
   [:link {:href "/css/core.css" :rel "stylesheet"}]
   [:link {:href "/css/fontawesome.min.css" :rel "stylesheet"}]
   [:link {:href "/css/solid.min.css" :rel "stylesheet"}]])

(defn- split-path
  [s]
  (rest (str/split s #"/")))

(defn- link
  [href text]
  [:a {:href href} text])

(defn- path->links
  [path]
  (reduce
   (fn [input x]
     (conj input
           (link (str (-> input last second :href (str/replace #"/$" "")) "/" x) x)))
   [(link "/" [:i {:class "far fa-home"}])]
   (split-path path)))

(defn- navigation
  [path]
  [:div {:class "navigation"}
   (conj [:ul {:class "navigation"}]
         (map (fn [link]
                [:li {:class "navigation"} link])
              (path->links path)))])

(defn- contents
  [element]
  [:div {:class "contents"} element])

(defn- ->body
  [title path element]
  [:body [:h1 title]
   (navigation path)
   (contents element)])

(defn- parse-gopher-item
  [line]
  (->> line
       (re-matches #"(?i)^([0-9a-z\+])([^\t]*)\t([^\t]+)\t([^\t]+)\t(\d+)")
       rest
       seq))

(defmulti ^:private gopher-line->tr first)

(defmethod ^:private gopher-line->tr "i"
  [[_ display-text _ _ _]]
  (let [display-text' (if (str/blank? display-text)
                        (h/raw "&nbsp;")
                        display-text)]
    [:tr [:td] [:td [:pre display-text']]]))

(defmethod ^:private gopher-line->tr "0"
  [[_ display-text selector _ _]]
  [:tr
   [:td [:i {:class "far fa-file icon"}]]
   [:td [:a {:href selector} display-text]]])

(defmethod ^:private gopher-line->tr "1"
  [[_ display-text selector _ _]]
  [:tr
   [:td [:i {:class "far fa-folder icon"}]]
   [:td [:a {:href selector} display-text]]])

(defmethod ^:private gopher-line->tr "I"
  [[_ display-text selector _ _]]
  [:tr
   [:td [:i {:class "far fa-image icon"}]]
   [:td [:a {:href selector} display-text]]])

(defn- remove-proxy
  [selector]
  (if-let [[_ repo ref] (re-matches #"/github\$/([\w-\.]+)/archive/(\w+)" selector)]
    (str "https://github.com/20centaurifux/"
         repo
         "/archive/refs/heads/"
         ref
         ".zip")
    selector))

(defmethod ^:private gopher-line->tr "9"
  [[_ display-text selector _ _]]
  [:tr
   [:td [:i {:class "far fa-download icon"}]]
   [:td [:a {:href (remove-proxy selector)} display-text]]])

(defmethod ^:private gopher-line->tr "h"
  [[_ display-text selector _ _]]
  [:tr
   [:td [:i {:class "far fa-globe icon"}]]
   [:td [:a {:href (subs selector 4) :target "_blank"} display-text]]])

(defmethod ^:private gopher-line->tr nil
  [_])

(defn- gophermap->table
  [gophermap]
  (conj [:table]
        (map (comp gopher-line->tr
                   parse-gopher-item)
             (str/split-lines gophermap))))

(defn gophermap->html
  [title uri gophermap]
  (str (->page (->head title)
               (->body title
                       uri
                       (gophermap->table gophermap)))))