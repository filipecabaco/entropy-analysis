(ns analyser.inverse-term-frequency)

; https://en.wikipedia.org/wiki/Tf%E2%80%93idf
(defn calculate
  [total values]
  (map (fn [[w c]] 
         {:word w 
          :freq (-> (+ 1 c) 
              (/ total)
              (Math/log))}) values))
