(ns hello
  (:require
   [mazes.core :refer (make-grid)]
  ;[mazes.repr.ascii :refer (string-for-grid)]
   [mazes.repr.html-table :as table]
   [mazes.alg.binary-tree :as alg]
   [mazes.analysis.dijkstra :as analysis]
   [dommy.core :as dommy :refer-macros [sel sel1]]
   [clojure.string :as str]))

(def cell-size "2.5em")
(def border-size "0.6em")
(def height 49)
(def width 26)
(def base-grid (make-grid height width))

;; (defn draw-grid []
;;   (let [container (sel1 "#ascii-maze")]
;;     (dommy/set-text! container (string-for-grid (alg/on base-grid)))))
(defn draw-grid []
  (let [container (sel1 "#maze")]
    (do
      (dommy/set-html! container
                       (table/represent (analysis/compute-distances (alg/on base-grid) [(rand-int height) (rand-int width)])))
      (dommy/set-attr! container
                       :style
                       (str/join "" ["height: calc("
                                     "(" border-size " + " cell-size ")"
                                     " * "
                                     height
                                     " + " border-size
                                     ");"])))))

(js/setInterval draw-grid 15000)
(draw-grid)
