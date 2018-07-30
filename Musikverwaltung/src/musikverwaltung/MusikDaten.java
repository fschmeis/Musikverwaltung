package musikverwaltung;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class MusikDaten {
	public void MusikSpeichern(String titel, String interpret, String album, Object genre, String path) {
		//Leider Geil,Deichkind,Befehl von ganz unten,Electronic,music/Leider Geil.wav
		if (titel.equals("") || interpret.equals("") || album.equals("") ||genre.equals("Auswählen...")) {
			JOptionPane.showMessageDialog(null, "Nicht alle Felder ausgefüllt!", "", JOptionPane.WARNING_MESSAGE);
		}
		else {
			try {
				File f = new File("playlists/alleLieder.txt");
				PrintWriter pw = new PrintWriter(new FileOutputStream(f,true));
				String date = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
				pw.append(titel + "," + interpret + "," + album + "," + genre + "," + date + "," + path + "\n");
				pw.close();
			} catch (FileNotFoundException e) {}
		}
		
	}
}
