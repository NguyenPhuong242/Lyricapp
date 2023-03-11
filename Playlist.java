package app.lyricsapp.model;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.print.PrinterAttributes;

public class Playlist implements Serializable {
    
    private ArrayList<Musique> musiqueList;
    private static final String API_ENDPOINT_LYRIC = "http://api.chartlyrics.com/apiv1.asmx/SearchLyricText";
    private static final String API_ENDPOINT_ARTIST_TITRE = "http://api.chartlyrics.com/apiv1.asmx/SearchLyric";
    private static final String API_ENDPOINT_LYRICID_LYRICCHECKSUM = "http://api.chartlyrics.com/apiv1.asmx/GetLyric";
    private String path;

    public Playlist() {
        this.musiqueList = new ArrayList<>();
    }

    public void addMusique(Musique musique) throws IOException {
        musiqueList.add(musique);
        // saveToFile(path);
    }

    public void removeMusique(Musique musique) throws IOException {
        musiqueList.remove(musique);
        // saveToFile(path);
    }

    public void removeMusique(int index) throws IOException {
        musiqueList.remove(index);
        // saveToFile(path);
    }

    public ArrayList<Musique> getMusiqueList() {
        return musiqueList;
    }

    protected boolean isEmpty() {
        return this.musiqueList.size() == 0;
    }

    public void saveToFile(String path) throws IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("playlist");
            doc.appendChild(rootElement);
            for (Musique musique : musiqueList) {
                Element musiqueElement = doc.createElement("musique");
                rootElement.appendChild(musiqueElement);
                Element artistElement = doc.createElement("artist");
                artistElement.appendChild(doc.createTextNode(musique.getArtist()));
                musiqueElement.appendChild(artistElement);
                Element titreElement = doc.createElement("titre");
                titreElement.appendChild(doc.createTextNode(musique.getSong()));
                musiqueElement.appendChild(titreElement);
                Element albumElement = doc.createElement("songUrl");
                albumElement.appendChild(doc.createTextNode(musique.getSongUrl()));
                musiqueElement.appendChild(albumElement);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    // public static Playlist loadFromFile(String path) throws IOException, ClassNotFoundException {
    //     return null;
    // }

    public boolean contains(Musique musique) {
        if(!musiqueList.isEmpty()){
            for(Musique o: musiqueList){
                if(o.equals(musique)){
                    return true;
                }
            }
        }
        return false;
    }

    public Musique get(int i) {
        return this.musiqueList.get(i);
    }

    public int size() {
        return this.musiqueList.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Musique musique : musiqueList) {
            sb.append(musique.toString()).append("\n");
        }
        return sb.toString();
    }

    //------------------------------------------Write API to XML File ---------------------------------------------------------------//

    public void getArtistTitreAndWriteToXMLFile(String artist,String titre, String fileName) throws Exception {
        String encodedArtistText = URLEncoder.encode(artist, "UTF-8");
        String encodedTitreText = URLEncoder.encode(titre, "UTF-8");

        URL url = new URL(API_ENDPOINT_ARTIST_TITRE + "?artist=" + encodedArtistText + "&song=" + encodedTitreText);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        FileWriter writer = new FileWriter(fileName);
        writer.write(response.toString());
        writer.close();
    }

    public void getLyricsAndWriteToXMLFile(String lyricText, String fileName) throws Exception {
        String encodedLyricText = URLEncoder.encode(lyricText, "UTF-8");

        URL url = new URL(API_ENDPOINT_LYRIC + "?lyricText=" + encodedLyricText);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        FileWriter writer = new FileWriter(fileName);
        writer.write(response.toString());
        writer.close();
    }

    public void getlyricidlyricchecksumAndWriteToXMLFile(String lyricid,String lyricchecksum, String fileName) throws Exception {
        String encodedlyricidText = URLEncoder.encode(lyricid, "UTF-8");
        String encodedlyricchecksumText = URLEncoder.encode(lyricchecksum, "UTF-8");

        URL url = new URL(API_ENDPOINT_LYRICID_LYRICCHECKSUM + "?lyricId=" + encodedlyricidText + "&lyricCheckSum=" + encodedlyricchecksumText);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        FileWriter writer = new FileWriter(fileName);
        writer.write(response.toString());
        writer.close();
    }

    //------------------------------ Transform XML to Arraylist -------------------------------------------------------------//

    public static Playlist XMLToList(String path) throws IOException {
        List<String> songs = new ArrayList<>();
        List<String> artists = new ArrayList<>();
        try {
            File inputFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("SearchLyricResult");
            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Node songNode = eElement.getElementsByTagName("Song").item(0);
                    if (songNode != null) {
                        songs.add(songNode.getTextContent());
                    }
                    Node artistNode = eElement.getElementsByTagName("Artist").item(0);
                    if (artistNode != null) {
                        artists.add(artistNode.getTextContent());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Playlist liste = new Playlist();
        Musique musique;
        for (int i = 0; i < songs.size(); i++) {
            musique = new Musique(songs.get(i), artists.get(i));
            liste.addMusique(musique);
        }
        return liste;
    }


    public static Playlist XMLToLyrics(String path) throws IOException {
        List<String> songs = new ArrayList<>();
        List<String> artists = new ArrayList<>();
        List<String> lyrics = new ArrayList<>();
        try {
            File inputFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("GetLyricResult");
            System.out.println("----------------------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Node songNode = eElement.getElementsByTagName("LyricSong").item(0);
                    if (songNode != null) {
                        songs.add(songNode.getTextContent());
                    }
                    Node artistNode = eElement.getElementsByTagName("LyricArtist").item(0);
                    if (artistNode != null) {
                        artists.add(artistNode.getTextContent());
                    }
                    Node lyricNode = eElement.getElementsByTagName("Lyric").item(0);
                    if (lyricNode != null) {
                        lyrics.add(lyricNode.getTextContent());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Playlist liste = new Playlist();
        Musique musique;
        for (int i = 0; i < songs.size(); i++) {
            musique = new Musique(songs.get(i), artists.get(i), lyrics.get(i));
            liste.addMusique(musique);
        }
        return liste;
    }

    public static List<String> XMLToIDandChecksum(String path) throws IOException {
        List<String> text = new ArrayList<String>();
        try {
            File inputFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("SearchLyricResult");
            System.out.println("----------------------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Node checksumNode = eElement.getElementsByTagName("LyricChecksum").item(0);
                    if (checksumNode != null) {
                        text.add(checksumNode.getTextContent());
                    }
                    Node lyricidNode = eElement.getElementsByTagName("LyricId").item(0);
                    if (lyricidNode != null) {
                        text.add(lyricidNode.getTextContent());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> result = text;
        return result;
    }

    public static ArrayList<Musique> readXML(String fileName) throws Exception{
        ArrayList<Musique> musiques = new ArrayList<>();
        try {
            File xmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("musique");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String titre = element.getElementsByTagName("titre").item(0).getTextContent();
                String artiste = element.getElementsByTagName("artiste").item(0).getTextContent();

                Musique m = new Musique(titre, artiste);
                musiques.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return musiques;
    }
    
}

