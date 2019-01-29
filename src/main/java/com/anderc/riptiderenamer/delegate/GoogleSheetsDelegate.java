package com.anderc.riptiderenamer.delegate;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.anderc.riptiderenamer.model.Song;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

public class GoogleSheetsDelegate {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final String applicationPropertiesFile = "/application.properties";

    private Properties appProperties;

    private Credential credential;

    private NetHttpTransport transport;

    public GoogleSheetsDelegate(Credential credential, NetHttpTransport transport) {
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

    public List<Song> getSongs() throws IOException {
        // Build a new authorized API client service.
        final String spreadsheetId = appProperties.getProperty("band.sheet.id");
        final String range = appProperties.getProperty("band.sheet.song.range");
        final String appName = appProperties.getProperty("app.name");
        List<Song> songs = new ArrayList<>();

        Sheets service = new Sheets.Builder(transport, JSON_FACTORY, credential)
                .setApplicationName(appName)
                .build();

        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("Bringing In " + values.size() + " Songs.");

            for(int i=0; i<values.size(); i++) {
                Song song = new Song(values.get(i).get(0).toString(), values.get(i).get(1).toString());
                song.setIndex(i + 2);
                songs.add(song);

            }
        }

        return songs;
    }
}