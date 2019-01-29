package com.anderc.riptiderenamer.delegate;

import com.anderc.riptiderenamer.model.Song;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.Scanner;

public class SpotifyDelegate {

    private static final String SPOTIFY_API_SCOPES = "playlist-modify-public";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("https://example.com");

    private String playlistName;

    private SpotifyApi spotifyApi;
    private AuthorizationCodeUriRequest authorizationCodeUriRequest;

    public SpotifyDelegate() {
        Properties appProperties = new Properties();
        InputStream in = GoogleSheetsDelegate.class.getResourceAsStream("/application.properties");
        try {
            appProperties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String clientId = appProperties.getProperty("spotify.client.id");
        String clientSecret = appProperties.getProperty("spotify.client.secret");
        playlistName = appProperties.getProperty("spotify.playlist.name");

        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .build();

        authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .state("x4xkmn9pu3j6ukrs8n")
                .scope(SPOTIFY_API_SCOPES)
                .show_dialog(true)
                .build();
    }

    public void getAuthorizationCode() {
        final URI uri = authorizationCodeUriRequest.execute();
        System.out.println("URI: " + uri.toString());
    }

    public void authenticateToSpotify() {
        try {
            getAuthorizationCode();
            String code = getAuthCode();

            AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
                    .build();

            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addSongToPlaylist(String playlistId, Song song) {
        try {
        Paging<Track> tracks = spotifyApi.searchTracks(song.getSearchableString()).limit(1).offset(0).market(CountryCode.US).build().execute();

        if(tracks.getTotal() < 1) {
            System.out.println("Couln't add song: " + song.getTitle() + " - " + song.getArtist());
        } else {
            Track spotifyTrack = tracks.getItems()[0];
            String[] trackUriArray = {spotifyTrack.getUri()};
            spotifyApi.addTracksToPlaylist(playlistId, trackUriArray).build().execute();
        }

        } catch(SpotifyWebApiException | IOException e) {
            System.out.println(e.getCause());
        }
    }

    public String createPlaylist() throws IOException, SpotifyWebApiException {
        User currentUser =  spotifyApi.getCurrentUsersProfile().build().execute();
        Playlist playlist = spotifyApi.createPlaylist(currentUser.getId(), playlistName).public_(true).build().execute();
        return playlist.getId();
    }

    private String getAuthCode() {
        System.out.println("Enter Code: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
}
