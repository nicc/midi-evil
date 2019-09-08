(ns main-test
  (:use [clojure.test])
  (:require [main]))

; (def keydown-middle-c [{:chan 0 :cmd 144 :note 60 :vel 45 :data1 60 :data2 45}])
; (def keyup-middle-c   [{:chan 0 :cmd 128 :note 60 :vel 40 :data1 60 :data2 40}])

(def sample-state {
  :piano            {42 {:attack 70 :note 42}}
  :elems            {"2ddbe992-7346-41d1-b5a3-7e2dbf541513" {:tstamp "2019-08-26T12:34:18.679"
                                                             :attack 70 
                                                             :release 15
                                                             :note 42
                                                             :type :circle}}
  :elem-params      {"2ddbe992-7346-41d1-b5a3-7e2dbf541513" {:ttl 0 :x 12 :y 4 :diameter 44}}
  :mutator-fns      {"2ddbe992-7346-41d1-b5a3-7e2dbf541513" [identity]}
  :draw-fns         {"2ddbe992-7346-41d1-b5a3-7e2dbf541513" [#{}]}
  :note->element-id {42 "2ddbe992-7346-41d1-b5a3-7e2dbf541513"}})
  

(deftest initial-state
  (is (= {:elems {},
          :elem-params {},
          :mutator-fns {},
          :draw-fns {},
          :note->element-id {},
          :piano {}}
         (main/initial-state [:piano]))))

; (deftest update-state
;   (let [state {:piano {42 {:attack 70 :release 22 :note 42}}}
;         events (concat keydown-middle-c keyup-middle-c)
;         expected-piano-state {42 {:attack 70 
;                                     :release 22 
;                                     :note 42
;                                     :effects {
;                                       :fade-in {
;                                         :val 0 ; increase this at a rate relative to attack value
;                                         :mutator #( )} ; val - (attack % 100)
;                                       :fade-out {
;                                         :val 100 ; reduce this at a rate relative to release value
;                                         :mutator #( )}}} ; val - (release % 100)
;                                60 {:attack 45
;                                     :release 40
;                                     :note 60}}
;         expected-state        {:piano expected-piano-state}]
;     (is (= expected-state (main/generate-state state events)))))