package CPTGAME;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.*;

class MenuPanel extends JPanel {
    private String backgroundImageFilePath;
    private Clip backgroundMusicClip;

    public MenuPanel(String backgroundImageFilePath, Clip backgroundMusicClip) {
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

public class GameWinScreen {
    private static String backgroundImageFilePath = "SPRITES\\menuback.png";
    private static String backgroundMusicFilePath = "SPRITES\\mainmenumusik.wav";

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

        MenuPanel panel = new MenuPanel(backgroundImageFilePath, loadBackgroundMusic(backgroundMusicFilePath));
        
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        ArrayList<String> prints = new ArrayList<String>();
        prints.add("CONGRATS! YOU WIN!");
        
        for (int i=0;i<1;i++) {
            gbc.insets = new Insets(10, 0, 10, 0);
            JLabel titleLabel = new JLabel(prints.get(i));
            titleLabel.setForeground(Color.BLACK);
            titleLabel.setFont(new Font("Press Start 2P", Font.BOLD, 48));
            panel.add(titleLabel, gbc);
        }

        gbc.gridy++;
        JButton button6 = new JButton("BACK TO MAIN MENU");
        button6.setFont(new Font("Press Start 2P", Font.PLAIN, 24));
        button6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.stopBackgroundMusic();
                PlatformGameMenu.main(null);
                frame.dispose();
            }
        });
        gbc.insets = new Insets(0 + (100), 0, 10, 0);
        panel.add(button6, gbc);

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
