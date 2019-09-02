(ns device-state-test
  (:use clojure.test)
  (:require [device-state :as dvs]))


(def keydown-middle-c {:chan 0 :cmd 144 :note 60 :vel 45 :data1 60 :data2 45})
(def keyup-middle-c {:chan 0 :cmd 128 :note 60 :vel 40 :data1 60 :data2 40})
(def keydown-middle-d {:chan 0 :cmd 144 :note 62 :vel 15 :data1 62 :data2 15})


; {42 {:attack 70 :note 42}}

(deftest updates-device-state
  (is (= 
    {60 {:attack 45 :note 60}} 
    (dvs/update-notes {} [keydown-middle-c])))
  
  (is (= 
    {60 {:attack 55 :note 60}} 
    (dvs/update-notes {} [keydown-middle-c 
                        (merge keydown-middle-c {:vel 55 :data2 55})])))
  
  (is (= 
    {60 {:attack 55 :note 60}
      62 {:attack 15 :note 62}} 
    (dvs/update-notes {60 {:attack 55 :note 60}} [keydown-middle-d])))
  
  (is (= 
    {62 {:attack 15 :note 62}} 
    (dvs/update-notes {} [keydown-middle-c keyup-middle-c keydown-middle-d]))))