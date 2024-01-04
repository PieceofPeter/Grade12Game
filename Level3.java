package CPTGAME;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import javax.sound.sampled.*;

public class Level3 extends JPanel implements ActionListener, KeyListener {

    private Image spikes;
    private Image platformImage1, lava;
    private Image platformImage2;
    private Image platformImage3;
    private Image[] playerRightImages;
    private Image[] backgroundImages;
    private Image[] playerLeftImages;
    private Image[] playerJumpImages;
    private Image[] playerJumpLeftImages;
    private Image[] playerIdleImages;
    private Image[] playerIdleLeftImages;
    private Timer timer;
    private int playerX, playerY, dy, jumpCount;
    private boolean moveLeft, moveRight, isJumping, canJump;
    private int frameIndex;
    private Rectangle hitBox;
    private Clip musicClip;
    private Clip moveSoundClip;
    private Clip jumpSoundClip;
    private float musicVolume = 0.18f; // Default music volume
    private float soundVolume = 1.0f;
    private float JsoundVolume = 0.75f;// Default sound effect volume
    private boolean isMuted = false; // Default mute status
    private Image endFlagImage;
    private boolean levelEnded = false;
    private int health;
    private int lastMovementDirection;
    private Rectangle platformRect1;
    private Rectangle platformRect2;
    private boolean isOnGround;
    private int xspeed, yspeed;
    private boolean collision;
    private Rectangle greenFlagRect;
    private int minutes, seconds;
    private int scrollCount, scrollSpeed;
    private int currentBackgroundIndex;
    private boolean isScrolling;
    private long startTime;


    public Level3() {
        // Load images

        spikes = new ImageIcon("SPRITES\\spikes.png").getImage(); // Load end flag image
        endFlagImage = new ImageIcon("SPRITES\\FINISH1.png").getImage(); // Load end flag image
        lava = new ImageIcon("SPRITES\\LAVA.png").getImage(); // Load end flag image

        playerRightImages = new Image[10];
        playerLeftImages = new Image[10];
        playerJumpImages = new Image[10];
        playerJumpLeftImages = new Image[10];
        playerIdleImages = new Image[10];
        playerIdleLeftImages = new Image[10];
        backgroundImages = new Image[3];

        
        for (int i = 0; i < 3; i++) {
            backgroundImages[i] = new ImageIcon("SPRITES\\background3" + i + ".png").getImage();
        }
        
        for (int i = 0; i < 10; i++) {
            playerRightImages[i] = new ImageIcon("SPRITES\\right" + i + ".png").getImage();
        }

        for (int i = 0; i < 10; i++) {
            playerLeftImages[i] = new ImageIcon("SPRITES\\left" + i + ".png").getImage();
        }

        for (int i = 0; i < 10; i++) {
            playerJumpImages[i] = new ImageIcon("SPRITES\\jump" + i + ".png").getImage();
        }

        for (int i = 0; i < 10; i++) {
            playerIdleImages[i] = new ImageIcon("SPRITES\\idle" + i + ".png").getImage();
        }
        
        for (int i = 0; i < 10; i++) {
            playerJumpLeftImages[i] = new ImageIcon("SPRITES\\jumpl" + i + ".png").getImage();
        }
        
        for (int i = 0; i < 10; i++) {
            playerIdleLeftImages[i] = new ImageIcon("SPRITES\\idlel" + i + ".png").getImage();
        }
        
        platformImage1 = new ImageIcon("SPRITES\\platform1.png").getImage();
        
        int delay = 1000 / 60;
        timer = new Timer(delay, this);
        timer.start();
        playerX = 30;
        playerY = 335;
        hitBox = new Rectangle(playerX+30, playerY+10, 75, 130);
        jumpCount = 0;
        moveLeft = false;
        moveRight = false;
        isJumping = false;
        canJump = true;
        startTime = System.currentTimeMillis();
        isOnGround = true;
        xspeed = 8;
        yspeed = 8;
        frameIndex = 0;
        setFocusable(true);
        setPreferredSize(new Dimension(1720, 920));
        addKeyListener(this);
        scrollCount = -1;
        scrollSpeed = 23;
        isScrolling = false;
        currentBackgroundIndex = 0;
        health = 1;
        
        // Load music
        try {
            File musicFile = new File("SPRITES\\levelthree.wav");
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioInput);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            setMusicVolume(musicVolume); // Apply the initial volume
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }

        // Load move sound effect
        try {
            File moveSoundFile = new File("SPRITES\\move.wav");
            AudioInputStream moveSoundInput = AudioSystem.getAudioInputStream(moveSoundFile);
            moveSoundClip = AudioSystem.getClip();
            moveSoundClip.open(moveSoundInput);
            setSoundVolume(soundVolume); // Apply the initial volume
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }

        // Load jump sound effect
        try {
            File jumpSoundFile = new File("SPRITES\\jump.wav");
            AudioInputStream jumpSoundInput = AudioSystem.getAudioInputStream(jumpSoundFile);
            jumpSoundClip = AudioSystem.getClip();
            jumpSoundClip.open(jumpSoundInput);
            setSoundVolume(JsoundVolume); // Apply the initial volume
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }   
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Calculate the current background index based on the scroll count int 
        currentBackgroundIndex = (scrollCount / getWidth()) % backgroundImages.length; 

        // Calculate the x-coordinate for the current background 
        int bgX = -scrollCount % getWidth(); 

        // Draw the current background 
        g.drawImage(backgroundImages[currentBackgroundIndex], bgX, 0, getWidth(), getHeight(), this); 

        // Determine the next background index 
        int nextBackgroundIndex = (currentBackgroundIndex + 1) % backgroundImages.length; 

        // Calculate the x-coordinate for the next background 
        int nextBgX = bgX + getWidth(); 

        // Draw the next background (to achieve seamless scrolling) 
        g.drawImage(backgroundImages[nextBackgroundIndex], nextBgX, 0, getWidth(), getHeight(), this);

        // Draw player character
        Image playerImage;
        
        if (moveLeft) {
            lastMovementDirection = 1;
            playerImage = playerLeftImages[frameIndex];
            
        } 
        
        else if (moveRight) {
            lastMovementDirection = 0;
            playerImage = playerRightImages[frameIndex];
            
        }
        
        else if (moveRight && moveLeft) {
            lastMovementDirection = 0;
            playerImage = playerRightImages[frameIndex];
        }
                
        else if (isJumping) {
            if (isJumping && moveLeft || isJumping && lastMovementDirection == 1) {
                playerImage = playerJumpLeftImages[frameIndex];
            } 
            
            else if (isJumping && moveRight) {
                playerImage = playerJumpImages[frameIndex];
            } 
            
            else {
                playerImage = playerJumpImages[frameIndex];
            }
        } 
        
        else {
            
            if (lastMovementDirection == 1) {
                playerImage = playerIdleLeftImages[frameIndex];
            } 
            else if (lastMovementDirection == 0) {
                playerImage = playerIdleImages[frameIndex];
            } 
            else {
                playerImage = playerIdleImages[frameIndex];
            }
        }

        g.drawImage(playerImage, playerX, playerY, 150, 150, this);
        

        // Draw timer
        g.setColor(Color.BLACK);
        g.setFont(new Font("Press Start 2P", Font.BOLD, 35));

        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth("LEVEL ONE");
        int textX = (getWidth() - textWidth) / 2;
        int textHeight = fontMetrics.getHeight();
        int textY = 60;

        int rectWidth = 850;
        int rectHeight = textHeight + 100;
        int rectX = textX;
        int rectY = textY - fontMetrics.getAscent() - 15;
        
        g.setColor(Color.WHITE);
        g.fillRect(rectX-650, rectY, rectWidth, rectHeight);
        g.setColor(Color.BLACK);
        g.drawRect(rectX-650, rectY, rectWidth, rectHeight);
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Press Start 2P", Font.BOLD, 35));
        g.drawString("LEVEL THREE", 175, 100);

        g.drawImage(lava,0, 630, 1720, 600, this);
        
        for (int i=30;i < 1500;i=i+405) {
            g.drawImage(platformImage1, i, 550,115,500, this);
        }
               
        g.drawImage(endFlagImage, 1500, 290, 150, 300, this);
        g.drawImage(spikes, 1510 ,450, 75, 150, this);

        g.drawImage(platformImage1,1500,550,200,500,this);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Press Start 2P", Font.BOLD, 35));
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Press Start 2P", Font.BOLD, 28));
        g.drawString("TIME: ", 550, 100);
        g.drawString(String.format("%02d:%02d", minutes, seconds), 700, 100);

    }
    
    public void update() {
        // Scroll the background
        if (moveRight && playerX >= getWidth() / 15) {
            scrollCount += scrollSpeed;
        }

        // Check if the player has reached the end of the third background
        int maxScroll = backgroundImages.length * getWidth() - getWidth();
        if (scrollCount >= maxScroll) {
            scrollCount = maxScroll;  // Limit the scroll count to the maximum value
            isScrolling = false;  // Stop scrolling
        }
// Handle player movement
        if (moveLeft && playerX > 0) {
            playerX -= xspeed;
        }

        if (moveRight && playerX < getWidth() - 100) {
            playerX += xspeed;
        }
        
        
        if (isJumping) {
            
            playerY += dy; // Update player's vertical position

            if (dy < 0) {
                canJump = false;
                dy += 1.05; // Adjust the gravity value to control the jump speed and height
            } 
            
            else if (dy >= 0 && dy < 20) {
                dy += 2.1;
                canJump = true;
                
            } else isJumping = false;
            
        }
        // Update frame index for animation
        frameIndex = (frameIndex + 1) % 10;
        
        long startTime = System.currentTimeMillis();
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        seconds = (int) (elapsedTime % 60);
        minutes = (int) (elapsedTime / 60);
        
        // Repaint the panel
        repaint();  
    }

    private void checkCollisions() {
        Rectangle playerRect = new Rectangle(playerX + 30, playerY - 20, 75, 155);
        Rectangle greenFlagRect = new Rectangle(1500, 400, 150, 300);
        Rectangle lavaRect = new Rectangle(0, 630, 1720, 600);
        Rectangle spikeRect = new Rectangle(1510 ,450, 75, 125);
        ArrayList<Rectangle> platforms = new ArrayList<>();

        for (int i=30;i < 1500;i=i+405) {
            platforms.add(new Rectangle(i, 550, 115, 450));
        }
        
        if (greenFlagRect.intersects(playerRect)) {
            levelEnded = true;
            musicClip.stop();
            moveSoundClip.stop();
            GameWinScreen.main(null);
            
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        }
        
        if (lavaRect.intersects(playerRect)) {
            levelEnded = true;
            musicClip.stop();
            moveSoundClip.stop();
            DeadScreen.main(null);
            
           Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        }
        
        if (spikeRect.intersects(playerRect)) {
            levelEnded = true;
            musicClip.stop();
            moveSoundClip.stop();
            DeadScreen.main(null);
            
           Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        }
        
        Boolean flg = false;
        for (int i = 0; i < platforms.size(); i++) {
            if (platforms.get(i).intersects(playerRect)) {
                flg = true;
                break;
            }
        }
        if(flg){
            if(!isJumping){
                dy = 0;
                playerY = 440;
            }
        }else{
            isJumping = true;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        checkCollisions();
        repaint();
        update();
        
        long elapsedTime = System.currentTimeMillis() - startTime;
        seconds = (int) (elapsedTime / 1000) % 60;
        minutes = (int) (elapsedTime / 1000) / 60;
        
        if (levelEnded) {
            timer.stop();
        }
    }
    

    private boolean isMoveSoundPlaying = false; // Track if move sound effect is playing

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            moveLeft = true;


            if (!isMoveSoundPlaying) {
                playMoveSound();
                isMoveSoundPlaying = true;
            }
        }
        
        if (keyCode == KeyEvent.VK_RIGHT) {
            moveRight = true;
            if (!isMoveSoundPlaying) {
                playMoveSound();
                isMoveSoundPlaying = true;
            }
        }
        if (keyCode == KeyEvent.VK_SPACE && canJump) {
            if (!isJumping) {
                isJumping = true;
                playJumpSound();
                dy = -26; // Adjust the initial jump velocity for a higher jump
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            moveLeft = false;
            if (isMoveSoundPlaying) {
                stopMoveSound();
                isMoveSoundPlaying = false;
            }
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            moveRight = false;
            if (isMoveSoundPlaying) {
                stopMoveSound();
                isMoveSoundPlaying = false;
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    private void setMusicVolume(float volume) {
        if (musicClip != null) {
            FloatControl gainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }

    private void setSoundVolume(float volume) {
        if (moveSoundClip != null) {
            FloatControl gainControl = (FloatControl) moveSoundClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
        if (jumpSoundClip != null) {
            FloatControl gainControl = (FloatControl) jumpSoundClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
        
    }
    
    private void playMoveSound() {
        if (moveSoundClip.isRunning()) {
            moveSoundClip.stop();
        }
        moveSoundClip.setFramePosition(0);
        moveSoundClip.start();
        moveSoundClip.setMicrosecondPosition(0);
        moveSoundClip.setFramePosition(0);
        moveSoundClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the sound
        setSoundVolume(soundVolume); // Apply the sound effect volume
    }

    private void stopMoveSound() {
        moveSoundClip.stop();
        moveSoundClip.setFramePosition(0);
    }


    private void playJumpSound() {
        if (jumpSoundClip.isRunning()) {
            jumpSoundClip.stop();
        }
        jumpSoundClip.setFramePosition(0);
        jumpSoundClip.start();
        jumpSoundClip.setMicrosecondPosition(0);
        jumpSoundClip.setFramePosition(0);
        jumpSoundClip.start();
        setSoundVolume(soundVolume); // Apply the sound effect volume
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Castle Conquest");
                Level3 game = new Level3();
                frame.add(game);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}