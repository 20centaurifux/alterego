# Timesheet

**Timesheet** is the prototype of a progressive timesheet web app with support
for multiple user accounts. It's divided into to two parts:

## timesheet-api

**timesheet-api** is a .NET Core 2 REST service using Microsoft SQL server
as database backend. To setup the database please add your connectionstring to
the "appsettings" and run the following commands:

```
$ cd timesheet-api
$ dotnet ef database update
```

Connect to the database and run the "InitialData.sql" script to insert demo data.
Then start the self-hosted web server:

```
$ dotnet run
```

## timesheet-gui

The frontend is a single HTML page. Open the file in your web browser
(Firefox and Chrome work fine) and click on the "Authentication" link. Then enter
the credentials of the previously created demo account:

* username: foxmulder
* password: trustno1

Click the "Reconnect" button to connect the app to the REST service.

The URL of the REST API can be configured in the "timesheet.rest.js" JavaScript
file.

## Screenshots

<div id="screenshots" style="padding-left:20px;">
  <a href="/images/timesheet00.png" class="thumbnail" data-lightbox="timesheet"><img class="thumbnail" style="width:256px;" src="/images/timesheet00.png" alt="task list" /></a>
  <a href="/images/timesheet01.png" class="thumbnail" data-lightbox="timesheet"><img class="thumbnail" style="width:256px;" src="/images/timesheet01.png" alt="authentication" /></a>
</div>
