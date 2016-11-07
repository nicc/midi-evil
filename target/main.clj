(ns main
  (:require [quil.core :as q]
            [midi]))

(def launchpad (midi/midi-in))
(def notes (atom {}))

(defn print-handler [notelog]
  (fn [event ts]
    (swap! notelog assoc :last-e event :last-ts ts)))

(defn setup []
  (q/frame-rate 1)                    ;; Set framerate to 1 FPS
  (q/background 200)
  (midi/midi-handle-events launchpad (print-handler notes)))

(defn draw []
  (q/stroke (q/random 255))             ;; Set the stroke colour to a random grey
  (q/stroke-weight (q/random 10))       ;; Set the stroke thickness randomly
  (q/fill (q/random 255))               ;; Set the fill colour to a random grey

  (if-let [e (:last-e @notes)]
    (let [diam   (e :note)  
          x      (q/random (q/width))
          y      (q/random (q/height))]
      (q/ellipse x y diam diam))))
        
         

(q/defsketch example                  ;; Define a new sketch named example
  :title "Oh so many grey circles"    ;; Set the title of the sketch
  :settings #(q/smooth 2)             ;; Turn on anti-aliasing
  :setup setup                        ;; Specify the setup fn
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