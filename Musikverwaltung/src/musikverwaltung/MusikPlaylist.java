package musikverwaltung;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MusikPlaylist {
	
	public void playlistSpeichern() {
		
		JFrame parent = new JFrame();
		String name = JOptionPane.showInputDialog(parent,"What is your name?", null);
	}
	
	public String[][] playlistLesen(String strPlaylist) {
		
		File fPlaylist = new File("playlists/" + strPlaylist + ".txt");
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			Scanner s = new Scanner(fPlaylist);
			
			while (s.hasNextLine()){
				list.add(s.nextLine());
			}
			s.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
			
		String[][] data = new String[list.size()][];
		String helper;
		
		for (int i = 0; i < list.size(); i++) {
			
			helper = Integer.toString(i+1) + "," + list.get(i).toString();
			
			String[] row = list.get(i).replaceFirst(list.get(i).substring(0, list.get(i).length()), helper).split(",");
		    data[i] = row;
		}
		
		return data;
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
