package musikverwaltung;

import java.io.File;
import java.util.ArrayList;

public class MusikPlaylist {
	
	public void playlistSpeichern() {
		
	}
	
	public void playlistLesen() {
		
	}
	
	public String[] allePlaylists() {
		
		ArrayList<String> listPlaylists = new ArrayList<String>();
		
		String strFilename = "";
		
		int iExtension = 0;
		
		File folder = new File("playlists/");
		File[] listOfFiles = folder.listFiles();

		listPlaylists.add("bitte auswählen...");
		
		for (int i = 0; i < listOfFiles.length; i++) {
			
			iExtension = listOfFiles[i].getName().lastIndexOf('.');
			
            if (iExtension > 0) {
            	strFilename = listOfFiles[i].getName().substring(0, iExtension);
            }
            	
            listPlaylists.add(strFilename);
		}
		
		return listPlaylists.toArray(new String[0]);
	}
	
}
