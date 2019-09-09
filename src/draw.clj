(ns draw
  (:require [quil.core :as q]
            [colours]))

(def size {:x 646 :y 400})

(defn apply-to-elem-params [state]
  (doseq [[elem-id fns] (:draw-fns state)]
    (doseq [f fns]
      (apply f [(get-in state [:elem-params elem-id])]))))

(defn circle [{:keys [x y colour diameter]}]
  (q/stroke 0)
  (q/stroke-weight 1)
  (apply q/fill colour)
  (q/ellipse x y diameter diameter))

(defn new-position [_]
  (let [x (rand-int (size :x))
        y (rand-int (size :y))]
    [x y]))

(defmulti note-> :type)
(defmethod note-> :circle [_] [circle])