package musikverwaltung;

import java.io.BufferedReader;
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
	
	public void musikAbspielen(long dauer) {
		
			new Thread(new Runnable() {
	            public void run() {
	                try {
	                	audioFilePath = "music/Leider Geil.wav";
	            		
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
	
	
	public void musikWeiterspielen() {
		
		new Thread(new Runnable() {
            public void run() {
                try {
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
                }
                catch (Exception ex) {
                	ex.printStackTrace();
                }
            }
        }).start();
	}
	
	
//	public void savePlaylist() throws IOException{
//	    
//		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//		String filename = "playlist.dat";
//		File f = new File(filename);
//
//		try{
//			ObjectOutput ObjOut = new ObjectOutputStream(new FileOutputStream(f));
//
//			ObjOut.writeObject("test");
//			ObjOut.close();
//
//			System.out.println("Serializing an Object Creation completed successfully.");
//	    }
//
//		catch(IOException e){
//			System.out.println(e.getMessage());
//	    }
//
//	}
}
