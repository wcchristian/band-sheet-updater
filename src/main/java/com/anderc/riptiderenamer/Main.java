package com.anderc.riptiderenamer;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;

import com.anderc.riptiderenamer.delegate.AZLyricsDelegate;
import com.anderc.riptiderenamer.delegate.GoogleAppsScriptDelegate;
import com.anderc.riptiderenamer.delegate.GoogleCredentialDelegate;
import com.anderc.riptiderenamer.delegate.GoogleSheetsDelegate;
import com.anderc.riptiderenamer.model.Song;
import com.anderc.riptiderenamer.service.RandomSleepService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class Main {

    public static void main(String... args) throws IOException, GeneralSecurityException, InterruptedException {

        // Setup
        Properties appProperties = new Properties();
        InputStream in = GoogleSheetsDelegate.class.getResourceAsStream("/application.properties");
        try {
            appProperties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean shouldGetLyrics = Boolean.parseBoolean(appProperties.getProperty("app.lyrics"));
        boolean shouldGetSongLink = Boolean.parseBoolean(appProperties.getProperty("app.songlink"));

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = GoogleCredentialDelegate.getCredentials(HTTP_TRANSPORT);

        final AZLyricsDelegate lyricsDelegate = new AZLyricsDelegate();
        GoogleSheetsDelegate googleSheetsDelegate = new GoogleSheetsDelegate(credential, HTTP_TRANSPORT);
        GoogleAppsScriptDelegate appsScriptDelegate = new GoogleAppsScriptDelegate(credential, HTTP_TRANSPORT);

        // Feature Logic
        List<Song> songs =  googleSheetsDelegate.getSongs();
        for(int i=0; i<songs.size(); i++) {
            RandomSleepService.sleep();
            Song currentSong = songs.get(i);
            System.out.println(i + 1 + "/" + songs.size() + ": " + songs.get(i).getTitle() + " - " + songs.get(i).getArtist());

            if(shouldGetLyrics) {
                currentSong.setLyrics(lyricsDelegate.getLyrics(songs.get(i).getTitle(), songs.get(i).getArtist()));
                appsScriptDelegate.createLyricsDoc(currentSong);
            }

            if(shouldGetSongLink) {
                //test
            }
        }
    }
}