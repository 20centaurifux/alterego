# meat-a

## Introduction

**meat-a** is a [WSGI](http://wsgi.tutorial.codepoint.net/) based
[REST](https://en.wikipedia.org/wiki/Representational_state_transfer)
service allowing users to comment, rate and tag any kind of object.
The service could be an attractive option for you if you want to add
"social network" features to an existing object database like an
image gallery.

## Setup

If you want to test the service please install the
[Rocket](https://pypi.python.org/pypi/rocket/1.2.4) module and start
the test server:

```
$ python rocket_server.py
```

This will start a small HTTP server on port 8000.

You're welcome to use any HTTP server with WSGI support you like, of course.
Therefore configure your preferred server and setup the
[index()](https://github.com/20centaurifux/meat-a/blob/master/wsgi.py#L130)
function as handler.

Start the mailer to enable email sending:

```
$ python mailer.py
```

Data is stored in a [PostgreSQL](https://www.postgresql.org/) database.
Please ensure that the server is running and accessible.

The database 
[schema](https://github.com/20centaurifux/meat-a/blob/master/sql/structure.sql)
can be found in the sql subfolder. You can build additional
scripts with [GNU Make](https://www.gnu.org/software/make/). You need
[m4](https://www.gnu.org/software/m4/m4.html) for this step.

All configuration options are located in the
[config.py](https://github.com/20centaurifux/meat-a/blob/master/config.py)
file. You should change at least the following settings:

| Setting(s)                                          | Description                                                                                        |
| :-------------------------------------------------- | :------------------------------------------------------------------------------------------------- |
| WEBSITE_URL                                         | **meat-a** generates various URLs. Define the base URL of the web application here.                |
| PG\_HOST, PG\_PORT, PG\_DB, PG\_USER, PG\_PWD       | PostgreSQL connection details.                                                                     |
| TMP_DIR                                             | Directory for temporary files. An absolute filename is highly recommended.                         |
| AVATAR_DIR                                          | Directory for storing avatars. An absolute filename is highly recommended.                         |
| SMTP\_HOST, SMTP\_PORT, SMTP\_SSL, SMTP\_ADDRESS, SMTP\_USERNAME, SMTP\_PASSWORD | SMTP user credentials and details.                                    |
| MAILER\_HOST, MAILER\_PORT, MAILER\_ALLOWED_CLIENTS | Hostname and port of the mailer. You have also to specify the IP addresses of all allowed clients. |

Mails and websites are generated using the
[Cheetah](https://pypi.python.org/pypi/Cheetah/2.4.4) template framework.
The template files are located in the
[tpl](https://github.com/20centaurifux/meat-a/blob/master/tpl) directory.

You need the following additional packages to run **meat-a**. All modules
can be installed with [pip](https://pypi.python.org/pypi/pip).

* [PIL (image library)](https://pypi.python.org/pypi/pip)
* [Cheetah](https://pypi.python.org/pypi/Cheetah/2.4.4)
* [psycopg2](http://initd.org/psycopg/)
* [bson (shipped with pymongo)](https://pypi.python.org/pypi/pymongo/)
* [Rocket (optional)](https://pypi.python.org/pypi/rocket/1.2.4)

## Architecture - a brief overview for developers (only)

Objects are stored in the
[object database](https://github.com/20centaurifux/meat-a/blob/master/database.py#L395).
They are referenced by their *guid* and have a *source* (e.g. a link or filename).

Users can tag, rate and comment these objects. The service also offers the option
to organize a list of favorites.

Users automatically become friends once they follow each other. Friends are allowed to
recommend objects to one another. If a user profile is not protected everyone can
recommend objects to him or her. Users are organized in a separate
[user store](https://github.com/20centaurifux/meat-a/blob/master/database.py#L134).

Several activities generate
[notifications](https://github.com/20centaurifux/meat-a/blob/master/database.py#L572).
When a user profile is not protected these activities are public to all users of the
service. By protecting a profile only friends will receive notifications.

Mails are saved in a
[queue](https://github.com/20centaurifux/meat-a/blob/master/database.py#L593).
A separate 
[service](https://github.com/20centaurifux/meat-a/blob/master/mailer.py#L62)
sends them out. The queue is processed in a user-defined interval or may
be triggered by an [UDP](https://en.wikipedia.org/wiki/User_Datagram_Protocol)
message.

The different data stores should only be accessed through the
[Application](https://github.com/20centaurifux/meat-a/blob/master/app.py#L188)
class which provides the business logic.

The WSGI module tries to map a requested path to a corresponding
[Controller](https://github.com/20centaurifux/meat-a/blob/master/controller.py#L75).
Each controller returns a
[View](https://github.com/20centaurifux/meat-a/blob/master/view.py#L39)
used to generate the response.

To test the available modules execute the
[test.py](https://github.com/20centaurifux/meat-a/blob/master/test.py) file.

## The REST service

Many functions of the **meat-a** web interface require an authenticated request.
At the moment only
[HTTP basic authentication](https://en.wikipedia.org/wiki/Basic_access_authentication)
is supported.

**meat-a** provides the following resources:

### 1. User Accounts

#### 1.1. Request a new user account

| Option      | Value(s)           |
| :---------- | :----------------- |
| Url         | /rest/registration |
| Method      | POST               |
| Parameters  | username, email    |
| Response    | application/json   |
| Status code | 201                |

Username and email have to be unique. If both parameters are valid a
request id and related activation code is generated.

An activation link is sent by email and looks similar to the one found
below:

```
http://example.org/html/registration/cRJ0miyRgZKPnVz4tlZgnla7JKmJ26Ks?code=dETH8fPueOAaP9Q5p1z08WmJArC2spH7
```

__Example response headers__

```
Location: http://example.org/html/registration/cRJ0miyRgZKPnVz4tlZgnla7JKmJ26Ks
ETag: 274542b393fb3dfd63844f231bc4727dd288559df6ed66eaaa10fc050b786b2f
```

__Example response body__

```
{"Location": "http://example.org/html/registration/cRJ0miyRgZKPnVz4tlZgnla7JKmJ26Ks"}
```

#### 1.2. Activation form

| Option      | Value(s)                       |
| :---------- | :----------------------------- |
| Url         | /html/registration/$request-id |
| Method      | GET                            |
| Parameters  | code (optional)                |
| Response    | text/html                      |
| Status code | 200                            |

__Response body__

An HTML page providing a form to activate the requested user account. It has
a field to enter the related request code.

If the code parameter is specified in the URL the related input field is
filled.

#### 1.3. Activate a user account

| Option      | Value(s)                       |
| :---------- | :----------------------------- |
| Url         | /html/registration/$request-id |
| Method      | POST                           |
| Parameters  | code                           |
| Response    | text/html                      |
| Status code | 200                            |

If the request id and activation code are valid the account is
activated. An auto-generated password is sent by email.

__Response body__

An HTML page displaying a success message.

#### 1.4. View user details

| Option      | Value(s)              |
| :---------- | :-------------------- |
| Url         | /rest/user/$username  |
| Method      | GET                   |
| Headers     | Authorization         |
| Response    | application/json      |
| Status code | 200                   |

Only friends can see the "email", "following" and "avatar" fields
if the account is protected.

__Example response body__

```
{"avatar": null, "blocked": false, "created_on": {"$date": 1455608163842},
 "email": "john@example.org", "firstname": "John", "following": ["mark smith"],
 "gender": "male", "id": 158724, "language": null, "lastname": null,
 "protected": true, "username": "john.doe"}
```

#### 1.5. Update user details

| Option      | Value(s)                                                |
| :---------- | :------------------------------------------------------ |
| Url         | /rest/user/$username                                    |
| Method      | POST                                                    |
| Parameters  | firstname, lastname, email, gender, language, protected |
| Headers     | Authorization                                           |
| Response    | application/json                                        |
| Status code | 200                                                     |

__Response body__

A dictionary providing user details (see 1.4).

#### 1.6. Delete user account:

| Option      | Value(s)             |
| :---------- | :------------------- |
| Url         | /rest/user/$username |
| Method      | DELETE               |
| Headers     | Authorization        |
| Response    | text/plain           |
| Status code | 204                  |

Users can only delete themselves.

#### 1.7. Get avatar

| Option      | Value(s)                    |
| :---------- | :-------------------------- |
| Url         | /rest/user/$username/avatar |
| Method      | GET                         |
| Headers     | Authorization               |
| Response    | Application/Octet-Stream    |
| Status code | 200                         |

Only friends can download the avatar if the account is protected.

#### 1.8. Update avatar

| Option      | Value(s)                    |
| :---------- | :-------------------------- |
| Url         | /rest/user/$username/avatar |
| Method      | POST (multipart)            |
| Parameters  | filename, file              |
| Headers     | Authorization               |
| Response    | application/json            |
| Status code | 200                         |

This resource expects a multipart form.

__Example response body__

```
{"filename": "42b393fb27453dfd63831bc4727dd288544f259df6ed66efc050aaa10b786b2f"}
```

#### 1.9. Update password

| Option      | Value(s)                                      |
| :---------- | :-------------------------------------------- |
| Url         | /rest/user/$username/password                 |
| Method      | POST                                          |
| Parameters  | old\_password, new\_password1, new\_password2 |
| Headers     | Authorization                                 |
| Response    | application/json                              |
| Status code | 200                                           |

__Example response__

```
{"password": "my secret"}
```

#### 1.10. Request new password

| Option      | Value(s)                             |
| :---------- | :----------------------------------- |
| Url         | /rest/user/$username/password/change |
| Method      | POST                                 |
| Parameters  | email                                |
| Response    | application/json                     |
| Status code | 201                                  |

To change a password without knowing the current one a change request can
be created. A request id and code is sent by mail to the user. 

__Example password change link sent by mail__

```
http://example.org/html/user/john.doe/password/rest/8IlWH0aFitqAge7kqCskBuHbtbD5FpJu&code=nw8b0Veu1jYUaaLHfmXCCykH1J7xol23
```

__Example response headers__

```
Location: http://example.org/html/user/john.doe/password/rest/8IlWH0aFitqAge7kqCskBuHbtbD5FpJu
ETag: 93426807d9ebdb79d1276be519de4b97c4618c6b4ba39b5e5748243d85115e06
```

__Example response body__

```
{"Location": "http://example.org/html/user/john.doe/password/reset/8IlWH0aFitqAge7kqCskBuHbtbD5FpJu"}
```

#### 1.11. Password change form

| Option      | Value(s)                                        |
| :---------- | :---------------------------------------------- |
| Url         | /html/user/$username/password/reset/$request-id |
| Method      | GET                                             |
| Parameters  | code (optional)                                 |
| Response    | text/html                                       |
| Status code | 200                                             |

__Response body__

An HTML page providing a form to change the password of a user account. It
has three fields:

* code: request code related to the request id
* password1, password2: password to set

If the code parameter is specified in the URL the related input
field is filled.

#### 1.12. Set new password

| Option      | Value(s)                                          |
| :---------- | :------------------------------------------------ |
| Url         | /html/user/$username/password/reset/$request-id   |
| Method      | POST                                              |
| Parameters  | code, new\_password1, new\_password2              |
| Response    | text/html                                         |
| Status code | 200                                               |

__Example response__

A page displaying the success status or a failure message.

#### 1.13. Search users

| Option      | Value(s)                 |
| :---------- | :----------------------- |
| Url         | /rest/user/search/$query |
| Method      | GET                      |
| Headers     | Authorization            |
| Response    | application/json         |
| Status code | 200                      |

This function searches the user store. Only friends can see the "email", "following" and
"avatar" fields when the found account is protected.

__Response body__

An array with dictionaries providing user details (see 1.4).

#### 1.14. Change friendship

| Option      | Value(s)                        |
| :---------- | :------------------------------ |
| Url         | /rest/user/$username/friendship |
| Method      | PUT, DELETE, GET                |
| Headers     | Authorization                   |
| Response    | application/json                |
| Status code | 200                             |

__Example response body__

{"followed": false, "following": true}

#### 1.15. Get favorites

| Option      | Value(s)              |
| :---------- | :-------------------- |
| Url         | /rest/favorites       |
| Method      | PUT, DELETE, GET      |
| Parameters  | guid (only PUT & GET) |
| Headers     | Authorization         |
| Response    | application/json      |
| Status code | 200                   |

__Example response body__

```
[{"comments_n": 0, "created_on": {"$date": 1455720684068},
  "guid": "914423ec-d585-11e5-bedf-50d719563991", "locked": false, "reported": false,
  "score": {"down": 0, "fav": 1, "up": 0}, "source": "foo", "tags": []},
  {"comments_n": 0, "created_on": {"$date": 1455720667357},
  "guid": "8eb02b44-d585-11e5-bdfe-50d719563991", "locked": false, "reported": false,
  "score": {"down": 0, "fav": 1, "up": 0}, "source": "bar", "tags": []}]
```

#### 1.16. Get messages

| Option      | Value(s)                                                       |
| :---------- | :------------------------------------------------------------- |
| Url         | /rest/messages                                                 |
| Method      | GET                                                            |
| Headers     | Authorization                                                  |
| Parameters  | limit (optional, maximum number of messages), after (optional) |
| Response    | application/json                                               |
| Status code | 200                                                            |

A message can have one of the following types:

* "following"
* "unfollowing"
* "recommendation"
* "wrote-comment"
* "voted-object"

Each message has a "type" field, a "source" (the sender) and a "target" (e.g. a
voted or recommended object).

The "after" parameter may provide an UNIX timestamp (UTC). Only messages
created after this timestamp will be returned.

#### 1.17. Get public messages

| Option      | Value(s)                                                       |
| :---------- | :------------------------------------------------------------- |
| Url         | /rest/public                                                   |
| Method      | GET                                                            |
| Headers     | Authorization                                                  |
| Parameters  | limit (optional, maximum number of messages), after (optional) |
| Response    | application/json                                               |
| Status code | 200                                                            |

A message can have one of the following types:

* "wrote-comment"
* "voted-object"

Each message has a "type" field, a "source" (the sender) and a "target" (e.g.
a voted object).

The "after" parameter may provide an UNIX timestamp (UTC). Only messages
created after this timestamp will be returned.

### 2. Objects

#### 2.1. Get a single object

| Option      | Value(s)           |
| :---------- | :----------------- |
| Url         | /rest/object/$guid |
| Method      | GET                |
| Headers     | Authorization      |
| Response    | application/json   |
| Status code | 200                |

__Example response body__

```
{"comments_n": 0, "created_on": {"$date": 1455720684068},
 "guid": "914423ec-d585-11e5-bedf-50d719563991", "locked": false,
 "reported": false, "score": {"down": 0, "fav": 1, "up": 0},
 "source": "foo", "tags": []}
```

#### 2.2. Get multiple objects

| Option      | Value(s)           |
| :---------- | :----------------- |
| Urls        | /rest/objects, /rest/objects/page/$page |
| Method      | GET                                     |
| Headers     | Authorization                           |
| Parameters  | page_size (optional)                    |
| Response    | application/json                        |
| Status code | 200                                     |

__Response body__

An array holding objects (see 2.1).

#### 2.3. Get objects (filtered by tag)

| Option      | Value(s)                                                  |
| :---------- | :-------------------------------------------------------- |
| Urls        | /rest/objects/tag/$tag, /rest/objects/tag/$tag/page/$page |
| Method      | GET                                                       |
| Headers     | Authorization                                             |
| Parameters  | page_size (optional)                                      |
| Response    | application/json                                          |
| Status code | 200                                                       |

__Response body__

An array holding objects (see 2.1).

#### 2.4. Get tag cloud

| Option      | Value(s)           |
| :---------- | :----------------- |
| Url         | /rest/objects/tags |
| Method      | GET                |
| Headers     | Authorization      |
| Response    | application/json   |
| Status code | 200                |

__Example response body__

```
[{"count": 23, "tag": "foo"}, {"count": 42, "tag": "bar"}]
```

#### 2.5. Get/assign object tags

| Option      | Value(s)                              |
| :---------- | :------------------------------------ |
| Url         | /rest/object/$guid/tags               |
| Method      | PUT, GET                              |
| Parameters  | tags (comma-separated list, PUT only) |
| Headers     | Authorization                         |
| Response    | application/json                      |
| Status code | 200                                   |

__Example response body__

```
["foo", "bar", "23"]
```

#### 2.4. Get popular objects

| Option      | Value(s)                                                |
| :---------- | :------------------------------------------------------ |
| Urls        | /rest/objects/popular, /rest/objects/popular/page/$page |
| Method      | GET                                                     |
| Headers     | Authorization                                           |
| Parameters  | page_size (optional)                                    |
| Response    | application/json                                        |
| Status code | 200                                                     |

__Response body__

An array holding objects (see 2.1).

#### 2.5. Get random objects

| Option      | Value(s)             |
| :---------- | :------------------- |
| Url         | /rest/objects/random |
| Method      | GET                  |
| Headers     | Authorization        |
| Parameters  | page_size (optional) |
| Response    | application/json     |
| Status code | 200                  |

__Response body__

An array holding objects (see 2.1).

#### 2.6. Rate an object

| Option      | Value(s)                |
| :---------- | :---------------------- |
| Url         | /rest/object/$guid/vote |
| Method      | POST, GET               |
| Parameters  | up (POST only)          |
| Headers     | Authorization           |
| Response    | application/json        |
| Status code | 200                     |

Use the "up" parameter to upvote/downvote an object (e.g. "true" to upvote).

__Example response body__

```
{"up": true}
```

#### 2.7. Create/get comment(s)

| Option      | Value(s)                                                            |
| :---------- | :------------------------------------------------------------------ |
| Urls        | /rest/object/$guid/comments, /rest/object/$guid/comments/page/$page |
| Method      | POST, GET                                                           |
| Parameters  | text (POST only), page & page_size (GET only)                       |
| Headers     | Authorization                                                       |
| Response    | application/json                                                    |
| Status code | 200                                                                 |

__Example response body__

```
[{"created_on": {"$date": 1455726032630}, "deleted": false, "id": 1324558,
  "text": "foo", "user": {"avatar": null, "blocked": false,
  "created_on": {"$date": 1455719502808}, "email": "john@example.org",
  "firstname": null, "following": ["fnord"], "gender": null,
  "id": 158726, "lastname": null, "protected": true, "username": "john.doe"}}]
```

#### 2.8. Get comment

| Option      | Value(s)          |
| :---------- | :---------------- |
| Url         | /rest/comment/$id |
| Method      | GET               |
| Headers     | Authorization     |
| Response    | application/json  |
| Status code | 200               |

__Example response body__

```
{"created_on": {"$date": 1455726032630}, "deleted": false, "id": 1324558,
 "text": "foo", "user": {"avatar": null, "blocked": false,
 "created_on": {"$date": 1455719502808}, "email": "john@example.org",
 "firstname": null, "following": ["fnord"], "gender": null,
 "id": 158726, "lastname": null, "protected": true, "username": "john.doe"}}
```

#### 2.9. Get recommendations

| Option      | Value(s)                                                |
| :---------- | :------------------------------------------------------ |
| Urls        | /rest/recommendations, /rest/recommendations/page/$page |
| Method      | GET                                                     |
| Headers     | Authorization                                           |
| Parameters  | page_size (optional)                                    |
| Response    | application/json                                        |
| Status code | 200                                                     |

__Response body__

An array holding objects (see 2.1).

#### 2.11. Recommend object

| Option      | Value(s)                                      |
| :---------- | :-------------------------------------------- |
| Url         | /rest/object/$guid/recommend                  |
| Method      | PUT                                           |
| Headers     | Authorization                                 |
| Parameters  | receivers (comma-separated list of usernames) |
| Response    | application/json                              |
| Status code | 200                                           |

__Example response body__

```
{"guid": "914423EC-D585-11E5-BEDF-50D719563991", "receivers": ["john.doe", "mark smith"]}
```

#### 2.12. Report abuse

| Option      | Value(s)                 |
| :---------- | :----------------------- |
| Url         | /rest/object/$guid/abuse |
| Method      | PUT                      |
| Headers     | Authorization            |
| Response    | application/json         |
| Status code | 200                      |

__Example response body__

```
{"guid": "914423EC-D585-11E5-BEDF-50D719563991", "reported": true}
```
