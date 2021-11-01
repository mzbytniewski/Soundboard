/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mikez.soundboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author mikez
 */
public class Soundboard extends JFrame
{
    private static String SOUND_DIR;
    private final JPanel thePanel;
    private final JScrollPane scrollPane;
    
    public static void main(String args[])
    {
        Soundboard soundboard = new Soundboard();
        soundboard.setVisible(true);
    }
    
    public Soundboard()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Jack Black Soundboard");
        thePanel = new JPanel();
        thePanel.setLayout(new GridLayout(0, 10));
        thePanel.setBackground(new Color(94, 133, 44));
        scrollPane = new JScrollPane(thePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane, BorderLayout.BEFORE_FIRST_LINE);
        setSizes();
        
        ArrayList<String> names = getSounds();
        setButtons(names);
    }
    
    private void setSizes()
    {
        int width = 1500;
        int height = 750;
        
        this.setSize(width, height);
        scrollPane.setPreferredSize(new Dimension(width, height - 50));
    }
    
    private void setButtons(ArrayList<String> names)
    {
        for (String name : names)
        {
            JButton button = new JButton(name);
            
            button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    playSound(name);
                } 
            });
            
            button.setBackground(new Color(230, 183, 103));
            
            thePanel.add(button);
        }
    }
    
    private ArrayList<String> getSounds()
    {
        checkDir();
        ArrayList<String> sounds = new ArrayList<>();
        
        File soundDir = new File(SOUND_DIR);
        
        if (soundDir.exists())
        {
            File[] files = soundDir.listFiles();
            
            for (File file : files)
            {
                String filename = file.getName();
                
                if (filename.endsWith(".wav"))
                {
                    String sound = filename.substring(0, filename.lastIndexOf("."));
                    sounds.add(sound);
                }
            }
        }
        else
        {
            MessagePopup popup = new MessagePopup("\"Sounds\" folder does not exist!");
        }
        
        return sounds;
    }
    
    private void checkDir()
    {
        File soundDir = new File("Sounds");
        if (soundDir.exists() && soundDir.isDirectory())
        {
            SOUND_DIR = "Sounds/";
        }
        else
        {
            SOUND_DIR = "../Sounds/";
        }
    }
    
    private void playSound(String soundFileName)
    {
        if (soundFileName != null && !soundFileName.isEmpty())
        {
            File soundFile = new File(SOUND_DIR + soundFileName + ".wav");
            
            if (soundFile.exists())
            {
                try
                {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.start();
                }
                catch (IOException | LineUnavailableException | UnsupportedAudioFileException e)
                {
                    MessagePopup p = new MessagePopup("Could not play sound file: " + soundFileName + " " + e.getMessage());
                }
            }
            else
            {
                MessagePopup p = new MessagePopup("Sound " + soundFileName + " does not exist!");
            }
        }
    }
}
