(ns draw
  (:require [quil.core :as q]
            [colours]))

(defn new-id []
  (str (java.util.UUID/randomUUID)))

(defn circle [k note]
  (q/stroke 0)
  (q/stroke-weight 4)
  (let [rgba (colours/note->rgba-vector note)]
    (apply q/fill rgba))
  
  (let [diam 40
        x    (first (note :position))
        y    (second (note :position))]
    (q/ellipse x y diam diam)))

(defn position [_ x y]
  (let [x (rand-int x) ; currently random
        y (rand-int y)] ; currently random
    [x y]))