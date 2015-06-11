package Render.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class PlayerDead {
	private int x;
	private int y;
	private int xmap;
	private int ymap;
	
	private int width;
	private int height;
	
	private Animation animation;
	private BufferedImage[] sprites;
	
	private boolean remove;
	
	public PlayerDead(int x, int y) {
		
		this.x = x;
		this.y = y;
		
		width = 70;
		height = 80;
		
		try {		
			BufferedImage spritesheet = ImageIO.read( getClass().getResourceAsStream(
					"/Sprites/Player/player_dead.png"));
			
			sprites = new BufferedImage[10];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(40);
		
	}
	
	public void update() {
		animation.update();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setMapPosition(int x, int y) {
		xmap = x;
		ymap = y;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(animation.getImage(), x + xmap - width / 2, y + ymap - height / 2, null);
	}
}
