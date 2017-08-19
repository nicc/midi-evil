(ns main-test
  (:use [clojure.test]
        [uncomplicate.fluokitten core jvm])
  (:require [main]))

(def state { :piano { 42 { :attack 70 :release 22 :note 42 }}})
(def keydown-middle-c [{:chan 0 :cmd 144 :note 60 :vel 45 :data1 60 :data2 45}])
(def keyup-middle-c [{:chan 0 :cmd 128 :note 60 :vel 40 :data1 60 :data2 40}])
(def events (concat keydown-middle-c keyup-middle-c))

(deftest initial-state
  (is (= {:piano {}} (main/initial-state [:piano]))))



(deftest simple-applicative
  (let [half     #(/ % 2)
        mutators { :piano { 42 { :attack half :release half }}}

        expected-state { :piano { 42 { :attack 35 :release 11 :note 42 }}}]

    (is (=
          expected-state 
          (fmap (partial fmap fapply) mutators state)))))



(deftest update-state
  (let [expected-piano-state { 42 { :attack 70 
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
  (let [with-positions (main/set-positions state)
        position       (get-in with-positions [:piano 42 :position])]
    (is (= 2 (count position))))) ; vector with x and y