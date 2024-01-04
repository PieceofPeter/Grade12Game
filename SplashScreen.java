package CPTGAME;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SplashScreen extends PlatformGameMenu {
    private static Clip musicClip;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Game Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1720, 920);
        frame.setLocationRelativeTo(null);

        Image backgroundImage = new ImageIcon("SPRITES\\SPLASHSCREEN.png").getImage();

        // Create a JPanel to display the splash screen
        JPanel splashPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, 1720, 890, this);
            }
        };

        // Add the splash panel to the frame's content pane
        frame.getContentPane().add(splashPanel);
        frame.setVisible(true);

        // Load and play the music
        playMusic("SPRITES\\splashscreenmusic.wav");

        // Add a key listener to the frame
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    stopMusic(); // Stop the music
                    new PlatformGameMenu().main(null); // Launch the main class from the PlatformGameMenu file
                    frame.dispose(); // Close the splash screen frame
                }
            }
        });
    }

    private static void playMusic(String filePath) {
        try {
            File musicFile = new File(filePath);
            if (musicFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
                musicClip = AudioSystem.getClip();
                musicClip.open(audioStream);

                // Start playing the music
                musicClip.start();
            } else {
                System.err.println("Music file not found: " + filePath);
            }
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.err.println("Error playing music: " + e.getMessage());
        }
    }

    private static void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
        }
    }
}
