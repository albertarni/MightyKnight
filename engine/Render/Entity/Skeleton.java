package Render.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Render.MapBuilder.*;

public class Skeleton extends Enemy {

	
	private final int[] numFrames = { 12, 9 };


	public Player player;

	public boolean attacking;

	public Skeleton(TileMap tm, Player player) {
		super(tm);
		this.player = player;

		moveSpeed = 0.3;
		maxSpeed = 0.8;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;

		width = 62;
		height = 80;
		cwidth = 50;
		cheight = 80;

		health = maxHealth = 2;
		damage = 3;

		right = true;
		facingRight = true;

		loadSprites("/Sprites/Enemies/skeleton1_texture2.png");
	}

	private void loadSprites(String s) {
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(getClass()
					.getResourceAsStream(s));

			sprites = new ArrayList<BufferedImage[]>();
			for (int i = 0; i < 2; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for (int j = 0; j < numFrames[i]; j++) {
					if (i == WALKING)
						bi[j] = spritesheet.getSubimage(j * width, i * height,
								width, height);
					if (i == ATTACKING)
						bi[j] = spritesheet.getSubimage(j * 77, i * height, 77,
								height);
				}
				sprites.add(bi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites.get(WALKING));
		currentAction = WALKING;
		animation.setDelay(60);
	}

	public void checkAttack(Player player) {
		
		if(intersects(player) && attackTimer <= 10){
			attackTimer++;
		}
		
		else if (attackTimer >= 11 && attackTimer <= 70) {
			attackTimer++;
			setAttacking();
			
			if(facingRight){
				right = false;
				left = true;
			}
			else{
				right = true;
				left = false;
			}
		} 	
		else if (intersects(player) && attackTimer >= 71 && attackTimer <= 250) {
			setAttacking(false);
			attackTimer++;
		} else {
			attackTimer = 0;
		}
	}

	public void setAttacking() {
		attacking = true;
	}
	

	private void getNextPosition() {

		// movement
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		}

		if (currentAction == ATTACKING) {
			dx = 0;
		}

		// falling
		if (falling) {
			dy += fallSpeed;
		}

	}

	public void update() {

		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		checkAttack(player);
		
		
		if (currentAction == ATTACKING) {
			if (animation.hasPlayedOnce()){
				attacking = false;
			}
		}

		if (attacking) {
			if (currentAction != ATTACKING) {
				currentAction = ATTACKING;
				animation.setFrames(sprites.get(ATTACKING));
				animation.setDelay(100);
				width = 77;
			}
		} else if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(60);
				width = 62;
			}
		}

		
		// if it hits a wall, go other direction
		if (right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}

		else if (left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}

		// update animation
		animation.update();

	}

	public void draw(Graphics2D g) {

		// if(notOnScreen()) return;

		setMapPosition();
		super.draw(g);
	}

}
