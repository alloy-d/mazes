(ns mazes.alg.aldous-broder
  (:require [mazes.core :as maze]))

(defn visit [grid loc prev-loc unvisited]
  (if (seq unvisited)
    (let [next-loc (rand-nth (maze/neighbors grid loc))]
      (if (contains? unvisited loc)
        (let [grid (if prev-loc
                     (maze/link grid loc prev-loc)
                     grid)]
          (recur grid next-loc loc (disj unvisited loc)))
        (recur grid next-loc loc unvisited)))
    grid))

(defn on [grid]
  (visit grid
         (rand-nth (maze/locations grid))
         nil
         (set (maze/locations grid))))
