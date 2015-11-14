(ns mazes.web.components.cell
  (:require
   [clojure.string :as str]
   [om.core :as om]
   [sablono.core :as html :refer-macros [html]]))

(def ^:dynamic default-border-styles
  {:border-size "1px"
   :border-style "solid"})

(def ^:dynamic base-border-color [192, 192, 192, 255])

(defn no-border-color []
  (assoc base-border-color 3 0))

(defn- color->css
  "Converts a sequence describing a color to an rgba() CSS value."

  [color]
  {:pre (= (count color) 4)}
  (str "rgba(" (str/join "," color) ")"))
(defn- css->side
  "Converts a maze side name to a CSS side name."
  [side]
  (side {:top :north
         :bottom :south
         :right :east
         :left :west}))

(defn- border-styles [cell]
  (letfn [(css-color [side] (color->css (if (get cell side)
                                          base-border-color
                                          (no-border-color))))]
    (reduce (fn [m side] (assoc m (keyword (str "border-" (name side) "-color")) (css-color (css->side side))))
            default-border-styles
            #{:top :left :right :bottom})))

(defn- extra-styles [cell]
  (letfn [(css-color [distance]
            (color->css [0, 0, (max 0 (- 255 (int (* distance 3)))), 255]))]
    (when-let [distance (get cell :mazes.analysis.dijkstra/distance)]
      {:background-color (css-color distance)})))

(defn cell [data owner]
  (reify
    om/IRender
    (render [this]
      (let [cell (:cell data)
            size (str (:size data) "px")]
        (html
         [:td {:class "cell"
               :style (merge {:width size :height size}
                             (border-styles cell)
                             (extra-styles cell))}])))))
