package musikverwaltung;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;

public class MusikDaten {
	
	MusikPlaylist playlist = new MusikPlaylist();	
	/**
	 * speichert Informationen zu einem neuen Lied in der �bersicht und der Datei mit allen Lieder
	 * 
	 * @param titel
	 * @param interpret
	 * @param album
	 * @param genre
	 * @param path
	 */
	public void musikSpeichern(String titel, String interpret, String album, Object genre, String path) {
		
		if (titel.equals("") || interpret.equals("") || album.equals("") ||genre.equals("Ausw�hlen...")) {
			JOptionPane.showMessageDialog(null, "Nicht alle Felder ausgef�llt!", "", JOptionPane.WARNING_MESSAGE);
		}
		else {
			try {
				File f = new File("playlists/alleLieder.txt");
				PrintWriter pw = new PrintWriter(new FileOutputStream(f,true));
				String date = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
				pw.append(titel + "," + interpret + "," + album + "," + genre + "," + date + "," + path + "\n");
				pw.close();
			} catch (FileNotFoundException ex) {}
		}
		
	}
	
	/**
	 * l�scht das durch Rechtsklick ausgew�hlte Lied
	 * aus allen Playlisten oder einer gew�hlten Playlist
	 * 
	 * @param datenTitel
	 * @param strPlaylist
	 * @throws IOException
	 */
	public void musikLoeschen(String datenTitel, String strPlaylist) throws IOException {
		
		if(strPlaylist == "alleTitel") {
	        File folder = new File("playlists/");
	        File[] listOfFiles = folder.listFiles();
	        
	        for(int i = 0; i < listOfFiles.length; i++) {
	            System.out.println();
	            File inputFile = new File("playlists/" + listOfFiles[i].getName());
	            File tempFile = new File("playlists/TEMP" + listOfFiles[i].getName());
	            BufferedReader br = new BufferedReader(new FileReader(inputFile));
	            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
	            String line = null;

	            while ((line = br.readLine()) != null) {
	                if (!line.trim().equals(datenTitel)) {
	                    pw.println(line);
	                    pw.flush();
	                }
	            }

	            pw.close();
	            br.close();
	            inputFile.delete();
	            tempFile.renameTo(inputFile);
	        }
		}
		else {
			File inputFile = new File("playlists/" + strPlaylist + ".txt");
			File tempFile = new File("playlists/" + strPlaylist + "TEMP.txt");
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
	        PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
	        String line = null;
	        
	        while ((line = br.readLine()) != null) {
	            if (!line.trim().equals(datenTitel)) {
	                pw.println(line);
	                pw.flush();
	            }
	        }
	        
	        pw.close();
	        br.close();
	        inputFile.delete();
	        tempFile.renameTo(inputFile);
		}
	}
}
