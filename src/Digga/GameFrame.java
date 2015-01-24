package Digga;




import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.AWTGLCanvas;


public class GameFrame
{
	
	static Canvas canvas;
	static Client client;
	static GameWorld myWorld;
	static float WORLD_LENGTH = 50;
	static float WORLD_WIDTH = 50;
	static int ID = -1;
	static ArrayList<Rectangle> rects;
	static int displayListHandle = -1;
	
	
	final static JButton exit = new JButton("X");
	static double width, height;
	static CardLayout cardLayout;
	static String clientName;
	static JScrollPane scrollPane;
	static int frameX;
	static int frameY;
	public static void main(String [] args)
	{	
		
		
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		// JFrame frame = new JFrame("Test");

		canvas = new Canvas();
		canvas.setSize(new Dimension(640, 480));

		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setLayout(new BorderLayout());

		// JPanel panel = new JPanel();

		// JTextField textField = new JTextField(40);

		client = new Client();
		client.frame.setSize(640, 640);
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// client.frame.add(canvas, BorderLayout.CENTER);

		// //Setting full size to the screen of the game
		client.frame.setUndecorated(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		client.frame.setBounds(0, 0, screenSize.width, screenSize.height);
		client.frame.setLocationRelativeTo(null);
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();

		frameX = (int) width;
		frameY = (int) height;
		scrollPane = new JScrollPane(client.panel);
		client.holderPanel.setLayout(new CardLayout());
		client.gameHolderPanel.setLayout(new BorderLayout());
		cardLayout = (CardLayout) client.holderPanel.getLayout();
		
		client.gameHolderPanel.add(canvas, BorderLayout.CENTER);
		client.sp.setLayout(null);
		exit.setBounds(frameX - 100,10,50,50);
		client.sp.setPreferredSize(new Dimension(frameX,100));
		client.sp.add(exit);
		client.gameHolderPanel.add(client.sp, BorderLayout.NORTH);
		client.gameHolderPanel.add(scrollPane, BorderLayout.SOUTH);
		client.holderPanel.add(client.gameHolderPanel, "GAME_ACTION"); // the //
																		// game...ACTION!!!
		client.holderPanel.add(client.sm.mainPanel, "GAME_MENU_MAIN"); // the //
																		// panel
		client.holderPanel.add(client.sm.helpPanel, "GAME_MENU_HELP"); // help
		client.holderPanel.add(client.sm.apnPanel, "GAME_MENU_NAME"); // input
		client.frame.add(client.holderPanel);

		cardLayout.show(client.holderPanel, "GAME_MENU_MAIN");

		makeAction(client.sm.jtxName);

		client.sm.goBack.addMouseListener(new MouseListener() {
		
			public void mouseClicked(MouseEvent arg0) {
				cardLayout.show(client.holderPanel, "GAME_MENU_MAIN");
			}

		
			public void mouseEntered(MouseEvent arg0) {
				client.sm.goBack.setForeground(Color.YELLOW);
			}

		
			public void mouseExited(MouseEvent arg0) {
				client.sm.goBack.setForeground(Color.WHITE);

			}

			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}


			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		client.sm.playL.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
				cardLayout.show(client.holderPanel, "GAME_MENU_NAME");

			}

	
			public void mouseEntered(MouseEvent arg0) {
				client.sm.playL.setForeground(Color.YELLOW);

			}

			public void mouseExited(MouseEvent arg0) {
				client.sm.playL.setForeground(Color.WHITE);

			}

			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		client.sm.quitL.addMouseListener(new MouseListener() {
	
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}

		
			public void mouseEntered(MouseEvent arg0) {
				client.sm.quitL.setForeground(Color.YELLOW);

			}

			public void mouseExited(MouseEvent arg0) {
				client.sm.quitL.setForeground(Color.WHITE);

			}

			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		client.frame.setVisible(true);

		// adding exit button to the screen
		exit.setPreferredSize(new Dimension(10, 10));
		exit.setBackground(Color.RED);
		exit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		initDisplay();
		initGL();

//		Sound music = new Sound("enemy.wav");  
//		
//		
//		music.start();
		gameLoop();

		
		
	}
	public static void makeAction(final JTextPane textPane) {
		int condition = JComponent.WHEN_FOCUSED;
		InputMap iMap = textPane.getInputMap(condition);
		ActionMap aMap = textPane.getActionMap();

		String enter = "enter";
		iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter);
		aMap.put(enter, new AbstractAction() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				clientName = textPane.getText();
				
			//	System.out.println("Client name: " + clientName);
				Client.myName = clientName;
				cardLayout.show(client.holderPanel, "GAME_ACTION");
				client.start(); // start the thread after the user enters
								// his/her name and enters the game
								// world**********
			}
		});
	}
	private static void gameLoop()
	{
		myWorld = new GameWorld(1);

		rects = new ArrayList<Rectangle>();
		DiggerCam dcam = null;
		Digga myDigga = null;
		while(!Display.isCloseRequested())
		{
			if(ID != -1)
			{
				
				if(dcam == null)
				{
					myDigga = myWorld.diggas[ID];
					dcam = new DiggerCam(60, (float)Display.getWidth()/(float)Display.getHeight(), 0.3f, 100, myDigga);
				}
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				glLoadIdentity();
				
				
			
				myDigga.diggaLoop();
				
				//if(myDigga.changedPosition)
				//{
					client.updateMyDigga(myDigga);
					//client.updateGraves();
				//}
				dcam.setView();	
				
				
				for(int i = 0; i < myWorld.NUM_DIGGAS; i++)
				{
					if(myWorld.diggas[i] != null)
					{
						Digga d = myWorld.diggas[i];
						drawRectPrism(d.x, d.y, d.z, d.length, d.height, d.width, 0, d.rotation, 0);
					}
				}
				
				
				
				
				drawField();
				
				for(int i = 0; i < myWorld.NUM_GRAVES; i++)
				{
					if(myWorld.graves[i] != null)
					{
						if(myWorld.graves[i].digState == -1 && !myWorld.graves[i].changed)
						{
							client.updateGrave(i);
						}
						myWorld.graves[i].DrawPlot();
					}
					
					
				}
				for(int i = 0; i < myWorld.NUM_GHOSTS; i++){
					if (myWorld.ghosts[i] != null){
						myWorld.ghosts[i].ghostLoop();
					}
				}
				
				Display.update();
				}
			}
		
	}
	
	

	public static void CreateNextRectRight(Rectangle rect)
	{
		float xMax = 50;
		Rectangle nextXRect = rect;
		for(int i = 0; i < rects.size(); i++)
		{
			Rectangle r = rects.get(i);
			
			if(r.xMin >= rect.xMax && (r.xMin < nextXRect.xMin || nextXRect == rect) && r.zMax > rect.zMin && r.zMin < rect.zMax)
			{
				if(r.xMin == rect.xMax)
				{
					return;
				}
				nextXRect = r;
			}			
		}
		
		if(nextXRect != rect)
		{
			xMax = nextXRect.xMin;
		}
		
		Rectangle newRect = new Rectangle(rect.xMax,  xMax, rect.zMin, rect.zMax);
		rects.add(newRect);
		if(newRect.xMax < 50)
		{
			CreateNextRectRight(newRect);
		}
		if(newRect.zMax < 50)
		{
			CreateNextRectDown(newRect);
		}	
		if(newRect.zMin > -50)
		{
			CreateNextRectUp(newRect);
		}	
	}
	
	public static void CreateNextRectLeft(Rectangle rect)
	{
		float xMin = -50;
		Rectangle nextXRect = rect;
		for(int i = 0; i < rects.size(); i++)
		{
			Rectangle r = rects.get(i);
			
			if(r.xMax <= rect.xMin && (r.xMax > nextXRect.xMax || nextXRect == rect) && r.zMax > rect.zMin && r.zMin < rect.zMax)
			{
				if(r.xMax == rect.xMin)
				{
					return;
				}
				nextXRect = r;
			}			
		}
		
		if(nextXRect != rect)
		{
			xMin = nextXRect.xMax;
		}
		
		Rectangle newRect = new Rectangle(rect.xMin,  xMin, rect.zMin, rect.zMax);
		rects.add(newRect);
		if(newRect.xMin > -WORLD_LENGTH)
		{
			CreateNextRectLeft(newRect);
		}
		if(newRect.zMin > -WORLD_WIDTH)
		{
			CreateNextRectDown(newRect);
		}
		if(newRect.zMax < WORLD_WIDTH)
		{
			CreateNextRectUp(newRect);
		}		
	}
	
	public static void CreateNextRectUp(Rectangle rect)
	{
		float zMin = -50;
		Rectangle nextZRect = rect;
		for(int i = 0; i < rects.size(); i++)
		{
			Rectangle r = rects.get(i);
			
			if(r.zMax <= rect.zMin && (r.zMax > nextZRect.zMax || nextZRect == rect) &&  r.xMax > rect.xMin && r.xMin < rect.xMax)
			{
				if(r.zMax == rect.zMin)
				{
					return;
				}
				nextZRect = r;
			}
		}
		
		if(nextZRect != rect)
		{
			zMin = nextZRect.zMax;
		}
		
		Rectangle newRect = new Rectangle(rect.xMin, rect.xMax, rect.zMin, zMin);
		rects.add(newRect);
		if(newRect.xMin > -WORLD_LENGTH)
		{
			CreateNextRectLeft(newRect);
		}
		if(newRect.xMax < WORLD_LENGTH)
		{
			CreateNextRectRight(newRect);
		}
		if(newRect.zMin > -WORLD_WIDTH)
		{
			CreateNextRectUp(newRect);
		}
	}
	
	public static void CreateNextRectDown(Rectangle rect)
	{
		float zMax = 50;
		Rectangle nextZRect = rect;
		for(int i = 0; i < rects.size(); i++)
		{
			Rectangle r = rects.get(i);
			
			if(r.zMin >= rect.zMax && (r.zMin < nextZRect.zMin || nextZRect == rect) &&  r.xMax > rect.xMin && r.xMin < rect.xMax)
			{
				if(r.zMin == rect.zMax)
				{
					return;
				}
				nextZRect = r;
			}
		}
		
		if(nextZRect != rect)
		{
			zMax = nextZRect.zMin;
		}
		
		Rectangle newRect = new Rectangle(rect.xMin, rect.xMax, rect.zMax, zMax);
		rects.add(newRect);
		
		if(newRect.xMin > -WORLD_LENGTH)
		{
			CreateNextRectLeft(newRect);
		}
		if(newRect.xMax < WORLD_LENGTH)
		{
			CreateNextRectRight(newRect);
		}
		if(newRect.zMax < WORLD_WIDTH)
		{
			CreateNextRectDown(newRect);
		}
	}
	
	
	
	
	
	public static void setup()
	{
		displayListHandle = glGenLists(1);
		glNewList(displayListHandle, GL_COMPILE);
		
		for(int i = 0; i < myWorld.NUM_GRAVES; i++)
		{
			if(myWorld.graves[i] != null)
			{
				myWorld.graves[i].GraveDisplay();
			}
			
			
		}
		
		for(int i = myWorld.NUM_GRAVES; i < rects.size(); i++)
		{
			
			//(float)i/20f
			glBegin(GL_QUADS);
			{
				glColor3f(.1f, .1f, .1f);
				glVertex3f(rects.get(i).xMin, 0, rects.get(i).zMin);
				glVertex3f(rects.get(i).xMin, 0, rects.get(i).zMax);
				glVertex3f(rects.get(i).xMax, 0, rects.get(i).zMax);
				glVertex3f(rects.get(i).xMax, 0, rects.get(i).zMin);
			}
			glEnd();
		}
		glEndList();
	}
	public static void drawField()
	{
		
		
		
		if(rects.size() < myWorld.NUM_GRAVES)
		{
			for(int i = 0; i < myWorld.NUM_GRAVES; i++)
			{
				while(myWorld.graves[i] == null)
				{
					
				}
				rects.add(myWorld.graves[i].myRect);
				
			}
			//CreateNextRects(new Rectangle(WORLD_LENGTH, WORLD_WIDTH, WORLD_LENGTH, WORLD_WIDTH));
			//splitRectangles(-l, -w, l, w, -l, -w);
			for(int i = 0; i < myWorld.NUM_GRAVES; i++)
			{
				CreateNextRectRight(myWorld.graves[i].myRect);
				CreateNextRectUp(myWorld.graves[i].myRect);
				CreateNextRectDown(myWorld.graves[i].myRect);
				CreateNextRectLeft(myWorld.graves[i].myRect);
				
			}
			setup();
			//CreateNextRects(new Rectangle(-50, -50, -50, -50));
			
		}
		
		
		glCallList(displayListHandle);
		
		
		
		
		
		glPushMatrix();
		{
		glBegin(GL_QUADS);
		{
			float l = WORLD_LENGTH;
			float w = WORLD_WIDTH;
			float h = 3.0f;
			/*l /= 2;
			w /= 2;
			
			glColor3f(.1f, .1f, .1f);
			glVertex3f(-l, 0, -w);
			glVertex3f(-l, 0, w);
			glVertex3f(l, 0, w);
			glVertex3f(l, 0, -w);*/
			
			glColor3f(.25f, .25f, .25f);
			glVertex3f(-l, h, -w);
			glVertex3f(l, h, -w);
			glVertex3f(l, 0, -w);
			glVertex3f(-l, 0, -w);
			
			glVertex3f(-l, h, w);
			glVertex3f(l, h, w);
			glVertex3f(l, 0, w);
			glVertex3f(-l, 0, w);
			
			glVertex3f(-l, h, w);
			glVertex3f(-l, h, -w);
			glVertex3f(-l, 0, -w);
			glVertex3f(-l, 0, w);
			
			glVertex3f(l, h, w);
			glVertex3f(l, h, -w);
			glVertex3f(l, 0, -w);
			glVertex3f(l, 0, w);
		}
		glEnd();
		}
		glPopMatrix();
	
	}
	
	public static void drawRectPrism(float centerx, float centery, float centerz, float length, float height, float width, float rx, float ry, float rz)
	{
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		{
			glTranslatef(centerx, centery, centerz);
			glRotatef(rx, 1, 0, 0);
			glRotatef(ry, 0, 1, 0);
			glRotatef(rz, 0, 0, 1);
			glTranslatef(-centerx, -centery, -centerz);
			glTranslatef(-length/2f, -height/2f, -width/2f);
			
			
			glBegin(GL_QUADS);
			{
				glColor3f(1.0f, 0, 0);
				glVertex3f(centerx, centery, centerz);
				glVertex3f(centerx + length, centery, centerz);
				glVertex3f(centerx + length, centery + height, centerz);
				glVertex3f(centerx, centery + height, centerz);
				
				glColor3f(0, 1.0f, 0);
				glVertex3f(centerx, centery, centerz + width);
				glVertex3f(centerx + length, centery, centerz + width);
				glVertex3f(centerx + length, centery + height, centerz + width);
				glVertex3f(centerx, centery + height, centerz + width);
				
				glColor3f(0, 0, 1.0f);
				glVertex3f(centerx, centery, centerz);
				glVertex3f(centerx, centery + height, centerz);
				glVertex3f(centerx, centery + height, centerz + width);
				glVertex3f(centerx, centery, centerz + width);			
				
				glColor3f(0, 1.0f, 1.0f);
				glVertex3f(centerx + length, centery, centerz);
				glVertex3f(centerx + length, centery + height, centerz);
				glVertex3f(centerx + length, centery + height, centerz + width);
				glVertex3f(centerx + length, centery, centerz + width);	
				
				glColor3f(1.0f, 1.0f, 0);
				glVertex3f(centerx, centery, centerz);
				glVertex3f(centerx + length, centery, centerz);
				glVertex3f(centerx + length, centery, centerz + width);
				glVertex3f(centerx, centery, centerz + width);
				
				glColor3f(1.0f, 0, 1.0f);
				glVertex3f(centerx, centery + height, centerz);
				glVertex3f(centerx + length, centery + height, centerz);
				glVertex3f(centerx + length, centery + height, centerz + width);
				glVertex3f(centerx, centery + height, centerz + width);			
			}
			glEnd();
		}
		glPopMatrix();
	}
	
	
	
	  
	private static void initGL()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), 0, Display.getHeight(), -1, 1000);
		glMatrixMode(GL_MODELVIEW);
		
		glClearColor(0, 0, 0, 1);
	}
	
	private static void cleanUp()
	{
		Display.destroy();
	}
	
	private static void initDisplay()
	{
		try 
		{
			
			Display.setParent(canvas);
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.create();
		} 
		catch (LWJGLException e) 
		{
			e.printStackTrace();
		}
	}

}

class Client extends Thread
{
	static String myName;
	static String myID;
	BufferedReader in;
	PrintWriter out;
	JFrame frame = new JFrame("Chatter");
	JTextField textField = new JTextField(40);
	JTextArea messageArea = new JTextArea(8, 40);
	JPanel panel = new JPanel(new BorderLayout());
	
	ScoreboardPanel sp = new ScoreboardPanel();
	private JTextArea tf;
	
	static int minute;
	static int second;
	
	
	StartMenu sm = new StartMenu();
	JPanel holderPanel = new JPanel(); // created new...
	JPanel gameHolderPanel = new JPanel(); // created new...
	//boolean gameOver = false;
	static boolean scoreUpdate = true;

	public Client()
	{

		
		
		// Layout GUI
		textField.setEditable(false);
		messageArea.setEditable(false);
		panel.add(textField, "North");
		panel.add(new JScrollPane(messageArea), "Center");
		frame.add(panel, BorderLayout.SOUTH);
		frame.setResizable(false);
		
		//
		//sp.setPreferredSize(new Dimension(0, 120));
		
		//frame.add(sp, BorderLayout.NORTH);
		//frame.pack();

		// Add Listeners
		textField.addActionListener(new ActionListener()
		{
			/**
			 * Responds to pressing the enter key in the textfield by sending
			 * the contents of the text field to the server. Then clear the text
			 * area in preparation for the next message.
			 */
			public void actionPerformed(ActionEvent e)
			{
				out.println("CHAT"  + Client.myName + ":" + textField.getText());
				textField.setText("");
			}
		});
	}

	/**
	 * Prompt for and return the address of the server.
	 */
	private String getServerAddress()
	{
		return JOptionPane.showInputDialog(frame,
				"Enter IP Address of the Server:", "Welcome to the Chatter",
				JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * Prompt for and return the desired screen name.
	 */
	public String name()
	{
		return JOptionPane.showInputDialog(frame, "Choose a screen name:",
				"Screen name selection", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Connects to the server then enters the processing loop.
	 */
	public void run()
	{
		try 
		{
		// Make connection and initialize streams
		//String serverAddress = getServerAddress();
		Socket socket;
		
			socket = new Socket("localhost", 9001);
		
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		// Process all messages from server, according to the protocol.
		
		while (true)
		{
			String line = in.readLine();
			
			if (line.startsWith("SUBMITNAME"))
			{
				//out.println("Daler");
			}
			else if (line.startsWith("NAMEACCEPTED"))
			{
				
				textField.setEditable(true);
				myID  = line.substring(12, line.length());
			}
			else if (line.startsWith("MESSAGE"))
			{
				messageArea.append(line.substring(8) + "\n");
			}
			else if(line.startsWith("Player"))
			{
				int playerID = Integer.parseInt("" + line.charAt(6));
				if(GameFrame.myWorld.diggas[playerID] == null)
				{
					GameFrame.myWorld.addDigga(playerID, line);
					if(GameFrame.ID == -1)
					{
						GameFrame.ID = playerID;
						myID  = "Player" + playerID;
					}
				}
				else
				{
					GameFrame.myWorld.changeDigga(playerID, line);

					//GameFrame.myWorld.diggas[playerID].changedPosition = false;
				}
			}
			else if(line.startsWith("Grave"))
			{
				int graveID = Integer.parseInt("" + line.charAt(5));
				
				if(line.length() > 10)
				{
					
					GameFrame.myWorld.changeGrave(graveID, line);
					
				}
				else
				{
					GameFrame.myWorld.changeGrave(graveID, line.substring(6, line.length()));
					GameFrame.myWorld.parseDigState(line);
			
				}
					
			}
			
		
			else if (line.startsWith("Ghost"))
			{
				int ghostID = Integer.parseInt("" + line.charAt(5));
				GameFrame.myWorld.changeGhost(ghostID, line);
			}
			else if(line.startsWith("Time"))
			{
				ScoreboardPanel.minute = Integer.parseInt(line.substring(4, 5));
				ScoreboardPanel.second = Integer.parseInt(line.substring(6, 8));
			}
			else if(line.startsWith("Score"))
			{
				Scanner scan = new Scanner(line);
				scan.next();
				
				ScoreboardPanel.player1Score = Integer.parseInt(scan.next());
				ScoreboardPanel.player2Score = Integer.parseInt(scan.next());
				scan.close();
			}
			else if(line.startsWith("WinCondition")){
				Scanner scan = new Scanner(line);
				scan.next();
				
				ScoreboardPanel.player1Score = Integer.parseInt(scan.next());
				ScoreboardPanel.player2Score = Integer.parseInt(scan.next());
				if(ScoreboardPanel.player1Score > ScoreboardPanel.player2Score){
					ScoreboardPanel.winMessage = "Team A Win";
				}else if(ScoreboardPanel.player1Score < ScoreboardPanel.player2Score){
					ScoreboardPanel.winMessage = "Team B Win";
					
				}else if(ScoreboardPanel.player1Score == ScoreboardPanel.player2Score){
					ScoreboardPanel.winMessage = "Draw";
				}
				ScoreboardPanel.winCondition = true;
			}
		}
		
		
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	

	public void updateMyDigga(Digga digga)
	{
		out.println(myID  + digga.createObjectStream());
	}
	
	public void updateGrave(int i)
	{
		if(GameFrame.myWorld.graves[i] != null)
		{
			out.println("Grave" + i + " " + GameFrame.myWorld.graves[i].digState + " " + myID);
		}		
	}

	/**
	 * Main method that runs the thread.
	 */
}

class ScoreboardPanel extends JPanel
{
	
	static int minute = 3; //Change to start minute
	static int second;
	public static int player1Score = 0;
	public static int player2Score = 0;
	static boolean winCondition = false;
	static String winMessage = "";
		public ScoreboardPanel(){
			this.setLayout(null);
		}
		
		public void paintComponent(Graphics g){
			
			super.paintComponent(g);
			//Score container
			
			//Background
			g.setColor(Color.black);
			g.fillRect(0,0, GameFrame.frameX, 160);
			
			//Score labels
			if(winCondition == false){
			Font font = new Font("Arial", Font.BOLD, 30);
			g.setFont(font);
			g.setColor(Color.blue);
			g.drawString("Team A: " + player1Score , 200, 75); 
			g.setColor(Color.yellow);
			g.drawString("Team B: " + player2Score , GameFrame.frameX - 400, 75);
			
			//Timer box
			g.setColor(Color.darkGray);
			g.fillRect(GameFrame.frameX - 1070, 0, 240, 250);
			
			//g.drawString(minute + minute2 + " : " + second + second2, 275, 80);
			
			
			if(minute >= 1){
				if(second > 9){
					g.setColor(Color.green);
					g.drawString(minute + " : " + second, GameFrame.frameX - 1000, 75);
				}
				else if(second <= 9 && second >= 0){
					g.setColor(Color.green);
					g.drawString(minute + " : " + "0" + second, GameFrame.frameX - 1000, 75);
				}
			}
			
			if(minute < 1){
				if(second > 9){
					g.setColor(Color.red);
					g.drawString(minute + " : " + second, GameFrame.frameX - 1000, 75);
				}
				else if(second <= 9 && second >= 0){
					g.setColor(Color.red);
					g.drawString(minute + " : " + "0" + second, GameFrame.frameX - 1000, 75);
				}
			}
			}
			
			if(winCondition == true){
				g.setColor(Color.darkGray);
				g.fillRect(GameFrame.frameX - 1070, 0, 240, 250);
				g.setColor(Color.yellow);
				Font font = new Font("Arial", Font.BOLD, 30);
				g.setFont(font);
				g.drawString(winMessage, GameFrame.frameX - 1100, 75);
			}
			
			repaint();
			revalidate();
		}
		
		//Run listening to score received from Clients
		public void run(){
			
		
				}
			
			
		}
	
	


