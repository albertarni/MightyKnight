package Render.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Render.MapBuilder.TileMap;

public class Player extends MapObject {

	private int health;
	private int maxHealth;
	private boolean dead;
	private int damage;
	private boolean flinching;
	private long flinchTimer;

	// animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = { 2, 10, 3, 1, 9 };

	// animation action
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int ATTACKING = 4;

	private boolean attacking;

	public Player(TileMap tm) {
		super(tm);

		/*
		 * width = 60; height = 60; cwidth = 50; cheight = 60;
		 */

		width = 57;
		height = 80;
		cwidth = 50;
		cheight = 80;

		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.14;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		health = maxHealth = 5;
		damage = 1;

		facingRight = true;

		loadSprites("/Sprites/Player/player_texture7.png");
	}

	public void loadSprites(String s) {
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass()
					.getResourceAsStream(s));

			sprites = new ArrayList<BufferedImage[]>();
			for (int i = 0; i < 5; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for (int j = 0; j < numFrames[i]; j++) {
					if (i == IDLE)
						bi[j] = spriteSheet.getSubimage(j * width, i * height,
								width, height);
					else if (i == ATTACKING)
						bi[j] = spriteSheet.getSubimage(j * 77, i * height, 77,
								height);
					else
						bi[j] = spriteSheet.getSubimage(j * 62, i * height, 62,
								height);
				}
				sprites.add(bi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
	}

	public void checkAttack(ArrayList<Enemy> enemies) {
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if (intersects(e)) {
				if (attacking) {
					e.hit(damage);
				}

				if (e.getAttackTimer()) {
					hit(e.getDamage());
				}
			}
		}
	}

	public void hit(int damage) {
		if (flinching)
			return;
		health -= damage;
		if (health < 0)
			health = 0;
		if (health == 0)
			dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead() {
		dead = false;
		health = maxHealth = 5;
	}
	

	public void setAttacking() {
		attacking = true;
	}

	public void getNextPosition() {
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
		} else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) {
					dx = 0;
				}
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0) {
					dx = 0;
				}
			}
		}

		if ((currentAction == ATTACKING) && !(jumping || falling)) {
			dx = 0;
		}

		// jumping
		if (jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		// falling
		if (falling) {
			// if(dy > 0 && gliding) dy += fallSpeed * 0.1;
			// else
			dy += fallSpeed;

			if (dy > 0)
				jumping = false;
			if (dy < 0 && !jumping)
				dy += stopJumpSpeed;

			if (dy > maxFallSpeed)
				dy = maxFallSpeed;
		}
	}

	public void update() {

		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		// check done flinching
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 1000) {
				flinching = false;
			}
		}

		// check attack has stopped
		if (currentAction == ATTACKING) {
			if (animation.hasPlayedOnce())
				attacking = false;
		}

		if (attacking) {
			if (currentAction != ATTACKING) {
				currentAction = ATTACKING;
				animation.setFrames(sprites.get(ATTACKING));
				animation.setDelay(50);
				width = 77;
			}
		}

		else if (dy > 0) {
			if (currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 62;
			}
		} else if (dy < 0) {
			if (currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(200);
				width = 62;
			}
		} else if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(60);
				width = 62;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 57;
			}
		}

		animation.update();

		// set direction
		if (currentAction != ATTACKING) {
			if (right)
				facingRight = true;
			if (left)
				facingRight = false;
		}
	}

	public void draw(Graphics2D g) {

		setMapPosition();

		// draw player
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed / 100 % 2 == 0) {
				return;
			}
		}

		// draw player
		super.draw(g);

	}

}
