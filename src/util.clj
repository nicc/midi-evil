(ns util)

(defn map-with-memo [f memo coll]
  (reduce
    (fn [memo item] 
        (-> 
          memo
          (first)
          (f item)
          (update 1 (partial conj (second memo)))))
    [memo []]
    coll))