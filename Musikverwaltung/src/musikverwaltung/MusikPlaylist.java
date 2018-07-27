package musikverwaltung;

import java.io.File;

public class MusikPlaylist {
	
	public void playlistSpeichern() {
		
	}
	
	public void playlistLesen() {
		
	}
	
	public String allePlaylists() {
		
		String strPlaylists = "bitte auswählen...";
		
		File folder = new File("playlists/");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			strPlaylists = strPlaylists + "," + listOfFiles[i].getName().toString();
		}
		
		return strPlaylists;
	}
	
}
