package musikverwaltung;

import java.io.*;

import javax.swing.JOptionPane;

public class MusikDaten {
	public void MusikSpeichern(String titel, String interpret, String album, Object genre, String path) {
		//Leider Geil,Deichkind,Befehl von ganz unten,Electronic,music/Leider Geil.wav
		if (titel.equals("") || interpret.equals("") || album.equals("") ||genre.equals("Ausw�hlen...")) {
			JOptionPane.showMessageDialog(null, "Nicht alle Felder ausgef�llt!", "", JOptionPane.WARNING_MESSAGE);
		}
		else {
			try {
				File f = new File("playlists/AlleTitel.txt");
				PrintWriter pw = new PrintWriter(new FileOutputStream(f,true));
				pw.append(titel + "," + interpret + "," + album + "," + genre + "," + path + "\n");
				pw.close();
			} catch (FileNotFoundException e) {}
		}
		
	}
}
