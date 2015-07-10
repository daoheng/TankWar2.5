import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Wall {
	private int x,y,m,n;
	TankClient tc;
	
	public Wall(int x, int y, int m, int n, TankClient tc) {
		this.x = x;
		this.y = y;
		this.m = m;
		this.n = n;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		Color c=g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(x, y, m, n);
		g.setColor(c);
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,m,n);
	}
}
