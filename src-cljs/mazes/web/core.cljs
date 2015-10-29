(ns mazes.web.core
  (:require
   [mazes.core :as maze :refer (make-grid)]
   [mazes.repr.ascii :refer (string-for-grid)]
   [mazes.repr.html-table :as table]
   [mazes.alg.sidewinder :as alg]
   [mazes.analysis.dijkstra :as analysis]
   [om.core :as om]
   [om.dom :as dom]
   [sablono.core :as html :refer-macros [html]]
   [dommy.core :as dommy :refer-macros [sel sel1]]
   [clojure.string :as str]))

(enable-console-print!)
(def default-cell-size 20)

(defn dimensions [node]
  (let [bounds (.getBoundingClientRect node)]
    {:height (.-height bounds)
     :width (.-width bounds)}))

(def get-maze
  (memoize
   (fn [height width gen-maze]
     (gen-maze (make-grid height width)))))

(defn dumb-maze [data owner]
  (reify
    om/IDidMount
    (did-mount [this]
      (let [node (om/get-node owner)
            bounds (.getBoundingClientRect node)
            height (.-height bounds)
            width (.-width bounds)]
        (doto js/console
          (.group "container size")
          (.log "height" height)
          (.log "width" width)
          (.groupEnd "container size"))))

    om/IRender
    (render [this]
      (let [{:keys [grid cell-size]} data
            cells-high (maze/num-rows grid)
            maze-height (* cells-high cell-size)]
        (js/console.log (str "cells high: " cells-high))
        (dom/div #js {:style #js {:height "100%"}
                      :className "maze-canvas"}
         (dom/div
          #js {:style #js {:height maze-height}
               :className "maze"
               :dangerouslySetInnerHTML
               #js {:__html (table/represent grid)}}))))))

(defn maze-world [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:height nil
       :width nil
       :cell-size default-cell-size
       :cells-wide nil
       :cells-high nil})

    om/IDidMount
    (did-mount [this]
      (let [node (om/get-node owner)
            resize (fn []
                     (let [{:keys [width height]} (dimensions node)
                           {:keys [cells-high cells-wide]} data
                           fit-width (when cells-wide
                                       (Math/floor (/ width cells-wide)))
                           fit-height (when cells-high
                                        (Math/floor (/ height cells-high)))
                           cell-size (if-let [size (apply min (filter identity [fit-width fit-height]))]
                                       size
                                       default-cell-size)]
                       (om/set-state! owner
                                      {:width width
                                       :height height
                                       :cell-size cell-size
                                       :cells-wide (if cells-wide
                                                     cells-wide
                                                     (Math/floor (/ width cell-size)))
                                       :cells-high (if cells-high
                                                     cells-high
                                                     (Math/floor (/ height cell-size)))})))]
        (do
          (resize)
          (dommy/listen! js/window :resize resize))))

    om/IRenderState
    (render-state [this {:keys [:height :width :cell-size :cells-high :cells-wide]}]
      (let [maze (get-maze cells-high cells-wide alg/on)]
        (om/build dumb-maze {:grid maze :cell-size cell-size})))))

(om/root maze-world {}
         {:target (sel1 "#maze")})
