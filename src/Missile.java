import java.awt.*;
import java.util.List;

public class Missile {
	public static final int XSPEED=10;
	public static final int YSPEED=10;
	public static final int WIDTH=10;
	public static final int HEIGHT=10;
	
	TankClient tc;
	
	int x,y;
	Tank.Direction dir;
	
	private boolean live=true;
	private boolean good;
	
	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isLive() {
		return live;
	}

	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x,int y,boolean good,Tank.Direction dir,TankClient tc){
		this(x,y,dir);
		this.good=good;
		this.tc=tc;
	}
	
	public void draw(Graphics g){
		if(!live){
			tc.missiles.remove(this);
			return;
			}
		Color c=g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		move();
	}

	private void move() {
		switch (dir){
		case L:
			x-=XSPEED;
			break;
		case UL:
			x-=XSPEED/5*4;
			y-=YSPEED/5*4;
			break;
		case R:
			x+=XSPEED;
			break;
		case UR:
			x+=XSPEED/5*4;
			y-=YSPEED/5*4;
			break;
		case U:
			y-=YSPEED;
			break;
		case DL:
			x-=XSPEED/5*4;
			y+=YSPEED/5*4;
			break;
		case D:
			y+=YSPEED;
			break;
		case DR:
			x+=XSPEED/5*4;
			y+=YSPEED/5*4;
			break;
		case STOP:
			break;
		}
		if (x<0||x>TankClient.GAME_WIDTH||y<0||y>TankClient.GAME_HEIGHT){
			live=false;
			Explode e=new Explode(x,y,tc);
			tc.explodes.add(e);
		}
	}
	
	public boolean hitTank(Tank t){
		if(this.getsect().intersects(t.getsect())&&t.isLive()&&this.live&&this.good!=t.isGood()) {
			this.setLive(false);
			if(!t.isRobot()){
				if(t.getLife()<=0){
					t.setLive(false);
				}
				else{
					t.setLife(t.getLife()-20);
					if(t.getLife()<=0){
						t.setLive(false);
					}
				}
			}
			else
			t.setLive(false);
			Explode e=new Explode(x,y,tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks){
		for(int i=0;i<tanks.size();i++){
			if(hitTank(tanks.get(i))){
				return true;
			}
		}
		return false;
	}
	
	public Rectangle getsect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	public boolean hitWall(Wall w){
		if(this.getsect().intersects(w.getRect())&&this.live){
			this.live=false;
			Explode e=new Explode(x,y,tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitMissile(Missile m){
		if(this.getsect().intersects(m.getsect())&&this.live&&m.live&&this.good!=m.good){
			this.live=false;
			m.live=false;
			Explode e=new Explode(x,y,tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitMissiles(List<Missile> missiles){
		for(int i=0;i<missiles.size();i++){
			if(this.hitMissile(missiles.get(i))){
				return true;
			}
		}
		return false;
	}
	
}
