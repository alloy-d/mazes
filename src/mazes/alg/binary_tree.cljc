(ns mazes.alg.binary-tree
  (:require [mazes.core :as maze :refer (in-grid? link loc+ locations)]
            [mazes.grids.rectangular :as rect]))

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

(defn- stepseq [grid remaining-locs opts]
  (lazy-seq
   (if (seq remaining-locs)
     (let [step (visit-cell grid (first remaining-locs) opts)]
       (cons step (stepseq step (rest remaining-locs) opts)))
     '())))

(defn stepwise [grid & opts]
  (stepseq grid (locations grid) opts))

(def BinaryTree
  (reify
    maze/PGridModifier
    (modify-grid [_ grid]
      (on grid))
    (modify-steps [_ grid]
      (stepwise grid))))
