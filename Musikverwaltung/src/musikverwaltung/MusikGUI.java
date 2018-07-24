package musikverwaltung;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MusikGUI extends JFrame {
	
	//Komponenten
	Container cp;			//contentPane
	JPanel p1;
	
	JPanel p2;
	JButton btnPlay = new JButton("Play");
	
	//Menüleiste
	JMenuBar bar;
	JMenu dateimenu;
	JMenuItem beendenitem;
	
	//Konstruktor
	public MusikGUI() {
		this.setTitle("Musikverwaltung");
		this.setLocation(300, 400);
		this.setSize(600, 300);
		
		//Menü
		bar = new JMenuBar();
		dateimenu = new JMenu("Datei");
		beendenitem = new JMenuItem("Beenden");
		beendenitem.addActionListener(e->{System.exit(0);});
		
		dateimenu.add(beendenitem);
		bar.add(dateimenu);
		this.setJMenuBar(bar);
		
		//contentPane
		cp = this.getContentPane();
		cp.setLayout(new GridLayout(2,1));
		p1 = new JPanel();
		p1.setLayout(new BorderLayout());
		cp.add(p1);
		
		p2 = new JPanel();
		p2.setLayout(null);
		p2.setBackground(Color.ORANGE);
		
		p2.add(btnPlay);
		btnPlay.setBounds(350, 50, 150, 20);
		
		cp.add(p2);
		
		btnPlay.addActionListener(e->musikAbspielen());
	}
	
	public void musikAbspielen() {
		
		String audioFilePath = "test.wav";
		
		File audioFile = new File(audioFilePath);
		 
        try {        	
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
 
            AudioFormat format = audioStream.getFormat();
 
            DataLine.Info info = new DataLine.Info(Clip.class, format);
 
            Clip audioClip = (Clip) AudioSystem.getLine(info);
 
            audioClip.open(audioStream);
            
            long duration = audioClip.getMicrosecondLength();
            duration = duration / 1_000_000;
             
            audioClip.start();
            
            while (duration > 0) {
                // wait for the playback completes
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                
                duration--;
            }
             
            audioClip.close();
             
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
	}
}
