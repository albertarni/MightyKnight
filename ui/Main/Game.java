package Main;


import javax.swing.JFrame;


public class Game implements Runnable{
	private JFrame window;
	private GamePanel gamePanel;
	
	//game thread
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	public Game(){
		window = new JFrame("Game");
		gamePanel = new GamePanel();
		window.setContentPane(gamePanel);
		//window.add(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
	}
	
	public synchronized void start(){
		running = true;
		new Thread(this).start();
	}
	
	public synchronized void stop(){
		running = false;
	}
	
	public void run(){
		long start, elapsed, wait;
		
		while(running){
			start = System.nanoTime();
			
			gamePanel.update();
			gamePanel.draw();
			gamePanel.drawToScreen();
			
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			if(wait < 0){
				wait = 5;
			}
			try{
				Thread.sleep(wait);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}		
	}
}
