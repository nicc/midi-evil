(ns notes-test
  (:use [clojure.test]
        [clojure.set])
  (:require [notes]))

(def keydown-middle-c [{:chan 0 :cmd 144 :note 60 :vel 45 :data1 60 :data2 45}])
(def keyup-middle-c [{:chan 0 :cmd 128 :note 60 :vel 40 :data1 60 :data2 40}])
(def keyupdown-middle-c (concat keydown-middle-c keyup-middle-c))
(def middle-c-repeated (concat keydown-middle-c keyup-middle-c keydown-middle-c keyup-middle-c))
(def middle-c-repeated-held (concat keydown-middle-c keyup-middle-c keydown-middle-c))

(deftest updown-notemap
  (let [notemap (notes/->notemap keyupdown-middle-c)
        expected { 60 { :attack 45
                        :release 40
                        :note 60
                        :ttl 0}}]
    (is (= expected notemap))))

(deftest down-notemap
  (let [notemap (notes/->notemap keydown-middle-c)
        expected { 60 { :attack 45 :note 60 }}]
    (is (= expected notemap))))

(deftest up-notemap
  (let [notemap (notes/->notemap keyup-middle-c)
        expected { 60 { :release 40 :note 60 }}]
    (is (= expected notemap))))

(deftest repeated-notemap
  (let [notemap (notes/->notemap middle-c-repeated)
        expected { 60 { :attack 45
                        :release 40 ; use last :release
                        :note 60 
                        :ttl 0}}]
    (is (= expected notemap))))

(deftest elem-notemaps
  (let [notemap                       { 60 { :attack 45
                                             :release 40 ; use last :release
                                             :note 60 
                                             :ttl 0}
                                        70 { :attack 55
                                             :release 50 ; use last :release
                                             :note 70 
                                             :ttl 10}}
        mappings                      {60 "some-guid"}
        [new-mappings elem-notemap]   (notes/notemap-by-elem-ids mappings notemap)
        new-guid                      (first (difference (set (vals new-mappings)) #{"some-guid"}))]
  
  
  (is (= (elem-notemap "some-guid") (notemap 60)))
  (is (= (elem-notemap new-guid) (notemap 70)))
  (is (= 2 (count (keys new-mappings))))
  (is (= 2 (count (keys elem-notemap))))))

(deftest note-names
  (is (= :C (notes/->name {:note 24})))
  (is (= :C# (notes/->name {:note 49})))
  (is (= :D (notes/->name {:note 98})))
  (is (= :D# (notes/->name {:note 39})))
  (is (= :E (notes/->name {:note 52})))
  (is (= :F (notes/->name {:note 77})))
  (is (= :F# (notes/->name {:note 54})))
  (is (= :G (notes/->name {:note 67})))
  (is (= :G# (notes/->name {:note 32})))
  (is (= :A (notes/->name {:note 81})))
  (is (= :A# (notes/->name {:note 58})))
  (is (= :B (notes/->name {:note 83}))))

(deftest note-octaves
  (is (= 0 (notes/->octave {:note 11})))
  (is (= 1 (notes/->octave {:note 12})))
  (is (= 2 (notes/->octave {:note 29})))
  (is (= 3 (notes/->octave {:note 36})))
  (is (= 4 (notes/->octave {:note 59})))
  (is (= 5 (notes/->octave {:note 60})))
  (is (= 6 (notes/->octave {:note 76})))
  (is (= 7 (notes/->octave {:note 88})))
  (is (= 8 (notes/->octave {:note 102})))
  (is (= 9 (notes/->octave {:note 111})))
  (is (= 10 (notes/->octave {:note 122}))))