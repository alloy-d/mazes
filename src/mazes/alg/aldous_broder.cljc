(ns mazes.alg.aldous-broder
  (:require [mazes.core :as maze]))

(defn visit-in [grid loc]
  (let [cell (get-in grid loc)]
    (assoc-in grid loc (assoc cell ::visited true))))

(defn visited? [grid loc]
  (get (get-in grid loc) ::visited))

(defn visit [grid loc prev-loc]
  (if (not (every? #(visited? grid %1) (maze/locations grid)))
    (let [next-loc (rand-nth (vals (maze/neighbors grid loc)))]
      (if (not (visited? grid loc))
        (let [grid (visit-in (if (not (nil? prev-loc))
                               (maze/link grid loc prev-loc)
                               grid)
                             loc)]
          (recur grid next-loc loc))
        (recur grid next-loc loc)))
    grid))

(defn on [grid & opts]
  (visit grid (rand-nth (maze/locations grid)) nil))
