(ns mazes.util.representable
  (:require [mazes.core :as maze]))

(defprotocol Vector2D
  "Allows various grid types to marshall themselves into
  a common 2D vector format that can be used by display routines.

  The first vector will contain rows, and each row vector will
  contain columns.  Thus, (get-in <result> [2 3]) will retrieve
  the cell in the third row and the fourth column.

  If a cell is not in the grid (e.g., it is masked), its entry
  in the grid will be nil.

  At the time of this writing, only square-celled grids were
  represented visually, so this expects to deal only in square
  cells.

  Each cell will be expected to have keys :north, :south, :east,
  and :west, which will be true if there is a wall on that side
  and false if that side is a passage."
  (->2d-vector [this]))
