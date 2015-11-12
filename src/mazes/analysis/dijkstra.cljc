(ns mazes.analysis.dijkstra
  (:require [mazes.core :as m]))

(defn distance [grid loc]
  (m/annotation grid loc ::distance))

(def ^:private unvisited? (comp nil? distance))

(defn- update-distance [grid loc distance]
  (m/annotate grid loc {::distance distance}))

(defn- unvisited-links [grid loc]
  (filter #(and (unvisited? grid %)
                (m/linked? grid loc %))
          (m/neighbors grid loc)))

(defn- visit [grid [current & remaining] next-locs]
  (cond
    current
    (let [next-distance (+ 1 (distance grid current))
          to-visit (unvisited-links grid current)
          updated-grid (reduce #(update-distance %1 %2 next-distance)
                               grid
                               to-visit)]
      (recur updated-grid remaining (concat next-locs to-visit)))

    (seq next-locs)
    (recur grid next-locs [])

    :else
    grid))

(defn- visit-seq [grid [current & remaining] next-locs]
  (lazy-seq
   (cond
     current
     (let [next-distance (+ 1 (distance grid current))
           to-visit (unvisited-links grid current)
           updated-grid (reduce #(update-distance %1 %2 next-distance)
                                grid
                                to-visit)]
       (cons updated-grid (visit-seq updated-grid remaining (concat next-locs to-visit))))

     (seq next-locs)
     (visit-seq grid next-locs [])

     :else
     '())))

(defn compute-distances [f grid root-loc]
  (f (update-distance grid root-loc 0)
     [root-loc]
     '()))

(defrecord DistanceCalculator [root-loc]
  m/PGridModifier
  (modify-grid [_ grid]
    (compute-distances visit grid root-loc))
  (modify-steps [_ grid]
    (compute-distances visit-seq grid root-loc)))
