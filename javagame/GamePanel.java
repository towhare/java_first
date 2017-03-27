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
	//��Ϸ���˵�״̬
	private static final int STATE_GAME = 0;
	//��Ϸ״̬
	private int mState = STATE_GAME;
	//��ʼ����Ϸ������Դ
	private Image mBitMenuBG0 = null;
	private Image mBitMenuBG1 = null;
	//��¼���ű���ͼƬ����ʱ��y����
	private int mBitposY0 = 0;
	private int mBitposY1 = 0;
	//�ӵ�����
	final static int BULLET_POOL_COUNT = 8;
	//�ɻ��ƶ����ٶ�
	final static int PLAN_STEP = 40;
	//ÿ500ms����һ���ӵ�
	final static int PLAN_TIME = 500;
	//���˶�������
	final static int ENEMY_POOL_COUNT = 5;
	//���˷ɻ�ƫ����
	final static int ENEMY_POS_OFF = 65;
	//��Ϸ���߳�
	private Thread mThread = null ;
	//�߳�ѭ����־
	private boolean gameIsRunning = false;
	//�ɻ�����Ļ�е�����
	public int mAirPosX = 0;
	public int mAirPosY = 0;
	//�л����������
	Enemy mEnemy[] = null;
	//�ӵ���������
	Bullet mBullet[] = null ;
	//��ʼ�������ӵ���ID
	public int mSendId = 0;
	//��һ���ӵ������ʱ��
	public long mSendTime = 0L;
	Image myPlanePic[];//��ҷɻ���ͼƬ
	public int myPlaneID = 0;//��ҷɻ���ǰ��֡
	public static int score = 0;
	public GamePanel(){
		setPreferredSize(new Dimension(mScreenWidth,mScreenHeight));
		//�趨�����ڱ����岢�����������
		setFocusable(true);
		addKeyListener(this);
		init();//�����д  ��ʼ������
		setGameState(STATE_GAME);
		gameIsRunning = true ;
		mThread = new Thread(this);//ʵ�����߳�
		//������Ϸ�߳�
		mThread.start();
		setVisible(true);
	}
	public void keyPressed(KeyEvent e) {
		// TODO �Զ����ɵķ������
		int key = e.getKeyCode();
		System.out.println(key);
		if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP){mAirPosY -= PLAN_STEP;}//�Ϸ����
		if(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN){mAirPosY += PLAN_STEP;}//�·����
		if(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT){//�����
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
			//ˢ����Ļ
			Draw();
			//��ʱ0.1s
			try{Thread.sleep(50);}
			catch(InterruptedException e){e.printStackTrace();}
		}
	}
	private void init(){
		mBitMenuBG0 = Toolkit.getDefaultToolkit().getImage("image\\map_0.png");
		mBitMenuBG1 = Toolkit.getDefaultToolkit().getImage("image\\map_1.png");
		//��һ��ͼƬ��������Ļ��0,0���㴦
		mBitposY0 = 0;
		mBitposY1 = -mScreenHeight;
		//��ʼ����ҷɻ�������
		mAirPosX = 150;
		mAirPosY = 500;
		//��ʼ����ҷɻ���ص�����ͼƬ
		myPlanePic = new Image[6];
		for(int i = 0;i<myPlanePic.length;i++){
			myPlanePic[i] = Toolkit.getDefaultToolkit().getImage("image\\plan_"+i+".png");
		}
		//�����л�����
		mEnemy = new Enemy[ENEMY_POOL_COUNT];
		for(int i = 0;i<ENEMY_POOL_COUNT;i++){
			mEnemy[i] = new Enemy();
			mEnemy[i].init(i * ENEMY_POS_OFF, i*ENEMY_POS_OFF-300);
		}
		//�����ӵ������
		mBullet = new Bullet[BULLET_POOL_COUNT];
		for(int i = 0;i<BULLET_POOL_COUNT;i++){
			mBullet[i] = new Bullet();
		}
		mSendTime = System.currentTimeMillis();
	}
	protected void Draw(){
		switch(mState){
		case STATE_GAME:
			renderBg();//������Ϸ������������ɻ� �ӵ�
			updateBg();//������Ϸ�߼�
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
		//������Ϸ��ͼ
		g.drawImage(mBitMenuBG0, 0, mBitposY0, this);
		g.drawImage(mBitMenuBG1, 0, mBitposY1, this);
		//�����Լ��ɻ�����
		g.drawImage(myPlanePic[myPlaneID],mAirPosX, mAirPosY, this);
		//�����ӵ�
		for(int i = 0;i < BULLET_POOL_COUNT;i++)mBullet[i].DrawBullet(g, this);
		//���Ƶл�����
		for(int i = 0; i <ENEMY_POOL_COUNT;i++){
			mEnemy[i].DrawEnemy(g, this);
		}
	}
	
	public void updateBg(){
		//������Ϸ����ͼƬʵ�����¹���Ч��
		mBitposY0 +=10;
		mBitposY1 += 10;
		if(mBitposY0 >= mScreenHeight){
			mBitposY0 = -mScreenHeight;
		}
		if(mBitposY1 == mScreenHeight){
			mBitposY1 = -mScreenHeight;
		}
		//�����ӵ�λ��
		for(int i = 0;i < BULLET_POOL_COUNT; i++){
			mBullet[i].UpdateBullet();
		}
		//���µл���λ��
		for(int i = 0;i<ENEMY_POOL_COUNT;i++){
			mEnemy[i].UpdateEnemy();
			//�л��������ߵл�������Ļ��û������������Ŀ��
			if((mEnemy[i].mAnimState == Enemy.ENEMY_DEATH_STATE && mEnemy[i].mPlayID <= 6)||mEnemy[i].m_posY>=mScreenHeight){
				mEnemy[i].init(UtilRandom(0,ENEMY_POOL_COUNT)*ENEMY_POS_OFF,0);
			}
		}
		//����ʱ���ʼ����Ҫ������ӵ�λ������ҷɻ�ǰ��
		if(mSendId < BULLET_POOL_COUNT){
			long now = System.currentTimeMillis();//��ȡ��ǰʱ��
			if(now - mSendTime >=PLAN_TIME){
				//ÿ��500ms�����ӵ� �����λ�õ���ǰ��
				System.out.println("����"+mSendId);
				mBullet[mSendId].init(mAirPosX - Bullet.BULLET_LEFT_OFFSET, mAirPosY-Bullet.BULLET_UP_OFFSET);
				mSendTime = now;
				mSendId++;
			}
		}
		else{
			mSendId=0;
		}
		Collision();//��ײ���麯��
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
					planeFrame.Score.setText("����"+score);
				}
			}
		}
	}
	private int UtilRandom(int min,int max){
		return((int)(Math.random()*(max-min)+min));
	}
	public void keyReleased(KeyEvent arg0) {
		// TODO �Զ����ɵķ������
	}
	public void keyTyped(KeyEvent arg0) {
		// TODO �Զ����ɵķ������
		
	}
	
}
