(ns hello
  (:require
   [mazes.core :refer (make-grid)]
  ;[mazes.repr.ascii :refer (string-for-grid)]
   [mazes.repr.html-table :as table]
   [mazes.alg.sidewinder :as alg]
   [mazes.analysis.dijkstra :as analysis]
   [dommy.core :as dommy :refer-macros [sel sel1]]
   [clojure.string :as str]))

(def height 46)
(def width 26)
(def base-grid (make-grid height width))

;; (defn draw-grid []
;;   (let [container (sel1 "#ascii-maze")]
;;     (dommy/set-text! container (string-for-grid (alg/on base-grid)))))
(defn draw-grid []
  (let [container (sel1 "#maze")]
    (dommy/set-html! container
                     (table/represent (analysis/compute-distances (alg/on base-grid) [(rand-int height) (rand-int width)])))))

(js/setInterval draw-grid 5000)
(draw-grid)
