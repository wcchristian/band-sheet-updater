package com.anderc.riptiderenamer.delegate;

import com.anderc.riptiderenamer.model.Song;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.script.Script;
import com.google.api.services.script.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GoogleAppsScriptDelegate {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final String applicationPropertiesFile = "/application.properties";

    private static Properties appProperties;
    private Credential credential;

    private NetHttpTransport transport;

    public GoogleAppsScriptDelegate(Credential credential, NetHttpTransport transport) {
        appProperties = new Properties();
        InputStream in = GoogleSheetsDelegate.class.getResourceAsStream(applicationPropertiesFile);
        try {
            appProperties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.credential = credential;
        this.transport = transport;
    }

    public void createLyricsDoc(final Song song) throws IOException {
        final String appName = appProperties.getProperty("app.name");
        final String appsScriptId = appProperties.getProperty("google.apps.script.id");
        final String riptideSheetId = appProperties.getProperty("band.sheet.id");
        final String lyricsFolderId = appProperties.getProperty("band.drive.lyric.folder");
        final String lyricsUrlPrefix = appProperties.getProperty("band.sheet.lyrics.prefix");

        // Build a new authorized API client service.
        Script service = new Script.Builder(transport, JSON_FACTORY, credential)
                .setApplicationName(appName)
                .build();

        // execute script
        Script.Scripts scripts = service.scripts();

        List<Object> parameters = new ArrayList<>();

        // Filename
        parameters.add(buildLyricsTitle(song));

        // Lyrics Array
        parameters.add(song.getLyrics().getLyrics());

        // Band Folder Id
        parameters.add(lyricsFolderId);

        // Sheet Id
        parameters.add(riptideSheetId);

        // Destination Cell Ref
        parameters.add(lyricsUrlPrefix + song.getIndex());

        Script.Scripts.Run run = scripts.run(appsScriptId, new ExecutionRequest().setFunction("createAndLinkLyrics").setParameters(parameters).setDevMode(true));

        run.execute();

        System.out.println("Lyrics added for " + buildLyricsTitle(song));
    }

    private String buildLyricsTitle(Song song) {
        return song.getTitle() + " - " + song.getArtist();
    }
}
