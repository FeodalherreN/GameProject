package com.jagers.spelet;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.jagers.spelet.models.MPPlayer;
import com.jagers.spelet.network.Client;

public class MainMenu {
	private JFrame ivFrame;
	private JFrame ivFindGameFrame;
	private JPanel ivMainPanel;
	private JPanel ivFindPanel;
	private JTextField ivTxtIP;
	private JButton ivBtnFind;
	private JLabel ivLblIP;
	private JLabel ivTitle;
	private JLabel ivStartGame;
	private JLabel ivFindGame;
	private JLabel ivAbout;
	
	public MainMenu()
	{
		this.ivFrame = new JFrame();
		this.ivMainPanel = new JPanel();
		this.ivTitle = new JLabel("Meningslösaste spelet", SwingConstants.CENTER);
		this.ivStartGame = new JLabel("Host Game", SwingConstants.CENTER);
		this.ivFindGame = new JLabel("Find Game", SwingConstants.CENTER);
		this.ivAbout = new JLabel("About", SwingConstants.CENTER);
		initGui();
	}
	
	private void initGui()
	{
		ivFrame.setSize(1280, 720);
		ivFrame.setTitle("Main menu");
		ivFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ivFrame.setLocationRelativeTo(null);
		ivFrame.getContentPane().setBackground(Color.BLACK);
		ivFrame.setLayout(new GridLayout());
		ivMainPanel.setLayout(new BoxLayout(ivMainPanel, BoxLayout.Y_AXIS));
		ivStartGame.setForeground(Color.WHITE);
		ivMainPanel.setBackground(Color.BLACK);
		ivAbout.setForeground(Color.WHITE);
		ivTitle.setForeground(Color.CYAN);
		ivFindGame.setForeground(Color.WHITE);
		ivTitle.setHorizontalAlignment(JLabel.CENTER);
		
		ivTitle.setFont(new Font("Serif", Font.PLAIN, 30));
		
		ivStartGame.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    	ivFrame.setVisible(false);
		    	new StartGame().start();
		    }  
		}); 
		ivFindGame.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    	initFindGameGUI();
		    }  
		}); 
		ivAbout.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    	System.out.println("test");
		    }  
		}); 
		ivMainPanel.add(ivTitle);
		ivMainPanel.add(ivStartGame);
		ivMainPanel.add(ivFindGame);
		ivMainPanel.add(ivAbout);
		ivFrame.add(ivMainPanel);
		ivFrame.setVisible(true);
	}
	private void initFindGameGUI()
	{
		ivFindGameFrame = new JFrame();
		ivFindGameFrame.setLocationRelativeTo(null);
		ivTxtIP = new JTextField(10);
		ivBtnFind = new JButton("Submit");
		ivLblIP = new JLabel("Enter ip");
		ivFindPanel = new JPanel();
		ivFindPanel.setLayout(new BoxLayout(ivFindPanel, BoxLayout.Y_AXIS));
		ivFindGameFrame.setLayout(new FlowLayout());
		ivBtnFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        		Client client = new Client(ivTxtIP.getText(), new MPPlayer(), new MPPlayer()); 
        		Thread clientThread = new Thread(client);
        		clientThread.setDaemon(true); // important, otherwise JVM does not exit at end of main()
        		clientThread.start(); 
        		ivFindGameFrame.setVisible(false);
        		ivFrame.setVisible(false);
		    	new StartGame().join();
            }
        });
		ivFindPanel.add(ivLblIP);
		ivFindPanel.add(ivTxtIP);
		ivFindPanel.add(ivBtnFind);
		ivFindGameFrame.add(ivFindPanel);
		ivFindGameFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		ivFindGameFrame.setTitle("Find game");
		ivFindGameFrame.setSize(300, 120);
		ivFindGameFrame.setVisible(true);
	}
	
	
}
