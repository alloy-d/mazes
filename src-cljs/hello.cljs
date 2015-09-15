(ns hello
  (:require [mazes.core :refer (make-grid)]
            [mazes.repr.ascii :refer (string-for-grid)]
            [mazes.alg.sidewinder :as alg]
            [clojure.string :as str]))

(let [grid (make-grid 26 44)]
  (.write js/document (str/join ["<pre>"
                                 (string-for-grid (alg/on grid))
                                 "</pre>"])))
