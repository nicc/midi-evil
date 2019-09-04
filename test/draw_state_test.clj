(ns draw-state-test
  (:use clojure.test)
  (:require [draw-state :as drs]
            [draw]))

(def keydown-middle-c [{:chan 0 :cmd 144 :note 60 :vel 45 :data1 60 :data2 45}])
(def keyup-middle-c [{:chan 0 :cmd 128 :note 60 :vel 40 :data1 60 :data2 40}])
(def keyupdown-middle-c (concat keydown-middle-c keyup-middle-c))
(def middle-c-repeated (concat keydown-middle-c keyup-middle-c keydown-middle-c keyup-middle-c))
(def middle-c-repeated-held (concat keydown-middle-c keyup-middle-c keydown-middle-c))
(def mappings {60 "guidy-two-shoes" 70 "guid-help-us"})
(def elem-notes {"guidy-two-shoes" {:attack 45
                                    :release 40
                                    :note 60}
                 "guid-help-us"    {:attack 55
                                    :release 50
                                    :note 70}})

(deftest updates-elems
  (is (= {"guidy-two-shoes" {:attack 45
                             :release 40
                             :note 60
                             :type :circle}
          "guid-help-us"    {:attack 55
                             :release 50
                             :note 70
                             :type :circle}}
          (drs/update-elems {"guidy-two-shoes" {:attack 45
                                                :note 60
                                                :type :circle}}
                            elem-notes))))

(deftest update-elem-params 
  (let [elems   {"guidy-two-shoes" {:attack 45
                                    :release 40
                                    :note 60
                                    :type :circle}
                 "guid-help-us"    {:attack 55
                                    :note 70
                                    :type :circle}}
        prior   {"guidy-two-shoes" [nil 300 201 35]}
        params  (drs/update-elem-params prior elems)]
    (is (= [0 300 201 45] (params "guidy-two-shoes")))
    
    (is (= 4 (count (params "guid-help-us"))))
    (is (= nil (first (params "guid-help-us"))))
    (is (>= (draw/size :x) (nth (params "guid-help-us") 1)))
    (is (>= (draw/size :y) (nth (params "guid-help-us") 2)))
    (is (= 55 (nth (params "guid-help-us") 3)))))

(deftest updates-mutator-fns
  (is (= :tested true))
  )

(deftest updates-draw-fns
  (is (= :tested true))
  )




