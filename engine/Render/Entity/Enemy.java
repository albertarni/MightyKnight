package Render.Entity;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Render.Entity.MapObject;
import Render.MapBuilder.*;

public class Enemy extends MapObject{
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected int attackTimer;
	
	protected static final int WALKING = 0;
	protected static final int ATTACKING = 1;
	protected static final int DEAD = 2;
	
	protected ArrayList<BufferedImage[]> sprites;

	
	public Enemy(TileMap tm){
		super(tm);
	}
	
	public boolean isDead() { return dead; }
	
	public int getDamage() { return damage; }
	
	public boolean getAttackTimer(){ 
		if(attackTimer == 60) return true;
		else return false;
	}
	
	public void hit(int damage) {
		if(dead) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
	}
	
	public void update() {}
}
