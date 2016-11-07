(set-env!
  :resource-paths #{"src"}
  :target "target"
  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [quil "2.5.0"]
                  [thi.ng/color "1.2.0"]
                  [overtone/midi-clj "0.1"]])

(task-options!
 pom {:project 'midi-evil
      :version "0.1"}
 jar {:main 'main}
 aot {:all true})

(deftask build []
  (comp
    (aot)
    (pom)
    (uber)
    (jar)
    (uber)
    (target)))
