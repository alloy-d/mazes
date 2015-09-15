(ns mazes.web.views
  (:require
   [hiccup
    [page :refer [html5]]
    [page :refer [include-js include-css]]]))

(defn index-page []
  (html5
   [:head
    [:title "Mazes"]
    (include-css "css/main.css")]
   [:body
    [:div {:id "maze" :class "maze"} "Generating your maze..."]
    (include-js "js/out/main.js")]))
