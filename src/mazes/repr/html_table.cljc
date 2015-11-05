(ns mazes.repr.html-table
  (:require [mazes.core]
            [mazes.analysis.dijkstra :as analysis]
            [clojure.string :as str]))

(defn- represent-cell [cell]
  (str/join (flatten ["<td class='"
                      (str/join " " (map name
                                         (filter #(%1 cell)
                                                 #{:north :south :east :west})))
                      "'"
                      ;; FIXME: This might be one of the worst things I've ever done.
                      (when-let [distance (get cell :mazes.analysis.dijkstra/distance)]
                        (str/join "" ["style='background-color: rgb(0,0,"
                                      (max 0 (- 255 (int (* 2 distance))))
                                      ");'"]))
                      "/>"])))

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
