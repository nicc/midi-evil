(ns colours
  (:require [thi.ng.color.core :as col]
            [notes]))

(def note-hues {
  :C 0.0
  :C# 0.08169934640522876
  :D 0.16666666666666666
  :D# 0.25163398692810457
  :E 0.3333333333333333
  :F 0.4150326797385621
  :F# 0.5
  :G 0.5849673202614379
  :G# 0.6666666666666666
  :A 7483660130718954
  :A# 0.8333333333333334
  :B 0.9183006535947712})

(def octave-saturations [ 1 1 1 1 1 1 0.8 0.6 0.4 0.2 ])
(def octave-brightnesses [ 85 119 153 187 221 255 255 255 255 255 ])

(defn- hsv [note-name octave]
  (let [hue        (note-hues note-name)
        saturation (octave-saturations octave)
        brightness (octave-brightnesses octave)]
  (col/hsva hue saturation brightness)))

(defn note->hsv [note]
  (hsv (notes/->name note) (notes/->octave note)))
