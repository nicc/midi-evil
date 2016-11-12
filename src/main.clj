(ns main
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [midi]
            [devices]
            [draw]
            [notes]))

(defn initial-state []
  (reduce 
    #(assoc %1 %2 {})
    {}
    (devices/names)))

(defn setup []
  (q/frame-rate 60)
  (q/background 200)
  (devices/register! :piano)
  (initial-state)) ; return initial state

(defn update-state [state]
  (->>
    (devices/pull-events! :piano)
    (notes/->map)
    (hash-map :piano)
    (merge state)))

(defn draw [state]
  ; (throw (Exception. (with-out-str (pp/pprint state))))
  (doseq [[n e] (seq (state :piano))]
    (draw/circle n e)))

(q/defsketch example 
  :middleware [m/fun-mode]
  :title "midi-evil"
  :settings #(q/smooth 2) ; anti-aliasing
  :setup setup
  :update update-state
  :draw draw
  :size [646 400])


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
