package com.anderc.riptiderenamer.model;

public class Lyric {
    private String lyrics;
    private String lyricUrl;

    public Lyric() {}

    public Lyric(String lyrics, String lyricUrl) {
        this.lyrics = lyrics;
        this.lyricUrl = lyricUrl;
    }

    public String getLyrics() {
        return lyrics;
    }

    public Lyric setLyrics(String lyrics) {
        this.lyrics = lyrics;
        return this;
    }

    public String getLyricUrl() {
        return lyricUrl;
    }

    public Lyric setLyricUrl(String lyricUrl) {
        this.lyricUrl = lyricUrl;
        return this;
    }

    public static Lyric getDefaultLyric() {
        return new Lyric("No Lyrics Found", "No Lyrics Found");
    }
}
