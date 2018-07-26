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

public class MusikPlayer {
	
	Clip audioClip;
	
	public void musikAbspielen() {
		
			new Thread(new Runnable() {
	            public void run() {
	                try {
	                	String audioFilePath = "music/test.wav";
	            		
	            		File audioFile = new File(audioFilePath);
	            		 
	                    try {        	
	                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
	             
	                        AudioFormat format = audioStream.getFormat();
	             
	                        DataLine.Info info = new DataLine.Info(Clip.class, format);
	             
	                        audioClip = (Clip) AudioSystem.getLine(info);
	             
	                        audioClip.open(audioStream);
	                        
	                        long duration = audioClip.getMicrosecondLength();
	                        duration = duration / 1_000_000;
	                        
	                        audioClip.start();
	                        
	                        while (duration > 0) {
	                            
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
	                } catch (Exception ex) {
	                    System.out.println("play sound error: " + ex.getMessage() + " for " + "filename");
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
}
