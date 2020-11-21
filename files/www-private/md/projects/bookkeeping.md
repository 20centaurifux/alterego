# Bookkeeping

## Introduction

**Bookkeeping** is a Java based cross-platform accounting software for
the desktop. It supports double-entry bookkeeping and requires zero
configuration.

The application creates an own [SQLite](https://sqlite.org/) database
for each user automatically. The goal of **Bookkeeping** is to provide
a simple solution for home bookkeeping.

Multiple accounts with different currencies are supported. When transferring
money from one account to another exchange rates can be applied
automatically.

**Bookkeeping** exports accounting records to
[CSV](https://en.wikipedia.org/wiki/Comma-separated_values). This
makes it possible to process the data with Excel or other programs.

## Requirements

You need the following libraries to build **Bookkeeping**:

* [PicoContainer](http://picocontainer.org)
* [SQLite JDBC driver](https://bitbucket.org/xerial/sqlite-jdbc)

## Screenshots

<div id="screenshots" style="padding-left:20px;">
  <a href="/images/bookkeeping00.png" class="thumbnail" data-lightbox="bookkeeping"><img class="thumbnail" style="width:256px;" src="/images/bookkeeping00.png" alt="Transactions" /></a>
  <a href="/images/bookkeeping01.png" class="thumbnail" data-lightbox="bookkeeping"><img class="thumbnail" style="width:256px;" src="/images/bookkeeping01.png" alt="Accounts" /></a>
  <a href="/images/bookkeeping02.png" class="thumbnail" data-lightbox="bookkeeping"><img class="thumbnail" style="width:256px;" src="/images/bookkeeping02.png" alt="Transfer" /></a>
  <a href="/images/bookkeeping03.png" class="thumbnail" data-lightbox="bookkeeping"><img class="thumbnail" style="width:256px;" src="/images/bookkeeping03.png" alt="Exchange rates" /></a>
</div>
