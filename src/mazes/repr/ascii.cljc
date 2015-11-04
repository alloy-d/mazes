(ns mazes.repr.ascii
  (:require [mazes.core]
            [clojure.string :as str]))

(defn string-for-part [has-wall? wall-glyph passage-glyph closer]
  (let
      [draw-cell (fn [cell]
                   (if (has-wall? cell)
                     wall-glyph
                     passage-glyph))]
    (fn [row]
      (str/join (flatten [(map draw-cell row) (closer (last row))])))))

(def string-for-top (string-for-part
                     :north
                     "+---"
                     "+   "
                     (fn [_] "+")))

(def string-for-middle (string-for-part
                        :west
                        "|   "
                        "    "
                        (fn [cell]
                          (if (:east cell)
                            "|"
                            " "))))

(def string-for-bottom (string-for-part
                        :south
                        "+---"
                        "+   "
                        (fn [_] "+")))

(defn string-for-row [row]
  (str/join "\n" [(string-for-top row)
                  (string-for-middle row)]))

(defn string-for-grid [grid]
  (str/join "\n"
            (flatten [(map string-for-row grid)
                      (string-for-bottom (last grid))])))

(defn print-grid [grid]
  (println (string-for-grid (:grid grid))))
