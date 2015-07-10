import java.awt.*;


public class Explode {

	int x, y;
	private boolean live=true;
	
	int[] diameter={4,8,12,16,20,25,31,38,45,55,60,58,52,42,30,15,5};
	int step=0;
	
	private TankClient tc;
	
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!live){
			tc.explodes.remove(this);
			return;
		}
		if(step==diameter.length){
			step=0;
			this.live=false;
			return;
		}	
		
		Color c=g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		step++;
	}
}
