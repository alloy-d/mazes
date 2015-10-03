(ns mazes.alg.binary-tree
  (:require [mazes.core :as maze]))

(defn- visit-cell [grid loc {:keys [side1 side2 ratio] :or {side1 :right side2 :top ratio 0.5}}]
  (let [adjoining (maze/neighbors grid loc)
        neighbor1 (get adjoining side1)
        neighbor2 (get adjoining side2)]
    (cond (and (nil? neighbor1)
               (nil? neighbor2))
          grid

          (nil? neighbor1)
          (maze/link grid loc neighbor2)

          (nil? neighbor2)
          (maze/link grid loc neighbor1)

          :else
          (if (> ratio (rand))
            (maze/link grid loc neighbor1)
            (maze/link grid loc neighbor2)))))

(defn on [grid & opts]
  (reduce #(visit-cell %1 %2 opts) grid (maze/locations grid)))
