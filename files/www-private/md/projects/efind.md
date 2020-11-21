# efind

## Introduction

**efind** (extendable find) searches for files in a directory hierarchy.

Basically it's a wrapper for [GNU find](https://www.gnu.org/software/findutils/) providing an easier and more intuitive
expression syntax. It can be extended by custom functions to filter search
results.

## A quick example

Let's assume you want to find all MP3 (\*.mp3) and Ogg Vorbis (\*.ogg) files
that were modified less than two days ago in your music folder. That's no
problem with **efind's** self-explanatory expression syntax:

```
$ efind ~/music '(name="*.mp3" or name="*.ogg") and mtime<2 days'
```

Additionally you can filter the search result by audio tags and properties with
the [taglib](https://github.com/20centaurifux/efind-taglib) extension:

```
$ efind ~/music '(name="*.mp3" or name="*.ogg") and mtime<2 days \
  and artist_matches("Welle: Erdball") and audio_length()>120'
```

Use the --order-by option to sort the search result. In this example we sort the
found files by size (descending) and path (ascending):

```
$ efind ~/music '(name="*.mp3" or name="*.ogg") and mtime<2 days \
  and artist_matches("Welle: Erdball") and audio_length()>120' \
  --order-by "-{bytes}{path}"
```

**efind** also provides options to limit the output:

```
$ efind ~/music '(name="*.mp3" or name="*.ogg") and mtime<2 days \
  and artist_matches("Welle: Erdball") and audio_length()>120' \
  --limit 1
```

The example above prints the first file matching the search criteria and
aborts the search immediately.

## Project website

If you want more information or install **efind** visit the
[project website](http://efind.dixieflatline.de).
