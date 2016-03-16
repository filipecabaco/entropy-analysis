(ns entropy.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))



(defn entropy [s]
  {:word s :entropy (let [len (count s), log-2 (Math/log 2)]
     (->> (frequencies s)
          (map (fn [[_ v]]
                 (let [rf (/ v len)]
                   (-> (Math/log rf) (/ log-2) (* rf) Math/abs))))
          (reduce +)))})

(defn file
  [file]
  (map entropy (str/split (slurp file) #"\s+")))

(defn isDirectory? [file] (.isDirectory file))

(defn files
  "Get all files"
  ([] 
   (map files (.listFiles (io/file "."))))
  ([f]
   (if (isDirectory? f) 
     (let [f f] (map files (.listFiles f))) 
     (let [f f] (file f)))))

(defn candidate?
  [c]
  (> c 5))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [entropies (flatten (files))]
    (println entropies)))
