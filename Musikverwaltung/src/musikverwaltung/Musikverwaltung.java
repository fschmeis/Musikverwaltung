package musikverwaltung;

import musikverwaltung.MusikGUI;
import musikverwaltung.Musikverwaltung;

public class Musikverwaltung {
	
	private MusikGUI anzeige;
	
	public Musikverwaltung() {
		anzeige = new MusikGUI();
	}
	
	public void aktivieren() {
		anzeige.setVisible(true);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Musikverwaltung musik = new Musikverwaltung();
		musik.aktivieren();
	}
}
