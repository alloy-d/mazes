(ns mazes.util.visitor
  (:require [clojure.set :as set]
            [mazes.core :as maze]))

(defprotocol PVisit
  (visit [this loc] "Marks a location as visited.")
  (visited? [this loc] "Checks if a location is visited."))

(defprotocol PTrackUnvisited
  (unvisited? [this loc] "Checks if a location is unvisited."))

(defprotocol PListUnvisited
  (unvisited [this] "Returns a set of unvisited locations.")
  (count-unvisited [this] "Returns the count of unvisited locations."))

(defprotocol PFilterNeighbors
  (visited-neighbors [this grid loc]
    "Returns a list of visited neighbors for a locations on a grid.")
  (unvisited-neighbors [this grid loc]
    "Returns a list of unvisited neighbors for a location on a grid."))

;; A Visitor keeps track of visited locations by
;; building up a set of locations that it has visited.
;;
;; On a "fresh" grid, a Visitor should be initialized
;; with an empty set, to represent that no locations
;; have yet been visited.
(defrecord Visitor [visited]
  PVisit
  (visit [_ loc]
    (Visitor. (conj visited loc)))
  (visited? [_ loc]
    (contains? visited loc))

  PTrackUnvisited
  (unvisited? [this loc]
    (not (visited? this loc)))

  PFilterNeighbors
  (visited-neighbors [this grid loc]
    (filter (partial visited? this)
            (vals (maze/neighbors grid loc))))
  (unvisited-neighbors [this grid loc]
    (filter (partial unvisited? this)
            (vals (maze/neighbors grid loc)))))

;; An Ununvisitor keeps track of unvisited locations
;; by removing visited locations from the set of locations
;; that have not been visited.
;;
;; On a "fresh" grid, an Ununvisitor should be initialized
;; with the set of *all* locations in the grid, signifying
;; that none have yet been visited.
(defrecord Ununvisitor [unvisited]
  PVisit
  (visit [_ loc]
    (Ununvisitor. (disj unvisited loc)))
  (visited? [this loc]
    (not (unvisited? this loc)))

  PTrackUnvisited
  (unvisited? [_ loc]
    (contains? unvisited loc))

  PListUnvisited
  (unvisited [_] unvisited)
  (count-unvisited [_] (count unvisited)))
