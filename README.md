# Riptide Sheet Updater

## Prepare the Google Cloud Project

Go to 

[Google Cloud Platform](https://console.developers.google.com/cloud-resource-manager?previousPage=%2Fapis%2Fdashboard%3Fproject%3Dquickstart-1548466483709%26authuser%3D0&angularJsUrl=%2Fcloud-resource-manager%3FpreviousPage%3D%252Fapis%252Fdashboard%253Fproject%253Dquickstart-1548466483709%2526authuser%253D0&project=&folder=&organizationId=0&authuser=0)

And create a new project. This way we can set our cloud project to have matching credentials with the script we are going to upload. This way everything has access to the right stuff.

Then go to

[Google Cloud Platform](https://console.developers.google.com/apis/dashboard?project=anderc&authuser=0)

For the project that was just created, and create new api credentials to use with our application.

Go to the OAuth consent screen and fill this out for the application.

After the consent screen is filled out, create new credentials for OAuth

After the credentials are created, download them and name them credentials.json and save them in the resources. This is what the application uses to authenticate to google services.

Go here

[Google Cloud Platform](https://console.developers.google.com/apis/library/script.googleapis.com?project=anderc)

And ensure that the apps script api is enabled for your account.


## Prepare Google Drive

Go to google drive and create the shared folder for your band. Upload the sample spreadsheet.

[Base Band Management Sheet](https://docs.google.com/spreadsheets/d/1W28W75_w3ZTFPQVApatjdav_SecXLUn3BGRI3zvlk3o/edit#gid=1694087045)

Then create a folder for Lyrics.


## Upload the script

In your band folder, create a new google app script and upload the content from resources in this project to the script.

Then go to the settings for the script to deploy it and change the cloud platform to use the project we created in the first step.


## Set up the app for use with Spotify

Go to the [spotify developer portal](https://developer.spotify.com/dashboard/applications)

Create a new app.

Add your clientid and client secret to application.properties

Set example.com as a callback url in the settings for your application.

Run the spotify argument, you will be reiderected, copy the code from the url and paste it in the console for the application.


## Application Properties

Fill in the application properties based on the actual ids for the resources you created.

Ranges should remain the same.


## Running the Application

Upon the first time running the application, you will be prompted to authenticate to your google account.

After this, the token will be saved in the tokens folder.

For the first run, enable any apis that this requests for your google account. The console application should give you links to handle this.

Run with the feature you want.

## Application Properties
In the resources dir, create application.properties and fill in the following information accordingly.

```
app.name=Band Spreadsheet Updater

# Google Sheets
band.sheet.id=
band.sheet.song.range=Songs!A2:B
band.sheet.lyrics.prefix=Songs!F

# Google Scripts
google.apps.script.id=


# Google Drive
band.drive.lyric.folder=

# Spotify
spotify.client.id=
spotify.client.secret=
spotify.playlist.name=Band Master Playlist
```
