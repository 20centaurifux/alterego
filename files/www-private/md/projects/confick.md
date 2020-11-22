# confick

confick is a tiny library to load and cache configuration settings from an [EDN](https://github.com/edn-format/edn) file.

## Installation

The library can be installed from Clojars using Leiningen:

[![Clojars Project](https://img.shields.io/clojars/v/zcfux/confick.svg)](https://clojars.org/zcfux/confick)

## Example

```
(require '[confick.core :as cnf])

(cnf/bind [^:required addr [:tcp :address]
           ^{:default 80} port [:tcp :port]]
  (format \"%s:%d\" addr port))
```
