(ns notes
  (:require [midi]))

(def note-names [:C :C# :D :D# :E :F :F# :G :G# :A :A# :B ])

(defn ->name [note]
  (let [midi-note (note :note)
        note-num (mod midi-note (count note-names))]
    (note-names note-num)))

(defn ->octave [note]
  (let [midi-note (note :note)]
    (quot midi-note (count note-names))))

(defn velocity [event]
  (cond
    (= (event :cmd) 144) { :amp (event :vel) } ; note start
    (= (event :cmd) 128) { :decay (event :vel) } ; note end
    :else {}))

(defn event->notemap [event]
  (let [note (event :note)]
    (conj {:note note} (velocity event))))

; stack :amp, but use last :decay
; TODO: this is hideous. Make it nicer
(defn merge-notemaps [first-note second-note]
  (if-not
    (= (first-note :note) (second-note :note))
    (throw (Exception. "Don't know how to merge mismatched notes")))

  (let [note (first-note :note)
        amp1 (or (first-note :amp) 0)
        amp2 (or (second-note :amp) 0)
        amp (+ amp1 amp2)
        decay (or (second-note :decay) nil)]
    (into {} (filter second {:amp amp :decay decay :note note}))))

(defn ->map [events]
  (let [merge-f #(merge-with merge-notemaps %1 {(%2 :note) (event->notemap %2)})]
    (reduce merge-f {} events)))
