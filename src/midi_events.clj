(ns midi-events
  (:require [clojure.pprint :as pp]))

(def device-events (atom {:launchpad []}))

(defn record-fn [device-name]
  (fn [event ts]
    (swap! ; TODO: add timestamp to event
      device-events
      assoc device-name (conj (@device-events device-name) event))))

(defn pull! [device-name]
  (let [events (@device-events device-name)]
    (swap!
      device-events
      assoc device-name [])
    (if (> (count events) 0)
      (spit "./event.log" (with-out-str (pp/pprint events)) :append true))
    events))