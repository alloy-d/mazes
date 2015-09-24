(ns mazes.analysis.dijkstra
  (:require [mazes.core :as m]))

(defn distance [grid loc]
  (let [cell (get-in grid loc)]
    (get cell ::distance)))

(defn- visit [grid locs next-locs]
  (letfn [(update-distance [grid loc dist]
            (let [cell (get-in grid loc)]
              (assoc-in grid loc (assoc cell ::distance dist))))]
    (if (seq locs)
      (let [linked (map second (m/linked-neighbors grid (first locs)))
            unvisited (filter #(nil? (distance grid %1)) linked)
            current-distance (distance grid (first locs))
            updated-grid (reduce #(update-distance %1 %2 (+ 1 current-distance)) grid unvisited)]
        (recur updated-grid (rest locs) (concat next-locs unvisited)))
      (if (seq next-locs)
        (recur grid next-locs [])
        grid))))

(defn compute-distances [grid root-loc]
  (let [root (assoc (get-in grid root-loc) ::distance 0)
        grid (assoc-in grid root-loc root)]
    (visit grid [root-loc] [])))
