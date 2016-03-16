(ns entropy.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def arguments (atom {}))

;Shannon Entropy - https://en.wiktionary.org/wiki/Shannon_entropy
;https://rosettacode.org/wiki/Entropy#Clojure
(defn entropy [s]
  {:word s :entropy (let [len (count s), log-2 (Math/log 2)]
     (->> (frequencies s)
          (map (fn [[_ v]]
                 (let [rf (/ v len)]
                   (-> (Math/log rf) (/ log-2) (* rf) Math/abs))))
          (reduce +)))})

(defn length-limit
  [w]
  (> (count w) 5))

(defn extention-filter
  [file]
  (let [f (.getName file)
        ext (re-find #"[.].*" f)
        exts (:ext @arguments)]  
    (some (partial = ext) exts)))

(defn file
  [file] 
  (if (extention-filter file)
    (let [f file
          content (slurp f)
          all-words (str/split content #"\s")
          filtered (filter length-limit all-words)]
      {:file f :result (map entropy filtered)})))

(defn isDirectory? [file] (.isDirectory file))

(defn files
  "Get all files"
  ([]
   (map files (.listFiles (io/file "."))))
  ([f]
   (if (isDirectory? f) 
     (let [f f] (map files (.listFiles f) )) 
     (let [f f] (file f)))))

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
  (swap! arguments assoc :ext (drop 1 args))
  (def entropies (flatten (files)))
  (def sorted-candidates (map candidates entropies))
  (doall (map print-candidates sorted-candidates)))
