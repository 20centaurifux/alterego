# blocks-monger

This library implements a [content-addressable](https://en.wikipedia.org/wiki/Content-addressable_storage) block store for [blocks](https://github.com/greglook/blocks).

## Installation

The library can be installed from Clojars using Leiningen:

[![Clojars Project](https://img.shields.io/clojars/v/zcfux/blocks-monger.svg)](https://clojars.org/zcfux/blocks-monger)

## Usage

The *blocks.store.monger* namespace provides the *monger-block-store* constructor. Specify
at least the *host* and *db-name* option to connect to your MongoDB server.

Blocks are stored in a collection named *blocks*. Each document contains the hex-encoded
id of the stored block, meta data, algorithm and data (base64 encoded).

```
=> (require '[blocks.core :as blocks]
            '[blocks.store.monger])

; create a new block store:
=> (def store (blocks/->store "monger://localhost/my-storage"))

; insert block:
=> (blocks/put! store (blocks/read! "hello world"))
Block[hash:sha2-256:b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9 11 *]

; listing blocks - data is filtered on server-side:
=> (blocks/list store :limit 1 :algorithm :sha2-256 :after "122")
(Block[hash:sha2-256:b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9 11 *])

; deleting all documents:
(blocks/erase!! store)
```
