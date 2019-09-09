(ns colours
  (:require [thi.ng.color.core :as col]
            [notes]))

; map the circle of fifths
; to a 12-step segmentation of the colour wheel
(def note-hues {
  :C 0.0
  :G 0.08169934640522876
  :D 0.16666666666666666
  :A 0.25163398692810457
  :E 0.3333333333333333
  :B 0.4150326797385621
  :F# 0.5
  :C# 0.5849673202614379
  :G# 0.6666666666666666
  :D# 7483660130718954
  :A# 0.8333333333333334
  :F 0.9183006535947712})

(def octave-saturations [
  1 
  1 
  1 ; fully saturated in the lower octaves
  1 
  1 
  1 
  0.8 ; then incrimentally less so 
  0.6 ; to give a whiter,
  0.4 ; brighter colour.
  0.2 ])

(def octave-brightnesses [ 
  85 ; low brightness in the lower octaves
  119 ; to give a blacker,
  153 ; darker colour.
  187 
  221 
  255 ; then full brightness,
  255 ; as saturation drops to increase whiteness
  255 
  255 
  255 ])

(defn- hsv [note-name octave]
  (let [hue        (note-hues note-name)
        saturation (octave-saturations octave)
        brightness (octave-brightnesses octave)]
  (col/hsva hue saturation brightness)))

(defn note->hsv [note]
  (hsv (notes/->name note) (notes/->octave note)))

(defn note->rgba-vector [note]
  (let [hsv-col (note->hsv note)
        rgb      (col/as-rgba hsv-col)]
    [(col/red rgb) (col/green rgb) (col/blue rgb)]))
