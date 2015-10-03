(ns mazes.web.handler
  (:use compojure.core
        mazes.web.views
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]))

(defroutes main-routes
  (GET "/" [] (index-page))
  (route/resources "/")
  (route/not-found "I think you might be thinking of something else."))

(def handler
  (-> (handler/site main-routes)
      (wrap-base-url)))
