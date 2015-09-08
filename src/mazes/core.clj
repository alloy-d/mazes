(ns mazes.core
  (:require [clojure.string :as str])
  (:gen-class))

(defrecord Cell [top right bottom left])

(defn make-row [cols]
  (vec (map (fn [_] (Cell. true true true true)) (range cols))))

(defn make-grid [rows cols]
  (vec (map (fn [_] (make-row cols)) (range rows))))

(defn string-for-part [has-wall? wall-glyph passage-glyph closer]
  (let
      [draw-cell (fn [cell]
                   (if (has-wall? cell)
                     wall-glyph
                     passage-glyph))]
    (fn [row]
      (str/join (flatten [(map draw-cell row) (closer (last row))])))))

(def string-for-top (string-for-part
                     :top
                     "+---"
                     "+   "
                     (fn [_] "+")))

(def string-for-middle (string-for-part
                        :left
                        "|   "
                        "    "
                        (fn [cell]
                          (if (:right cell)
                            "|"
                            " "))))

(def string-for-bottom (string-for-part
                        :bottom
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

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
