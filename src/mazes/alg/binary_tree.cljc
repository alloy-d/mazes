(ns mazes.alg.binary-tree
  (:require [mazes.core :as maze :refer (in-grid? link loc+ locations)]
            [mazes.grids.square-array :as rect]))

(defn- visit-cell [grid loc {:keys [choices ratio]
                             :or {choices [rect/north rect/east] ratio 0.5}}]
  {:pre (= 2 (count choices))}

  (let [choices (transduce (comp (map (partial loc+ loc))
                                 (filter (partial in-grid? grid)))
                           conj choices)]
    (cond (= 0 (count choices))
          grid

          (= 1 (count choices))
          (link grid loc (first choices))

          :else
          (if (> ratio (rand))
            (link grid loc (first choices))
            (link grid loc (last choices))))))

(defn on [grid & opts]
  (reduce #(visit-cell %1 %2 opts) grid (locations grid)))
