package musikverwaltung;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
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
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class MusikGUI extends JFrame {
	
	MusikPlayer player = new MusikPlayer();
	MusikPlaylist playlist = new MusikPlaylist();
	
	//Komponenten
	Container cp;						//contentPane
	JPanel pBenutzermod;				//BenutzermodusPanel
	JPanel pVerwaltungsmod;				//VerwaltungsmodusPanel

	JButton btnPlay = new JButton();
	JButton btnStop = new JButton();
	JButton btnNewPlaylist = new JButton("Neue Playlist");
	
	JProgressBar progBar = new JProgressBar();
	
	String[] columnNames = {"Nr", "Titel", "Interpret", "Album", "Genre"};
	
	Object[][] data = {
		    {new Integer(1), "Leider Geil", "Deichkind", "Befehl von ganz unten", "Electronic"},
		    {new Integer(2), "So ne Musik", "Deichkind", "Niveau Weshalb Warum", "Electronic"},
	};
	
	JTable tblPlaylist = new JTable(data, columnNames);
	JScrollPane scrollpane = new JScrollPane(tblPlaylist);
	JComboBox cPlaylist = new JComboBox(playlist.allePlaylists());
	
	//Menüleiste
	JMenuBar bar;
	JMenu dateimenu;
	JMenu modusmenu;
	JMenuItem beendenItem;
	JMenuItem bModusItem;
	JMenuItem vModusItem;
	
	boolean play = false;
	boolean pause = false;
	
	//Konstruktor
	public MusikGUI() {
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.setTitle("Musikverwaltung");
		this.setSize(1200, 700);
		
		//Mitte des Bildschirms
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		//Vollbild
//		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
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
		
		pBenutzermod.add(scrollpane);
		scrollpane.setBounds(50, 200, 700, 200);
		
		pBenutzermod.add(cPlaylist);
		cPlaylist.setBounds(50, 100, 200, 30);
		
		pBenutzermod.add(btnPlay);
		btnPlay.setBounds(50, 550, 38, 38);
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
		btnStop.setBounds(100, 550, 38, 38);
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
		btnNewPlaylist.setBounds(330, 550, 150, 30);
		btnNewPlaylist.addActionListener(e->stoppen());
		btnNewPlaylist.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
		
		pBenutzermod.add(progBar);
		progBar.setBounds(50, 600, 700, 30);
		progBar.setValue(0);
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
				pause = false;
				
				player.musikAbspielen(0L);
		}
		else {
			
			if(pause == false) {
				try {
					Image img = ImageIO.read(new FileInputStream("icons/play.png"));
					btnPlay.setIcon(new ImageIcon(img));
					btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
				} catch (Exception ex) {
				    System.out.println(ex);
				}
				
				pause = true;
				
				player.musikStoppen();
			}
			else {
				try {
					Image img = ImageIO.read(new FileInputStream("icons/pause.png"));
					btnPlay.setIcon(new ImageIcon(img));
					btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
				} catch (Exception ex) {
				    System.out.println(ex);
				}
				
				pause = false;
				
				player.musikWeiterspielen();
			}
			
			
			
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
