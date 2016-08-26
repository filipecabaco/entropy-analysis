# Basic Entropy Analyser

A really ugly, poorly written, poorly tested and not optimized word entropy analyzer.

Using Shannon Entropy ( https://en.wiktionary.org/wiki/Shannon_entropy ) we calculate the entropy of words of length superior to 8 charaters (might add argument to control this in the future) in a given folder.

We also calculate the Inverse Term Frequency (https://en.wikipedia.org/wiki/Tf%E2%80%93idf) which is also taken into account on the calculations of the score.

In theory, this can be used to find secrets in code.

The entropy code calculation provided by https://rosettacode.org/wiki/Entropy#Clojure  

## Usage
	$sh entropy.sh <<entropy level>> <<extensions to analyse>>
