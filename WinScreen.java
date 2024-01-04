package CPTGAME;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.*;

class MenuPanel7 extends JPanel {
    
    private static Clip musicClip;
    private String backgroundImageFilePath;
    private Clip backgroundMusicClip;

    public MenuPanel7(String backgroundImageFilePath, Clip backgroundMusicClip) {
        this.backgroundImageFilePath = backgroundImageFilePath;
        this.backgroundMusicClip = backgroundMusicClip;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image backgroundImage = new ImageIcon(backgroundImageFilePath).getImage();
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    public void playBackgroundMusic() {
        if (backgroundMusicClip != null) {
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusicClip != null) {
            backgroundMusicClip.stop();
        }
    }
}

public class WinScreen {
    private static String backgroundImageFilePath = "SPRITES\\victoryimg.jpg";
    private static String backgroundMusicFilePath = "SPRITES\\victory.wav";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        
        JFrame frame = new JFrame("Castle Conquest");
        frame.setSize(1720, 920);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the frame

        MenuPanel7 panel = new MenuPanel7(backgroundImageFilePath, loadBackgroundMusic(backgroundMusicFilePath));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.insets = new Insets(130, 0, 10, 0);

        JLabel titleLabel = new JLabel("VICTORY!");
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Press Start 2P", Font.BOLD, 48));
        panel.add(titleLabel, gbc);
        
        gbc.gridy++;
        JButton button1 = new JButton("LEVEL SELECT");
        button1.setFont(new Font("Press Start 2P", Font.PLAIN, 24));
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.stopBackgroundMusic();
                LevelMenu.main(null);
                frame.dispose();
            }
        });
        panel.add(button1, gbc);

        gbc.gridy++;
        JButton button3 = new JButton("BACK TO MAIN MENU");
        button3.setFont(new Font("Press Start 2P", Font.PLAIN, 24));
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.stopBackgroundMusic();
                PlatformGameMenu.main(null);
                frame.dispose();
            }
        });
        panel.add(button3, gbc);

        gbc.gridy++;
        JButton button4 = new JButton("EXIT");
        button4.setFont(new Font("Press Start 2P", Font.PLAIN, 24));
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.stopBackgroundMusic();
                SplashScreen.main(null);
                frame.dispose();
            }
        });
        panel.add(button4, gbc);

        frame.getContentPane().add(panel);
        frame.setVisible(true);

        panel.playBackgroundMusic();
    }

    private static Clip loadBackgroundMusic(String musicFilePath) {
        try {
            File musicFile = new File(musicFilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    

}
