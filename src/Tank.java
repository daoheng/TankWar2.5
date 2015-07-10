import java.awt.*;
import java.util.*;
import java.util.List;
import java.awt.event.*;

public class Tank {
	public static final int XSPEED=5;
	public static final int YSPEED=5;
	public static final int WIDTH=30;
	public static final int HEIGHT=30;

	TankClient tc;
	private boolean good;
	private int oldX, oldY;
	private int life=100;

	BloodBar bb=new BloodBar();
	
	private boolean robot;
	
	public boolean isRobot() {
		return robot;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	private static Random rm= new Random();
	
	private int ptx,pty;
	private int x,y;
	private boolean bL=false,bR=false,bU=false,bD=false;
	enum Direction{L,UL,R,UR,U,DR,D,DL,STOP};
	
	private Direction dir=Direction.STOP;
	private Direction ptdir=Direction.D;
	public boolean live=true;
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good=good;
	}
	
	public Tank(int x,int y, boolean good, Direction dir,boolean robot,TankClient tc){
		this(x,y,good);
		this.dir=dir;
		this.robot=robot;
		this.tc=tc;
	}
	
	public int step=rm.nextInt(15)+5;
	
	public void draw(Graphics g){
		if(this.live==false){
			if(robot){
				tc.tanks.remove(this);
				return;
			}
			return;
		}
		Color c=g.getColor();
		if(good==true)
		g.setColor(Color.RED);
		else{
			g.setColor(Color.BLUE);
		}
			
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		ptlocation();
		g.drawLine(x+WIDTH/2, y+HEIGHT/2, ptx, pty);
		if(!robot)
		bb.draw(g);
		move();
	}
	
	void ptlocation(){
		if(robot){
			while(dir==Tank.Direction.STOP){
				Direction[] dirs=Direction.values();
				int rn=rm.nextInt(dirs.length);
				dir=dirs[rn];
			}
			ptdir=dir;
		}
		switch (ptdir){
		case L:
			ptx=x;
			pty=y+HEIGHT/2;
			break;
		case UL:
			ptx=x;
			pty=y;
			break;
		case R:
			ptx=x+WIDTH;
			pty=y+HEIGHT/2;
			break;
		case UR:
			ptx=x+WIDTH;
			pty=y;
			break;
		case U:
			ptx=x+WIDTH/2;
			pty=y;
			break;
		case DL:
			ptx=x;
			pty=y+HEIGHT;
			break;
		case D:
			ptx=x+WIDTH/2;
			pty=y+HEIGHT;
			break;
		case DR:
			ptx=x+WIDTH;
			pty=y+HEIGHT;
			break;
		}
	}
	
	void move(){
		oldX=x;oldY=y;
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
		if(x<0) x=0;
		if(y<20) y=20;
		if(x+Tank.WIDTH>TankClient.GAME_WIDTH) x=TankClient.GAME_WIDTH-Tank.WIDTH;
		if(y+Tank.HEIGHT>TankClient.GAME_HEIGHT) y=TankClient.GAME_HEIGHT-Tank.HEIGHT;
		
		if(robot){
			if(!good){
				Direction[] dirs=Direction.values();
				if(step==0){
					step=rm.nextInt(25)+5;
					int rn=rm.nextInt(dirs.length);
					dir=dirs[rn];
				}
				step--;
				if(rm.nextInt(40)>36) this.fire();
			}
			else{
				Direction[] dirs=Direction.values();
				if(step==0){
					step=rm.nextInt(25)+5;
					int rn=rm.nextInt(dirs.length);
					dir=dirs[rn];
				}
				step--;
				if(rm.nextInt(40)>29) this.fire();
			}
		}
	}
	
	public void keyPressed(KeyEvent e){
		int key= e.getKeyCode();
		switch (key){
		
		case KeyEvent.VK_LEFT:
			bL=true;
			break;
		case KeyEvent.VK_RIGHT:
			bR=true;
			break;
		case KeyEvent.VK_UP:
			bU=true;
			break;
		case KeyEvent.VK_DOWN:
			bD=true;
			break;
		}
		locateDirection();
	}

	private Missile fire() {
		if(!live) return null;
		int x=this.x+WIDTH/2-Missile.WIDTH/2;
		int y=this.y+HEIGHT/2-Missile.HEIGHT/2;
		Missile m=new Missile(x,y,good,ptdir,tc);
		tc.missiles.add(m);
		return m;
	}
	
	private Missile fire(Direction dir) {
		if(!live) return null;
		int x=this.x+WIDTH/2-Missile.WIDTH/2;
		int y=this.y+HEIGHT/2-Missile.HEIGHT/2;
		Missile m=new Missile(x,y,good,dir,tc);
		tc.missiles.add(m);
		return m;
	}

	void locateDirection(){
		if(bL&&!bR&&!bU&&!bD) ptdir=dir=Direction.L;
		else if(bL&&!bR&&bU&&!bD) ptdir=dir=Direction.UL;
		else if(!bL&&bR&&!bU&&!bD) ptdir=dir=Direction.R;
		else if(!bL&&bR&&bU&&!bD) ptdir=dir=Direction.UR;
		else if(!bL&&!bR&&bU&&!bD) ptdir=dir=Direction.U;
		else if(!bL&&bR&&!bU&&bD) ptdir=dir=Direction.DR;
		else if(!bL&&!bR&&!bU&&bD) ptdir=dir=Direction.D;
		else if(bL&&!bR&&!bU&&bD) ptdir=dir=Direction.DL;
		else if(!bL&&!bR&&!bU&&!bD) dir=Direction.STOP;
	}

	public void keyReleased(KeyEvent e) {
		int key= e.getKeyCode();
		switch (key){
		case KeyEvent.VK_Z:
			fire();
			break;
		case KeyEvent.VK_LEFT:
			bL=false;
			break;
		case KeyEvent.VK_RIGHT:
			bR=false;
			break;
		case KeyEvent.VK_UP:
			bU=false;
			break;
		case KeyEvent.VK_DOWN:
			bD=false;
			break;
		case KeyEvent.VK_X:
			superfire();
			break;
		}
		locateDirection();
	}
	
	public Rectangle getsect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	public boolean hitWall(Wall w){
		if(this.getsect().intersects(w.getRect())&&this.live){
			this.stay();
			return true;
		}
		return false;
	}
	
	public void stay(){
		x=oldX;y=oldY;
	}
	
	public void hitTanks(List<Tank> Tanks){
		for(int i=0;i<Tanks.size();i++){
			if(this!=Tanks.get(i)){
				if(this.getsect().intersects(Tanks.get(i).getsect())&&this.live&&Tanks.get(i).live){
					this.stay();
					Tanks.get(i).stay();
				}
			}
		}
	}
	
	public void superfire(){
		Direction[] dirs=Direction.values();
		for(int i=0;i<8;i++){
			fire(dirs[i]);
		}
	}
	
	private class BloodBar{
		public void draw(Graphics g){
			Color c=g.getColor();
			g.setColor(Color.BLACK);
			g.drawRect(x, y-11, WIDTH, 10);
			int v=life*30/100;
			g.setColor(Color.MAGENTA);
			g.fillRect(x, y-11, v, 10);
			g.setColor(c);
		}
	}
	
}
