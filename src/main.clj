(ns main
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [midi]
            [devices]
            [draw]
            [notes]
            [clojure.algo.generic.functor :as funct]))

(def size {:x 646 :y 400})

(defn initial-state [device-names]
  (reduce 
    #(assoc %1 %2 {})
    {}
    device-names))

(defn merge-device-state [old-state new-state]
  (merge-with notes/merge-notemaps old-state new-state))

(defn set-positions [state]
  (let [get-position #(draw/position % (size :x) (size :y))
        or-position  #(or (% :position) (get-position %))
        set-position #(into % {:position (or-position %)})]
    (update-in state [:piano] (partial funct/fmap set-position))))

; TODO: make this generic for devices
(defn generate-state [state events]
  (let [new-state (notes/->map events)]
    (->> events
      (notes/->map)
      (update-in state [:piano] merge-device-state)
      (set-positions))))

(defn draw-state [state]
  (doseq [[n e] (seq (state :piano))]
    (draw/circle n e)))


; v --- quil functions --- v
    (defn setup []
      (q/frame-rate 60)
      (q/background 200)
      (devices/register! :piano)
      (initial-state (devices/names))) ; return initial state

    (defn update-state [state]
      (let [events (devices/pull-events! :piano)]
        (generate-state state events)))

    (defn draw [state]
      (draw-state state))

    (q/defsketch example 
      :middleware [m/fun-mode]
      :title "midi-evil"
      :settings #(q/smooth 2) ; anti-aliasing
      :setup setup
      :update update-state
      :draw draw
      :size [(size :x) (size :y)])


; (defn adam-handler [inst]
;  (let [notes* (atom {})]
;    (fn [event ts]
;      (cond
;        (= (:cmd event) :note-on)
;        (swap! notes* assoc (:note event) (inst :note (:note event) :velocity (:vel event)))

;        (or (= (:cmd event) :note-off)
;            (and (not (zero? (:note event)))
;                 (zero? (:vel event))))

;        (if-let [note-id (get @notes* (:note event))]
;          (ctl note-id :gate 0))))))
