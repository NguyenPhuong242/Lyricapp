package app.lyricsapp.model;

public class Musique {
    private String LyricId;
    private String LyricSong;
    private String LyricArtist;
    private String Lyric;
    private String LyricSongUrl;
    private String LyricArtistUrl;
    private Boolean like;

    public Musique(String LyricSong, String LyricArtist) {
        this.LyricSong = LyricSong;
        this.LyricArtist = LyricArtist;
        this.like = false;
    }

    public Musique(String song, String artist, String lyric) {
        this.LyricSong = song;
        this.LyricArtist = artist;
        this.Lyric = lyric;
        this.like = false;
    }

    public Musique(String song, String artist, String songurl, String artisturl) {
        this.LyricSong = song;
        this.LyricArtist = artist;
        this.LyricSongUrl = songurl;
        this.LyricArtistUrl = artisturl;
        this.like = false;
    }

    public String getSong() {
        return this.LyricSong;
    }

    public String getArtist() {
        return this.LyricArtist;
    }

    public String getLyric() {
        return this.Lyric;
    }

    public String getSongUrl(){
        return this.LyricSongUrl;
    }

    public String getArtistUrl(){
        return this.LyricArtistUrl;
    }

    public String toString() {
        return LyricSong + " || " + LyricArtist;
    }

    public String toStrings() {
        return LyricSong + " || " + LyricArtist + "\n" + "<" + Lyric + ">";
    }

    public Boolean getLike() {
        return this.like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

}
