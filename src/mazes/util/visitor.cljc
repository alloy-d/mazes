(ns mazes.util.visitor
  (:require [clojure.set :as set]
            [mazes.core :as maze]))

(defprotocol IVisit
  (visit [this loc] "Marks a location as visited.")
  (visited? [this loc] "Checks if a location is visited."))

(defprotocol ITrackUnvisited
  (unvisited? [this loc] "Checks if a location is unvisited."))

(defprotocol IFilterNeighbors
  (visited-neighbors [this grid loc]
    "Returns a list of visited neighbors for a locations on a grid.")
  (unvisited-neighbors [this grid loc]
    "Returns a list of unvisited neighbors for a location on a grid."))

(defrecord Visitor [visited]
  IVisit
  (visit [_ loc]
    (Visitor. (conj visited loc)))
  (visited? [_ loc]
    (contains? visited loc))

  ITrackUnvisited
  (unvisited? [this loc]
    (not (visited? this loc)))

  IFilterNeighbors
  (visited-neighbors [this grid loc]
    (filter (partial visited? this)
            (vals (maze/neighbors grid loc))))
  (unvisited-neighbors [this grid loc]
    (filter (partial unvisited? this)
            (vals (maze/neighbors grid loc)))))
