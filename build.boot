(set-env!
  :resource-paths #{"src" "test"}
  :target "target"
  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [quil "2.5.0"]
                  [thi.ng/color "1.2.0"]
                  [overtone/midi-clj "0.1"]
                  [adzerk/boot-test "1.1.1" :scope "test"]
                  [org.clojure/algo.generic "0.1.2"]
                  [uncomplicate/fluokitten "0.6.0"]])

(task-options!
 pom {:project 'midi-evil
      :version "0.1"}
 jar {:main 'main}
 aot {:all true})

(require '[adzerk.boot-test :refer :all])

(deftask build []
  (comp
    (test)
    (aot)
    (pom)
    (uber)
    (jar)
    (uber)
    (target)))
