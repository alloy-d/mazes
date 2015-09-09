(ns mazes.alg.binary-tree
  (:require [mazes.core :refer :all]))

(defn- visit-cell [grid loc]
  (let [neighbors (neighbors grid loc)]
    (cond (and (nil? (:right neighbors))
               (nil? (:top neighbors)))
          grid

          (nil? (:right neighbors))
          (link grid loc (:top neighbors))

          (nil? (:top neighbors))
          (link grid loc (:right neighbors))

          :else
          (if (> 0.5 (rand))
            (link grid loc (:top neighbors))
            (link grid loc (:right neighbors))))))

(defn- visit-row [grid row]
  (reduce visit-cell grid (map (fn [col] [row col])
                               (range (num-cols grid)))))

(defn binary-tree [grid]
  (reduce visit-row grid (range (num-rows grid))))
