(ns analyser.entropy
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))
; https://en.wiktionary.org/wiki/Shannon_entropy
(defn calculate
  "Calculate a single word entropy"
  [w]
  (->> (frequencies w)
       (map (fn [[_ v]]
              (let [probability (/ v (count w))] ;Probability of symbol
                (-> (Math/log probability) ;;this ... 
                    (/ (Math/log 2)) ;;... and this ==  log(probability) with log base 2
                    (* probability) 
                    Math/abs))))
       (reduce +)))
