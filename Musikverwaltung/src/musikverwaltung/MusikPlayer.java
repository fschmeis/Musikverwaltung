package musikverwaltung;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class MusikPlayer {
	
	Clip audioClip;
	String audioFilePath;
	File audioFile;
	AudioInputStream audioStream;
	AudioFormat audioFormat;
	long duration;
	
	public void musikAbspielen(String pfad) {
		
			new Thread(new Runnable() {
	            public void run() {
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
	                        
	                    } catch (UnsupportedAudioFileException ex) {
	                        System.out.println("Audio Format wird nicht unterstützt.");
	                        ex.printStackTrace();
	                    } catch (LineUnavailableException ex) {
	                        System.out.println("Audiospur ist nicht verfügbar.");
	                        ex.printStackTrace();
	                    } catch (IOException ex) {
	                        System.out.println("Fehler beim Abspielen.");
	                        ex.printStackTrace();
	                    }
	                } catch (Exception ex) {
	                    System.out.println("Abspielfehler: " + ex.getMessage() + " für " + pfad);
	                }
	            }
	        }).start();
	}
	
	
	public void musikStoppen() {
		try {
			audioClip.stop();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	
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
                }
                catch (Exception ex) {
                	ex.printStackTrace();
                }
            }
        }).start();
	}

	public int getduration() {
		try {
			return ((int) audioClip.getMicrosecondLength()) / 1_000_000;
		} catch(NullPointerException er) {
			return 100;
		}
	}

	
}
