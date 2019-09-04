(ns draw-state
  (:require [draw]
            [colours]
            [mutators]))

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
  (defmethod update-one-elem-params false [params-state [elem-id note]]
    (let [params (into []
                   (concat 
                     [(get-ttl note)]
                     (draw/new-position note)
                     [(colours/note->rgba-vector note)]
                     (get-draw-params note)))]
      (assoc params-state elem-id params)))
  
  (defmethod update-one-elem-params true [params-state [elem-id note]]
    (let [params  (-> (params-state elem-id)
                    (assoc 0 (get-ttl note))
                    (assoc 3 (colours/note->rgba-vector note))
                    (assoc 4 (note :attack)))]
      (assoc params-state elem-id params)))

(defn update-elem-params [params-state elem-notes]
  (reduce update-one-elem-params params-state elem-notes))


; ----- MUTATOR FNS -----
(defn update-mutator-fn [mutators-state [elem-id note]]
  (assoc mutators-state elem-id (mutators/note-> note)))

(defn update-mutator-fns [mutators-state elem-notes]
  (reduce update-mutator-fn mutators-state elem-notes))
                   
; ----- DRAW FNS ----- 
(defn update-draw-fn [draw-state [elem-id note]]
  (assoc draw-state elem-id (draw/note-> note)))

(defn update-draw-fns [draw-state elem-notes]
  (reduce update-draw-fn draw-state elem-notes))