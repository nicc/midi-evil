(ns draw
  (:require [quil.core :as q]
            [colours]
            [clojure.pprint :as pp]))

(defn circle [k note]
  (q/stroke 0)
  (q/stroke-weight 4)
  (let [rgba (colours/note->rgba-vector note)]
    ; (spit "./event.log" (with-out-str (pp/pprint rgba)) :append true)
    (apply q/fill rgba))
  
  (let [diam 40
        x    (q/random (q/width))
        y    (q/random (q/height))]
    (q/ellipse x y diam diam)))