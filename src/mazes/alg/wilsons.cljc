(ns mazes.alg.wilsons
  (:require [mazes.core :as maze :refer (link locations neighbors)]
            [mazes.util.visitor :refer (make-ununvisitor visit unvisited visited?)]
            [clojure.set :as set]))

(defn- link-path [grid path]
  (if (< 1 (count path))
    (let [passages (map (partial conj [])
                        path
                        (drop 1 path))]
      (reduce (fn [grid [loc1 loc2]]
                (link grid loc1 loc2))
              grid passages))))

(defn- build-path
  ([grid visitor]
   (if (< 0 (count (unvisited visitor)))
     (build-path grid visitor [(rand-nth (seq (unvisited visitor)))])
     grid))

  ([grid visitor current-path]
   (let [prev-loc (first current-path)
         next-loc (rand-nth (neighbors grid prev-loc))]
     (cond (some #(= next-loc %1) current-path)
           (recur grid visitor (drop-while #(not (= %1 next-loc)) current-path))

           (visited? visitor next-loc)
           (build-path (link-path grid (cons next-loc current-path))
                       (reduce visit visitor current-path))

           :else
           (recur grid visitor (cons next-loc current-path))))))

(defn on [grid]
  (let [locs (locations grid)
        initially-visited (rand-nth locs)]
    (build-path grid
                (visit (make-ununvisitor grid) initially-visited))))
