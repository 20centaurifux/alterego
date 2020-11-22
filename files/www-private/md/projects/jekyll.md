# Jekyll

## Introduction

**Jekyll** is a [GTK+](http://www.gtk.org/) based multi-platform Twitter
client. It has been developed and tested on GNU/Linux and Microsoft Windows.

<div id="screenshots" style="padding-left:20px;">
  <a href="/images/jekyll.png" class="thumbnail" data-lightbox="jekyll"><img class="thumbnail" src="/images/jekyll.png" alt="Image" /></a>
</div>

## Building

**Jekyll** uses [GNU Make](https://www.gnu.org/software/make/) as build system.
It has a separate Makefile for
[Microsoft Windows](https://github.com/20centaurifux/Jekyll/blob/master/Makefile.windows)
and [GNU/Linux](https://github.com/20centaurifux/Jekyll/blob/master/Makefile.linux).
It depends on the following components:

* [GNU Make](https://www.gnu.org/software/make/)
* [gcc](https://gcc.gnu.org) or [MinGW](http://www.mingw.org) (on Microsoft Windows)
* [GLib/GTK+ 2](http:///www.gtk.org/) development files and shared libraries
  (including all dependencies like GObject, Pango, GDK, ...)
* [OpenSSL](https://www.openssl.org) development files and shared libraries

You also need your own [OAuth](https://en.wikipedia.org/wiki/OAuth) consumer
key and secret. Please define the related settings in the
[Makefile.build](https://github.com/20centaurifux/Jekyll/blob/master/Makefile.build#L11)
file ("TWITTER\_CONSUMER\_KEY" and "TWITTER\_CONSUMER\_SECRET").

If all required components are installed properly on your machine you
can build **Jekyll** using the GNU Make utility:

```
$ make -f Makefile.{platform} depend
$ make -f Makefile.{platform}
```

To build **Jekyll** on Microsoft Windows please ensure that all
directories in the related Makefile are correct.

By default **Jekyll** will be installed in "/usr/local". To change the
location you can set the PREFIX variable:

```
$ make -f Makefile.{platform} PREFIX=/foo/bar
```

## Installation

If **Jekyll** has been build successfully install it by typing in

```
$ make -f Makefile.{platform} install PREFIX=/foo/bar
```

## Code documentation

To create the code documentation ensure that
[Doxygen](http://www.stack.nl/~dimitri/doxygen/) is installed on your
system and run

```
$ make -f Makefile.{platform} documentation
```
