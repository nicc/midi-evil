(ns main
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [midi]
            [devices]
            [draw]
            [midi-events :as events]
            [notes]))

(defn setup []
  (q/frame-rate 60)
  (q/background 200)
  (midi/midi-handle-events
    (devices/registry :launchpad)
    (events/record-fn :launchpad))
  {}) ; return initial state

(defn update-state [state]
  (->>
    (events/pull! :launchpad)
    (notes/->map)
    (hash-map :launchpad)
    (merge state)))

(defn draw [state]
  (doseq [[n e] (seq (state :launchpad))]
    ; (throw (Exception. (with-out-str (clojure.pprint/pprint e))))
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
