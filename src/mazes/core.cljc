(ns mazes.core
  (:require [clojure.string :as str])
  #?(:clj  (:gen-class)))

(defprotocol PCell
  "Necessary interactions for maze cells in a grid.")

(defprotocol PGrid
  "Necessary interactions for a maze grid."

  (locations [this] "Produces a vector of all locations in the grid.")
  (neighbors [this loc] "Returns a list of neighbors to a given location.")
  (link [this loc1 loc2] "Links two cells.")
  (linked? [this loc1 loc2] "Checks if two cells are linked."))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
