//package Digga;
//
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.File;
//import java.io.IOException;
//
//import javax.sound.sampled.AudioFormat;
//import javax.sound.sampled.AudioInputStream;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.DataLine;
//import javax.sound.sampled.LineUnavailableException;
//import javax.sound.sampled.SourceDataLine;
//import javax.sound.sampled.UnsupportedAudioFileException;
//import javax.swing.JButton;
//import javax.swing.JFrame;
// 
//public class Sound extends Thread{
// 
//    private static final int BUFFER_SIZE = 4096;
//    static String audioFilePath = "enemy.wav";
//    
//    public Sound(){
//    	/*JFrame frame = new JFrame();
//		frame.setSize(200, 200);
//		JButton button = new JButton("Music On");
//		frame.add(button);
//		button.addActionListener(new SoundOn());
//    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);*/
//    }
//    
//  /*  public static class SoundOn implements ActionListener{
//		public final void actionPerformed(ActionEvent e){
//			play(audioFilePath);
//		}
//	}*/
//
//    static void play(String audioFilePath) {
//        File audioFile = new File(audioFilePath);
//        try {
//            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
// 
//            AudioFormat format = audioStream.getFormat();
// 
//            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
// 
//            SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);
// 
//            audioLine.open(format);
// 
//            audioLine.start();
//             
//            byte[] bytesBuffer = new byte[BUFFER_SIZE];
//            int bytesRead = -1;
// 
//            while ((bytesRead = audioStream.read(bytesBuffer)) != -1) {
//                audioLine.write(bytesBuffer, 0, bytesRead);
//            }
//             
//            audioLine.drain();
//            audioLine.close();
//            audioStream.close();
//             
//            System.out.println("Playback completed.");
//             
//        } catch (UnsupportedAudioFileException ex) {
//            System.out.println("The specified audio file is not supported.");
//            ex.printStackTrace();
//        } catch (LineUnavailableException ex) {
//            System.out.println("Audio line for playing back is unavailable.");
//            ex.printStackTrace();
//        } catch (IOException ex) {
//            System.out.println("Error playing the audio file.");
//            ex.printStackTrace();
//        }      
//    }
//     
//    public void run()
//    {
//    	Sound.play(audioFilePath);
//    }
// 
//}