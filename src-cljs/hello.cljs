(ns hello
  (:require
   [mazes.core :refer (make-grid)]
  ;[mazes.repr.ascii :refer (string-for-grid)]
   [mazes.repr.html-table :as table]
   [mazes.alg.sidewinder :as alg]
   [dommy.core :as dommy :refer-macros [sel sel1]]
   [clojure.string :as str]))

(def base-grid (make-grid 46 26))

;; (defn draw-grid []
;;   (let [container (sel1 "#ascii-maze")]
;;     (dommy/set-text! container (string-for-grid (alg/on base-grid)))))
(defn draw-grid []
  (let [container (sel1 "#maze")]
    (dommy/set-html! container (table/represent (alg/on base-grid)))))

(js/setInterval draw-grid 5000)
(draw-grid)
