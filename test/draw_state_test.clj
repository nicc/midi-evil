(ns draw-state-test
  (:use clojure.test)
  (:require [draw-state :as drs]
            [draw]))

(def elem-notes {"guidy-two-shoes" {:attack 45
                                    :release 40
                                    :note 60
                                    :type :circle}
                 "guid-help-us"    {:attack 55
                                    :release 50
                                    :note 70
                                    :type :circle}})

(def sample-state {
  :piano            {60 {:attack 52 :note 60}}
  :elems            {"2ddbe992-7346-41d1-b5a3-7e2dbf541513" {:tstamp "2019-08-26T12:34:18.679"
                                                             :attack 70 
                                                             :release 15
                                                             :note 42
                                                             :type :circle}
                     "728f675f-8dfa-46cf-9acf-d957f60b3d04" {:tstamp "2019-08-26T12:36:09.233"
                                                             :attack 52 
                                                             :note 60
                                                             :type :circle}}
  :elem-params      {"2ddbe992-7346-41d1-b5a3-7e2dbf541513" {:ttl 0 :x 12 :y 4 :diameter 70}
                     "728f675f-8dfa-46cf-9acf-d957f60b3d04" {:ttl nil :x 87 :y 143 :diameter 52}}
  :mutator-fns      {"2ddbe992-7346-41d1-b5a3-7e2dbf541513" [identity]
                     "728f675f-8dfa-46cf-9acf-d957f60b3d04" [identity]}
  :draw-fns         {"2ddbe992-7346-41d1-b5a3-7e2dbf541513" [draw/circle]
                     "728f675f-8dfa-46cf-9acf-d957f60b3d04" [draw/circle]}
  :note->element-id {42 "2ddbe992-7346-41d1-b5a3-7e2dbf541513"
                     60 "728f675f-8dfa-46cf-9acf-d957f60b3d04"}})

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
  (let [elems     {"guidy-two-shoes" {:attack 45
                                      :release 40
                                      :note 60
                                      :type :circle}
                   "guid-help-us"    {:attack 55
                                      :note 70
                                      :type :circle}}
        prior     {"guidy-two-shoes" {:ttl nil 
                                      :x 300 
                                      :y 201 
                                      :colour [255.0 0.0 0.0] 
                                      :diameter 35}}
        params    (drs/update-elem-params prior elems)]
    (is (= 
      {:ttl 0 :x 300 :y 201 :colour [255.0 0.0 0.0] :diameter 45}
      (params "guidy-two-shoes")))
    
    (is (= 5 (count (keys (params "guid-help-us")))))
    (is (= nil (get-in params ["guid-help-us" :ttl])))
    (is (>= (draw/size :x) (get-in params ["guid-help-us" :x])))
    (is (>= (draw/size :y) (get-in params ["guid-help-us" :y])))
    (is (= [255.0 0.0 255.0] (get-in params ["guid-help-us" :colour])))
    (is (= 55 (get-in params ["guid-help-us" :diameter])))))

(deftest updates-mutator-fns
  (let [elems   {"guidy-two-shoes" {:attack 45
                                    :release 40
                                    :note 60
                                    :type :circle}
                 "guid-help-us"    {:attack 55
                                    :note 70
                                    :type :circle}}
        prior   {"guidy-two-shoes" [identity]}
        fns     (drs/update-mutator-fns prior elems)]
    (is (= ["guidy-two-shoes" "guid-help-us"] (keys fns)))
    (is (= [mutators/fall] (fns "guidy-two-shoes")))
    (is (= [mutators/fall] (fns "guid-help-us")))))

(deftest updates-draw-fns
  (let [elems   {"guidy-two-shoes" {:attack 45
                                    :release 40
                                    :note 60
                                    :type :circle}
                 "guid-help-us"    {:attack 55
                                    :note 70
                                    :type :circle}}
        prior   {"guidy-two-shoes" [identity]}
        fns     (drs/update-draw-fns prior elems)]
    (is (= ["guidy-two-shoes" "guid-help-us"] (keys fns)))
    (is (= [draw/circle] (fns "guidy-two-shoes")))
    (is (= [draw/circle] (fns "guid-help-us")))))

(deftest clears-the-dead
  (let [expected   {:piano            {60 {:attack 52 :note 60}}
                    :elems            {"728f675f-8dfa-46cf-9acf-d957f60b3d04" {:tstamp "2019-08-26T12:36:09.233"
                                                                               :attack 52 
                                                                               :note 60
                                                                               :type :circle}}
                    :elem-params      {"728f675f-8dfa-46cf-9acf-d957f60b3d04" {:ttl nil :x 87 :y 143 :diameter 52}}
                    :mutator-fns      {"728f675f-8dfa-46cf-9acf-d957f60b3d04" [identity]}
                    :draw-fns         {"728f675f-8dfa-46cf-9acf-d957f60b3d04" [draw/circle]}
                    :note->element-id {60 "728f675f-8dfa-46cf-9acf-d957f60b3d04"}}]
    (is (= expected (drs/clear-the-dead sample-state)))))
