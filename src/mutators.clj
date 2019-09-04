(ns mutators)

(defn circle [[ttl x y rgba diam]]
  [ttl x y rgba diam]) ; do nothing for now

(defmulti note-> :type)
(defmethod note-> :circle [_] circle)