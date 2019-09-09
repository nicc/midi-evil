(ns main
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [log]
            [midi]
            [devices]
            [draw]
            [notes]
            [device-state :as dvs]
            [draw-state :as drs]
            [java-time :as jt]
            [mutators]))

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

(defn register-new-elems [state elems]
  (-> state
    (update-in [:elems] drs/update-elems elems)
    (update-in [:elem-params] drs/update-elem-params elems)
    (update-in [:mutator-fns] drs/update-mutator-fns elems)
    (update-in [:draw-fns] drs/update-draw-fns elems)))
    
  
; v --- quil functions --- v
    (defn setup []
      (q/frame-rate 60)
      (q/background 200)
      (devices/register! :piano)
      (initial-state (devices/names)))

    (defn update-state [state]
      (let [events                              (devices/pull-events! :piano)
            notemap                             (notes/->notemap events)
            [new-mappings notemap-by-elem-id]   (notes/notemap-by-elem-ids (:note->element-id state) notemap)]
        (-> state
          (update-piano-state notemap)
          (assoc :note->element-id new-mappings)
          (mutators/apply-to-elems)
          (register-new-elems notemap-by-elem-id)
          (drs/clear-the-dead))))

    (defn draw [state]
      (q/background 200)
      (draw/apply-to-elem-params state))

    (q/defsketch example 
      :middleware [m/fun-mode]
      :title "midi-evil"
      :settings #(q/smooth 2) ; anti-aliasing
      :setup setup
      :update update-state
      :draw draw
      :size [(draw/size :x) (draw/size :y)])
