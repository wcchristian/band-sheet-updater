package com.anderc.riptiderenamer.delegate;


import com.anderc.riptiderenamer.model.Lyric;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class AZLyricsDelegate {

    private static final String AZ_LYRICS_SEARCH_URL = "https://search.azlyrics.com/search.php?q=";

    public AZLyricsDelegate() {
    }

    public Lyric getLyrics(String songTitle, String songArtist) {
        Lyric lyric;
        String lyricLink = "NO LYRICS";
        try {
            lyricLink = getLyricLink(songTitle, songArtist);

            if(lyricLink != null) {
                lyric = new Lyric().setLyricUrl(lyricLink);
                Document doc = Jsoup.connect(lyricLink).get();

                try {
                    lyric.setLyrics(doc.body().getElementsByClass("lyricsh").parents().get(0).getElementsByTag("div").get(7).text());
                    return lyric;

                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Couldn't get Lyrics for: "+ songTitle + "-" + songArtist + " at " + lyricLink);
                    return Lyric.getDefaultLyric();
                }

            } else {
                return Lyric.getDefaultLyric();
            }
        } catch(IOException e) {
            System.out.println("Failed: " + lyricLink + " Song: " + songTitle);
            return Lyric.getDefaultLyric();
        }
    }

    private String getLyricLink(String title, String artist) throws IOException {
        String topLink;
        String query = title + "+" + artist;
        Document doc = Jsoup.connect(AZ_LYRICS_SEARCH_URL + query).get();
        Elements tables = doc.body().getElementsByTag("table");

        if(!tables.isEmpty()) {
            Elements links = tables.get(0).getElementsByTag("a");
            topLink = findFirstLyricLink(links);
        } else {
            topLink = getLyricLink(title);
        }

        return topLink;
    }

    private String getLyricLink(String title) throws IOException {
        String topLink;
        Document doc = Jsoup.connect(AZ_LYRICS_SEARCH_URL + title).get();
        Elements tables = doc.body().getElementsByTag("table");

        if(!tables.isEmpty()) {
            Elements links = tables.get(0).getElementsByTag("a");
            topLink = findFirstLyricLink(links);
        } else {
            topLink = null;
        }

        return topLink;
    }

    private String findFirstLyricLink(Elements links) {
        for(Element el: links) {
            String href = el.attributes().get("href");
            if(href.contains("https")) {
                return href;
            }
        }
        return null;
    }
}
