(ns diceware.core
  (:refer-clojure :exclude [rand rand-int rand-nth])
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [diceware.secure-rand :as securerandom])
  (:gen-class))

(defn one-roll
  "return an int from a roll of a single die"
  []
  (inc (securerandom/rand-int 6)))

(defn five-rolls
  "return a keyword formed from a roll of five dice, such as :12346"
  []
  (->> (repeatedly 5 one-roll) ; e.g, => (1 2 3 4 6)
       (apply str)             ; e.g, => "12346"
       (keyword)))

;; You might try to convert the io/resource to io/file ...
;; and then a PushbackReader for edn/read ... 
;; but that works in the REPL but doesn't work in an Uberjar.
;; Something about io/file returning a URL and not a File.
;; http://stackoverflow.com/a/32237761/3599738
(def diceware-map
  (-> "diceware-map.edn"
      io/resource
      slurp
      edn/read-string))

(defn -main
  "Print some random die rolls"
  [& args]
  (def n (try
           (Integer/parseInt (first args))
           (catch Exception e 5)))
  (dotimes [_ n]
    (let [roll (five-rolls)]
      (println (roll diceware-map)))))
