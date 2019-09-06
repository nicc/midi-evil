(ns mutators-test
  (:use [clojure.test])
  (:require [mutators :as mt]))

(def sample-state 
  {:elems            {}
   :elem-params      {:a {:ttl 0 :x 1 :y 2 :whatevs 3} :b {:ttl 9 :x 8 :y 7}}
   :mutator-fns      {:a [#(assoc % :x (+ 1 (:x %))) #(assoc % :y (+ 1 (:y %)))]
                      :b [#(assoc % :y (- 10 (:y %)))]}
   :draw-fns         {}
   :note->element-id {}})

(deftest mutates-elem-params
  (is (= 
    (mt/apply-to-elems sample-state)
    (assoc sample-state :elem-params {:a {:ttl      0
                                          :x        2 
                                          :y        3 
                                          :whatevs  3}
                                      :b {:ttl      9
                                          :x        8
                                          :y        3}}))))