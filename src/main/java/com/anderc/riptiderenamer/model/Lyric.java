package com.anderc.riptiderenamer.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Lyric {
    private static final String HTML_COMMENT_REGEX = "<!--.*-->";
    private static final String HTML_LINE_BREAK_REGEX = "<br>";
    private static final String NEWLINE_CHAR = "\n";

    private List<String> lyricLines;
    private String lyricUrl;

    public Lyric() {}

    public Lyric(List<String> lyricLines, String lyricUrl) {
        this.lyricLines = lyricLines;
        this.lyricUrl = lyricUrl;
    }

    public Lyric setLyrics(String lyricHtml) {
        lyricHtml = lyricHtml.replaceAll(HTML_COMMENT_REGEX, "").trim();
        lyricHtml = lyricHtml.replaceAll(NEWLINE_CHAR, "");
        this.lyricLines = Arrays.asList(lyricHtml.split(HTML_LINE_BREAK_REGEX));
        return this;
    }

    public List<String> getLyrics() {
        return lyricLines;
    }

    public Lyric setLyrics(List<String> lyricLines) {
        this.lyricLines = lyricLines;
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
        return new Lyric(Collections.emptyList(), "No Lyrics Found");
    }
}
