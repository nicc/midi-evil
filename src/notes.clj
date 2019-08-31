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
    (= (event :cmd) 144) { :attack (event :vel) } ; note start
    (= (event :cmd) 128) { :release (event :vel) } ; note end
    :else {}))

(defn event->notemap [event]
  (let [note (event :note)]
    (conj {:note note} (velocity event))))

; stack :attack, but use last :release
; TODO: this is hideous. Make it nicer
(defn merge-notemaps [first-note second-note]
  (if-not
    (= (first-note :note) (second-note :note))
    (throw (Exception. "Don't know how to merge mismatched notes")))

  (let [note (first-note :note)
        position (first-note :position)
        attack1 (or (first-note :attack) 0) ; TODO: fmap
        attack2 (or (second-note :attack) 0) ; TODO: fmap
        attack attack2
        release (or (second-note :release) nil)]
    (into {} (filter second {:attack attack :release release :note note :position position}))))

(defn ->map [events]
  (let [merge-f #(merge-with merge-notemaps %1 {(%2 :note) (event->notemap %2)})]
    (reduce merge-f {} events)))
