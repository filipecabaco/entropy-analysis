(ns analyser.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [analyser.entropy :as entropy]
            [analyser.files :as files]))

(def arguments (atom {}))

(defn candidate-filters
  [{score :score}]
  (> score (read-string (:score @arguments))))

(defn candidates
  [{f :file r :result}]
  {:file f :candidates (sort-by :score (filter candidate-filters r))})

(defn print-candidate
  [c]
  (println (str "\t" c)))

(defn print-candidates
  [{f :file c :candidates}]
  (if (> (count c) 0)
      (let []  
        (println (str "File path - " f))
        (doall (map print-candidate c)))))

(defn -main
  [& args]
  (println (str "Scanning for high entropy keywords."))
  (println (str "Checking entropy * inverse frequency of words on files with the following extension:" (drop 1 args)))
  (println (str "Score level: " (first args)))
  (swap! arguments assoc :score (first args))
  (def extensions (drop 1 args))
  (def scores (files/process extensions))
  (def sorted-candidates (map candidates scores))
  (doall (map print-candidates sorted-candidates)))
