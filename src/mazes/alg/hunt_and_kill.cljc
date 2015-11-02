(ns mazes.alg.hunt-and-kill
  (:require [mazes.core :as maze]
            [clojure.set :as set]))

(defn- rand-entry [coll]
  (rand-nth (vec coll)))

(defn- walk
  "Carve a continuous path through the grid by visiting neighboring
  unvisited cells at random for as long as possible."
  ([grid unvisited visited]
   (let [next (rand-entry unvisited)]
     (walk grid
           (disj unvisited next)
           (conj visited next)
           next)))

  ([grid unvisited visited prev]
   (let [unvisited-neighbors (filterv #(contains? unvisited %)
                                      (maze/neighbors grid prev))]
     (if (seq unvisited-neighbors)
       (let [next (rand-nth unvisited-neighbors)]
         (recur (maze/link grid prev next)
                (disj unvisited next)
                (conj visited next)
                next))
       grid
                                        ;(hunt grid unvisited visited)
       ))))

;; (defn- hunt
;;   "Finds an unvisited cell with at least one visited neighbor,
;;   links it to one of its visited neighbors, and then uses it
;;   to start a new walk."
;;   [grid unvisited visited]
;;   (if (seq unvisited)
;;     (let [next
;;                                         ; TODO: I could probably make use of a transducer here.
;;           ])

;;     grid)

(defn on
  "Apply the Hunt-and-Kill Algorithm to a grid."
  [grid]
  (let [locations (maze/locations grid)
        unvisited (set locations)]
    (walk grid unvisited #{})))
