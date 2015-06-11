package Render.MapBuilder;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class TileMap {
	
	//position
	private double x;
	private double y;
	
	//bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;

	private double tween;
	
	//map
	private Map map;
	private int tileSize;
	
	//tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	//drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	public TileMap(int tileSize){
		map = new Map();
		
		this.tileSize = tileSize;
		map.setTileSize(tileSize);
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
	}
	
	public int getX() {
		return (int)x;
	}


	public int getY() {
		return (int)y;
	}
	
	public int getNumRows() { return map.getNumRows(); }
	public int getNumCols() { return map.getNumCols(); }


	public int getTileSize() {
		return tileSize;
	}
	
	public int getType(int row, int col){
		int rc = map.getMapElement(row, col);
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	
	public void setPosition(double x, double y){
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
		fixBounds();
		
		colOffset = (int) - this.x / tileSize;
		rowOffset = (int) - this.y / tileSize;
	}
	
	private void fixBounds(){
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	public void draw(Graphics2D g){
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++){
			if(row >= map.getNumRows()) break;
			for(int col = colOffset; col < colOffset + numColsToDraw; col++){
				if(col >= map.getNumCols()) break;
				
				if(map.getMapElement(row, col) == 0) continue;
				
				 int rc = map.getMapElement(row, col);
				 int r = rc / numTilesAcross;
				 int c = rc % numTilesAcross;
				 
				 g.drawImage(tiles[r][c].getImage(),(int)x + col * tileSize,(int)y + row * tileSize,null);
				 
			}
		}
	}

	public void loadeTiles(String s){
		try{
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];
			
			BufferedImage subImage;
			for(int col=0; col<numTilesAcross; col++){
				subImage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subImage, Tile.NORMAL);
				
				subImage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tile(subImage, Tile.BLOCKED);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void loadMap(String s){
		map.loadMap(s);
	}
	
	public void printMap(){
		for(int i = 0;i < map.getHeight();i++){
			for(int j = 0;j < map.getWidth();j++){
				System.out.print(map.getMapElement(i, j));
			}
			System.out.println();
		}
	}

}
