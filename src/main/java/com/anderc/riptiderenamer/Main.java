package com.anderc.riptiderenamer;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;

import com.anderc.riptiderenamer.delegate.*;
import com.anderc.riptiderenamer.model.Song;
import com.anderc.riptiderenamer.service.RandomSleepService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import javafx.scene.effect.Light;

public class Main {

    private static final String SONG_LINK_BASE_URL = "https://song.link/https://open.spotify.com/track/";

    public static void main(String... args) throws IOException, GeneralSecurityException, InterruptedException, SpotifyWebApiException {

        if(args.length == 0) {
            printHelp();
            System.exit(0);
        }

        switch(args[0]) {
                case "spotifyPlaylist":
                    addSongsToSpotifyPlaylist();
                    break;
                case "lyricsDoc":
                    // Setup
                    Properties appProperties = new Properties();
                    InputStream in = GoogleSheetsDelegate.class.getResourceAsStream("/application.properties");
                    try {
                        appProperties.load(in);
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
                            currentSong.setLyrics(lyricsDelegate.getLyrics(songs.get(i).getTitle(), songs.get(i).getArtist()));
                            appsScriptDelegate.createLyricsDoc(currentSong);
                    }
                    break;
                default:
                    printHelp();
                    break;
        }
    }

    private static void addSongsToSpotifyPlaylist() throws GeneralSecurityException, IOException, SpotifyWebApiException {
        SpotifyDelegate spotifyDelegate = getSpotifyDelegate();

        String playlistId = spotifyDelegate.createPlaylist();
        List<Song> songs = getSongs();
        for(Song song: songs) {
            spotifyDelegate.addSongToPlaylist(playlistId, song);
        }
    }

    private static SpotifyDelegate getSpotifyDelegate() {
        SpotifyDelegate spotifyDelegate = new SpotifyDelegate();
        spotifyDelegate.authenticateToSpotify();
        return spotifyDelegate;
    }

    private static List<Song> getSongs() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = GoogleCredentialDelegate.getCredentials(HTTP_TRANSPORT);

        GoogleSheetsDelegate googleSheetsDelegate = new GoogleSheetsDelegate(credential, HTTP_TRANSPORT);

        // Feature Logic
        return googleSheetsDelegate.getSongs();
    }

    private static void printHelp() {
        System.out.println("Please run this with an argument");
        System.out.println("First Param");
        System.out.println("    spotifyPlaylist - add songs to the spotify playlist");
        System.out.println("    lyricsDoc - get and save lyrics docs for the project");
    }
}