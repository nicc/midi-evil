(ns draw
  (:require [quil.core :as q]
            [colours]))

(def size {:x 646 :y 400})

(defn circle [k note]
  (q/stroke 0)
  (q/stroke-weight 4)
  (let [rgba (colours/note->rgba-vector note)]
    (apply q/fill rgba))
  
  (let [diam 40
        x    (first (note :position))
        y    (second (note :position))]
    (q/ellipse x y diam diam)))

(defn new-position [_]
  (let [x (rand-int (size :x))
        y (rand-int (size :y))]
    [x y]))