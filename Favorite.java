    package app.lyricsapp.model;

    import java.io.IOException;

    public class Favorite extends Playlist {

        Playlist playlist;

        public Favorite() {
            super();
        }

        public Playlist getFavorite() throws IOException {
            Playlist favoritePlaylist = new Playlist();
            for (Musique musique : this.playlist.getMusiqueList()) {
                if (musique.getLike()) {
                    favoritePlaylist.addMusique(musique);
                }
            }
            return favoritePlaylist;
        }

        public void removeToFavorite(Musique musique) throws IOException {
            if (!this.getFavorite().isEmpty()) {
                if (!this.getFavorite().contains(musique)) {
                    System.out.println("Can't remove: musique not in list");
                } else {
                    musique.setLike(false);
                    this.getFavorite().removeMusique(musique);
                    System.out.println("Musique removed from favorite list");
                    this.getFavorite().saveToFile(
                            "C://Users//Dell//My Projet//lyricsapp//src//main//resources//app//lyricsapp//Data//favorite.xml");
                }
            } else {
                System.out.println("Can't remove: favorite list is empty");
            }
        }

        public static void addToFavorite(Musique musique) throws IOException {
            Favorite favoritePlaylist = new Favorite();
            if (!favoritePlaylist.getFavorite().contains(musique)) {
                favoritePlaylist.getFavorite().addMusique(musique);
                favoritePlaylist.getFavorite().saveToFile(
                        "C://Users//Dell//My Projet//lyricsapp//src//main//resources//app//lyricsapp//Data//favorite.xml");
            }
        }

        public void refreshFavorite() throws IOException {
            if (this.getFavorite().isEmpty()) {
                for (Musique musique : this.getFavorite().getMusiqueList()) {
                    musique.setLike(false);
                }
                this.getFavorite().getMusiqueList().clear();
                this.saveToFile(
                        "C://Users//Dell//My Projet//lyricsapp//src//main//resources//app//lyricsapp//Data//favorite.xml");
            }
        }

    }
