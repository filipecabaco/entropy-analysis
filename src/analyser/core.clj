(ns analyser.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [analyser.entropy :as entropy]
            [analyser.files :as files]))

(def arguments (atom {}))

(defn candidate-filters
  [{e :entropy}]
  (> e (read-string (:entropy @arguments))))

(defn candidates
  [{f :file r :result}]
  {:file f :candidates (sort-by :entropy (filter candidate-filters r))})

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
  (println (str "Checking entropy on files with the following extension:" (drop 1 args)))
  (println (str "Entropy level: " (first args)))
  (swap! arguments assoc :entropy (first args))
  (def extensions (drop 1 args))
  (def entropies (files/process extensions))
  (def sorted-candidates (map candidates entropies))
  (doall (map print-candidates sorted-candidates)))
