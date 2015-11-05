(ns mazes.alg.recursive-backtracker
  (:require [mazes.core :as maze]
            [mazes.util.visitor :refer (->Visitor visit unvisited?)]))

(defn- random-entry [coll]
  (rand-nth (vec coll)))

(defn- unvisited-neighbors [visitor grid loc]
  (filterv (partial unvisited? visitor)
           (maze/neighbors grid loc)))

(defn- walk
  ([grid]
   (let [start (random-entry (maze/locations grid))]
     (walk grid (->Visitor #{start}) start)))

  ([grid visitor current]
   (let [neighbors (unvisited-neighbors visitor grid current)]
     (if (seq neighbors)
       (let [next (rand-nth neighbors)
             ;; Visit an unvisited neighbor at random.
             walked (walk (maze/link grid current next)
                          (visit visitor next)
                          next)
             {:keys [grid visitor]} walked]
         ;; Then recur on this cell in the updated grid.
         (recur grid visitor current))
       {:grid grid
        :visitor visitor}))))

(defn on
  "Uses Recursive Backtracker to create a maze on a grid."
  [grid]
  (:grid (walk grid)))
