# Basic Entropy Analyser

A really ugly, poorly written and not optimized word entropy analyzer.

Using Shannon Entropy ( https://en.wiktionary.org/wiki/Shannon_entropy ) we calculate the entropy of words of length superior to 5 charaters (might add argument to control this in the future) in a given folder.

In theory, this can be used to find secrets in code.

The entropy code calculation provided by https://rosettacode.org/wiki/Entropy#Clojure  

## Usage
	```$sh entropy.sh <<entropy level>> <<extensions to analyse>>```
