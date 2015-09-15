(ns mazes.web.views
  (:require
   [hiccup
    [page :refer [html5]]
    [page :refer [include-js]]]))

(defn index-page []
  (html5
   [:head
    [:title "Mazes"]
    (include-js "js/main.js")]
   [:body]))
