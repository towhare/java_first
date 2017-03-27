package javagame;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import javax.swing.JPanel;

public class Enemy {
	//敌机存活状态
	public static final int ENEMY_ALIVE_STATE = 0;
	//敌机死亡状态
	public static final int ENEMY_DEATH_STATE = 1;
	//敌机行走的Y轴速度
	static final int ENEMY_STEP_Y = 5;
	//敌机的（x，y）坐标
	public int m_posX = 0;
	public int m_posY = 0;
	//敌机状态
	public int mAnimState = ENEMY_ALIVE_STATE;
	private Image enemyExplorePic[] = new Image[6];
	//当前帧的id
	public int mPlayID = 0 ;
	public Enemy(){
		for(int i = 0; i <enemyExplorePic.length;i++){
			enemyExplorePic[i] = Toolkit.getDefaultToolkit().getImage("image\\bomb_enemy.png");
		
		}
	}
	//初始化坐标
	public void init(int x ,int y){
		m_posX = x;
		m_posY = y;
		mAnimState = ENEMY_ALIVE_STATE;
		mPlayID = 0;
	}
	//绘制敌机
	public void DrawEnemy(Graphics g ,JPanel i){
		//当敌机状态为死亡并且死亡动画播放完毕时不再绘制敌机
		if(mAnimState == ENEMY_DEATH_STATE && mPlayID<6){
			g.drawImage(enemyExplorePic[mPlayID], m_posX, m_posY, (ImageObserver)i);
			mPlayID++;
			return;
		}
		//敌机存活状态
		Image pic = Toolkit.getDefaultToolkit().getImage("image\\e1_0.png");
		g.drawImage(pic,m_posX,m_posY,(ImageObserver)i);
	}
	public void UpdateEnemy(){
		m_posY+=ENEMY_STEP_Y;
	}
}
