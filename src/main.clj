(ns main
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [midi]
            [devices]
            [draw]
            [notes]
            [clojure.algo.generic.functor :as funct]
            [java-time :as jt]))

(def size {:x 646 :y 400})

(defn initial-state [device-names]
  (reduce 
    #(assoc %1 %2 {})
    {}
    device-names))

(defn merge-device-state [old-state new-state]
  (merge-with notes/merge-notemaps old-state new-state))

(defn set-positions [state]
  (let [get-position #(draw/position % (size :x) (size :y))
        or-position  #(or (% :position) (get-position %))
        set-position #(into % {:position (or-position %)})]
    (update-in state [:piano] (partial funct/fmap set-position))))

; TODO: make this generic for devices
(defn generate-state [state events]
  (->> events
    (notes/->map)
    (update-in state [:piano] merge-device-state)
    
    (set-positions)))

; ----- QUIL UPDATE FN -----
; apply update-piano-state to piano-state and note events (sets :piano)
; map piano events to have guid keys
; update note->element-id
; apply register-new-events to draw-state and note events (sets :draw-state, :update-fns, and :draw-fns)
; fapply :update-fns to :draw-state (sets :draw-state)
; remove any ttl 0 elements


; ----- QUIL DRAW FN -----
; fapply :draw-fns to :draw-state


; ----- update-piano-state -----
; add any new notes
; remove any released notes
; what about non-note values?
; still keyed by note? probs not. just a vector of current... stuff?


; ----- register-new-events -----
; adds any missing element guids
; updates any existing element guids (e.g. note off should set release and a ttl)
; registers update fns
; registers draw fns
; set new update / draw fns for released notes??


; ----- inner update fns -----
; should count down ttl
; control and modulate params to draw fns
; polymorphism on held / released notes??



(defn draw-state [state]
  (doseq [[n e] (seq (state :piano))]
    (draw/circle n e)))


; v --- quil functions --- v
    (defn setup []
      (q/frame-rate 60)
      (q/background 200)
      (devices/register! :piano)
      (initial-state (devices/names))) ; return initial state

    (defn update-state [state]
      (let [events (devices/pull-events! :piano)]
        (generate-state state events)))

    (defn draw [state]
      (draw-state state))

    (q/defsketch example 
      :middleware [m/fun-mode]
      :title "midi-evil"
      :settings #(q/smooth 2) ; anti-aliasing
      :setup setup
      :update update-state
      :draw draw
      :size [(size :x) (size :y)])

