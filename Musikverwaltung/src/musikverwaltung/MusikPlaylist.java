package musikverwaltung;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class MusikPlaylist {
	
	String playlistName;
	
	/**
	 * öffnet einen Dialog und erstellt eine leere Playlist
	 * 
	 * @return 1 wenn die Playlist erstellt werden konnte, ansonsten 0
	 */
	public int speichernLeer() {
		
		boolean bereitsVorhanden = false;
		ArrayList<String> listAllePlaylists = new ArrayList<String>();

		ImageIcon icon = new ImageIcon("images/playlist.png");
		
		playlistName = (String) JOptionPane.showInputDialog(null, "Name:", "Neue Playlist", JOptionPane.OK_CANCEL_OPTION, icon, null, null);
		
		if(playlistName != null) {
			
			if(playlistName.equals("")) {
				JOptionPane.showMessageDialog(null, "Keinen Namen angegeben!", "", JOptionPane.WARNING_MESSAGE);
				
				return 0;
			}
			else {
				
				File file = new File("playlists/" + playlistName + ".txt");
				
				String[] playlists = allePlaylists();
				
				//überprüfen, ob es bereits eine Playlist mit dem eingegebenen Namen gibt
				for(int i = 0; i < playlists.length; i++) {
					if(playlists[i].equals(playlistName)) {
						bereitsVorhanden = true;
					}
				}
				
				if(bereitsVorhanden == false) {
					try {
						file.createNewFile();
						return 1;
					} catch (IOException e) {
						e.printStackTrace();
						return 0;
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Playlistname bereits vergeben!", "", JOptionPane.WARNING_MESSAGE);
					return 0;
				}
				
			}
		}	
		else {
			return 0;
		}
	}
	
	/**
	 * löscht die ausgewählte Playlist
	 * 
	 * @param delPlayList
	 */
	public void loeschen(String delPlayList) {
		
		File inputFile = new File("playlists/" + delPlayList + ".txt");

		if(inputFile.delete()) {
			JOptionPane.showMessageDialog(null, "Playlist wurde erfolgreich gelöscht", "", JOptionPane.WARNING_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(null, "Fehler aufgetreten, konnte nicht gelöscht werden", "", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * liest die ausgewählte Playlist aus,
	 * damit die Daten zu den Liedern in der Tabelle dargestellt werden
	 * 
	 * @param strPlaylist
	 * @return String[][] data mit den gespeicherten Lied-Informationen
	 */
	public String[][] lesen(String strPlaylist) {
		
		File fPlaylist = new File("playlists/" + strPlaylist + ".txt");
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			Scanner s = new Scanner(fPlaylist);
			
			while(s.hasNextLine()){
				list.add(s.nextLine());
			}
			
			s.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
			
		String[][] data = new String[list.size()][];
		
		for(int i = 0; i < list.size(); i++) {
		    data[i] = list.get(i).split(",");
		}
		
		return data;
	}
	
	/**
	 * liest alle Playlistnamen aus dem Ordner aus für die Combobox
	 * 
	 * @return String[] mit allen Playlistnamen
	 */
	public String[] allePlaylists() {
		
		ArrayList<String> listPlaylists = new ArrayList<String>();
		
		String strFilename = "";
		
		int iExtension = 0;
		
		File folder = new File("playlists/");
		File[] listOfFiles = folder.listFiles();

		for(int i = 0; i < listOfFiles.length; i++) {
			
			iExtension = listOfFiles[i].getName().lastIndexOf('.');
			
            if(iExtension > 0) {
            	strFilename = listOfFiles[i].getName().substring(0, iExtension);
            }
            	
            listPlaylists.add(strFilename);
		}
		
		return listPlaylists.toArray(new String[0]);
	}
	
	/**
	 * gibt den Namen einer neuen Playlist zurück
	 * 
	 * @return Name der neuen Playlist
	 */
	public String getnew() {
		try {
			return playlistName;
		} catch(NullPointerException ex) {
			return "alleLieder";
		}
	}
	
	/**
	 * fügt Infomationen eines Liedes zur ausgewählten Playlist hinzu
	 * 
	 * @param Playlist
	 * @param Lied
	 * @param nummer
	 * @throws IOException
	 */
	public static void addToPlaylist(String Playlist, String Lied, int nummer) throws IOException {
		
		File alleTitel = new File("playlists/alleLieder.txt");
		BufferedReader br = new BufferedReader(new FileReader(alleTitel));
		String Zeile = null; 
		
		for(int i = 1; i <= nummer + 1; i++) {
			Zeile = br.readLine();
		}
		
		File actualPlaylist = new File("playlists/" + Playlist + ".txt");

		PrintWriter pw = new PrintWriter(new FileOutputStream(actualPlaylist,true));
		pw.append(Zeile + "\n");
		pw.close();
		
	}
}