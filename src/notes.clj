(ns notes
  (:require [midi]))

(defn note-viz [event]
  (cond
    (= (event :cmd) 144) { :amp (event :vel) } ; note start
    (= (event :cmd) 128) { :decay (event :vel) } ; note end
    :else {}))

(defn ->map [events]
  (let [merge-f #(merge-with merge %1 {(:note %2) (note-viz %2)})]
    (reduce merge-f {} events)))
