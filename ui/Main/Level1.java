package Main;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Render.Entity.*;
import Render.MapBuilder.*;

public class Level1 {

	private TileMap tileMap;
	private Background bg;

	public Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<EnemyDead> deads;
	private ArrayList<PlayerDead> playerDead;

	private int deadTimer = 0;

	public Level1() {
		tileMap = new TileMap(60);
		tileMap.loadeTiles("/Tilesets/tile_map_2.png");
		tileMap.loadMap("/Maps/map5.map");
		tileMap.setPosition(0, 0);

		bg = new Background("/Backgrounds/background.png", 0.4);

		player = new Player(tileMap);
		player.setPosition(100, 300);

		populateEnemies();
		deads = new ArrayList<EnemyDead>();
		playerDead = new ArrayList<PlayerDead>();
	}

	private void populateEnemies() {
		enemies = new ArrayList<Enemy>();

		Skeleton s;
		Point[] points = new Point[] { new Point(400, 400), new Point(100, 150), };
		for (int i = 0; i < points.length; i++) {
			s = new Skeleton(tileMap, player);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
	}

	public void update() {
		bg.setPosition(tileMap.getX(), tileMap.getY());
		
		int playerX = 0;
		int playerY = 0;
		
		if (player.isDead()) {
			deadTimer++;
			if (deadTimer == 30) {
				System.out.println("dead");
				
				playerX = player.getX();
				playerY = player.getY();
				
				player.setPosition(-100, -100);
			}
			
			if (deadTimer >= 120) {
				player.setDead();
				player = new Player(tileMap);
				player.setPosition(100, 300);
				populateEnemies();
				deadTimer = 0;
			}
		}
	

		player.update();
		player.checkAttack(enemies);

		// update all enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if (e.isDead()) {
				enemies.remove(i);
				i--;
				deads.add(new EnemyDead(e.getX(), e.getY()));
			}
		}

		// update enemy deads
		for (int i = 0; i < deads.size(); i++) {
			deads.get(i).update();
			if (deads.get(i).shouldRemove()) {
				deads.remove(i);
				i--;
			}
		}

		// update player dead
		for (int i = 0; i < playerDead.size(); i++) {
			playerDead.get(i);
			if (playerDead.get(i).shouldRemove()) {
				playerDead.remove(i);
				i--;
			}
		}
	}

	public void draw(Graphics2D g) {
		// draw bg
		bg.draw(g);

		// draw tilemap
		tileMap.draw(g);

		// draw player
		player.draw(g);

		// draw enemies
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}

		// draw enemy deads
		for (int i = 0; i < deads.size(); i++) {
			deads.get(i).setMapPosition((int) tileMap.getX(),
					(int) tileMap.getY());
			deads.get(i).draw(g);
		}

		// draw player dead
		for (int i = 0; i < playerDead.size(); i++) {
			playerDead.get(i).setMapPosition((int) tileMap.getX(),
					(int) tileMap.getY());
			playerDead.get(i).draw(g);
		}

	}

	public void keyPressed(int k) {
		if (k == KeyEvent.VK_LEFT)
			player.setLeft(true);
		if (k == KeyEvent.VK_RIGHT)
			player.setRight(true);
		if (k == KeyEvent.VK_UP)
			player.setUp(true);
		if (k == KeyEvent.VK_DOWN)
			player.setDown(true);
		if (k == KeyEvent.VK_SPACE)
			player.setJumping(true);
		if (k == KeyEvent.VK_CONTROL)
			player.setAttacking();
	}

	public void keyReleased(int k) {
		if (k == KeyEvent.VK_LEFT)
			player.setLeft(false);
		if (k == KeyEvent.VK_RIGHT)
			player.setRight(false);
		if (k == KeyEvent.VK_UP)
			player.setUp(false);
		if (k == KeyEvent.VK_DOWN)
			player.setDown(false);
		if (k == KeyEvent.VK_SPACE)
			player.setJumping(false);
		if (k == KeyEvent.VK_CONTROL)
			player.setAttacking(false);
	}
}
