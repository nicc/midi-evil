(ns draw
  (:require [quil.core :as q]))

(defn circle [k note]
  (q/stroke 0)
  (q/stroke-weight 4)
  (q/fill (q/random 255))
  
  (let [diam note       
        x    (q/random (q/width))
        y    (q/random (q/height))]
    (q/ellipse x y diam diam)))