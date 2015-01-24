package Digga;

import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class StartMenu 
{
	Panel mainPanel;
	GamePanel gamePanel;
	HelpPanel helpPanel;
	PlayerInputPanel apnPanel; //panel responsible for the user input
	int frameX;
    int frameY;
	static BufferedImage img;
	static Font font = new Font("Comic Sans MS", Font.ITALIC, 100);
	protected static String clientName;
	JLabel goBack = new JLabel("Back To Main Menu");
	JLabel playL = new JLabel("Play");
	JLabel helpL = new JLabel("Help");
	JLabel quitL = new JLabel("Quit");
	JTextPane jtxName = new JTextPane();
	
	public StartMenu()
	{	
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		this.frameX = (int) width;
		this.frameY = (int) height;
		mainPanel = new Panel("background.jpg");
		helpPanel = new HelpPanel("background.jpg");
		apnPanel = new PlayerInputPanel("background.jpg");
	}
	class HelpPanel extends JPanel
	{
		String imgFileName;
		JLabel helpLabel = new JLabel("Goal: collect points by digging graves");
		
		public HelpPanel(String str)
		{
			setLayout(null);
			imgFileName = str;
			img = scaleImage(frameX, frameY, imgFileName);

			goBack = new JLabel("Back To Main Menu");
			goBack.setFont(font);
			goBack.setForeground(Color.WHITE);
			add(goBack);
			goBack.setBounds(500, 500, 1000, 300);
			//goBack.setBounds(frameX / 2 - frameX / 4 + frameX/16, frameY / 2 + frameY / 5, frameX - 200, 300);
			
			//goBack.addMouseListener(new GoBack(goBack));
		}

		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(img, 0, 0, this);

		}
	}

	class GamePanel extends JPanel
	{
		String fileName;
		public GamePanel(String fileName)
		{
			this.fileName = fileName;
			img = scaleImage(frameX, frameY, fileName);
		}
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(img, 0, 0, this);
		}
	}
	
	class PlayerInputPanel extends JPanel
	{
		String fileName;
		JLabel inputPlayerLabel = new JLabel("Input Your Name: ");
		Font font2 = new Font("Comic Sans MS", Font.ITALIC, 50);
		Font fontForInput = new Font("Comic Sans MS", Font.ITALIC, 20);

		public PlayerInputPanel(String str)
		{
			setLayout(null);
			fileName = str;
			img = scaleImage(frameX, frameY, fileName);

			inputPlayerLabel.setFont(font2);
			add(inputPlayerLabel);
			inputPlayerLabel.setForeground(Color.WHITE);
			inputPlayerLabel.setBounds(frameX / 4, frameY / 6, frameX - 200, 300);

			jtxName.setFont(fontForInput);
			add(jtxName);
			
			makeAction(jtxName); //adds action listner to jtxtName
			
			jtxName.setEditable(true);
			jtxName.setBounds(frameX / 2 - frameX / 50, frameY / 4 + frameY / 22, 300, 35);
			add(jtxName);

			goBack.setFont(font);
			goBack.setForeground(Color.WHITE);
			add(goBack);
			goBack.setBounds(frameX / 2 - 420, frameY / 2 + frameY / 20, 1000, 300);
			goBack.addMouseListener(new GoBack(goBack));

		}

		public void makeAction(final JTextPane textPane)
		{
			int condition = JComponent.WHEN_FOCUSED;
			InputMap iMap = textPane.getInputMap(condition);
			ActionMap aMap = textPane.getActionMap();

			String enter = "enter";
			iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter);
			aMap.put(enter, new AbstractAction()
			{
	
				public void actionPerformed(ActionEvent arg0)
				{
					clientName = textPane.getText();
					//System.out.println("Client name: " + clientName);
					
//					cardLayout.show(mainP, "Panel 4");
					
				}
			});
		}

		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(img, 0, 0, this);
		}
	}

	class Panel extends JPanel
	{
		String fileName;

		public Panel(String str)
		{
			setLayout(null);
			fileName = str;
			img = scaleImage(frameX, frameY, fileName);

			playL.setFont(font);
			add(playL);
			playL.setForeground(Color.WHITE);
			playL.setBounds(frameX / 2 - frameX / 22, frameY / 6, 200, 200);

//			helpL.setFont(font);
//			add(helpL);
//			helpL.setForeground(Color.WHITE);
//			helpL.setBounds(frameX / 2 - frameX / 18, frameY / 3, 250, 200);

			quitL.setFont(font);
			add(quitL);
			quitL.setForeground(Color.WHITE);
			quitL.setBounds(frameX / 2 - frameX / 16, frameY / 3, 250, 200);

			playL.addMouseListener(new MouseListener()
			{

	
				public void mouseClicked(MouseEvent arg0)
				{
//					cardLayout.show(mainP, "Panel 3");

				}

		
				public void mouseEntered(MouseEvent arg0)
				{
					playL.setForeground(Color.YELLOW);

				}

	
				public void mouseExited(MouseEvent arg0)
				{
					playL.setForeground(Color.WHITE);

				}

		
				public void mousePressed(MouseEvent arg0)
				{
					// TODO Auto-generated method stub

				}

	
				public void mouseReleased(MouseEvent arg0)
				{
					// TODO Auto-generated method stub

				}

			});
//			quitL.addMouseListener(new MouseListener()
//			{
//				@Override
//				public void mouseClicked(MouseEvent arg0)
//				{
//					System.exit(0);
//				}
//
//				@Override
//				public void mouseEntered(MouseEvent arg0)
//				{
//					quitL.setForeground(Color.YELLOW);
//				}
//
//				@Override
//				public void mouseExited(MouseEvent arg0)
//				{
//					quitL.setForeground(Color.WHITE);
//
//				}
//
//				@Override
//				public void mousePressed(MouseEvent arg0)
//				{
//
//				}
//
//				@Override
//				public void mouseReleased(MouseEvent arg0)
//				{
//				}
//
//			});
			helpL.addMouseListener(new MouseListener()
			{
		
				public void mouseClicked(MouseEvent arg0)
				{
//					cardLayout.next(mainP);
				}

		
				public void mouseEntered(MouseEvent arg0)
				{
					helpL.setForeground(Color.YELLOW);
				}

		
				public void mouseExited(MouseEvent arg0)
				{
					helpL.setForeground(Color.WHITE);

				}

			
				public void mousePressed(MouseEvent arg0)
				{

				}

			
				public void mouseReleased(MouseEvent arg0)
				{
				}

			});
		}

		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(img, 0, 0, this);

		}
	}

	public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) //int WIDTH, int HEIGHT, String filename
	{
		BufferedImage bi = null;
		try
		{
			//System.out.println("WIDTH: " + frameX + " HEIGHT: " + frameY);
			ImageIcon ii = new ImageIcon("background.jpg");// path to image
			bi = new BufferedImage(frameX, frameY, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = (Graphics2D) bi.createGraphics();
			g2d.addRenderingHints(new RenderingHints(
					RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY));
			g2d.drawImage(ii.getImage(), 0, 0, frameX, frameY, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return bi;
	}

	class GoBack implements MouseListener
	{
		JLabel goBack;

		public GoBack(JLabel goBack)
		{
			this.goBack = goBack;
		}


		public void mouseClicked(MouseEvent arg0)
		{
//			cardLayout.show(mainP, "Panel 1");
		}


		public void mouseEntered(MouseEvent arg0)
		{
			goBack.setForeground(Color.YELLOW);
		}


		public void mouseExited(MouseEvent arg0)
		{
			goBack.setForeground(Color.WHITE);

		}


		public void mousePressed(MouseEvent arg0)
		{
			// TODO Auto-generated method stub

		}

	
		public void mouseReleased(MouseEvent arg0)
		{
			// TODO Auto-generated method stub

		}
	}
	public String getClientName()
	{
		return clientName;
	}
}
