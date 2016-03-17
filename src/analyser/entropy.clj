(ns analyser.entropy
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn calculate
  "Calculate a single word entropy"
  [w]
  (->> (frequencies w)
       (map (fn [[_ v]]
              (let [probability (/ v (count w))] ;Probability of symbol
                (-> (Math/log probability)
                    (/ (Math/log 2)) ;;log(probability) with log base 2
                    (* probability) 
                    Math/abs))
              ))
       (reduce +)))
