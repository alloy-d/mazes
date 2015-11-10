(ns mazes.alg.sidewinder
  (:require [mazes.core :as maze :refer (in-grid? link linked? loc+ locations)]
            [mazes.grids.rectangular :as rect]))

(defn- opposite [offset] (mapv - offset))

(defn- get-run [grid run side]
  (let [neighbor (loc+ (last run) (opposite side))]
    (if (and (in-grid? grid neighbor)
             (linked? grid (last run) neighbor))
      (get-run grid (conj run neighbor) side)
      run)))

(defn- close-run [grid run side]
  (let [linkable (transduce (comp (map (partial loc+ side))
                                  (filter (partial in-grid? grid)))
                            conj run)]
    (if (seq linkable)
      (let [to-link (rand-nth linkable)]
        (link grid to-link (loc+ to-link (opposite side))))
      grid)))

(defn- visit-cell [grid loc {:keys [sides ratio]
                             :or {sides [rect/east rect/north] ratio 0.5}}]
  {:pre (= (count sides) 2)}

  (let [choices (transduce (comp (map (partial loc+ loc))
                                 (map #(if (in-grid? grid %1) %1 nil)))
                           conj sides)]
    (cond (every? nil? choices)
          grid

          (nil? (first choices))
          (close-run grid (get-run grid [loc] (first sides)) (last sides))

          (nil? (second choices))
          (link grid loc (first choices))

          :else
          (if (> ratio (rand))
            (link grid loc (first choices))
            (close-run grid (get-run grid [loc] (first sides)) (last sides))))))

(defn on [grid & opts]
  (reduce #(visit-cell %1 %2 opts) grid (maze/locations grid)))

(defn- stepseq [grid remaining-locs opts]
  (lazy-seq
   (if (seq remaining-locs)
     (let [step (visit-cell grid (first remaining-locs) opts)]
       (cons step (stepseq step (rest remaining-locs) opts)))
     '())))

(defn stepwise [grid & opts]
  (stepseq grid (locations grid) opts))
