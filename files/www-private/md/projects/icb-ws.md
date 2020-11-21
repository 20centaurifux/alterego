# icb-ws

## Introduction

**icb-ws** is an experimental HTML5 front-end for [Internet CB Network](http://www.icb.net/).

![alt text](/images/icb-ws0.png "architecture")

A Python WebSocket service allows the browser to connect to a Internet CB Network server.

## Website

The front-end consists only of static web pages and scripts. Set the WebSocket url in modules/config.mjs.

## Service

```
$ python3.7 icb-ws.py -U wss://localhost:7329 -L 127.0.0.1 -P 7329 -s internetcitizens.band -p 7326 \
  --ssl-key localhost.key --ssl-cert localhost.crt
```

## Screenshots

<div id="screenshots" style="padding-left:20px;">
  <a href="/images/icb-ws1.jpg" class="thumbnail" data-lightbox="icb-ws"><img class="thumbnail" src="/images/icb-ws1.jpg" alt="Image" /></a>
  <a href="/images/icb-ws2.jpg" class="thumbnail" data-lightbox="icb-ws"><img class="thumbnail" src="/images/icb-ws2.jpg" alt="Image" /></a>
</div>
