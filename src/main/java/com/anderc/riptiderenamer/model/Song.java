package com.anderc.riptiderenamer.model;

public class Song {
    private String title;
    private String artist;
    private Lyric lyrics;

    public Song() {}

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public Song setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Song setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getArtist() {
        return this.artist;
    }

    public Lyric getLyrics() {
        return lyrics;
    }

    public Song setLyrics(Lyric lyrics) {
        this.lyrics = lyrics;
        return this;
    }
}