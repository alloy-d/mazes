(ns mazes.web.views
  (:require
   [clojure.string :as str]
   [hiccup
    [page :refer [html5]]
    [page :refer [include-js include-css]]]))

(defn- cache-bust [asset]
  (str/join [asset "?" (.getTime (new java.util.Date))]))

(defn index-page []
  (html5
   [:head
    [:title "Mazes"]
    (include-css (cache-bust "css/main.css?"))]
   [:body
    [:div {:id "maze" :class "maze"} "Generating your maze..."]
    (include-js (cache-bust "js/out/main.js"))]))
