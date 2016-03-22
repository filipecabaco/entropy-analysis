# Secret Key Finder

A really ugly, poorly written, poorly tested and not optimized secret key finder.

We started by using Shannon Entropy ( https://en.wiktionary.org/wiki/Shannon_entropy ) but now we will use the percetange of numeric values in a String and if the key is trully random and base64 then it should have ~50% of numeric values.

In theory, this can be used to find secrets in code.

## Usage
	$sh entropy.sh <<entropy level>> <<extensions to analyse>>
