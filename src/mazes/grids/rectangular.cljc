(ns mazes.grids.rectangular
  (:require [mazes.core :as maze]
            [mazes.util.representable :as repr]))

(def north [-1 0])
(def south [+1 0])
(def east  [0 +1])
(def west  [0 -1])

(def directions [north south east west])
(def sides [:north :south :east :west])

(def side->direction
  "Given a side (e.g., :north), returns the direction
  (e.g., [-1 0]) that that represents."
  (let [mapping (reduce merge
                        (map (partial assoc {})
                             sides directions))]
    (fn [side]
      (get mapping side))))

(def direction->side
  "Given a direction (e.g., [-1 0]), returns the side
  (e.g., :north) that that represents."
  (let [mapping (reduce merge
                        (map (partial assoc {})
                             directions sides))]
    (fn [direction]
      (get mapping direction))))

(defn- direction-to
  "Given two locations, returns the direction
  from the first to the second.

  Note that the returned directions can have a magnitude
  greater than one, which cannot represent a connection
  under the constraints of this grid.

  You should verify that the location you get back makes sense."
  [loc1 loc2]
  (mapv - loc2 loc1))

(defrecord RectangularCell [north south east west])

(defrecord RectangularGrid [rows cols grid]
  maze/PGrid
  (locations [_]
    (apply concat
           (map (fn [r]
                  (map (partial conj [r]) (range cols)))
                (range rows))))

  (neighbors [this loc]
    (let [locate (map (partial mapv + loc))
          constrain (filter (partial maze/in-grid? this))]
      (transduce (comp locate constrain)
                 conj
                 directions)))

  (linked? [_ loc1 loc2]
    (if-let [side (direction->side (direction-to loc1 loc2))]
      (not (get (get-in grid loc1) side))
      false))

  (link [_ loc1 loc2]
    (let [fdirection (direction-to loc1 loc2)
          bdirection (mapv - fdirection)
          fcell (get-in grid loc1)
          bcell (get-in grid loc2)]
      (when-let [fside (direction->side fdirection)]
        (let [bside (direction->side bdirection)
              new-grid (-> grid
                           (assoc-in loc1 (assoc fcell fside false))
                           (assoc-in loc2 (assoc bcell bside false)))]
          (RectangularGrid. rows cols new-grid)))))

  maze/PSquareGrid
  (rows [_] rows)
  (cols [_] cols)

  maze/PBoundedGrid
  (in-grid? [_ [row col]]
    (and (< -1 row rows)
         (< -1 col cols)))

  maze/PAnnotateCells
  (annotate [_ loc data]
    (let [cell (get-in grid loc)]
      (RectangularGrid. rows cols (assoc-in grid loc (merge cell data)))))
  (annotation [_ loc key]
    (get-in grid (conj loc key)))
  (annotations [_ loc]
    (get-in grid loc))

  repr/Vector2D
  (->2d-vector [_] grid))

(defn make-grid [rows cols]
  (letfn [(make-row [_]
            (mapv (fn [_]
                    (->RectangularCell true true true true))
                  (range cols)))]
    (let [grid (mapv make-row (range rows))]
      (->RectangularGrid rows cols grid))))
