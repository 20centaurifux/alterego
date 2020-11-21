# meatjs

## Introduction

**meatjs** is a mobile cross-platform application based on [Apache Cordova](https://cordova.apache.org/)
and [jQuery Mobile](https://jquerymobile.com/). Users can tag, comment, vote and recommend
images from a shared gallery. Basically it's a
[REST](https://en.wikipedia.org/wiki/Representational_state_transfer) client for
[meat-a](https://github.com/20centaurifux/meat-a).

## Configuration & building

Before you build the app please set the correct endpoint address in the
[meat.rest.js](https://github.com/20centaurifux/meatjs/blob/master/www/meat.rest.js#L3)
file.

Now open a shell and run

```
$ cordova build android
```

or

```
$ cordova build browser
```

Other platforms haven't been tested yet. The mobile website works fine with Chrome and
Firefox.

## Screenshots

Kindly note that the images are pixelated due to copyright reasons.

<div id="screenshots" style="padding-left:20px;">
  <a href="/images/meatjs00.png" class="thumbnail" data-lightbox="meatjs"><img class="thumbnail" src="/images/meatjs00.png" alt="Image" /></a>
  <a href="/images/meatjs01.png" class="thumbnail" data-lightbox="meatjs"><img class="thumbnail" src="/images/meatjs01.png" alt="Image library" /></a>
  <a href="/images/meatjs02.png" class="thumbnail" data-lightbox="meatjs"><img class="thumbnail" src="/images/meatjs02.png" alt="Change avatar" /></a>
  <a href="/images/meatjs03.png" class="thumbnail" data-lightbox="meatjs"><img class="thumbnail" src="/images/meatjs03.png" alt="Change user details" /></a>
</div>
