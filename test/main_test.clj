(ns main-test
  (:use clojure.test)
  (:require [main]))

(def state { :piano { 42 { :amp 70 :decay 23 :note 42 }}})
(def keydown-middle-c [{:chan 0 :cmd 144 :note 60 :vel 45 :data1 60 :data2 45}])
(def keyup-middle-c [{:chan 0 :cmd 128 :note 60 :vel 40 :data1 60 :data2 40}])
(def events { :piano (concat keydown-middle-c keyup-middle-c)})

(deftest initial-state
  (is (= {:piano {}} (main/initial-state [:piano]))))

(deftest update-state
  (let [expected-piano-state { 42 { :amp 70 
                                    :decay 23 
                                    :note 42 }
                               60 { :amp 45
                                    :decay 40
                                    :note 60 }}
        expected-state        {:piano expected-piano-state}]
    (is (= expected-state (main/generate-state state events)))))
