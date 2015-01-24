package Digga;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Server
{
	private static final int PORT = 9001;
	private static HashSet<String> names = new HashSet<String>();
	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
	/*private static Ghost ghost;
	private static Ghost2 ghost2;
	private static String target;
	private static String target2;
	private static int trackCount = 0;
	private static int direction = 0;
	private static float remain;
	private static int direction2 = 0;
	private static float remain2;
	static String ghost1Phase;
	static String ghost2Phase;*/
	//public static boolean [] gravesDug;
	
	static boolean stopSending = false;
	
	public static GameWorld realWorld;
	
	public static int numConnectedClients;
	
	public static void main(String[] args) throws Exception
	{
		realWorld = new GameWorld(0);
		generateWorld();
		
		numConnectedClients = 0;
		ServerSocket listener = new ServerSocket(PORT);
		//gravesDug = new boolean[realWorld.NUM_GRAVES];
		try
		{
			while (true)
			{
				new Handler(listener.accept(), numConnectedClients).start();
				System.out.print("balls");
				numConnectedClients++;
				if(numConnectedClients == 4){
					
					break;
					
				}
			
			}
			TimerThread();
		}
		
		finally
		{
			listener.close();
		}
	}
	
	public static void TimerThread() {
	    try {
	      Runnable r=new Runnable() {
	        public void run() {
	          startTimer();
	    }
	      };
	    Thread Timer=new Thread(r,"Timer Thread");
	    Timer.start();
	    } catch(Exception exc) {
	      JOptionPane.showMessageDialog(new JFrame(),exc);
	    }
	  }
	
	  public static void startTimer() {
	    try {
	      for(int i=3; i>=0; i--){
	    	  if(i == 3){
	    		  ScoreboardPanel.minute = i;
	    		  Thread.sleep(1000);
	    		  stopSending = true;
	    		  i--;
	    		  ScoreboardPanel.minute = i;
	    	  }
	    	  
	    	  for(int j=59; j>= 0; j-- ){
	    		  	  ScoreboardPanel.minute = i;
		    		  ScoreboardPanel.second = j;
		      		  Thread.sleep(1000);
	      	  }
	    }
	    } catch(Exception exc) {
	      JOptionPane.showMessageDialog(new JFrame(),exc);
	    }
	  }
	
	public static void generateWorld()
	{
		Random randomgen = new Random();
		
		realWorld.diggas[0] = new Digga(realWorld, -35, 1.5f, -35, 2, 3, 1, 0);
		realWorld.diggas[1] = new Digga(realWorld, 35, 1.5f, -35, 2, 3, 1, 0); 
		realWorld.diggas[2] = new Digga(realWorld, -35, 1.5f, 35, 2, 3, 1, 0);
		realWorld.diggas[3] = new Digga(realWorld, 35, 1.5f, 35, 2, 3, 1, 0);
		
		
	
		
		for(int i = 0; i < realWorld.NUM_GRAVES; i++)
		{
			
			float randx = 0;
			float randz = 0;
			boolean space = false;
			while(!space)
			{
				randx = 40f - randomgen.nextFloat()*randomgen.nextInt(80);
				randz = 40f - randomgen.nextFloat()*randomgen.nextInt(80);
				if(i != 0)
				{
					for(int j = 0; j < i; j++)
					{
						if((Math.pow(realWorld.graves[j].x - randx, 2) + Math.pow(realWorld.graves[j].z - randz, 2)) > 400)
						{
							space = true;
							
						}
						else
						{
							space = false;
							break;
						}
					}
				}
				else
				{
					space = true;
				}
			}
			realWorld.graves[i] = new Grave(randx, 1, randz, 2, 2, 1, randomgen.nextFloat()*randomgen.nextInt(360));			
		}
		
		
		for(int i = 0; i < realWorld.NUM_GHOSTS; i++)
		{
			float randx = 0;
			float randz = 0;
			boolean space = false;
			while(!space)
			{
				randx = 40f - randomgen.nextFloat()*randomgen.nextInt(80);
				randz = 40f - randomgen.nextFloat()*randomgen.nextInt(80);
				if(i != 0)
				{
					for(int j = 0; j < i; j++)
					{
						if((Math.pow(realWorld.ghosts[j].x - randx, 2) + Math.pow(realWorld.ghosts[j].z - randz, 2)) > 400)
						{
							space = true;
							
						}
						else
						{
							space = false;
							break;
						}
					}
				}
				else
				{
					space = true;
				}
			}
			realWorld.ghosts[i] = new Ghost(randx, randz, randomgen.nextFloat()*randomgen.nextInt(360), 1);//, randomgen.nextInt(2) + 1);
			//realWorld.ghosts[i].setAI(randomgen.nextInt(2) + 1);
		}
		
		//remain = 30;
		//remain2 = -20;
		//realWorld.ghosts[0] = new GhostObject(25, 1.5f, 25, 2, 3, 1);
		//realWorld.ghosts[1] = new GhostObject(-25, 1.5f, 25, 2, 3, 1);
	}

	/**
	 * A handler thread class. Handlers are spawned from the listening loop and
	 * are responsible for a dealing with a single client and broadcasting its
	 * messages.
	 */
	private static class Handler extends Thread
	{
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;

		public Handler(Socket socket, int name)
		{
			this.socket = socket;
			this.name = "Player" + name;
		}
		
		public void sendWorld()
		{
			for(int i = Server.numConnectedClients - 1; i >= 0 ; i--)
			{
				out.println("Player" + i + realWorld.diggas[i].createObjectStream());
			}
			
			
			for(int i = 0; i < realWorld.NUM_GRAVES; i++)
			{
				out.println("Grave" + i + realWorld.graves[i].createObjectStream());
				out.println("Grave"  + i +  realWorld.graves[i].digState);
				
			}
			
			for(int i = 0; i < realWorld.NUM_GHOSTS; i ++)
			{
				realWorld.ghosts[i].patrol();
				out.println("Ghost" + i + realWorld.ghosts[i].createObjectStream());
				//out.println(realWorld.ghosts[i].createAIStream());
			}
			
			if(ScoreboardPanel.second > 9)
			{
				System.out.println("Time" + ScoreboardPanel.minute + " " + ScoreboardPanel.second);
				out.println("Time" + ScoreboardPanel.minute + " " + ScoreboardPanel.second);
			}
			else
			{
				out.println("Time" + ScoreboardPanel.minute + " " + "0" + ScoreboardPanel.second + "0");
			}
			if(ScoreboardPanel.minute == 0 && ScoreboardPanel.second == 0){
				out.println("WinCondition " + ScoreboardPanel.player1Score + " " + ScoreboardPanel.player2Score);
			}
			for(int i = 0; i < realWorld.NUM_GRAVES; i++)
			{
				if(realWorld.graves[i].digState != -1){
					break;
				}
				if(i == (realWorld.NUM_GRAVES - 1)){
					out.println("WinCondition " + ScoreboardPanel.player1Score + " " + ScoreboardPanel.player2Score);
				}
			}
			
			out.println("Score " + ScoreboardPanel.player1Score + " " + ScoreboardPanel.player2Score);
			
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
		
		  
		public void run()
		{
			try
			{
				// Create character streams for the socket.
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				// Request a name from this client. Keep requesting until
				// a name is submitted that is not already used. Note that
				// checking for the existence of a name and adding the name
				// must be done while locking the set of names.
			
				while (true)
				{
					//out.println("SUBMITNAME");
					//name = in.readLine();
					
					if (name == null)
					{
						return;
					}
					synchronized (names)
					{
						if (!names.contains(name))
						{
							names.add(name);
							break;
						}
					}
				}

				// Now that a successful name has been chosen, add the
				// socket's print writer to the set of all writers so
				// this client can receive broadcast messages.
				out.println("NAMEACCEPTED");
				writers.add(out);

				// Accept messages from this client and broadcast them.
				// Ignore other clients that cannot be broadcasted to.
				while (true)
				{
					/*out.println(name + " " + realWorld.diggas[Integer.parseInt("" + name.charAt(6))].createObjectStream());
					
					if(Server.numConnectedClients > 1)
					{
						int temp = 1 - Integer.parseInt("" + name.charAt(6));
						out.println("Player" + temp + realWorld.diggas[temp].createObjectStream());
					}*/
					sendWorld();
						
					
					String input = in.readLine();
					if (input == null)
					{
						return;
					}
					else if(input.substring(0, 6).equals("Player"))
					{
						float [] xzr = realWorld.parseObjectStream(input);
						realWorld.diggas[Integer.parseInt("" + input.charAt(6))].x = xzr[0];
						realWorld.diggas[Integer.parseInt("" + input.charAt(6))].z = xzr[1];
						realWorld.diggas[Integer.parseInt("" + input.charAt(6))].rotation = xzr[2];
						
						//Ghost type 1 Hard Code Start
					/*	if(realWorld.ghosts[0].normal == true){
							//ghost1Phase = "normal";
							//System.out.println("normal");
						}else if(realWorld.ghosts[0].tracking == true){
							//ghost1Phase = "tracking";
							//System.out.println("tracking");
						}else if(realWorld.ghosts[0].damage == true){
							//ghost1Phase = "attack";
							//System.out.println("atk");
						}
						
						if(	realWorld.ghosts[0].tracking == true){
							if(input.substring(0, 7).equals(target)){
					    ghost = new Ghost(realWorld.ghosts[0].x, realWorld.ghosts[0].z,realWorld.diggas[Integer.parseInt("" + input.charAt(6))].x
								, realWorld.diggas[Integer.parseInt("" + input.charAt(6))].z,ghost1Phase,direction,remain);
							}
						}else{
							 ghost = new Ghost(realWorld.ghosts[0].x, realWorld.ghosts[0].z,realWorld.diggas[Integer.parseInt("" + input.charAt(6))].x
										, realWorld.diggas[Integer.parseInt("" + input.charAt(6))].z,ghost1Phase,direction,remain);
							 if(ghost.changedirection == true){
								 direction = ghost.newdirection;
								 remain = ghost.newremain;
							 }
						}
					    if(ghost.attack == true){
					    	realWorld.ghosts[0].normal = false;
					    	realWorld.ghosts[0].tracking  = false;
					    	realWorld.ghosts[0].damage = true;
					    	if(input.substring(0, 7).equals("Player0") ||input.substring(0, 7).equals("Player1") ){
					    	realWorld.diggas[Integer.parseInt("" + input.charAt(6))].x = -35;
							realWorld.diggas[Integer.parseInt("" + input.charAt(6))].z = -35;
							realWorld.diggas[Integer.parseInt("" + input.charAt(6))].rotation = xzr[2];
							if(ScoreboardPanel.player1Score >= 1){
							ScoreboardPanel.player1Score -= 1;
							}
					    	}else if(input.substring(0, 7).equals("Player2") || input.substring(0, 7).equals("Player3")){
						    	realWorld.diggas[Integer.parseInt("" + input.charAt(6))].x = -35;
								realWorld.diggas[Integer.parseInt("" + input.charAt(6))].z = 35;
								realWorld.diggas[Integer.parseInt("" + input.charAt(6))].rotation = xzr[2];

								if(ScoreboardPanel.player2Score >= 1){
									ScoreboardPanel.player2Score -= 1;
									}
						    	}
					    }else if(ghost.normal == true){
					    	realWorld.ghosts[0].normal = true;
					    	realWorld.ghosts[0].tracking  = false;
					    	realWorld.ghosts[0].damage = false;
					    }else if(ghost.tracking == true){
					    	realWorld.ghosts[0].normal = false;
					    	realWorld.ghosts[0].tracking  = true;
					    	realWorld.ghosts[0].damage = false;
					    	target = input.substring(0, 7);
					    }
						realWorld.ghosts[0].x = ghost.newX;
						realWorld.ghosts[0].z = ghost.newY;
						//Ghost type 1 Hard Code done
						
						
						//Ghost type 2 Hard Code start
						if(realWorld.ghosts[1].normal == true){
							ghost2Phase = "normal";
							//System.out.println("normal");
						}else if(realWorld.ghosts[1].tracking == true){
							ghost2Phase = "tracking";
							//System.out.println("tracking");
						}else if(realWorld.ghosts[1].damage == true){
							ghost2Phase = "attack";
							//System.out.println("atk");
						}
						
						if(	realWorld.ghosts[1].tracking == true){
							if(input.substring(0, 7).equals(target2)){
					    ghost2 = new Ghost2(realWorld.ghosts[1].x, realWorld.ghosts[1].z,realWorld.diggas[Integer.parseInt("" + input.charAt(6))].x
								, realWorld.diggas[Integer.parseInt("" + input.charAt(6))].z,ghost2Phase,trackCount,direction2,remain2);
							}
							trackCount = ghost2.trackcount;
						}else{
							 ghost2 = new Ghost2(realWorld.ghosts[1].x, realWorld.ghosts[1].z,realWorld.diggas[Integer.parseInt("" + input.charAt(6))].x
										, realWorld.diggas[Integer.parseInt("" + input.charAt(6))].z,ghost2Phase,trackCount,direction2,remain2);
							 if(ghost.changedirection == true){
								 direction2 = ghost2.newdirection;
								 remain2 = ghost2.newremain;
							 }
						}
					    if(ghost2.attack == true){
					    	realWorld.ghosts[1].normal = false;
					    	realWorld.ghosts[1].tracking  = false;
					    	realWorld.ghosts[1].damage = true;
					    	if(input.substring(0, 7).equals("Player0") || input.substring(0, 7).equals("Player1") ){
						    	realWorld.diggas[Integer.parseInt("" + input.charAt(6))].x = -35;
								realWorld.diggas[Integer.parseInt("" + input.charAt(6))].z = -35;
								realWorld.diggas[Integer.parseInt("" + input.charAt(6))].rotation = xzr[2];
								if(ScoreboardPanel.player1Score >= 1){
									ScoreboardPanel.player1Score -= 1;
									}
						    	}else if(input.substring(0, 7).equals("Player2") || input.substring(0, 7).equals("Player3")){
							    	realWorld.diggas[Integer.parseInt("" + input.charAt(6))].x = -35;
									realWorld.diggas[Integer.parseInt("" + input.charAt(6))].z = 35;
									realWorld.diggas[Integer.parseInt("" + input.charAt(6))].rotation = xzr[2];
									if(ScoreboardPanel.player2Score >= 1){
										ScoreboardPanel.player2Score -= 1;
										}
							    	}
					    }else if(ghost2.normal == true){
					    	realWorld.ghosts[1].normal = true;
					    	realWorld.ghosts[1].tracking  = false;
					    	realWorld.ghosts[1].damage = false;
					    }else if(ghost2.tracking == true){
					    	realWorld.ghosts[1].normal = false;
					    	realWorld.ghosts[1].tracking  = true;
					    	realWorld.ghosts[1].damage = false;
					    	target2 = input.substring(0, 7);
					    	trackCount = 0;
					    }
						realWorld.ghosts[1].x = ghost2.newX;
						realWorld.ghosts[1].z = ghost2.newY;
						//Ghost type 2 Hard Code Done*/
					}
					else if(input.startsWith("Grave"))
					{
						//System.out.println(realWorld.graves[Integer.parseInt(input.substring(5,6))].digState);
						
						realWorld.graves[Integer.parseInt(input.substring(5,6))].digState = -1;
						
						Scanner scan = new Scanner(input);
						scan.next();
						scan.next();
						String compare = scan.next();
						if(compare.equals("Player0") || compare.equals("Player1")){
							ScoreboardPanel.player1Score += 1;
						}else if(compare.equals("Player2") || compare.equals("Player3")){
							ScoreboardPanel.player2Score += 1;
						}
						
						scan.close();
					
					
				
					}
					for (PrintWriter writer : writers)
					{
						if(input.substring(0, 4).equals("CHAT"))
						{
							writer.println("MESSAGE "  + input.substring(4, input.length()));
						}
					}
					
					
				}
			}
			catch (IOException e)
			{
				System.out.println(e);
			}
			finally
			{
				// This client is going down! Remove its name and its print
				// writer from the sets, and close its socket.
				if (name != null)
				{
					names.remove(name);
				}
				if (out != null)
				{
					writers.remove(out);
				}
				try
				{
					socket.close();
				}
				catch (IOException e)
				{
				}
			}
		}
	}
}
