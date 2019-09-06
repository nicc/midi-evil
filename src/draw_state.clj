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
    {:diameter (note :attack)})

(defmulti update-one-elem-params exists?)
  (defmethod update-one-elem-params false [params-state [elem-id note]]
    (let [[x y]        (draw/new-position note)
          base-params  {:ttl       (get-ttl note)
                       :x         x
                       :y         y
                       :colour    (colours/note->rgba-vector note)}]
      (assoc params-state elem-id (merge base-params (get-draw-params note)))))
  
  (defmethod update-one-elem-params true [params-state [elem-id note]]
    (let [params  (-> elem-id
                    (params-state)
                    (assoc :ttl (get-ttl note))
                    (assoc :colour (colours/note->rgba-vector note))
                    (merge (get-draw-params note)))]
      (assoc params-state elem-id params)))

(defn update-elem-params [params-state elem-notes]
  (reduce update-one-elem-params params-state elem-notes))


; ----- MUTATOR FNS -----
(defn update-mutator-fn [mutators-state [elem-id elem]]
  (assoc mutators-state elem-id (mutators/elem-> elem)))

(defn update-mutator-fns [mutators-state elems]
  (reduce update-mutator-fn mutators-state elems))
                   
                   
; ----- DRAW FNS ----- 
(defn update-draw-fn [draw-state [elem-id note]]
  (assoc draw-state elem-id (draw/note-> note)))

(defn update-draw-fns [draw-state elem-notes]
  (reduce update-draw-fn draw-state elem-notes))