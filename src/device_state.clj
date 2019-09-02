(ns device-state
  (:require [notes]))

(defn clean-notes [state]
  (->> state
    (remove (comp :release second))
    (into {})))

(defn update-notes [prior events] 
  (->> events
    (notes/->notemap)
    (merge-with notes/merge-notemaps prior)
    (clean-notes)))