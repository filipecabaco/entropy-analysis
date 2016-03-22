(ns analyser.files
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def arguments (atom {}))

(defn- number-rule
  "Gets the percentage of numbers in a string"
  [w]
  (let [numbers (str/replace w #"[\D]" "")]
    (def res (/ (count numbers) (count w)))
    (if (and (> res 0)
             (not (empty? numbers))
             (not (= numbers w))) 
      (double res)
      0)))

(defn- rules
  "Applies a score by applying the different rules to a word"
  [w]
  (def score (-> (+ 0 (number-rule w))))
  (if (> score 0) 
    {:word w 
     :score score}))

(defn- isDirectory? [file] (.isDirectory file))

(defn- isPrivate? [file] (.startsWith (.getName file) "."))

(defn- length-limit
  "We will ignore small words"
  [w]
  (> (count w) 8))

(defn- extention-filter
  "Filter files by extention"
  [file exts]
  (let [f (.getName file)
        ext (re-find #"[.].*" f)]  
    (some (partial = ext) exts)))

(defn- process-file
  "Process each file"
  [file exts]
  (if (extention-filter file exts)
    (let [f file
          content (slurp f)
          all-words  (doall (distinct (re-seq #"\w*" content)))
          filtered (filter length-limit all-words)
          score (remove nil? (map rules filtered))]
      {:file f 
       :result score})))

(defn process-files
  "Analyses files in current folder or given folder"
  ([file]   
   (if (not (isPrivate? file)) 
     (if (isDirectory? file) 
       (let [f file] (map process-files (.listFiles f) )) 
       (let [f file] (process-file f (:exts @arguments)))))))

(defn process
  "Initiates analysis of files"
  [exts]
  (swap! arguments assoc :exts exts)
  (->> (map process-files (.listFiles (io/file ".")))
       (flatten)
       (remove nil?)
       (remove (fn [{r :result}] (empty? r)))))
