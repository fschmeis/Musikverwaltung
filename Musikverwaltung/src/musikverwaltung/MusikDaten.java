package musikverwaltung;

import java.io.*;

public class MusikDaten {
	public void MusikSpeichern(String titel, String interpret, String album, Object genre, String path) {
		//Leider Geil,Deichkind,Befehl von ganz unten,Electronic,music/Leider Geil.wav
		try {
			File f = new File("playlists/AlleTitel.txt");
			PrintWriter pw = new PrintWriter(new FileOutputStream(f,true));
			pw.append(titel + "," + interpret + "," + album + "," + genre + "," + path + "\n");
			pw.close();
		} catch (FileNotFoundException e) {}
		
	}
}
