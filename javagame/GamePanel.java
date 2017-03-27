package javagame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable,KeyListener{
	private int mScreenWidth = 480;
	private int mScreenHeight = 640;
	//游戏主菜单状态
	private static final int STATE_GAME = 0;
	//游戏状态
	private int mState = STATE_GAME;
	//初始化游戏背景资源
	private Image mBitMenuBG0 = null;
	private Image mBitMenuBG1 = null;
	//记录两张背景图片更新时的y坐标
	private int mBitposY0 = 0;
	private int mBitposY1 = 0;
	//子弹数量
	final static int BULLET_POOL_COUNT = 8;
	//飞机移动的速度
	final static int PLAN_STEP = 40;
	//每500ms发射一颗子弹
	final static int PLAN_TIME = 500;
	//敌人对象数量
	final static int ENEMY_POOL_COUNT = 5;
	//敌人飞机偏移量
	final static int ENEMY_POS_OFF = 65;
	//游戏主线程
	private Thread mThread = null ;
	//线程循环标志
	private boolean gameIsRunning = false;
	//飞机在屏幕中的坐标
	public int mAirPosX = 0;
	public int mAirPosY = 0;
	//敌机对象的数组
	Enemy mEnemy[] = null;
	//子弹对象数组
	Bullet mBullet[] = null ;
	//初始化发射子弹的ID
	public int mSendId = 0;
	//上一颗子弹发射的时间
	public long mSendTime = 0L;
	Image myPlanePic[];//玩家飞机的图片
	public int myPlaneID = 0;//玩家飞机当前的帧
	public static int score = 0;
	public GamePanel(){
		setPreferredSize(new Dimension(mScreenWidth,mScreenHeight));
		//设定焦点在本窗体并赋予监听对象
		setFocusable(true);
		addKeyListener(this);
		init();//后面会写  初始化方法
		setGameState(STATE_GAME);
		gameIsRunning = true ;
		mThread = new Thread(this);//实例化线程
		//启动游戏线程
		mThread.start();
		setVisible(true);
	}
	public void keyPressed(KeyEvent e) {
		// TODO 自动生成的方法存根
		int key = e.getKeyCode();
		System.out.println(key);
		if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP){mAirPosY -= PLAN_STEP;}//上方向键
		if(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN){mAirPosY += PLAN_STEP;}//下方向键
		if(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT){//左方向键
			mAirPosX -= PLAN_STEP;
			if(mAirPosX < 0)mAirPosX = 0;
		}
		if(key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT){
			mAirPosX += PLAN_STEP;
			if(mAirPosX > mScreenWidth - 60)mAirPosX = mScreenWidth - 60;
		}
		System.out.println(mAirPosX +","+mAirPosY);
	}
	
	public void run() {
		while(gameIsRunning){
			//刷新屏幕
			Draw();
			//延时0.1s
			try{Thread.sleep(50);}
			catch(InterruptedException e){e.printStackTrace();}
		}
	}
	private void init(){
		mBitMenuBG0 = Toolkit.getDefaultToolkit().getImage("image\\map_0.png");
		mBitMenuBG1 = Toolkit.getDefaultToolkit().getImage("image\\map_1.png");
		//第一张图片紧贴在屏幕（0,0）点处
		mBitposY0 = 0;
		mBitposY1 = -mScreenHeight;
		//初始化玩家飞机的坐标
		mAirPosX = 150;
		mAirPosY = 500;
		//初始化玩家飞机相关的六张图片
		myPlanePic = new Image[6];
		for(int i = 0;i<myPlanePic.length;i++){
			myPlanePic[i] = Toolkit.getDefaultToolkit().getImage("image\\plan_"+i+".png");
		}
		//创建敌机对象
		mEnemy = new Enemy[ENEMY_POOL_COUNT];
		for(int i = 0;i<ENEMY_POOL_COUNT;i++){
			mEnemy[i] = new Enemy();
			mEnemy[i].init(i * ENEMY_POS_OFF, i*ENEMY_POS_OFF-300);
		}
		//创建子弹类对象
		mBullet = new Bullet[BULLET_POOL_COUNT];
		for(int i = 0;i<BULLET_POOL_COUNT;i++){
			mBullet[i] = new Bullet();
		}
		mSendTime = System.currentTimeMillis();
	}
	protected void Draw(){
		switch(mState){
		case STATE_GAME:
			renderBg();//绘制游戏界面包括背景飞机 子弹
			updateBg();//更新游戏逻辑
			break;
		}
	}
	private void setGameState(int newState){
		mState = newState;
	}
	public void renderBg(){
		myPlaneID++;
		if(myPlaneID >= 6)myPlaneID = 0;
		repaint();
	}
	
	public void paint(Graphics g){
		super.paint(g);  
		//绘制游戏地图
		g.drawImage(mBitMenuBG0, 0, mBitposY0, this);
		g.drawImage(mBitMenuBG1, 0, mBitposY1, this);
		//绘制自己飞机动画
		g.drawImage(myPlanePic[myPlaneID],mAirPosX, mAirPosY, this);
		//绘制子弹
		for(int i = 0;i < BULLET_POOL_COUNT;i++)mBullet[i].DrawBullet(g, this);
		//绘制敌机动画
		for(int i = 0; i <ENEMY_POOL_COUNT;i++){
			mEnemy[i].DrawEnemy(g, this);
		}
	}
	
	public void updateBg(){
		//更新游戏背景图片实现向下滚动效果
		mBitposY0 +=10;
		mBitposY1 += 10;
		if(mBitposY0 >= mScreenHeight){
			mBitposY0 = -mScreenHeight;
		}
		if(mBitposY1 == mScreenHeight){
			mBitposY1 = -mScreenHeight;
		}
		//更新子弹位置
		for(int i = 0;i < BULLET_POOL_COUNT; i++){
			mBullet[i].UpdateBullet();
		}
		//更新敌机的位置
		for(int i = 0;i<ENEMY_POOL_COUNT;i++){
			mEnemy[i].UpdateEnemy();
			//敌机死亡或者敌机超过屏幕还没有死亡则重置目标
			if((mEnemy[i].mAnimState == Enemy.ENEMY_DEATH_STATE && mEnemy[i].mPlayID <= 6)||mEnemy[i].m_posY>=mScreenHeight){
				mEnemy[i].init(UtilRandom(0,ENEMY_POOL_COUNT)*ENEMY_POS_OFF,0);
			}
		}
		//根据时间初始化将要发射的子弹位置在玩家飞机前方
		if(mSendId < BULLET_POOL_COUNT){
			long now = System.currentTimeMillis();//获取当前时间
			if(now - mSendTime >=PLAN_TIME){
				//每过500ms发射子弹 在玩家位置的正前方
				System.out.println("发射"+mSendId);
				mBullet[mSendId].init(mAirPosX - Bullet.BULLET_LEFT_OFFSET, mAirPosY-Bullet.BULLET_UP_OFFSET);
				mSendTime = now;
				mSendId++;
			}
		}
		else{
			mSendId=0;
		}
		Collision();//碰撞检验函数
	}
	public void Collision(){
		for(int i = 0 ; i < BULLET_POOL_COUNT; i++){
			for(int j = 0; j < ENEMY_POOL_COUNT;j++){
				if(mBullet[i].m_posX >= mEnemy[j].m_posX-80
						&&mBullet[i].m_posX <= mEnemy[j].m_posX +100
						&&mBullet[i].m_posY >= mEnemy[j].m_posY-20
						&&mBullet[i].m_posY <= mEnemy[j].m_posY+100
						){
					mEnemy[j].mAnimState = Enemy.ENEMY_DEATH_STATE;
					score+=1;
					planeFrame.Score.setText("分数"+score);
				}
			}
		}
	}
	private int UtilRandom(int min,int max){
		return((int)(Math.random()*(max-min)+min));
	}
	public void keyReleased(KeyEvent arg0) {
		// TODO 自动生成的方法存根
	}
	public void keyTyped(KeyEvent arg0) {
		// TODO 自动生成的方法存根
		
	}
	
}
