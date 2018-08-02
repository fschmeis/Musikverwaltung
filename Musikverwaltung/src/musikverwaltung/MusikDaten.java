package musikverwaltung;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;

public class MusikDaten {
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
			}
			catch (FileNotFoundException ex) {}
		}
		
	}

	public void musikLoeschen(String datenTitel) throws IOException {
		
		File inputFile = new File("playlists/alleLieder.txt");
		File tempFile = new File("playlists/alleLiederTEMP.txt");
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
