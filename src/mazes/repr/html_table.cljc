(ns mazes.repr.html-table
  (:require [mazes.core]
            [clojure.string :as str]))

(defn- represent-cell [cell]
  (str/join (flatten ["<td class='"
                      (str/join " " (map name
                                         (filter #(%1 cell)
                                                 #{:top :right :bottom :left})))
                      "'/>"])))

(defn- represent-row [row]
  (str/join (flatten ["<tr>"
                      (map represent-cell row)
                      "</tr>"])))

(defn- represent-grid [grid]
  (str/join (flatten ["<table><tbody>"
                      (map represent-row grid)
                      "</tbody></table>"])))

(defn represent [grid]
  (represent-grid grid))
