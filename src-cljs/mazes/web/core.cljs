(ns mazes.web.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require
   [mazes.core :as maze]
   [mazes.grids.rectangular :as grid :refer (make-grid)]
   [mazes.repr.ascii :refer (string-for-grid)]
   [mazes.repr.html-table :as table]
   [mazes.alg.aldous-broder :as ab]
   [mazes.alg.binary-tree :as bt]
   [mazes.alg.recursive-backtracker :as rb]
   [mazes.alg.sidewinder :as sidewinder]
   [mazes.alg.hunt-and-kill :as hak]
   [mazes.analysis.dijkstra :as analysis]
   [om.core :as om]
   [om.dom :as dom]
   [sablono.core :as html :refer-macros [html]]
   [dommy.core :as dommy :refer-macros [sel sel1]]
   [clojure.string :as str]
   [cljs.core.async :refer [poll! put! chan <! >!]]))

(enable-console-print!)
(def default-cell-size 50)

(def app-state (atom {:grid (grid/make-grid 20 40)}))

(defn clock
  "Returns a channel that produces a value at a given interval."
  ([]
   (clock 30))

  ([interval]
   (let [ticks (chan)]
     (js/setInterval #(put! ticks true) interval)
     ticks)))

(defn produce-steps [ticker modifier bases-in processed-out]
  (go-loop [steps '()]
    (if-let [new-base (poll! bases-in)]
      (recur (maze/modify-steps modifier new-base))
      (do
        (<! ticker)
        (if (seq steps)
          (do
            (>! processed-out (first steps))
            (recur (rest steps)))
          (recur '()))))))

(defn run-process-loop []
  (let [processed-grids (chan)
        base-grids (chan)
        ticker (clock)]
    (do (go-loop [next-grid (<! processed-grids)]
          (om/update! (om/ref-cursor (om/root-cursor app-state)) :grid next-grid)
          (recur (<! processed-grids)))
        (om/update! (om/ref-cursor (om/root-cursor app-state)) :base-grids base-grids)
        (put! base-grids (grid/make-grid 20 40))
        (produce-steps ticker sidewinder/Sidewinder base-grids processed-grids))))

(defn dimensions [node]
  (let [bounds (.getBoundingClientRect node)]
    {:height (.-height bounds)
     :width (.-width bounds)}))

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
            cells-high (maze/rows @grid)
            maze-height (* cells-high cell-size)]
        (dom/div #js {:style #js {:height "100%"}
                      :className "maze-canvas"}
         (dom/div
          #js {:style #js {:height maze-height}
               :className "maze"
               :dangerouslySetInnerHTML
               #js {:__html (table/represent @grid)}}))))))

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
      (run-process-loop)
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
                                       default-cell-size)
                           final-cells-high (if cells-high
                                              cells-high
                                              (Math/floor (/ (- height 1) cell-size)))
                           final-cells-wide (if cells-wide
                                              cells-wide
                                              (Math/floor (/ width cell-size)))]
                       (om/set-state! owner
                                      {:width width
                                       :height height
                                       :cell-size cell-size
                                       :cells-wide final-cells-wide
                                       :cells-high final-cells-high})
                       (put! (get @data :base-grids) (grid/make-grid final-cells-high final-cells-wide))))]
        (do
          (resize)
          (dommy/listen! js/window :resize resize))))

    om/IRenderState
    (render-state [this {:keys [:height :width :cell-size :cells-high :cells-wide]}]
      (if (and cells-high cells-wide)
        (let [maze (get data :grid)]
          (om/build dumb-maze {:grid maze :cell-size cell-size}))
        (html [:div])))))

(om/root maze-world app-state
         {:target (sel1 "#maze")})
