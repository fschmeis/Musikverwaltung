package musikverwaltung;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;


public class MusikPlayer {
	
	Clip audioClip;
	String audioFilePath;
	File audioFile;
	AudioInputStream audioStream;
	AudioFormat audioFormat;
	long duration;
	
	/**
	 * 
	 * @param pfad
	 * @return
	 */
	public int musikVorbereiten(String pfad) {
		try {
        	audioFilePath = pfad;
    		
    		audioFile = new File(audioFilePath);
    		 
            try {        	
                audioStream = AudioSystem.getAudioInputStream(audioFile);
     
                audioFormat = audioStream.getFormat();
     
                DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
     
                audioClip = (Clip) AudioSystem.getLine(info);
     
                audioClip.open(audioStream);
                
                duration = audioClip.getMicrosecondLength();
                duration = duration / 1_000_000;
                
                return 1;
                
            } catch (UnsupportedAudioFileException ex) {
                JOptionPane.showMessageDialog(null, "Audio Format wird nicht unterstützt.", "", JOptionPane.WARNING_MESSAGE);
                return 0;
            } catch (LineUnavailableException ex) {
                JOptionPane.showMessageDialog(null, "Audiospur ist nicht verfügbar.", "", JOptionPane.WARNING_MESSAGE);
                return 0;
            } catch (IOException ex) {
            	JOptionPane.showMessageDialog(null, "Fehler beim Abspielen oder Datei nicht vorhanden.", "", JOptionPane.WARNING_MESSAGE);
            	return 0;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Abspielfehler: " + ex.getMessage() + " für " + pfad, "", JOptionPane.WARNING_MESSAGE);
            return 0;
        }
	}
	
	/**
	 * 
	 * @param pfad
	 * @return
	 */
	public int musikAbspielen(String pfad) {
		
		if(musikVorbereiten(pfad) == 1) {
			
			new Thread(new Runnable() {
	            public void run() {
	                
	                audioClip.start();
	                
	                while (duration > 0) {   
	                    try {
	                        Thread.sleep(100000);
	                    } catch (InterruptedException ex) {
	                        ex.printStackTrace();
	                    }
	                         
	                    duration--;
	                }     
	                        
	                audioClip.close();
	            }
	        }).start();
			
			return 1;
		}
		else {
			return 0;
		}
			
	}
	
	/**
	 * 
	 */
	public void musikStoppen() {
		try {
			audioClip.stop();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 */
	public void musikWeiterspielen() {
		
		new Thread(new Runnable() {
            public void run() {
                try {
                	audioClip.start();
            
                	while (duration > 0) {
                
                		try {
                			Thread.sleep(100000);
                		} catch (InterruptedException ex) {
                			ex.printStackTrace();
                		}
                
                		duration--;
                	}
             
                	audioClip.close();
                } catch (Exception ex) {
                	ex.printStackTrace();
                }
            }
        }).start();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getduration() {
		
		try {
			return ((int) audioClip.getMicrosecondLength()) / 1_000_000;
		} catch(NullPointerException ex) {
			return 100;
		}
	}

	
}
