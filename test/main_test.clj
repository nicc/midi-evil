(ns main-test
  (:use [clojure.test]
        [uncomplicate.fluokitten core jvm])
  (:require [main]))

(def keydown-middle-c [{:chan 0 :cmd 144 :note 60 :vel 45 :data1 60 :data2 45}])
(def keyup-middle-c [{:chan 0 :cmd 128 :note 60 :vel 40 :data1 60 :data2 40}])

(def sample-state {
  :piano { 42 { :attack 70 :note 42 } }
  :draw-state { "2ddbe992-7346-41d1-b5a3-7e2dbf541513" { :tstamp "2019-08-26T12:34:18.679"
                                                         :ttl 50
                                                         :blah :blah } }
  :update-fns { "2ddbe992-7346-41d1-b5a3-7e2dbf541513" #{} }
  :draw-fns { "2ddbe992-7346-41d1-b5a3-7e2dbf541513" #{} }
  :note->element-id { 42 "2ddbe992-7346-41d1-b5a3-7e2dbf541513" } })
  

(deftest initial-state
  (is (= {:piano {}} (main/initial-state [:piano]))))

; TODO: this gives a very basic sense of how I'd like to use applicatives to create 
;       mutating functions that can be assigned to draw state. A note event could generate
;       a visual element that gets its own little data and function context. This would 
;       allow for more dynamic visuals with fading and movement. It should also neatly separate 
;       note state (which generates visual elements) from the lingering draw state required by 
;       ongoing visual elements. e.g. a high velocity note could fade more slowly, or 
;       we could parse out chords to render tonal colourscapes that are independent of 
;       individual note elements, etc.
(deftest simple-applicative
  (let [state { :piano { 42 { :attack 70 :release 22 :note 42 }}}
        half     #(/ % 2)
        mutators { :piano { 42 { :attack half :release half }}}
        expected-state { :piano { 42 { :attack 35 :release 11 :note 42 }}}]

    (is (=
          expected-state 
          (fmap (partial fmap fapply) mutators state)))))




; ; draw state model
; {
;    { :data {:position [100 100] :opacity 100 :diameter 15}
;       :mutators {:opacity fade :diameter grow}
;       :draw (fn [data] (circle data) ) }
 
;   k { :data {:position-x [50 200] :position-y [190 200] :opacity 80}
;       :mutators {:position-x down :position-y up :opacity fade}
;       :draw (fn [data] (line data) ) } 
; }



(deftest update-state
  (let [state { :piano { 42 { :attack 70 :release 22 :note 42 }}}
        events (concat keydown-middle-c keyup-middle-c)
        expected-piano-state { 42 { :attack 70 
                                    :release 22 
                                    :note 42
                                    :effects {
                                      :fade-in {
                                        :val 0 ; increase this at a rate relative to attack value
                                        :mutator #( )} ; val - (attack % 100)
                                      :fade-out {
                                        :val 100 ; reduce this at a rate relative to release value
                                        :mutator #( )}}} ; val - (release % 100)
                               60 { :attack 45
                                    :release 40
                                    :note 60 }}
        expected-state        {:piano expected-piano-state}]
    (is (= expected-state (main/generate-state state events)))))

(deftest set-positions
  (let [state { :piano { 42 { :attack 70 :release 22 :note 42 }}}
        with-positions (main/set-positions state)
        position       (get-in with-positions [:piano 42 :position])]
    (is (= 2 (count position))))) ; vector with x and y