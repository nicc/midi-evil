(ns main
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [midi]))

(def launchpad (midi/midi-in))
(def device-events (atom {:launchpad []}))
(def devices {:launchpad launchpad}) 

(defn record-events-fn [device-name]
  (fn [event ts]
    (swap! ; TODO: add timestamp to event
      device-events
      assoc device-name (conj (@device-events device-name) event))))

(defn pull-events! [device-name]
  (let [events (@device-events device-name)]
    (swap!
      device-events
      assoc device-name [])
    events))

(defn generate-note-map [events]
  (->> events
    (reduce #(-> %1 (conj (:note %2)) (conj %2)) [])
    (apply hash-map)))

(defn draw-note-circle [note _]
  (let [diam (q/random note)       
        x    (q/random (q/width))
        y    (q/random (q/height))]
    (q/ellipse x y diam diam)))

(defn setup []
  (q/frame-rate 60)
  (q/background 200)
  (midi/midi-handle-events launchpad (record-events-fn :launchpad))
    {})

(defn update-state [state]
  (->>
    (pull-events! :launchpad)
    (generate-note-map)
    (hash-map :launchpad)
    (merge state)))

(defn draw [state]
  (q/stroke 0)  
  (q/stroke-weight 4)
  (q/fill (q/random 255))

  (doseq [[n e] (seq (state :launchpad))]
    ; (throw (Exception. (with-out-str (clojure.pprint/pprint e))))
    (draw-note-circle n e)))

(q/defsketch example                  ;; Define a new sketch named example
  :middleware [m/fun-mode]
  :title "Oh so many grey circles"    ;; Set the title of the sketch
  :settings #(q/smooth 2)             ;; Turn on anti-aliasing
  :setup setup                        ;; Specify the setup fn
  :update update-state
  :draw draw                          ;; Specify the draw fn
  :size [646 400])                    ;; You struggle to beat the golden ratio


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
