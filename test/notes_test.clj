(ns notes-test
  (:use clojure.test)
  (:require [notes]))

(def keydown-middle-c [{:chan 0 :cmd 144 :note 60 :vel 45 :data1 60 :data2 45}])
(def keyup-middle-c [{:chan 0 :cmd 128 :note 60 :vel 40 :data1 60 :data2 40}])
(def keyupdown-middle-c (concat keydown-middle-c keyup-middle-c))
(def middle-c-repeated (concat keydown-middle-c keyup-middle-c keydown-middle-c keyup-middle-c))
(def middle-c-repeated-held (concat keydown-middle-c keyup-middle-c keydown-middle-c))

(deftest updown-notemap
  (let [notemap (notes/->map keyupdown-middle-c)
        expected { 60 { :amp 45
                        :decay 40 }}]
    (is (= expected notemap))))

(deftest down-notemap
  (let [notemap (notes/->map keydown-middle-c)
        expected { 60 { :amp 45 }}]
    (is (= expected notemap))))

(deftest up-notemap
  (let [notemap (notes/->map keyup-middle-c)
        expected { 60 { :decay 40 }}]
    (is (= expected notemap))))

(deftest repeated-notemap
  (let [notemap (notes/->map middle-c-repeated)
        expected { 60 { :amp 90
                        :decay 80 }}]
    (is (= expected notemap))))