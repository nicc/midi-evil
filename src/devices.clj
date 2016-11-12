(ns devices
  (:require [midi]))

(def registry {:launchpad (midi/midi-in)})