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

(defprotocol PBoundedGrid
  "Functionality around dealing with grid boundaries."

  (in-grid? [this loc] "Returns true if a loc is within the grid."))

(defprotocol PAnnotateCells
  "Allows annotating cells with extra data."

  (annotate [this loc data] "Associates data (a map) with the given loc.")
  (annotation [this loc key] "Returns the annotation for key for a given loc.")
  (annotations [this loc] "Returns (as a map) all annotations for a given loc."))

(defprotocol PSquareGrid
  "Methods and information specific to grids with square cells."
  (rows [this] "Returns the number of rows in this grid.")
  (cols [this] "Returns the number of columns in this grid."))


(defprotocol PGridModifier
  "A protocol for things that operate on a grid."

  (modify-grid [this grid] "Applies a modification, in full, to the grid.")
  (modify-steps [this grid]
    "Produces a sequence of grids representing each step taken in applying the full modification."))

(defn loc+
  "Takes a location and an offset, and returns the result of
  applying the offset to the location."
  [loc offset]
  {:pre (= (count loc) (count offset))}

  (mapv + loc offset))
