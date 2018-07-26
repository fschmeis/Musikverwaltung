package musikverwaltung;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class MusikGUI extends JFrame {
	
	//Komponenten
	Container cp;						//contentPane
	JPanel pBenutzermod;				//BenutzermodusPanel
	JPanel pVerwaltungsmod;				//VerwaltungsmodusPanel

	JButton btnPlay = new JButton();
	JButton btnStop = new JButton();
	JButton btnNewPlaylist = new JButton("Neue Playlist");
	
	String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
	
	JTable tblPlaylist;
	JComboBox playlist = new JComboBox(petStrings);
	
	//Menüleiste
	JMenuBar bar;
	JMenu dateimenu;
	JMenu modusmenu;
	JMenuItem beendenItem;
	JMenuItem bModusItem;
	JMenuItem vModusItem;
	
	MusikPlayer player = new MusikPlayer();
	
	boolean play = false;
	
	//Konstruktor
	public MusikGUI() {
		this.setTitle("Musikverwaltung");
		this.setLocation(100, 200);
		this.setSize(1200, 700);
		
		//Beenden bei Klick auf rotes Kreuz
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		//Menu
		bar = new JMenuBar();
		dateimenu = new JMenu("Datei");
		modusmenu = new JMenu("Modus");
		beendenItem = new JMenuItem("Beenden");
		beendenItem.addActionListener(e->{System.exit(0);});
		bModusItem = new JMenuItem("Benutzermodus");
		bModusItem.addActionListener(e->bModusAuf());
		vModusItem = new JMenuItem("Verwaltungsmodus");
		vModusItem.addActionListener(e->vModusAuf());
		
		dateimenu.add(beendenItem);
		modusmenu.add(bModusItem);
		modusmenu.add(vModusItem);
		bar.add(dateimenu);
		bar.add(modusmenu);
		this.setJMenuBar(bar);
		
		//contentPane
		cp = this.getContentPane();
		cp.setLayout(new CardLayout());
		pBenutzermod = new JPanel();
		pBenutzermod.setLayout(null);
		pBenutzermod.setBackground(Color.YELLOW);
		cp.add(pBenutzermod);
		
		pVerwaltungsmod = new JPanel();
		pVerwaltungsmod.setLayout(null);
		pVerwaltungsmod.setVisible(false);
		pVerwaltungsmod.setBackground(Color.CYAN);
		cp.add(pVerwaltungsmod);
		
		pBenutzermod.add(btnPlay);
		btnPlay.setBounds(50, 200, 70, 70);
		btnPlay.addActionListener(e->abspielen());
		btnPlay.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
		
		try {
			Image img = ImageIO.read(new FileInputStream("icons/play.png"));
		    btnPlay.setIcon(new ImageIcon(img));
		    btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
		} catch (Exception ex) {
		    System.out.println(ex);
		}
		
		pBenutzermod.add(btnStop);
		btnStop.setBounds(190, 200, 70, 70);
		btnStop.addActionListener(e->stoppen());
		btnStop.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
		
		try {
			Image img = ImageIO.read(new FileInputStream("icons/stop.png"));
			btnStop.setIcon(new ImageIcon(img));
			btnStop.setHorizontalTextPosition(SwingConstants.CENTER);
		} catch (Exception ex) {
		    System.out.println(ex);
		}
		
		pBenutzermod.add(btnNewPlaylist);
		btnNewPlaylist.setBounds(330, 200, 150, 30);
		btnNewPlaylist.addActionListener(e->stoppen());
		btnNewPlaylist.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
	}
	
	public void bModusAuf() {
		pBenutzermod.setVisible(true);
		pVerwaltungsmod.setVisible(false);
	}
	
	public void vModusAuf() {
		pBenutzermod.setVisible(false);
		pVerwaltungsmod.setVisible(true);
	}
	
	public void abspielen() {
		
		if(play == false) {
			try {
				Image img = ImageIO.read(new FileInputStream("icons/pause.png"));
				btnPlay.setIcon(new ImageIcon(img));
				btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
			} catch (Exception ex) {
			    System.out.println(ex);
			}
			
			play = true;
			
			player.musikAbspielen();
		}
		else {
			stoppen();
		}
		
	}
	
	public void stoppen() {
		
		if(play == true) {
			try {
				Image img = ImageIO.read(new FileInputStream("icons/play.png"));
				btnPlay.setIcon(new ImageIcon(img));
				btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
			} catch (Exception ex) {
			    System.out.println(ex);
			}
			
			play = false;
			
			player.musikStoppen();
		}
		
	}
}
