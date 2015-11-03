(ns mazes.alg.hunt-and-kill
  (:require [mazes.core :as maze]
            [mazes.util.visitor :as visit :refer [->Visitor visit visited? unvisited?]]
            [clojure.set :as set]))

(defn- rand-entry [coll]
  (rand-nth (vec coll)))

(def ^:private hunt)

(defn- walk
  "Carve a continuous path through the grid by visiting neighboring
  unvisited cells at random for as long as possible."
  ([grid visitor]
   (let [next (rand-entry (maze/locations grid))]
     (walk grid
           (visit visitor next)
           next)))

  ([grid visitor prev]
   (let [unvisited-neighbors (filterv (partial unvisited? visitor)
                                      (vals (maze/neighbors grid prev)))]
     (if (seq unvisited-neighbors)
       (let [next (rand-nth unvisited-neighbors)]
         (recur (maze/link grid prev next)
                (visit visitor next)
                next))
       (hunt grid visitor)))))

(defn hunt
  "Finds an unvisited cell with at least one visited neighbor,
  links it to one of its visited neighbors, and then uses it
  to start a new walk."
  [grid visitor]
  (let [visited? (partial visited? visitor)
        unvisited? (complement visited?)]
    (letfn [(target [_ loc]
              (if (and (unvisited? loc)
                       (some visited? (vals (maze/neighbors grid loc))))
                (reduced loc)
                nil))]
      (if-let [next (reduce target (maze/locations grid))]
        (let [to-link (->> (maze/neighbors grid next)
                           vals
                           (filter visited?)
                           rand-entry)]
          (walk (maze/link grid next to-link)
                (visit visitor next)
                next))
        grid))))

(defn on
  "Apply the Hunt-and-Kill Algorithm to a grid."
  [grid]
  (walk grid (->Visitor #{})))
