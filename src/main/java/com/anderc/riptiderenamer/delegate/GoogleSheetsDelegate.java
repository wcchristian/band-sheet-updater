package com.anderc.riptiderenamer.delegate;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.*;

import com.anderc.riptiderenamer.model.Song;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

public class GoogleSheetsDelegate {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String VALUE_INPUT_OPTION_RAW = "RAW";

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
        final String spreadsheetId = appProperties.getProperty("riptide.sheet.id");
        final String range = appProperties.getProperty("riptide.sheet.song.range");
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
            for (List row : values) {
                songs.add(new Song(row.get(0).toString(), row.get(1).toString()));
            }
        }

        return songs;
    }

    public void addLinksToSheet(List<Song> songs) throws IOException {
        // Build a new authorized API client service.
        final String spreadsheetId = appProperties.getProperty("riptide.sheet.id");
        final String range = appProperties.getProperty("riptide.sheet.lyric.range");
        final String appName = appProperties.getProperty("app.name");

        Sheets service = new Sheets.Builder(transport, JSON_FACTORY, credential)
                .setApplicationName(appName)
                .build();

        List<List<Object>> values = new ArrayList<>();
        songs.forEach(song -> {
            if(song.getLyrics() != null) {
                if(song.getLyrics().getLyricUrl() != null) {
                    values.add(Collections.singletonList(song.getLyrics().getLyricUrl()));
                } else {
                    values.add(Collections.singletonList("No Lyrics Found"));
                }
            }
        });
        ValueRange valueRange = new ValueRange().setValues(values);


        UpdateValuesResponse result =
                service.spreadsheets().values().update(spreadsheetId, range, valueRange)
                        .setValueInputOption(VALUE_INPUT_OPTION_RAW)
                        .execute();
        System.out.printf("%d cells updated.", result.getUpdatedCells());
    }
}