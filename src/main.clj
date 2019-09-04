(ns main
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [midi]
            [devices]
            [draw]
            [notes]
            [device-state :as dvs]
            [draw-state :as drs]
            [clojure.algo.generic.functor :as funct]
            [java-time :as jt]))

(defn initial-state [device-names]
  (reduce 
    #(assoc %1 %2 {})
    {:elems            {}
     :elem-params      {}
     :mutator-fns      {}
     :draw-fns         {}
     :note->element-id {}}
    device-names))

(defn update-piano-state [state notemap]
  (update-in state [:piano] dvs/update-notes notemap))

(defn register-new-events [state elem-notes] 
  (update-in state [:elems] drs/update-elems elem-notes)
  ; update elem-params
  ; update mutator-fns
  ; update draw-fns
  )
    
  
; v --- quil functions --- v
    (defn setup []
      (q/frame-rate 60)
      (q/background 200)
      (devices/register! :piano)
      (initial-state (devices/names))) ; return initial state

    (defn update-state [state]
      (let [events                              (devices/pull-events! :piano)
            notemap                             (notes/->notemap events)
            [new-mappings notemap-by-elem-id]   (notes/notemap-by-elem-ids (:note->element-id state) notemap)]
        (-> state
          (update-piano-state notemap)
          (assoc :note->element-id new-mappings)
          ; apply mutator-fns to elem-params
          (register-new-events notemap-by-elem-id)          
          ; clear dead things??
          )))

    (defn draw [state]
      (doseq [[n e] (seq (state :piano))]
        (draw/circle n e)))

    (q/defsketch example 
      :middleware [m/fun-mode]
      :title "midi-evil"
      :settings #(q/smooth 2) ; anti-aliasing
      :setup setup
      :update update-state
      :draw draw
      :size [(draw/size :x) (draw/size :y)])
    


; (def sample-state {
;   :piano            {42 {:attack 70 :note 42}}
;   :elems            {"2ddbe992-7346-41d1-b5a3-7e2dbf541513" {:tstamp "2019-08-26T12:34:18.679"
;                                                              :ttl 50
;                                                              :attack 70 
;                                                              :release 15
;                                                              :note 42
;                                                              :type :circle}}
;   :elem-params      {"2ddbe992-7346-41d1-b5a3-7e2dbf541513" [12 4 :red]} ; ??
;   :mutator-fns      {"2ddbe992-7346-41d1-b5a3-7e2dbf541513" #{}}
;   :draw-fns         {"2ddbe992-7346-41d1-b5a3-7e2dbf541513" #{}}
;   :note->element-id {42 "2ddbe992-7346-41d1-b5a3-7e2dbf541513"}})


; ----- QUIL UPDATE FN -----
; [x] apply update-piano-state to piano-state and note events (sets :piano)
; [x] map piano events to have guid keys
; [x] update note->element-id
; [ ] apply register-new-events to draw-state and note events (sets :draw-state, :mutator-fns, and :draw-fns)
; [ ] fapply :mutator-fns to :draw-state (sets :draw-state)
; [ ] remove any ttl 0 elements


; ----- QUIL DRAW FN -----
; [ ] fapply :draw-fns to :draw-state


; ----- update-piano-state -----
; [ ] add any new notes
; [ ] remove any released notes
; [ ] what about non-note values?
; [ ] still keyed by note? probs not. just a vector of current... stuff?


; ----- register-new-events -----
; [ ] adds any missing element guids
; [ ] updates any existing element guids (e.g. note off should set release and a ttl)
; [ ] registers update fns
; [ ] registers draw fns
; [ ] set new update / draw fns for released notes??


; ----- inner update fns -----
; [ ] should count down ttl
; [ ] control and modulate params to draw fns
; [ ] polymorphism on held / released notes??


