(ns analyser.files
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [analyser.entropy :as entropy]
            [analyser.inverse-term-frequency :as term-freq]))
(def arguments (atom {}))

(defn- entropy 
  "Get entropy values"
  [w]
  {:word w :entropy (entropy/calculate w)})

(defn- isDirectory? [file] (.isDirectory file))

(defn- length-limit
  "We will ignore small words"
  [w]
  (> (count w) 6))

(defn- extention-filter
  "Filter files by extention"
  [file exts]
  (let [f (.getName file)
        ext (re-find #"[.].*" f)]  
    (some (partial = ext) exts)))

(defn- process-file
  "Process each file"
  [file ext] 
  (if (extention-filter file ext)
    (let [f file
          content (slurp f)
          all-words  (doall (re-seq #"\w*" content))
          filtered (filter length-limit all-words)]
      {:file f 
       :result (map entropy filtered)})))

(defn process-files
  "Analyses files in current folder or given folder"
  ([file]
   (if (isDirectory? file) 
     (let [f file] (map process-files (.listFiles f) )) 
     (let [f file] (process-file f (:exts @arguments))))))

(defn- extract
  "Retrieves word from result map"
  ([{r :result}] 
   (map (fn [{w :word}] w) r)))

(defn- inverse-term 
  "Calculates the inverse term frequency by extracting the values from the entropy calculations"
  [results]
  (def total (count results)) ; get total files
  (->> (map extract results)
       (flatten)
       (frequencies)
       (term-freq/calculate total)
       (into {})))

(defn- combine
  "Combines analysis made by mulytiplying inverse with entropy"
  [inverse entropy]
  (map (fn [{f :file r :result}] 
         {:file f
          :result (distinct (map (fn [{w :word e :entropy}] 
                          {:word w
                           :score (* (get inverse w) e)}) r))}) entropy))

(defn process
  "Initiates analysis of files"
  [exts]
  (swap! arguments assoc :exts exts)
  (def entropy-result (->> (map process-files (.listFiles (io/file ".")))
                           (flatten)
                           (remove nil?)))
  (def inverse-term-result (inverse-term entropy-result))
  (combine inverse-term-result entropy-result))

  ;pokiasdlkjiuqweivxzncxbashgdwqipeuouuNBANMBSAUDYOAALSALSHJASJ2928W1098lkjn
