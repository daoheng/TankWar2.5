import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class TankClient extends Frame {

	public static final int GAME_WIDTH=800;
	public static final int GAME_HEIGHT=600;
	
	Tank myTank=new Tank(385,500,true,Tank.Direction.STOP,false,this);
	//Tank robot=new Tank(100,100,false,this);
	Wall w1=new Wall(50,290,250,20,this);
	Wall w2=new Wall(500,290,250,20,this);
	List<Missile> missiles=new ArrayList<Missile>();
	List<Explode> explodes=new ArrayList<Explode>();
	List<Tank> tanks=new ArrayList<Tank>();
	public int enemynum;
	

	Image OffScreenImage =null;
	
	public void paint(Graphics g) {
		g.drawString("missiles count:"+missiles.size(), 10, 40);
		g.drawString("explodes count:"+explodes.size(), 10, 55);
		g.drawString("friends tanks count:"+(tanks.size()-enemynum), 10, 85);
		g.drawString("tanks life count:"+myTank.getLife(),10,70);
		g.drawString("enemy tanks count:"+enemynum,10,100);
		g.drawString("方向键上下左右控制，z射击，x大招",10,580);
		
		if(!myTank.isLive()) g.drawString("haha~~~~you die,in chinese 胜败乃兵家常事，大侠请重新来过", 250, 280);
		if(enemynum==0) g.drawString("我军获胜！！！congradulations!!!!!  you win!!!!  why are you so awesome?!?!?!?!  我操！！！ 你竟然打光了敌军坦克，你为什么这么吊？？",60,230);
		if(!myTank.isLive()&&(tanks.size()-enemynum)==0)
			g.drawString("you lost!!!!  do you know what lost means??? it means you have been deteated totally!!!! you lost anything!!!"
					+ "我军全军覆没了，全体阵亡无一生还！",15,330);
		
			
		for(int i=0;i<missiles.size();i++){
			Missile m=missiles.get(i);
			//m.hitTank(robot);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.hitMissiles(missiles);
			m.draw(g);
		}
		for(int i=0;i<explodes.size();i++){
			Explode e=explodes.get(i);
			e.draw(g);
		}
		enemynum=0;
		for(int i=0;i<tanks.size();i++){
			
			Tank t=tanks.get(i);
			t.hitWall(w1);
			t.hitWall(w2);
			t.hitTanks(tanks);
			t.draw(g);
			if(!t.isGood())
				enemynum++;
		}
		
		myTank.draw(g);
		myTank.hitWall(w1);
		myTank.hitWall(w2);
		w1.draw(g);
		w2.draw(g);
		//robot.draw(g);
	}

	public void update(Graphics g) {
		if(OffScreenImage==null){
			OffScreenImage=this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics gOffScreen = OffScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH,GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(OffScreenImage, 0, 0, null);
	}
	
	public void lauchFrame(){
		for(int i=0;i<20;i++){
			tanks.add(new Tank(35*(i+1),50,false,Tank.Direction.D,true,this));
			}
		for(int i=0;i<20;i++){
			tanks.add(new Tank(35*(i+1),100,false,Tank.Direction.D,true,this));
			}
		for(int i=0;i<20;i++){
			tanks.add(new Tank(35*(i+1),150,false,Tank.Direction.D,true,this));
			}
		for(int i=0;i<20;i++){
			tanks.add(new Tank(35*(i+1),200,false,Tank.Direction.D,true,this));
			}
		for(int i=0;i<20;i++){
			tanks.add(new Tank(35*(i+1),250,false,Tank.Direction.D,true,this));
			}
		for(int i=0;i<20;i++){
			tanks.add(new Tank(35*(i+1),0,false,Tank.Direction.D,true,this));
			}
		for(int i=0;i<10;i++){
			tanks.add(new Tank(70*(i+1),580,true,Tank.Direction.U,true,this));
		}
		for(int i=0;i<10;i++){
			tanks.add(new Tank(70*(i+1),520,true,Tank.Direction.U,true,this));
		}
		for(int i=0;i<10;i++){
			tanks.add(new Tank(70*(i+1),460,true,Tank.Direction.U,true,this));
		}
		for(int i=0;i<10;i++){
			tanks.add(new Tank(70*(i+1),400,true,Tank.Direction.U,true,this));
		}
		for(int i=0;i<10;i++){
			tanks.add(new Tank(70*(i+1),350,true,Tank.Direction.U,true,this));
		}
		this.setLocation(300, 100);
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.setTitle("TankWar1-燕羊羊坦克大战");
		this.addWindowListener(new WindowAdapter (){
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		
		this.addKeyListener(new KeyMonitor());
		setVisible(true);
		
		new Thread(new PaintThread()).start();
	}
	
	public static void main(String[] args) {
		
		TankClient tc = new TankClient ();
		tc.lauchFrame();
	}
	
	private class PaintThread implements Runnable{
		public void run() {
			while(true){
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
	}

	private class KeyMonitor extends KeyAdapter{
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}

	}
	
	public void keyReleased(KeyEvent e){
		int key= e.getKeyCode();
		switch (key){
		case KeyEvent.VK_ESCAPE:
		System.exit(0);
		break;
		}
	}
}
