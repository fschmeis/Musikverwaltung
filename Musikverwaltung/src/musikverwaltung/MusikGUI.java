package musikverwaltung;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;


public class MusikGUI extends JFrame {
	
	MusikPlayer player = new MusikPlayer();
	MusikPlaylist playlist = new MusikPlaylist();
	MusikDaten musikdaten = new MusikDaten();
	
	//Komponenten
	Container cp;						//contentPane
	JPanel pBenutzermod;				//BenutzermodusPanel
	JPanel pVerwaltungsmod;				//VerwaltungsmodusPanel
	
	//Menüleiste
	JMenuBar bar;
	JMenu startmenu;
	JMenu modusmenu;
	JMenuItem hilfeItem;
	JMenuItem beendenItem;
	JMenuItem bModusItem;
	JMenuItem vModusItem;
	
	//--------------------------------------------Benutzermodus-Komponenten-------------------------------------------------------
	JLabel lblBenutzermodus = new JLabel("Benutzermodus");
	JLabel lblPlaylisten = new JLabel("Playlisten");
	
	JButton btnPlay = new JButton();
	JButton btnStop = new JButton();
	JButton btnPrevious = new JButton();
	JButton btnNext = new JButton();
	JButton btnNewPlaylist = new JButton("Neue Playlist");
	JButton btnAddToPlaylist = new JButton("Titel hinzufügen");
	JButton btnDeletePlaylist = new JButton("Playlist löschen");
	
	JProgressBar progBar = new JProgressBar();
	
	//Action-Listener - aktualisiert die Progressbar und ruft den nächsten Titel auf, wenn diese ihr Maximum erreicht
	ActionListener progressor = new ActionListener () {
	    public void actionPerformed(ActionEvent evt) {
	    	progress++;
	    	progBar.setValue(progress);
	    	progBar.setMaximum(player.getduration());
	    		
	    	progBar.setString(progTime(progress) + " / " + progTime(progBar.getMaximum()));
	    		
	    	if(progress == progBar.getMaximum()) {
	    		nextTitel();
	    	}
	    }
	};
	    
	Timer timer = new Timer(1000, progressor);
	
	String[] columnNames = {"Titel", "Interpret", "Album", "Genre", "Datum", "Pfad"};
	
	String[][] data = playlist.lesen("alleLieder");
	JLabel lblImage = new JLabel();
	JLabel lblAktTitel = new JLabel("");	//Vom Titel abhängiges Label
	
	DefaultTableModel dtmPlaylist = new DefaultTableModel(data, columnNames) {
		@Override
	    public boolean isCellEditable(int row, int column) {
	       //alle Zellen sind nicht editierbar
	       return false;
	    }
	};
	
	JTable tblPlaylist = new JTable(dtmPlaylist);
	JScrollPane scpPlaylist = new JScrollPane(tblPlaylist);
	
	JComboBox<String> cPlaylist = new JComboBox<String>(playlist.allePlaylists());
	
	//------------------------------------------Verwaltungsmodus-Komponenten--------------------------------------------------------
	JLabel lblVerwaltungsmodus = new JLabel("Verwaltungsmodus");
	
	JButton btnAddTitel = new JButton("Titel hinzufügen");
	JLabel lblImage2 = new JLabel();
	
	DefaultTableModel dtmAlleTitel = new DefaultTableModel(data, columnNames) {
		@Override
	    public boolean isCellEditable(int row, int column) {
	       //alle Zellen sind nicht editierbar
	       return false;
	    }
	};
	
	JTable tblAlleTitel = new JTable(dtmAlleTitel);
	JScrollPane scpAlleTitel = new JScrollPane(tblAlleTitel);
		
	//sonstige Variablen	
	boolean play = false;
	boolean pause = false;
	int selectedRow = 0;
	int progress = 0;
	
	//Konstruktor
	public MusikGUI() {
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		//programm-Titel
		this.setTitle("Musikverwaltung");
		this.setSize(1060, 700);
		
		//Mitte des Bildschirms
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		//Beenden bei Klick auf rotes Kreuz
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		//Programm-Icon
		this.setIconImage(new ImageIcon("images/speaker.png").getImage());
		
		//Menu
		bar = new JMenuBar();
		startmenu = new JMenu("Start");
		modusmenu = new JMenu("Modus");
		hilfeItem = new JMenuItem("Hilfe");
		hilfeItem.addActionListener(e->openHelp());
		beendenItem = new JMenuItem("Beenden");
		beendenItem.addActionListener(e->{System.exit(0);});
		bModusItem = new JMenuItem("Benutzermodus");
		bModusItem.addActionListener(e->bModusAuf());
		vModusItem = new JMenuItem("Verwaltungsmodus");
		vModusItem.addActionListener(e->{vModusAuf(); stoppen();});
		
		startmenu.add(hilfeItem);
		startmenu.add(beendenItem);
		modusmenu.add(bModusItem);
		modusmenu.add(vModusItem);
		bar.add(startmenu);
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
		
		//Horizontale Linien ausblenden
		tblPlaylist.setShowHorizontalLines(false);
		tblAlleTitel.setShowHorizontalLines(false);
		
		//Farbe der ausgewählten Reihe
		tblPlaylist.setSelectionBackground(Color.ORANGE);
		tblAlleTitel.setSelectionBackground(Color.ORANGE);
		
		//Mouse-Listener tblPlaylist - spielt das durch Doppelklick ausgewählte Lied ab
        tblPlaylist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
            	if(evt.getButton() == 1) {
            		if (evt.getClickCount() == 1) {
            			stoppen();
            		}
            		else {
            			abspielen();
            		}
            	}
            }
        });
        
        //Mouse-Listener tblAlleTitel - löscht den durch Rechtsklick ausgewählten Titel
        tblAlleTitel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
            	if(evt.getButton() == 3) {
            		try {
						delTitel();
					} catch (IOException ex) {
						ex.printStackTrace();
					} 
            	}	           		
            }
            
            public void mouseReleased(MouseEvent evt)
            {
                if (evt.getButton() == 3) {
                    int row = tblAlleTitel.rowAtPoint( evt.getPoint() );
                    int column = tblAlleTitel.columnAtPoint( evt.getPoint() );
         
                    if (! tblAlleTitel.isRowSelected(row))
                        tblAlleTitel.changeSelection(row, column, false, false);
                }
            }
        });
        
        tblPlaylist.setAutoCreateRowSorter(true);
        
        tblAlleTitel.getTableHeader().setReorderingAllowed(false);
        tblPlaylist.getTableHeader().setReorderingAllowed(false);
		
		pBenutzermod.add(scpPlaylist);
		scpPlaylist.setBounds(20, 150, 1000, 200);
		
		pBenutzermod.add(lblPlaylisten);
		lblPlaylisten.setForeground(Color.WHITE);
		lblPlaylisten.setBounds(20, 70, 200, 30);
		
		pBenutzermod.add(cPlaylist);
		cPlaylist.setBounds(20, 100, 200, 30);
		cPlaylist.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		cPlaylist.addActionListener(e->playlistWechseln());
		
		pBenutzermod.add(btnPrevious);
		btnPrevious.setBounds(20, 550, 38, 38);
		btnPrevious.addActionListener(e->previousTitel());
		btnPrevious.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
		
		try {
			Image img = ImageIO.read(new FileInputStream("images/previous.png"));
			btnPrevious.setIcon(new ImageIcon(img));
			btnPrevious.setHorizontalTextPosition(SwingConstants.CENTER);
		} catch (Exception ex) {
		    System.out.println(ex);
		}
		
		pBenutzermod.add(btnPlay);
		btnPlay.setBounds(70, 550, 38, 38);
		btnPlay.addActionListener(e->abspielen());
		btnPlay.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
		
		try {
			Image img = ImageIO.read(new FileInputStream("images/play.png"));
		    btnPlay.setIcon(new ImageIcon(img));
		    btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
		} catch (Exception ex) {
		    System.out.println(ex);
		}
		
		pBenutzermod.add(btnStop);
		btnStop.setBounds(120, 550, 38, 38);
		btnStop.addActionListener(e->stoppen());
		btnStop.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
		
		try {
			Image img = ImageIO.read(new FileInputStream("images/stop.png"));
			btnStop.setIcon(new ImageIcon(img));
			btnStop.setHorizontalTextPosition(SwingConstants.CENTER);
		} catch (Exception ex) {
		    System.out.println(ex);
		}
		
		pBenutzermod.add(btnNext);
		btnNext.setBounds(170, 550, 38, 38);
		btnNext.addActionListener(e->nextTitel());
		btnNext.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
		
		try {
			Image img = ImageIO.read(new FileInputStream("images/next.png"));
			
			btnNext.setIcon(new ImageIcon(img));
			btnNext.setHorizontalTextPosition(SwingConstants.CENTER);
		} catch (Exception ex) {
		    System.out.println(ex);
		}
		
		pBenutzermod.add(btnNewPlaylist);
		btnNewPlaylist.setBounds(230, 100, 150, 30);
		btnNewPlaylist.addActionListener(e->{stoppen(); playlistSpeichern();});
		btnNewPlaylist.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
		
		pBenutzermod.add(btnAddToPlaylist);
		btnAddToPlaylist.setBounds(390, 100, 150, 30);
		btnAddToPlaylist.addActionListener(e->{stoppen(); addToPlaylist();});
		btnAddToPlaylist.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
		
		pBenutzermod.add(btnDeletePlaylist);
		btnDeletePlaylist.setBounds(550, 100, 150, 30);
		btnDeletePlaylist.addActionListener(e->{playlistLoeschen();});
		btnDeletePlaylist.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
				
		pBenutzermod.add(progBar);
		progBar.setBounds(20, 600, 1000, 30);
		progBar.setValue(progress);
		progBar.setBorderPainted(true);
		progBar.setStringPainted(true);
		progBar.setString("00:00 / 00:00");
		
		pBenutzermod.add(lblBenutzermodus);
		lblBenutzermodus.setForeground(Color.WHITE);
		lblBenutzermodus.setBounds(20, 0, 400, 50);
		lblBenutzermodus.setFont(new Font("", Font.BOLD, 30));
		
		pBenutzermod.add(lblImage);
		lblImage.setForeground(Color.WHITE);
		lblImage.setBounds(456, 400, 128, 128);
		lblImage.setIcon(new ImageIcon("images/bModus.png"));
		
		pBenutzermod.add(lblAktTitel);
		lblAktTitel.setForeground(Color.WHITE);
		lblAktTitel.setBounds(220, 550, 550, 38);
		
		//Verwaltungsmodus-Panel
		pVerwaltungsmod = new JPanel();
		pVerwaltungsmod.setLayout(null);
		pVerwaltungsmod.setVisible(false);
		pVerwaltungsmod.setBackground(Color.DARK_GRAY);
		cp.add(pVerwaltungsmod);
		
		pVerwaltungsmod.add(btnAddTitel);
		btnAddTitel.setBounds(20, 100, 150, 30);
		btnAddTitel.addActionListener(e->neuerTitel());
		btnAddTitel.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
				
		pVerwaltungsmod.add(lblVerwaltungsmodus);
		lblVerwaltungsmodus.setForeground(Color.WHITE);
		lblVerwaltungsmodus.setBounds(20, 0, 400, 50);
		lblVerwaltungsmodus.setFont(new Font("", Font.BOLD, 30));
		
		tblAlleTitel.setAutoCreateRowSorter(true);
		
		pVerwaltungsmod.add(lblImage2);
		lblImage2.setForeground(Color.WHITE);
		lblImage2.setBounds(456, 400, 128, 128);
		lblImage2.setIcon(new ImageIcon("images/vModus.png"));
		
		pVerwaltungsmod.add(scpAlleTitel);
		scpAlleTitel.setBounds(20, 150, 1000, 200);
	}
	
	String path = new String();
	
	/**
	 * öffnet einen Dialog zum Hinzufügen eines neuen Liedes und
	 * ruft die Funktion zum Speichern des Eintrages auf
	 */
	private void neuerTitel() {
		
		JPanel pNeuerTitel = new JPanel();
		JTextField tfTitel = new JTextField(8);
	    JTextField tfInterpret = new JTextField(8);
	    JTextField tfAlbum = new JTextField(8);
	    JButton btnPfad = new JButton("...");
	      
	    String[] strGenre = {
	    		 "Auswählen...",
	 	         "Pop",
	 	         "Rock",
	 	         "Dance/Electronic",
	 	         "HipHop",
	 	         "Black Music",
	 	         "Alternative",
	 	         "Metal",
	 	         "Klassik",
	 	         "Volksmusik",
	 	         "Schlager",
	 	         "Comedy",
	 	         "Jazz",
	 	         "Sonstiges"
	 	};
	      
	      JComboBox cGenreListe = new JComboBox(strGenre);
	      pNeuerTitel.add(new JLabel("Titel:"));
	      pNeuerTitel.add(tfTitel);
	      pNeuerTitel.add(new JLabel("Interpret:"));
	      pNeuerTitel.add(tfInterpret);
	      pNeuerTitel.add(new JLabel("Album:"));
	      pNeuerTitel.add(tfAlbum);
	      pNeuerTitel.add(new JLabel("Genre:"));
	      pNeuerTitel.add(cGenreListe);
	      pNeuerTitel.add(new JLabel("Pfad:"));
	      pNeuerTitel.add(btnPfad);
	      
	      btnPfad.addActionListener(e->getPfad());
	      
	      int result = JOptionPane.showConfirmDialog(null, pNeuerTitel, "Neuen Titel hinzufügen:", JOptionPane.OK_CANCEL_OPTION);
	      
	      if (result == JOptionPane.OK_OPTION) {
	    	  musikdaten.musikSpeichern(tfTitel.getText(), tfInterpret.getText(), tfAlbum.getText(), cGenreListe.getSelectedItem(), path);
	      }
	      
	      data = playlist.lesen("alleLieder");
	      dtmAlleTitel.setDataVector(data, columnNames);
	      dtmAlleTitel.fireTableDataChanged();
	}
	
	/**
	 * öffnet einen Dialog zur Auswahl einer Datei
	 */
	private void getPfad() {
		
		JButton open = new JButton();
  	  	JFileChooser fc = new JFileChooser();
  	  	FileNameExtensionFilter filter = new FileNameExtensionFilter(".wav", "wav");
  	  	
  	  	fc.setFileFilter(filter);
  	  	fc.setAcceptAllFileFilterUsed(false);
  	  	fc.setDialogTitle("Pfad des Musikstückes:");
  	  	
  	  	if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
  	  	  path = fc.getSelectedFile().getAbsolutePath();
  	  	  path = path.replace("\\", "/");
  	  	}
	}
	
	/**
	 * öffnet einen Dialog, bei dessen Bestätigung 
	 * die Funktion zum Löschen des Titels aufgerufen wird
	 * 
	 * @throws IOException
	 */
	private void delTitel() throws IOException {
		
		int row = tblAlleTitel.getSelectedRow();
		String datenTitel = tblAlleTitel.getModel().getValueAt(row, 0).toString();
		
		for (int column = 1; column<tblAlleTitel.getColumnCount(); column++) {
			datenTitel = datenTitel + "," + tblAlleTitel.getModel().getValueAt(row, column).toString();
		}
		
		String titel = tblAlleTitel.getModel().getValueAt(row, 0).toString();
		String kuenstler = tblAlleTitel.getModel().getValueAt(row, 1).toString();
		
		int result = JOptionPane.showConfirmDialog(new JPanel(), titel + " von " + kuenstler, "Titel löschen?", JOptionPane.YES_NO_OPTION);
		
		if(result == 0) {
			musikdaten.musikLoeschen(datenTitel);
			JOptionPane.showMessageDialog(new JPanel(), titel + " von " + kuenstler + " gelöscht.", "Titel gelöscht", JOptionPane.PLAIN_MESSAGE);
		}
		
		//aktualisiert die Tabellen
		data = playlist.lesen(cPlaylist.getSelectedItem().toString());
		dtmPlaylist.setDataVector(data, columnNames);
		dtmPlaylist.fireTableDataChanged();
		
		data = playlist.lesen("alleLieder");
		dtmAlleTitel.setDataVector(data, columnNames);
		dtmAlleTitel.fireTableDataChanged();
	}
	
	/**
	 * öffnet den Benutzermodus
	 */
	public void bModusAuf() {
		pBenutzermod.setVisible(true);
		pVerwaltungsmod.setVisible(false);
	}
	
	/**
	 * öffnet den Verwaltungsmodus
	 */
	public void vModusAuf() {
		pBenutzermod.setVisible(false);
		pVerwaltungsmod.setVisible(true);
	}
	
	/**
	 * spielt das ausgewählte Lied ab oder pausiert
	 */
	public void abspielen() {
		
		//wenn kein Lied abgespielt wird
		if(play == false) {
			
            if(tblPlaylist.getSelectedRow() == -1) {
            	selectedRow = 0;
            	tblPlaylist.setRowSelectionInterval(selectedRow, selectedRow);
            }
            else {
                selectedRow = tblPlaylist.getSelectedRow();
            }
               
            if(player.musikAbspielen(tblPlaylist.getValueAt(selectedRow, 5).toString()) == 1) {
            	
            	play = true;
    			pause = false;
            	
            	lblAktTitel.setText((String) tblPlaylist.getValueAt(selectedRow, 0) + " - " + (String) tblPlaylist.getValueAt(selectedRow, 1)); 
            	
                timer.start();
                
                try {
    				Image img = ImageIO.read(new FileInputStream("images/pause.png"));
    				btnPlay.setIcon(new ImageIcon(img));
    				btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
    			} catch (Exception ex) {
    				System.out.println(ex);
    			}
            }
            
		}
		else {
			//wenn Lied nicht pausiert wird
			if(pause == false) {
				
				pause = true;
				player.musikStoppen();
				timer.stop();
				
				try {
					Image img = ImageIO.read(new FileInputStream("images/play.png"));
					btnPlay.setIcon(new ImageIcon(img));
					btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
				} catch (Exception ex) {
				    System.out.println(ex);
				}
			}
			else {
				
				pause = false;
				
				player.musikWeiterspielen();
				timer.start();
				
				try {
					Image img = ImageIO.read(new FileInputStream("images/pause.png"));
					btnPlay.setIcon(new ImageIcon(img));
					btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
				} catch (Exception ex) {
				    System.out.println(ex);
				}
			}
				
		}
		
	}

	/**
	 * stoppt das aktuelle Lied, hält die Progressbar an und setzt diese zurück
	 */
	public void stoppen() {
		
		if(play == true) {
			
			play = false;
			
			player.musikStoppen();
			
			try {
				Image img = ImageIO.read(new FileInputStream("images/play.png"));
				btnPlay.setIcon(new ImageIcon(img));
				btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
			} catch (Exception ex) {
			    System.out.println(ex);
			}
		}
		
		//Timer anhalten und Progressbar zurücksetzen
		timer.stop();
		progress = 0;
		progBar.setValue(progress);
		progBar.setString("00:00 / 00:00");
	}
	
	/**
	 * stoppt das aktuelle Lied und spielt das vorherige Lied ab 
	 */
	public void previousTitel() {
		stoppen();
		selectedRow--;
		
		if(selectedRow < 0) {
			selectedRow = 0;
			tblPlaylist.setRowSelectionInterval(selectedRow, selectedRow);
		}
		else {
			tblPlaylist.setRowSelectionInterval(selectedRow, selectedRow);
		}
		
		tblPlaylist.setRowSelectionInterval(selectedRow, selectedRow);
		abspielen();
	}
	
	/**
	 * stoppt das aktuelle Lied und spielt das darauf folgende Lied ab 
	 */
	public void nextTitel() {
		stoppen();
		
		selectedRow++;
		
		if(selectedRow < tblPlaylist.getRowCount()) {
			tblPlaylist.setRowSelectionInterval(selectedRow, selectedRow);
		}
		else {
			selectedRow = 0;
			tblPlaylist.setRowSelectionInterval(selectedRow, selectedRow);
		}
		
		abspielen();
	}
	
	/**
	 * stoppt das aktuelle Lied und aktualisiert die Tabelle beim Wechsel der Playlist
	 */
	public void playlistWechseln() {
		
		int selectedPlaylist = cPlaylist.getSelectedIndex();
		
		stoppen();
		
		cPlaylist.setSelectedIndex(selectedPlaylist);
		
		data = playlist.lesen(cPlaylist.getSelectedItem().toString());
		dtmPlaylist.setDataVector(data, columnNames);
		dtmPlaylist.fireTableDataChanged();
	}
	
	/**
	 * ruft die Funktion zum Anlegen einer Playlist auf und
	 * fügt deren Namen der Combobox hinzu
	 * 
	 * @return 1 wenn Playlist angelegt wurde, ansonsten 0
	 */
	public int cplaylistadd() {
		if(playlist.speichernLeer() == 1) {
			try {
				cPlaylist.addItem(playlist.getnew());
				cPlaylist.setSelectedItem(playlist.getnew());
				return 1;
			}
			catch(NullPointerException ex) {
				cPlaylist.setSelectedItem("alleLieder");
				return 0;
			}
		}
		else {
			return 0;
		}
	}
	
	/**
	 * öffnet einen Dialog beim Erstellen einer neuen Playlist
	 * Auswahl zwischen leerer oder vordefinierter Playlist
	 */
	public void playlistSpeichern() {
		
		JPanel pNeuePlaylist = new JPanel();	
		JLabel lTitel = new JLabel("Leere Playlist oder vordefiniert?");
		String[] strAuswahl = {"Leer", "Interpret", "Album", "Genre", "Datum"};
		JComboBox cAuswahl = new JComboBox(strAuswahl);
		
		pNeuePlaylist.add(lTitel);
		pNeuePlaylist.add(cAuswahl);
		
		int result = JOptionPane.showConfirmDialog(null, pNeuePlaylist, "Neue Playlist erstellen:", JOptionPane.OK_CANCEL_OPTION);
		
		//Auswahl zwischen leerer oder vordefinierter Playlist 
		if(result == JOptionPane.OK_OPTION) {
			switch ((String)cAuswahl.getSelectedItem()) {
			
				case "Leer":	cplaylistadd();
								break;
								
				default:		playlistSpeichernKrit((String)cAuswahl.getSelectedItem());
								break;
			}			
		}	
	}
	
	/**
	 * speichert eine Playlist nach ausgewähltem Kriterium
	 * 
	 * @param krit
	 */
	public void playlistSpeichernKrit(String krit) {
		
		int nummer = 0;
		
		//Auswahl des Kriteriums
		switch (krit) {
			case "Interpret":	nummer = 1;
								break;
								
			case "Album":		nummer = 2;
								break;
								
			case "Genre": 		nummer = 3;
								break;
								
			case "Datum":		nummer = 4;
								break;
		}
		
		//wenn eine leere Playlist erstellt wurde
		if(cplaylistadd() == 1) {
			
			JPanel pAddToPlaylist = new JPanel();
		    
			data = playlist.lesen("alleLieder");
			
			ArrayList<String> listKrit = new ArrayList<String>();
			boolean inListe = false;
			
			//erstes Element anfügen
			listKrit.add(data[0][nummer]);
			
			//weitere Elemente anfügen, aber nur wenn noch nicht in Liste vorhanden
			for(int i = 0; i < data.length; i++) {
				for(int j = 0; j < listKrit.size(); j++) {
					if(listKrit.get(j) != null) {
						if(listKrit.get(j).equals(data[i][nummer])) {
							inListe = true;
						}
					}
				}
				
				if(inListe == false) {
					listKrit.add(data[i][nummer]);
				}
				else {
					inListe = false;
				}
			}
	      
			int nr = 0;
			JComboBox cKritListe = new JComboBox(listKrit.toArray(new String[0]));
			
			pAddToPlaylist.add(cKritListe);
			
			int result = JOptionPane.showConfirmDialog (null, pAddToPlaylist, "Auswahl des " + krit + "s :", JOptionPane.OK_CANCEL_OPTION);
			
			//Titel werden hinzugefügt
			if(result == JOptionPane.OK_OPTION) {
				for(int i = 0; i < data.length; i++) {
					if(data[i][nummer].equals((String)cKritListe.getSelectedItem())) {  	 
						try {
							MusikPlaylist.addToPlaylist((String) cPlaylist.getSelectedItem(), (String) cKritListe.getSelectedItem(), i);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}	
			}
			
			//aktualisiert die Tabelle
			data = playlist.lesen(cPlaylist.getSelectedItem().toString());
			dtmPlaylist.setDataVector(data, columnNames);
			dtmPlaylist.fireTableDataChanged();
		}
		
	}
	
	/**
	 * löscht eine Playlist
	 */
	public void playlistLoeschen() {
		
		if(cPlaylist.getSelectedItem().equals("alleLieder")) {
			JOptionPane.showMessageDialog(null, "Kann nicht gelöscht werden", "", JOptionPane.WARNING_MESSAGE);
		}
		else {
			playlist.loeschen((String) cPlaylist.getSelectedItem());

			cPlaylist.removeItem(cPlaylist.getSelectedItem());
			cPlaylist.setSelectedItem("alleLieder");
		}
	}
	
	/**
	 * fügt einen neuen Titel zur ausgewählten Playlist hinzu
	 */
	public void addToPlaylist() {
		
		if(cPlaylist.getSelectedItem().equals("alleLieder")) {
			JOptionPane.showMessageDialog(null, "Playlist enthält bereits alle Titel.", "", JOptionPane.WARNING_MESSAGE);
		}
		else {
			JPanel pAddToPlaylist = new JPanel();

			data = playlist.lesen("alleLieder");
			String[] strTitel = new String[data.length];

			for(int i = 0; i < data.length; i++) {
				strTitel[i] = data[i][0];
			}
	      
			int nummer = 0;
			JComboBox cTitelListe = new JComboBox(strTitel);
			
			pAddToPlaylist.add(cTitelListe);
			
			int result = JOptionPane.showConfirmDialog(null, pAddToPlaylist, "Titel zur Playlist hinzufügen:", JOptionPane.OK_CANCEL_OPTION);
			
			//Titel hinzufügen
			if (result == JOptionPane.OK_OPTION) {
				for(int i = 0; i < data.length; i++) {
					if(data[i][0].equals((String)cTitelListe.getSelectedItem())) {
						nummer = i;
					}
				}
	    	 
				try {
					MusikPlaylist.addToPlaylist((String) cPlaylist.getSelectedItem(), (String) cTitelListe.getSelectedItem(), nummer);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
			int selectedPlaylist = cPlaylist.getSelectedIndex();

			cPlaylist.setSelectedIndex(selectedPlaylist);
			
			//aktualisiert die Tabelle
			data = playlist.lesen(cPlaylist.getSelectedItem().toString());
			dtmPlaylist.setDataVector(data, columnNames);
			dtmPlaylist.fireTableDataChanged();
		}
	}
	
	/**
	 * formatiert die Zeit
	 * 
	 * @param time
	 * @return Zeit
	 */
	public String progTime(int time) {
		
		int progTimeSec, progTimeMin = 0;
		String min, sec = "00";
		
		progTimeSec = time % 60;
		progTimeMin = time / 60;
		
		if(progTimeSec < 10) {
			sec = ("0" + progTimeSec);
		}
		else {
			sec = Integer.toString(progTimeSec);
		}
		
		if(progTimeMin < 10) {
			min = ("0" + progTimeMin);
		}
		else {
			min = Integer.toString(progTimeMin);
		}
		
		return (min + ":" + sec);
	}
	
	/**
	 * öffnet die Anleitung
	 */
	public void openHelp() {
		
		if (Desktop.isDesktopSupported()) {
		    try {
		        File file = new File("anleitung.pdf");
		        
		        if(file.exists()) {
		        	Desktop.getDesktop().open(file);
		        }
		        else {
		        	JOptionPane.showMessageDialog(null, "Datei nicht gefunden", "", JOptionPane.WARNING_MESSAGE);
		        }
		        
		    } catch (IOException ex) {
		    	JOptionPane.showMessageDialog(null, "Datei konnte nicht geöffnet werden", "", JOptionPane.WARNING_MESSAGE);
		    }
		}
	}
}