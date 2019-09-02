(ns draw-state
  ; (:require [java-time :as jt])
  )

(defn elem-type [note] :circle)

(defmulti update-elem (fn [state [elem-id _]] (state elem-id)))

  (defmethod update-elem nil [state [elem-id note]] ; new elem
    (let [elem (assoc note :type   (elem-type note))]   ; :tstamp (str (jt/local-date-time))
      (assoc state elem-id elem)))

  (defmethod update-elem :default [state [elem-id note]] ; existing elem
    (update-in state [elem-id] merge note)) ; merge could/should be fancier

(defn update-elems [elem-state elem-notes]
  (reduce update-elem elem-state elem-notes))


(defn update-elem-params [state elem-notes]
  
  )


(defn update-mutator-fns [state elem-notes]
  
  )

                    
(defn update-draw-fns [state elem-notes]
  
  )