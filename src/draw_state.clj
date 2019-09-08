(ns draw-state
  (:require [draw]
            [colours]
            [mutators]))

(defn elem-type [note] :circle)

(defn exists? [prior-state [elem-id _]]
  (boolean (prior-state elem-id)))

(defn- dead? [[elem-id elem]]
  (= 0 (:ttl elem)))

(defn clear-the-dead [state]
  (let [dead-elems      (keys (filter dead? (:elem-params state)))
        dead-notes      (map #(get-in state [:elems % :note]) dead-elems)
        clear-fn        #(apply dissoc (cons %2 %1))
        clear-elems-fn  (partial clear-fn dead-elems)]
    (-> state
      (update-in [:elems] clear-elems-fn)
      (update-in [:elem-params] clear-elems-fn)
      (update-in [:mutator-fns] clear-elems-fn)
      (update-in [:draw-fns] clear-elems-fn)
      (update-in [:note->element-id] (partial clear-fn dead-notes)))))

; ----- ELEMS -----
(defmulti update-elem exists?)
  (defmethod update-elem false [elem-state [elem-id note]]
    (assoc elem-state elem-id note))
  
  (defmethod update-elem true [elem-state [elem-id note]]
    (update-in elem-state [elem-id] merge note)) ; merge could/should be fancier

(defn update-elems [elem-state elem-notes]
  (reduce update-elem elem-state elem-notes))


; ----- ELEM PARAMS -----
(defn get-ttl [note]
  (if (note :release) 0 nil))

(defmulti get-draw-params :type)
  (defmethod get-draw-params :circle [note]
    {:diameter  (:attack note)
     :ttl       (get-ttl note)
     :colour    (colours/note->rgba-vector note)})

(defmulti update-one-elem-params exists?)
  (defmethod update-one-elem-params false [params-state [elem-id note]]
    (let [[x y]        (draw/new-position note)
          base-params  {:x         x
                        :y         y}]
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