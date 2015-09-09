(ns mazes.core
  (:require [clojure.string :as str])
  (:gen-class))

(defrecord Cell [top right bottom left])

(defn make-row [cols]
  (vec (map (fn [_] (Cell. true true true true)) (range cols))))

(defn make-grid [rows cols]
  (vec (map (fn [_] (make-row cols)) (range rows))))

(defn col [loc] (last loc))
(defn row [loc] (first loc))

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



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
