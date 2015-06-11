package Render.MapBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Map {
	
	private int[][] map;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	private int tileSize;
	
	public Map(){
	}
	
	public int getNumRows(){
		return numRows;
	}
	
	public int getNumCols(){
		return numCols;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getMapElement(int row, int col){
		return map[row][col];
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	
	public void setHeight(int height){
		this.height = height;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}
	
	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}
	
	public void loadMap(String s){
		try{
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			String delims = ",";
			for(int row = 0; row < numRows; row++){
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++){
					int k = Integer.parseInt(tokens[col]);
					if(k != 0)
						map[row][col] = k - 1;
					else
						map[row][col] = k;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	
}
