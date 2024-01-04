package CPTGAME;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.*;

class MenuPanel2 extends JPanel {
    private String backgroundImageFilePath;
    private Clip backgroundMusicClip;

    public MenuPanel2(String backgroundImageFilePath, Clip backgroundMusicClip) {
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

public class Credits {
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

        MenuPanel2 panel2 = new MenuPanel2(backgroundImageFilePath, loadBackgroundMusic(backgroundMusicFilePath));
        
        panel2.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        ArrayList<String> prints = new ArrayList<String>();
        prints.add("CREDITS");
        prints.add("CREATED BY:");
        prints.add("PETER ANGIOLELLA");
        prints.add("MATTHEW SACCONE");
        prints.add("KEVISH SURI");
        prints.add("BENJAMIN SPOONER");
        prints.add("ALL MUSIC PRODUCED BY PETER");
        prints.add("GRADE 12 COMP SCI ISU - ICS4U11");
        
        
        for (int i=0;i<8;i++) {
            gbc.insets = new Insets(-600 + (i*100), 0, 10, 0);
            JLabel titleLabel = new JLabel(prints.get(i));
            titleLabel.setForeground(Color.BLACK);
            titleLabel.setFont(new Font("Press Start 2P", Font.BOLD, 48));
            panel2.add(titleLabel, gbc);
        }

        gbc.gridy++;
        JButton button6 = new JButton("BACK");
        button6.setFont(new Font("Press Start 2P", Font.PLAIN, 24));
        button6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel2.stopBackgroundMusic();
                PlatformGameMenu.main(null);
                frame.dispose();
            }
        });
        gbc.insets = new Insets(0 + (100), 0, 10, 0);
        panel2.add(button6, gbc);

        frame.getContentPane().add(panel2);
        frame.setVisible(true);

        panel2.playBackgroundMusic();
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
