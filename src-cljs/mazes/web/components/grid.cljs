(ns mazes.web.components.grid
  (:require
   [mazes.util.representable :refer (->2d-vector)]
   [mazes.web.components.cell :refer (cell)]
   [om.core :as om]
   [sablono.core :as html :refer-macros [html]]))

(defn grid [data owner]
  (reify
    om/IRender
    (render [this]
      ;(println "representing grid")
      (let [grid (->2d-vector @(:grid data))
            cell-size (:cell-size data)]
        (letfn [(represent-cell [cell-data]
                  ;(println "representing cell" cell-data)
                  (om/build cell {:cell cell-data :size cell-size}))
                (represent-row [row]
                  ;(println "representing row" row)
                  (into [:tr {:class "grid-row"}]
                        (mapv represent-cell row)))]
          (html
           [:table {:class "maze"
                    :style {:width (str (* cell-size (count (first grid))) "px")
                            :height (str (* cell-size (count grid)) "px")}}
            (into [:tbody]
                  (mapv represent-row grid))]))))))
