# mail2twitter

## Introduction

**mail2twitter** allows you to share a Twitter account with other persons
without having to share the password. You can tweet and follow users by
email. 

## Requirements

You need [Python 2](https://www.python.org/) to execute the script.
**mail2twitter** depends on the following packages:

* [pyicu](https://pypi.python.org/pypi/PyICU/)
* [tweepy](http://www.tweepy.org/)

## Configuration

Before using **mail2twitter** for the very first time open the
[config.py](https://github.com/20centaurifux/mail2twitter/blob/master/config.py)
file to configure the application.

**mail2twitter** stores information in a database file. You can specify the
location with the *DB_PATH* setting.

To send and receive mails enter all POP3 and SMTP related details.

Now login to Twitter to create a consumer key and secret. **mail2twitter**
needs these values to access the Twitter service endpoint. Copy both strings
to the configuration file (*CONSUMER\_KEY* and *CONSUMER\_SECRET*).

**mail2twitter** can handle mails in HTML format by using the
[Lynx](http://lynx.browser.org/) browser for rendering. Therefore set
the path to your Lynx binary (*LYNX_EXECUTABLE*).

## Usage

When you have finished the configuration you can create your first user:

```
$ mail2twitter.py --create-user [username] [firstname] [lastname] [email]
```

There are two options available to query user information:

```
$ mail2twitter.py --show-users
$ mail2twitter.py --show-user [username]
```

If you want to enable or disable an account use the *--enable-user*
or *--disable-user* option. Please note that **mail2twitter** will only
process mails from enabled users.

To post a tweet an enabled user has to send a mail with the subject "tweet"
to the configured **mail2twitter** email address . The body of the message will
be tweeted.

It's also possible to change friendship. Therefore send a mail with the subject
"follow" or "unfollow" to change the relationship. The message body can be a
comma-separated list of Twitter user names or a single name.

Running **mail2twitter** with the *--fetch-mails* option it receives mails
via POP3 and stores the received commands in an internal queue. You can print
this queue with the following command:

```
$ mail2twitter.py --show-queue
```

To process the queue use the *--post* option.

When **mail2twitter** receives mails or connects to Twitter it will generate
various messages (e.g. when a received mail is invalid). It's possible to
send these messages with the *--send-messages* option. **mail2twitter** uses
the defined SMTP credentials to transfer mails.

You can print a list with all available commands by entering

```
$ mail2twitter.py --help
```
