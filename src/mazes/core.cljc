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

(defprotocol PAnnotateCells
  "Allows annotating cells with extra data."

  (annotate [this loc data] "Associates data (a map) with the given loc.")
  (annotation [this loc key] "Returns the annotation for key for a given loc.")
  (annotations [this loc] "Returns (as a map) all annotations for a given loc."))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
