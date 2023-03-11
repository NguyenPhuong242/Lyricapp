package app.lyricsapp;

import app.lyricsapp.model.Favorite;
import app.lyricsapp.model.Musique;
import app.lyricsapp.model.Playlist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import static app.lyricsapp.model.Playlist.XMLToList;
import static app.lyricsapp.model.Playlist.XMLToLyrics;
import static app.lyricsapp.model.Playlist.XMLToIDandChecksum;

public class Command {

    static String pathlyric = "C://Users//Dell//My Projet//lyricsapp//src//main//resources//app//lyricsapp//Data//lyrics.xml";
    static String pathartisttitre = "C://Users//Dell//My Projet//lyricsapp//src//main//resources//app//lyricsapp//Data//artistetitre.xml";
    static String pathtext = "C://Users//Dell//My Projet//lyricsapp//src//main//resources//app//lyricsapp//Data//textLyrics.xml";
    static String pathfavorite = "C://Users//Dell//My Projet//lyricsapp//src//main//resources//app//lyricsapp//Data//favorite.xml";

    private static Scanner scanner = new Scanner(System.in);
    private Scanner scan;
    private static Favorite favorite;

    public Command() throws Exception {
        scan = new Scanner(System.in);
        if(!Favorite.readXML(pathfavorite).isEmpty()){
            favorite.refreshFavorite();
        }
    }
    public static void showMenu() throws Exception {
        int choice = 0;
        while (choice != 3) {
            System.out.println("Welcome to the lyrics app");
            System.out.println("-----------------------------");
            System.out.println("Votre voulez quoi?  ");
            System.out.println("1. Accedez liste favoris");
            System.out.println("2. Recherche");
            System.out.println("3. Exit");
            System.out.print("Entrez votre choisisez: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    ArrayList<Musique> favorites = Favorite.readXML(pathfavorite);
                    if (favorites != null) {
                        System.out.println(favorites.toString());
                    } else {
                        System.out.println("No favorites found.");
                    }
                    break;
                case 2:
                    recherche();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    public static void recherche() throws Exception {
        System.out.println("-------------------------------------");
        System.out.println("Votre recherche peux se faire : ");
        System.out.println("1. par titre et artiste");
        System.out.println("2. par paroles");
        System.out.println("3. retournez menu");
        System.out.print("Entrez votre choisisez: ");

        int choix = scanner.nextInt();

        switch (choix) {
            case 1:
                String artist = getArtistInput();
                String titre = getTitreInput();
                searchByAT(artist, titre);
                Playlist afficher = XMLToList(pathartisttitre);
                System.out.println(afficher.toString());// afficher toute le chanson qui contient le parole
                StepContinueAT();
                break;
            case 2:
                String paroles = getParolesInput();
                searchByLyric(paroles);
                Playlist show = XMLToLyrics(pathtext);
                for (int i = 0; i < show.size(); i++) {
                    System.out.println(show.get(i).toStrings());
                }
                StepContinueLyric();
                break;
            case 3:
                showMenu();
            default:
                System.out.println("Choix invalide.");

        }
    }

    public static void StepContinueAT() throws Exception {
        System.out.println("-------------------------------------------------------");
        System.out.println("1. Recherche");
        System.out.println("2. Ajouter liste favoris");
        System.out.println("3. Quit");

        Scanner scanner1 = new Scanner(System.in);

        int after = scanner1.nextInt();

        if (after == 1) {
            recherche();
            after = scanner1.nextInt();
        } else if (after == 2) {
            System.out.print("input your musique you want to add?: ");
            String input = scanner1.next();
            for(Musique musique: Favorite.readXML(pathartisttitre)){
                if(musique.getSong().equals(input)){
                    System.out.println(musique);
                }
            }
            System.out.println("Done");

            after = scanner1.nextInt();
        } else if (after == 3) {
            System.exit(0);
        } else {
            System.out.println("Choix valide");
        }
    }

    public static void StepContinueLyric() throws Exception {
        System.out.println("-------------------------------------------------------");
        System.out.println("1. Recherche");
        System.out.println("2. Ajouter liste favoris");
        System.out.println("3. Quit");

        Scanner scanner1 = new Scanner(System.in);

        int after = scanner1.nextInt();

        if (after == 1) {
            recherche();
            after = scanner1.nextInt();
        } else if (after == 2) {
            System.out.print("input your musique you want to add?: ");
            String input = scanner1.next();
            System.out.println("Done");

            after = scanner1.nextInt();
        } else if (after == 3) {
            System.exit(0);
        } else {
            System.out.println("Choix valide");
        }
    }

    // ----------------------- function search --------------------------------//
    public static void searchByLyric(String input) throws Exception {
        Playlist listparolemusique = new Playlist();
        listparolemusique.getLyricsAndWriteToXMLFile(input, pathlyric);
        List<String> lyric = XMLToIDandChecksum(pathlyric);
        Playlist listparole = new Playlist();
        if (lyric.isEmpty()) {
            System.out.println("pas trouver");
        } else {
            String checkSum = lyric.get(0);
            String id = lyric.get(1);
            listparole.getlyricidlyricchecksumAndWriteToXMLFile(id, checkSum, pathtext);
        }
    }

    public static void searchByAT(String input1, String input2) throws Exception {
        Playlist listtitreartist = new Playlist();
        listtitreartist.getArtistTitreAndWriteToXMLFile(input1, input2, pathartisttitre);
    }

    private static String getArtistInput() {
        System.out.print("Artiste : ");
        scanner.nextLine();
        return scanner.nextLine();
    }

    private static String getTitreInput() {
        System.out.print("Titre : ");
        return scanner.nextLine();
    }

    private static String getParolesInput() {
        System.out.print("Paroles : ");
        scanner.nextLine();
        return scanner.nextLine();
    }

}
