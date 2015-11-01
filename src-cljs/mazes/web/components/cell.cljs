(ns mazes.web.components.cell
  (:require
   [om.core :as om]
   [sablono.core :as html :refer-macros [html]]))

(def ^:dynamic default-border-styles
  {:border-size "0.1em"
   :border-color "black"})

(defn- border-styles [cell]
  (letfn [(style [side] (if (get cell side) "solid" "transparent"))]
    (reduce (fn [m side] (assoc m (keyword (str "border-" side "-style")) (style side)))
            #{:top :left :right :bottom})))

(defn cell [data owner]
  (reify
    om/IRender
    (render [this]
      (html
       [:span {:class "cell"
               :style (border-styles (get data :cell))}]))))
