(ns mazes.alg.wilsons
  (:require [mazes.core :as maze]
            [clojure.set :as set]))

(defn visit-in [grid loc]
  (let [cell (get-in grid loc)]
    (assoc-in grid loc (assoc cell ::visited true))))

(defn visited? [grid loc]
  (get (get-in grid loc) ::visited))

(defn link-path-in [grid path]
  (if (> (count path) 1)
    (do ;(println "linking" path)
        (let [[loc1 loc2] (take 2 path)
              next-grid (maze/link grid loc1 loc2)]
          (recur next-grid (rest path))))
    grid))

(defn build-path [grid unvisited visited current-path]
  {:pre [(not (empty? visited))]}

  (cond
    (empty? unvisited)
    grid

    (empty? current-path)
    (recur grid unvisited visited [(rand-nth (seq unvisited))])

    :else
    (let [prev-loc (first current-path)
          next-loc (rand-nth (vals (maze/neighbors grid prev-loc)))]
      (cond
        (some #(= next-loc %1) current-path)
        (let [truncated-path (vec (drop-while #(not (= %1 next-loc)) current-path))]
          (recur grid unvisited visited truncated-path))

        (contains? visited next-loc)
        (let [grid (link-path-in grid (cons next-loc current-path))
              visited (into visited current-path)
              unvisited (set/difference unvisited (set current-path))]
          (recur grid unvisited visited []))

        :else
        (recur grid unvisited visited (vec (cons next-loc current-path)))))))

(defn on [grid]
  (let [locations (maze/locations grid)
        unvisited (set locations)
        initially-visited (rand-nth locations)]
    (build-path grid
                (disj unvisited initially-visited)
                (conj #{} initially-visited)
                [])))
