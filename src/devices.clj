(ns devices
  (:require [midi]
            [clojure.pprint :as pp]))

(def registry (atom {}))
(def midi-events (atom {}))

(defn- record-fn [device-name]
  (fn [event ts]
    (swap! ; TODO: add timestamp to event
      midi-events
      assoc device-name (conj (@midi-events device-name) event))))

(defn- register-device-handler! [device-name]
  (midi/midi-handle-events
    (@registry device-name)
    (record-fn device-name)))

(defn register! [device-name]
  (swap! registry assoc device-name (midi/midi-in))
  (swap! midi-events assoc device-name [])
  (register-device-handler! device-name))

(defn pull-events! [device-name]
  (let [events (@midi-events device-name)]
    (swap! midi-events assoc device-name [])
    (if (> (count events) 0)
      (spit "./event.log" (with-out-str (pp/pprint events)) :append true))
    events))

(defn names []
  (keys @registry))