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
  (q/frame-rate 1)
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
  :size [323 200])                    ;; You struggle to beat the golden ratio



  ; (midi-handle-events launchpad adam-handler)

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


; (defn setup []
;   (q/frame-rate 1)                    ;; Set framerate to 1 FPS
;   (q/background 200))                 ;; Set the background colour to
;                                       ;; a nice shade of grey.
; (defn draw []
;   (q/stroke (q/random 255))             ;; Set the stroke colour to a random grey
;   (q/stroke-weight (q/random 10))       ;; Set the stroke thickness randomly
;   (q/fill (q/random 255))               ;; Set the fill colour to a random grey

;   (let [diam (q/random 100)             ;; Set the diameter to a value between 0 and 100
;         x    (q/random (q/width))       ;; Set the x coord randomly within the sketch
;         y    (q/random (q/height))]     ;; Set the y coord randomly within the sketch
;     (q/ellipse x y diam diam)))         ;; Draw a circle at x y with the correct diameter

; (q/defsketch example                  ;; Define a new sketch named example
;   :title "Oh so many grey circles"    ;; Set the title of the sketch
;   :settings #(q/smooth 2)             ;; Turn on anti-aliasing
;   :setup setup                        ;; Specify the setup fn
;   :draw draw                          ;; Specify the draw fn
;   :size [323 200])                    ;; You struggle to beat the golden ratio