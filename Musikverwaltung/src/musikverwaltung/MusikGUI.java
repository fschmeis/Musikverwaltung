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
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class MusikGUI extends JFrame {
	
	MusikPlayer player = new MusikPlayer();
	MusikPlaylist playlist = new MusikPlaylist();
	
	//Komponenten
	Container cp;						//contentPane
	JPanel pBenutzermod;				//BenutzermodusPanel
	JPanel pVerwaltungsmod;				//VerwaltungsmodusPanel
	
	//Menüleiste
	JMenuBar bar;
	JMenu dateimenu;
	JMenu modusmenu;
	JMenuItem beendenItem;
	JMenuItem bModusItem;
	JMenuItem vModusItem;
	
	//Benutzermodus-Komponenten
	JLabel lblPlaylisten = new JLabel("Playlisten");
	
	JButton btnPlay = new JButton();
	JButton btnStop = new JButton();
	JButton btnNewPlaylist = new JButton("+");
	
	JProgressBar progBar = new JProgressBar();
	
	String[] columnNames = {"Nr", "Titel", "Interpret", "Album", "Genre"};
	
	Object[][] data = {
		    {new Integer(1), "Leider Geil", "Deichkind", "Befehl von ganz unten", "Electronic"},
		    {new Integer(2), "So ne Musik", "Deichkind", "Niveau Weshalb Warum", "Electronic"},
	};
	
	JTable tblPlaylist = new JTable(data, columnNames);
	JScrollPane scpPlaylist = new JScrollPane(tblPlaylist);
	JComboBox<String> cPlaylist = new JComboBox<String>(playlist.allePlaylists());
	
	//Verwaltungsmodus-Komponenten
	JLabel lblTitel = new JLabel("Titel");
	JLabel lblInterpret = new JLabel("Interpret");
	JLabel lblAlbum = new JLabel("Album");
	JLabel lblGenre = new JLabel("Genre");
	
	JTextField tfTitel = new JTextField();
	JTextField tfInterpret = new JTextField();
	JTextField tfAlbum = new JTextField();
	JTextField tfGenre = new JTextField();
	
	JButton btnAddTitel = new JButton("+");
	JButton btnDelTitel = new JButton("-");
	
	JTable tblAlleTitel = new JTable(data, columnNames);
	JScrollPane scpAlleTitel = new JScrollPane(tblAlleTitel);
		
	//sonst. Variablen	
	boolean play = false;
	boolean pause = false;
	
	JLabel lblAktTitel = new JLabel(data[0][1] + " - " + data[0][2]);	//Vom Titel abhängiges Label
	
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
		
		//Benutzermodus-Panel
		pBenutzermod = new JPanel();
		pBenutzermod.setLayout(null);
		pBenutzermod.setBackground(Color.DARK_GRAY);
		cp.add(pBenutzermod);
		
		pBenutzermod.add(scpPlaylist);
		scpPlaylist.setBounds(50, 200, 700, 200);
		
		pBenutzermod.add(lblPlaylisten);
		lblPlaylisten.setForeground(Color.WHITE);
		lblPlaylisten.setBounds(50, 70, 200, 30);
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
		btnNewPlaylist.setBounds(260, 100, 150, 30);
		btnNewPlaylist.addActionListener(e->stoppen());
		btnNewPlaylist.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
		
		pBenutzermod.add(progBar);
		progBar.setBounds(50, 600, 700, 30);
		progBar.setValue(15);
			
		pBenutzermod.add(lblAktTitel);
		lblAktTitel.setForeground(Color.WHITE);
		lblAktTitel.setBounds(150, 550, 200, 38);
		
		//Verwaltungsmodus-Panel
		pVerwaltungsmod = new JPanel();
		pVerwaltungsmod.setLayout(null);
		pVerwaltungsmod.setVisible(false);
		pVerwaltungsmod.setBackground(Color.GRAY);
		cp.add(pVerwaltungsmod);
		
		pVerwaltungsmod.add(btnAddTitel);
		btnAddTitel.setBounds(780, 200, 50, 50);
		pVerwaltungsmod.add(btnDelTitel);
		btnDelTitel.setBounds(780, 260, 50, 50);
		
		pVerwaltungsmod.add(lblTitel);
		lblTitel.setBounds(50, 425, 200, 25);
		pVerwaltungsmod.add(lblInterpret);
		lblInterpret.setBounds(50, 475, 200, 25);
		pVerwaltungsmod.add(lblAlbum);
		lblAlbum.setBounds(275, 425, 200, 25);
		pVerwaltungsmod.add(lblGenre);
		lblGenre.setBounds(275, 475, 200, 25);
		
		pVerwaltungsmod.add(tfTitel);
		tfTitel.setBounds(50, 450, 200, 25);
		pVerwaltungsmod.add(tfInterpret);
		tfInterpret.setBounds(50, 500, 200, 25);
		pVerwaltungsmod.add(tfAlbum);
		tfAlbum.setBounds(275, 450, 200, 25);
		pVerwaltungsmod.add(tfGenre);
		tfGenre.setBounds(275, 500, 200, 25);
		
		pVerwaltungsmod.add(scpAlleTitel);
		scpAlleTitel.setBounds(50, 200, 700, 200);
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
