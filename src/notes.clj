(ns notes
  (:require [midi]))

(defn note-viz [event]
  (cond
    (= (event :cmd) 144) { :amp (event :vel) } ; note start
    (= (event :cmd) 128) { :decay (event :vel) } ; note end
    :else {}))

; stack :amp, but use last :decay
; TODO: this is hideous. Make it nicer
(defn note-viz-merge [first-viz second-viz]
  (let [amp1 (or (first-viz :amp) 0)
        amp2 (or (second-viz :amp) 0)
        amp (+ amp1 amp2)
        decay (or (second-viz :decay) nil)]
    (into {} (filter second {:amp amp :decay decay}))))

(defn ->map [events]
  (let [merge-f #(merge-with note-viz-merge %1 {(:note %2) (note-viz %2)})]
    (reduce merge-f {} events)))
