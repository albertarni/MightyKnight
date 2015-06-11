package Main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class GamePanel extends JPanel{

	//demensions
	public static final int WIDTH = 960;
	public static final int HEIGHT = 540;
	public static final int SCALE = 1;
	
	private BufferedImage image;
	private Graphics2D g;
	Level1 level1 = new Level1();

	KeyListener listener = new MyKeyListener(level1);
	
	public GamePanel(){
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
		
		image = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		addKeyListener(listener);
	}
	
	public void update(){
		level1.update();
	}
	
	public void draw(){
		level1.draw(g);
	}
	
	public void drawToScreen(){
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}
}
