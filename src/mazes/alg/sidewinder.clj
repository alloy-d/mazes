(ns mazes.alg.sidewinder
  (:require [mazes.core :refer :all]))

(defn- get-run [grid run side]
  (let [neighbor (loc-to (opposite side) (first run))]
    (if (and (contains-loc? grid neighbor)
             (linked? grid (first run) neighbor))
      (get-run grid (cons neighbor run) side)
      run)))

(defn- close-run [grid run side]
  (let [loc-to-link (rand-nth run)]
    (link grid loc-to-link (loc-to side loc-to-link))))

(defn- visit-cell [grid loc {:keys [side1 side2 ratio]
                             :or {side1 :right side2 :top ratio 0.5}}]
  (let [adjoining (neighbors grid loc)
        neighbor1 (get adjoining side1)
        neighbor2 (get adjoining side2)]
    (cond (and (nil? neighbor1)
               (nil? neighbor2))
          grid

          (nil? neighbor1)
          (close-run grid (get-run grid [loc] side1) side2)

          (nil? neighbor2)
          (link grid loc neighbor1)

          :else
          (if (> ratio (rand))
            (link grid loc neighbor1)
            (close-run grid (get-run grid [loc] side1) side2)))))

(defn on [grid & opts]
  (reduce #(visit-cell %1 %2 opts) grid (locations grid)))
