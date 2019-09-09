(ns mutators)

(defn fall [params]
  params) ; e.g. this would decrement :y

(defmulti elem-> :type)
(defmethod elem-> :circle [_] [fall])
(defmethod elem-> :default [_] []) ; no mutators if we don't know the elem type

(defn apply-elem [elem-params fns]
  (reduce (fn [p f] (f p)) elem-params fns))

(defn apply-to-elems [{:keys [elem-params mutator-fns], :as state}]
  (->>
    mutator-fns
    (merge-with apply-elem elem-params)
    (assoc state :elem-params)))