(ns draw
  (:require [quil.core :as q]
            [colours]))

(def size {:x 646 :y 400})

(defn circle [[ttl x y rgba diam]]
  (q/stroke 0)
  (q/stroke-weight 2)
  (apply q/fill rgba)
  (q/ellipse x y diam diam))

(defn new-position [_]
  (let [x (rand-int (size :x))
        y (rand-int (size :y))]
    [x y]))

(defmulti note-> :type)
(defmethod note-> :circle [_] [circle])