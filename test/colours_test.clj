(ns colours-test
  (:use clojure.test)
  (:require [colours]
            [thi.ng.color.core :as thing-col]))

(def second-octave-c { :attack 45 :release 40 :note 24 })
(def middle-c { :attack 45 :release 40 :note 60 })
(def middle-b { :attack 45 :release 40 :note 71 })
(def middle-f { :attack 45 :release 40 :note 65 })
(def eighth-octave-c { :attack 45 :release 40 :note 96 })

; hue is mapped directly to notes
(deftest hue-test
  (is (= 0.0 (thing-col/hue (colours/note->hsv middle-c))))
  (is (= 0.9183006535947712 (thing-col/hue (colours/note->hsv middle-f))))
  (is (= 0.4150326797385621 (thing-col/hue (colours/note->hsv middle-b)))))

; saturation remains at maximum throughout lower octaves
; and drops in higher octaves to make a whiter/brighter colour
(deftest saturation-test
  (is (= 1.0 (thing-col/saturation (colours/note->hsv second-octave-c))))
  (is (= 0.4 (thing-col/saturation (colours/note->hsv eighth-octave-c)))))

; brightness/value is low in the lower octaves to make a darker/blacker colour
; and increases in the higher octaves to provide more colour 
; (will be whitened bt saturation in high octaves)
(deftest brightness-test
  (is (= 153.0 (thing-col/brightness (colours/note->hsv second-octave-c))))
  (is (= 255.0 (thing-col/brightness (colours/note->hsv eighth-octave-c)))))
