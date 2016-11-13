(ns colours
  (:require [thi.ng.color.core :as col]))

(def note-hue {
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

(def octave-saturation [ 1 1 1 1 1 1 0.8 0.6 0.4 0.2 ])
(def octave-brightness [ 85 119 153 187 221 255 255 255 255 255 ])

; (defn note->colour [note]
;   (let [note-num (mod midi-note (count notes))]
;     (notes note-num)))

; (defn midi->colour [midi-note]
;     (quot midi-note (count notes)))



; (def note-colours {
;   :C (col/hsva 255 0 0) ; red ; #thi.ng.color.core.HSVA{:h 0.0, :s 1.0, :v 255.0, :a 1.0}
;   :C# (col/hsva 255 125 0) ; orange ; #thi.ng.color.core.HSVA{:h 0.08169934640522876, :s 1.0, :v 255.0, :a 1.0}
;   :D (col/hsva 255 255 0) ; yellow ; #thi.ng.color.core.HSVA{:h 0.16666666666666666, :s 1.0, :v 255.0, :a 1.0}
;   :D# (col/hsva 125 255 0) ; spring green ; #thi.ng.color.core.HSVA{:h 0.25163398692810457, :s 1.0, :v 255.0, :a 1.0}
;   :E (col/hsva 0 255 0) ; green ; #thi.ng.color.core.HSVA{:h 0.3333333333333333, :s 1.0, :v 255.0, :a 1.0}
;   :F (col/hsva 0 255 125) ; turqoise ; #thi.ng.color.core.HSVA{:h 0.4150326797385621, :s 1.0, :v 255.0, :a 1.0}
;   :F# (col/hsva 0 255 255) ; cyan ; #thi.ng.color.core.HSVA{:h 0.5, :s 1.0, :v 255.0, :a 1.0}
;   :G (col/hsva 0 125 255) ; ocean #thi.ng.color.core.HSVA{:h 0.5849673202614379, :s 1.0, :v 255.0, :a 1.0}
;   :G# (col/hsva 0 0 255) ; blue ; #thi.ng.color.core.HSVA{:h 0.6666666666666666, :s 1.0, :v 255.0, :a 1.0}
;   :A (col/hsva 125 0 255) ; violet ; #thi.ng.color.core.HSVA{:h 0.7483660130718954, :s 1.0, :v 255.0, :a 1.0}
;   :A# (col/hsva 255 0 255) ; magenta ; #thi.ng.color.core.HSVA{:h 0.8333333333333334, :s 1.0, :v 255.0, :a 1.0}
;   :B (col/hsva 255 0 125)}) ; raspberry ; #thi.ng.color.core.HSVA{:h 0.9183006535947712, :s 1.0, :v 255.0, :a 1.0}