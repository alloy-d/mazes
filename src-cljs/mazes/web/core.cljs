(ns mazes.web.core
  (:require
   [mazes.core :refer (make-grid)]
   [mazes.repr.ascii :refer (string-for-grid)]
   [mazes.repr.html-table :as table]
   [mazes.alg.sidewinder :as alg]
   [mazes.analysis.dijkstra :as analysis]
   [om.core :as om]
   [om.dom :as dom]
   [sablono.core :as html :refer-macros [html]]
   [dommy.core :as dommy :refer-macros [sel sel1]]
   [clojure.string :as str]))

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
      (dom/div
       #js {:dangerouslySetInnerHTML
            #js {:__html (table/represent (:grid data))}}))))

(defn maze-world [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:height nil :width nil})

    om/IDidMount
    (did-mount [this]
      (let [node (om/get-node owner)]
        (do
          (om/set-state! owner (dimensions node))
          (dommy/listen! js/window :resize
                         #(om/set-state! owner (dimensions node))))))

    om/IRenderState
    (render-state [this {:keys [:height :width]}]
      (let [maze (get-maze (int (/ height 50)) (int (/ width 50)) alg/on)]
        (om/build dumb-maze {:grid maze})))))

(om/root maze-world {}
         {:target (sel1 "#maze")})
