(ns hello
  (:require
   [mazes.core :refer (make-grid)]
  ;[mazes.repr.ascii :refer (string-for-grid)]
   [mazes.repr.html-table :as table]
   [mazes.alg.binary-tree :as alg]
   [mazes.analysis.dijkstra :as analysis]
   [dommy.core :as dommy :refer-macros [sel sel1]]
   [clojure.string :as str]))

(def height 31)
(def width 52)
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
                       (str/join "" ["height: calc((1.5em + 0.15em) * "
                                     height
                                     " + 0.15em"
                                     ");"])))))

(js/setInterval draw-grid 5000)
(draw-grid)
