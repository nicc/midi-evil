(ns draw-state
  (:require [draw]))

(defn elem-type [note] :circle)

(defn exists? [prior-state [elem-id _]]
  (boolean (prior-state elem-id)))

; ----- ELEMS -----
(defmulti update-elem exists?)
  (defmethod update-elem false [elem-state [elem-id note]]
    (let [elem (assoc note :type (elem-type note))]   ; :tstamp (str (jt/local-date-time))
      (assoc elem-state elem-id elem)))
  
  (defmethod update-elem true [elem-state [elem-id note]]
    (update-in elem-state [elem-id] merge note)) ; merge could/should be fancier

(defn update-elems [elem-state elem-notes]
  (reduce update-elem elem-state elem-notes))


; ----- ELEM PARAMS -----
(defn get-ttl [note]
  (if (note :release) 0 nil))

(defmulti get-draw-params :type)
  (defmethod get-draw-params :circle [note]
    [(note :attack)])

(defmulti update-one-elem-params exists?)
  (defmethod update-one-elem-params false [param-state [elem-id note]]
    (let [params (into []
                   (concat 
                     [(get-ttl note)] 
                     (draw/new-position note) 
                     (get-draw-params note)))]
      (assoc param-state elem-id params)))
  
  (defmethod update-one-elem-params true [param-state [elem-id note]]
    (let [params  (-> (param-state elem-id) 
                    (assoc 0 (get-ttl note))
                    (assoc 3 (note :attack)))]
      (assoc param-state elem-id params)))

(defn update-elem-params [param-state elem-notes]
  (reduce update-one-elem-params param-state elem-notes))


; ----- MUTATOR FNS -----
(defn update-mutator-fns [state elem-notes]
  
  )

                   
; ----- DRAW FNS ----- 
(defn update-draw-fns [state elem-notes]
  
  )