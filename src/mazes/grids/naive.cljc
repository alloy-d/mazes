(ns mazes.grids.naive
  (:require [clojure.string :as str]))

(defrecord Cell [top right bottom left])

(defn make-row [cols]
  (vec (map (fn [_] (Cell. true true true true)) (range cols))))

(defn make-grid [rows cols]
  (vec (map (fn [_] (make-row cols)) (range rows))))

(defn num-rows [grid]
  (count grid))
(defn num-cols [grid]
  (count (first grid)))

(defn locations [grid]
  (reduce concat
          (map (fn [r]
                 (map (fn [c] [r c]) (range (num-cols grid))))
               (range (num-rows grid)))))

(defn col [loc] (last loc))
(defn row [loc] (first loc))

;; Returns the opposite side of a given side.
(defn opposite [side]
  (get {:top :bottom
        :bottom :top
        :right :left
        :left :right} side))

;; Returns the loc to the given side of loc.
(defn loc-to [side loc]
  (cond (= side :top) [(- (row loc) 1) (col loc)]
        (= side :bottom) [(+ (row loc) 1) (col loc)]
        (= side :left) [(row loc) (- (col loc) 1)]
        (= side :right) [(row loc) (+ (col loc) 1)]))

;; Returns the wall that connects loc1 to loc2, from loc1's perspective.
(defn connection [loc1 loc2]
  (let [col-offset (- (col loc2) (col loc1))
        row-offset (- (row loc2) (row loc1))]
    (cond (= col-offset 0) (cond (= row-offset 1) :bottom
                                 (= row-offset -1) :top
                                 :else nil)
          (= row-offset 0) (cond (= col-offset 1) :right
                                 (= col-offset -1) :left
                                 :else nil)
          :else nil)))

(defn link [grid loc1 loc2]
  (let [cell1 (get-in grid loc1)
        cell2 (get-in grid loc2)]
    (assoc-in (assoc-in grid loc1
                        (assoc cell1 (connection loc1 loc2) false))
              loc2 (assoc cell2 (connection loc2 loc1) false))))

(defn linked? [grid loc1 loc2]
  (let [cell1 (get-in grid loc1)]
    (if-let [shared-side (connection loc1 loc2)]
      (not (get cell1 shared-side))
      false)))

(defn contains-loc? [grid loc]
  (let [row (row loc)
        col (col loc)]
    (and (>= row 0)
         (>= col 0)
         (< row (num-rows grid))
         (< col (num-cols grid)))))

(defn neighbors [grid loc]
  (reduce (fn [valid side]
            (let [neighbor (loc-to side loc)]
              (if (contains-loc? grid neighbor)
                (assoc valid side neighbor)
                valid)))
          {} [:top :bottom :left :right]))

(defn linked-neighbors [grid loc]
  (let [all-neighbors (neighbors grid loc)]
    (filter #(linked? grid loc (second %1)) all-neighbors)))

(defn annotate
  "Associate extra metadata with a given location."
  [grid data loc]
  (let [cell (get-in grid loc)]
    (assoc-in grid loc (merge cell data))))
