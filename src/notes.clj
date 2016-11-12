(ns notes)

(defn ->map [events]
  (->> events
    (reduce #(-> %1 (conj (:note %2)) (conj %2)) [])
    (apply hash-map)))