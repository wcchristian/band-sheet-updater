package com.anderc.riptiderenamer;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.anderc.riptiderenamer.delegate.AZLyricsDelegate;
import com.anderc.riptiderenamer.delegate.GoogleSheetsDelegate;
import com.anderc.riptiderenamer.model.Song;
import com.anderc.riptiderenamer.service.RandomSleepService;

public class Main {

    public static void main(String... args) throws IOException, GeneralSecurityException, InterruptedException {
        List<Song> songs =  GoogleSheetsDelegate.getSongs();
        final AZLyricsDelegate lyricsDelegate = new AZLyricsDelegate();

        for(int i=0; i<songs.size(); i++) {
            RandomSleepService.sleep();
            System.out.println(i + 1 + "/" + songs.size() + ": " + songs.get(i).getTitle() + " - " + songs.get(i).getArtist());
            songs.get(i).setLyrics(lyricsDelegate.getLyrics(songs.get(i).getTitle(), songs.get(i).getArtist()));
        }

        // set the lyrics into google sheets

        // get the lyrics from
//        String foo = new AZLyricsDelegate().getLyrics("Shoot To Thrill", "ACDC");
    }
}