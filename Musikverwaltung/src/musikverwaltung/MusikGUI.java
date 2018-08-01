package musikverwaltung;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;

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
	JMenu dateimenu;
	JMenu modusmenu;
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
	
	String[] columnNames = {"Titel", "Interpret", "Album", "Genre", "Datum", "Pfad"};
	
	String[][] data = playlist.playlistLesen("alleLieder");
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
	
	JButton btnAddTitel = new JButton();
	
	DefaultTableModel dtmAlleTitel = new DefaultTableModel(data, columnNames) {
		@Override
	    public boolean isCellEditable(int row, int column) {
	       //alle Zellen sind nicht editierbar
	       return false;
	    }
	};
	
	JTable tblAlleTitel = new JTable(dtmAlleTitel);
	JScrollPane scpAlleTitel = new JScrollPane(tblAlleTitel);
	
	ActionListener progressor = new ActionListener () {
    	public void actionPerformed(ActionEvent evnt) {
    		progress++;
    		progBar.setValue(progress);
    		progBar.setMaximum(player.getduration());
    	}
    };
    Timer timer = new Timer(1000, progressor);
		
	//sonst. Variablen	
	boolean play = false;
	boolean pause = false;
	int selectedRow = 0;
	int progress = 0;
	
	//Konstruktor
	public MusikGUI() {
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.setTitle("Musikverwaltung");
		this.setSize(1060, 700);
		
		//Mitte des Bildschirms
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		//Vollbild
//		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		//Beenden bei Klick auf rotes Kreuz
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		//Programm-Icon
		this.setIconImage(new ImageIcon("images/speaker.png").getImage());
		
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
		
		//Horizontale Linien ausblenden
		tblPlaylist.setShowHorizontalLines(false);
		tblAlleTitel.setShowHorizontalLines(false);
		
		//Farbe der ausgewählten Reihe
		tblPlaylist.setSelectionBackground(Color.ORANGE);
		tblAlleTitel.setSelectionBackground(Color.ORANGE);
		
		//Mouse-Listener tblPlaylist
        tblPlaylist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
            	if(evt.getButton() == 1)
//                int row = tblPlaylist.rowAtPoint(evt.getPoint());
//                int col = tblPlaylist.columnAtPoint(evt.getPoint());
                
            		if (evt.getClickCount() == 1) {
            			stoppen();
            		}
            		else {
            			abspielen();
            		}
            }
        });
        
        tblAlleTitel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
            	if(evt.getButton() == 3)
					try {
						delTitel();
					} catch (IOException e) {
						e.printStackTrace();
					}            		
            }
            public void mouseReleased(MouseEvent evt)
            {
                if (evt.getButton() == 3){
                    int row = tblAlleTitel.rowAtPoint( evt.getPoint() );
                    int column = tblAlleTitel.columnAtPoint( evt.getPoint() );
         
                    if (! tblAlleTitel.isRowSelected(row))
                        tblAlleTitel.changeSelection(row, column, false, false);
                }
            }
        });
        
        tblPlaylist.setAutoCreateRowSorter(true);
		
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
		btnNewPlaylist.addActionListener(e->{stoppen(); playlist.playlistSpeichern(); cplaylistadd();});
		btnNewPlaylist.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
		
		pBenutzermod.add(btnAddToPlaylist);
		btnAddToPlaylist.setBounds(390, 100, 150, 30);
		btnAddToPlaylist.addActionListener(e->{stoppen();});
		btnAddToPlaylist.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
		
		pBenutzermod.add(btnDeletePlaylist);
		btnDeletePlaylist.setBounds(550, 100, 150, 30);
		btnDeletePlaylist.addActionListener(e->{playlistloeschen();});
		btnDeletePlaylist.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
				
		pBenutzermod.add(progBar);
		progBar.setBounds(20, 600, 1000, 30);
		progBar.setValue(progress);
		progBar.setBorderPainted(true);		
		
		pBenutzermod.add(lblBenutzermodus);
		lblBenutzermodus.setForeground(Color.WHITE);
		lblBenutzermodus.setBounds(20, 0, 400, 50);
		lblBenutzermodus.setFont(new Font("", Font.BOLD, 30));
		
		pBenutzermod.add(lblImage);
		lblImage.setForeground(Color.WHITE);
		lblImage.setBounds(420, 400, 128, 128);
		lblImage.setIcon(new ImageIcon("images/default.png"));
		
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
		btnAddTitel.setBounds(20, 390, 38, 38);
		btnAddTitel.addActionListener(e->neuerTitel());
		btnAddTitel.setCursor((Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));
		
		try {
			Image img = ImageIO.read(new FileInputStream("images/plus.png"));
			btnAddTitel.setIcon(new ImageIcon(img));
			btnAddTitel.setHorizontalTextPosition(SwingConstants.CENTER);
		} catch (Exception ex) {
		    System.out.println(ex);
		}
				
		pVerwaltungsmod.add(lblVerwaltungsmodus);
		lblVerwaltungsmodus.setForeground(Color.WHITE);
		lblVerwaltungsmodus.setBounds(20, 0, 400, 50);
		lblVerwaltungsmodus.setFont(new Font("", Font.BOLD, 30));
		
		tblAlleTitel.setAutoCreateRowSorter(true);
		
		pVerwaltungsmod.add(scpAlleTitel);
		scpAlleTitel.setBounds(20, 150, 1000, 200);
	}
	
	String path = new String();	//nicht optimal
	
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
	      
	      btnPfad.addActionListener(e->optPfad());
	      
	      int result = JOptionPane.showConfirmDialog(null, pNeuerTitel, "Neuen Titel hinzufügen:", JOptionPane.OK_CANCEL_OPTION);
	      if (result == JOptionPane.OK_OPTION) {
	    	  musikdaten.MusikSpeichern(tfTitel.getText(), tfInterpret.getText(), tfAlbum.getText(), cGenreListe.getSelectedItem(), path);
	      }
	      
	      data = playlist.playlistLesen("alleLieder");
			
	      dtmAlleTitel.setDataVector(data, columnNames);
	      dtmAlleTitel.fireTableDataChanged();
}
	
	private void optPfad() {
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
	
	private void delTitel() throws IOException {
		int row = tblAlleTitel.getSelectedRow();
		String datenTitel = tblAlleTitel.getModel().getValueAt(row, 0).toString();
		
		for (int column = 1; column<tblAlleTitel.getColumnCount(); column++) {
			datenTitel = datenTitel + "," + tblAlleTitel.getModel().getValueAt(row, column).toString();
		}
		System.out.println("- DELETED - " + datenTitel);
		musikdaten.MusikLoeschen(datenTitel);
		String titel = tblAlleTitel.getModel().getValueAt(row, 0).toString();
		String kuenstler = tblAlleTitel.getModel().getValueAt(row, 1).toString();
		JOptionPane.showMessageDialog(new JPanel(), titel + " von " + kuenstler + " gelöscht.", "Titel gelöscht",
		        JOptionPane.PLAIN_MESSAGE);
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
				Image img = ImageIO.read(new FileInputStream("images/pause.png"));
				btnPlay.setIcon(new ImageIcon(img));
				btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
			} catch (Exception ex) {
				System.out.println(ex);
			}
				
			play = true;
			pause = false;
				
            if(tblPlaylist.getSelectedRow() == -1) {
            	selectedRow = 0;
            	tblPlaylist.setRowSelectionInterval(selectedRow, selectedRow);
            }
            else {
                selectedRow = tblPlaylist.getSelectedRow();
            }
            
                 
            player.musikAbspielen(tblPlaylist.getValueAt(selectedRow, 5).toString());
            lblAktTitel.setText((String) tblPlaylist.getValueAt(selectedRow, 0) + " - " + (String) tblPlaylist.getValueAt(selectedRow, 1)); 
            
            timer.start();
		}
		else {
			if(pause == false) {
				try {
					Image img = ImageIO.read(new FileInputStream("images/play.png"));
					btnPlay.setIcon(new ImageIcon(img));
					btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
				} catch (Exception ex) {
				    System.out.println(ex);
				}
				
				pause = true;
				player.musikStoppen();
				timer.stop();
			}
			else {
				try {
					Image img = ImageIO.read(new FileInputStream("images/pause.png"));
					btnPlay.setIcon(new ImageIcon(img));
					btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
				} catch (Exception ex) {
				    System.out.println(ex);
				}
				
				pause = false;
				
				player.musikWeiterspielen();
				timer.start();
			}
				
		}
		
	}


	public void stoppen() {
		
		if(play == true) {
			try {
				Image img = ImageIO.read(new FileInputStream("images/play.png"));
				btnPlay.setIcon(new ImageIcon(img));
				btnPlay.setHorizontalTextPosition(SwingConstants.CENTER);
			} catch (Exception ex) {
			    System.out.println(ex);
			}
			
			play = false;
			
			player.musikStoppen();
		}
		
//		cPlaylist.setModel(new DefaultComboBoxModel<String>(playlist.allePlaylists()));
//		
//		data = playlist.playlistLesen(cPlaylist.getSelectedItem().toString());
//		dtmPlaylist.setDataVector(data, columnNames);
//		dtmPlaylist.fireTableDataChanged();
		
		timer.stop();
		progress = 0;
		progBar.setValue(0);
	}
	
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
	
	public void playlistWechseln() {
		
		int selectedPlaylist = cPlaylist.getSelectedIndex();
		
		stoppen();
		
		cPlaylist.setSelectedIndex(selectedPlaylist);
		
		data = playlist.playlistLesen(cPlaylist.getSelectedItem().toString());
		
		dtmPlaylist.setDataVector(data, columnNames);
		dtmPlaylist.fireTableDataChanged();
	}	
	
	public void cplaylistadd() {
		cPlaylist.addItem(playlist.getnew());
	}
	
	public void playlistloeschen(){
		if(cPlaylist.getSelectedItem().equals("alleLieder")) {
			JOptionPane.showMessageDialog(null, "Kann nicht gelöscht werden", "", JOptionPane.WARNING_MESSAGE);
		} else {
			playlist.playlistLoeschen((String) cPlaylist.getSelectedItem());
			cPlaylist.removeItem(cPlaylist.getSelectedItem());
	
		}
	}
}