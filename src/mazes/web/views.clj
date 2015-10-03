(ns mazes.web.views
  (:require
   [clojure.string :as str]
   [hiccup
    [page :refer [html5]]
    [page :refer [include-js include-css]]]))

(defn- cache-bust [asset]
  (str/join [asset "?" (.getTime (new java.util.Date))]))

(defn index-page [& {:keys [:variant]}]
  (html5 [:head
          [:title "Mazes"]
          (include-css (cache-bust "css/main.css"))
          (when variant
            (include-css (cache-bust (str/join ["css/" (name variant) ".css"]))))]
         [:body
          [:div {:id "maze" :class "maze"}
           [:h1 {:class "loading"}
            "a-"
            [:br]
            [:em "maze"]
            [:br]
            "-ing"
            [:br]]]
          (include-js "js/out/goog/base.js")
          (include-js (cache-bust "js/out/main.js"))]))
