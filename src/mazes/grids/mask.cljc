(ns mazes.grids.mask
  (:require [mazes.core :as maze :refer (locations neighbors link linked? annotate annotation annotations rows cols in-grid?)]
            [mazes.util.representable :as repr]))

(defprotocol PMasksGrid
  "Extra methods for dealing with masking data."

  (masks [this] "Returns the set of masked locations.")
  (masked? [this loc] "Returns true if a cell is masked."))

;; Implements a mask over another grid type.
;;
;; Masked locations (included in the masked-locs as a set)
;; are not returned by neighbors or locations.
;;
;; Note: link creation and checks are passed straight to the
;; wrapped grid, so linking operations that touch masked
;; cells *will* succeed.
(defrecord MaskedGrid [grid masked-locs]

  PMasksGrid
  (masks [_] masked-locs)
  (masked? [_ loc]
    (contains? masked-locs loc))

  maze/PGrid
  (locations [this]
    (filterv #(not (masked? this %))
             (locations grid)))
  (neighbors [this loc]
    (filter #(not (masked? this %))
            (neighbors grid loc)))
  (link [_ loc1 loc2]
    (MaskedGrid. (link grid loc1 loc2) masked-locs))
  (linked? [_ loc1 loc2]
    (linked? grid loc1 loc2))

  maze/PBoundedGrid
  (in-grid? [this loc]
    (if (masked? this loc)
      false
      (in-grid? grid loc)))

  maze/PAnnotateCells
  (annotate [_ loc data]
    (MaskedGrid. (annotate grid loc data) masked-locs))
  (annotation [_ loc key]
    (annotation grid loc key))
  (annotations [_ loc]
    (annotations grid loc))

  maze/PSquareGrid
  (rows [_] (rows grid))
  (cols [_] (cols grid))

  repr/Vector2D
  (->2d-vector [_]
    (letfn [(nilify [grid loc]
              (assoc-in grid loc nil))]
      (let [parent (repr/->2d-vector grid)]
        (reduce nilify parent masked-locs)))))
