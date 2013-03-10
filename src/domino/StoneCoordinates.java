package domino;

public class StoneCoordinates {

	private int posx;
	private int posy;
	private int width;
	private int height;
	private boolean direction;
	private boolean isVertical;
	
	public StoneCoordinates(){

	}
	
	public StoneCoordinates(int posx, int posy, boolean isVertical, boolean direction){
		this.posx = posx;
		this.posy = posy;
		this.isVertical = isVertical;
		this.direction = direction;
	}
	
	public void setCoordinates(int posx, int posy){
		this.posx = posx;
		this.posy = posy;
	}
	
	public void setParametrs(int posx, int posy, int width, int height){
		this.posx = posx;
		this.posy = posy;
		this.width = width;
		this.height = height;
	}
	
	public void setParametrs(int posx, int posy, boolean isVertical){
		this.posx = posx;
		this.posy = posy;
		this.isVertical = isVertical;
	}
	
	public void setParametrs(int width, int height, boolean isVertical, boolean direction){
		this.posx = width;
		this.posy = height;
		this.isVertical = isVertical;
		this.direction = direction;
	}
	
	public void setParametrs(int posx, int posy, int width, int height, boolean isVertical, boolean direction){
		this.posx = posx;
		this.posy = posy;
		this.width = width;
		this.height = height;
		this.isVertical = isVertical;
		this.direction = direction;
	}
	
	public int getX(){
		return this.posx;
	}
	
	public int getY(){
		return this.posy;
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	public boolean getIsVertical(){
		return this.isVertical;
	}
	
	public boolean getDirection(){
		return this.direction;
	}
}
