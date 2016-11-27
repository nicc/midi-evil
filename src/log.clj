(ns log
  (:require [clojure.pprint :as pp]))

(defn to-file [filename value]
  (spit filename (with-out-str (pp/pprint value)) :append true))