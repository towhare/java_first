package javagame;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import javax.swing.JPanel;

public class Enemy {
	//�л����״̬
	public static final int ENEMY_ALIVE_STATE = 0;
	//�л�����״̬
	public static final int ENEMY_DEATH_STATE = 1;
	//�л����ߵ�Y���ٶ�
	static final int ENEMY_STEP_Y = 5;
	//�л��ģ�x��y������
	public int m_posX = 0;
	public int m_posY = 0;
	//�л�״̬
	public int mAnimState = ENEMY_ALIVE_STATE;
	private Image enemyExplorePic[] = new Image[6];
	//��ǰ֡��id
	public int mPlayID = 0 ;
	public Enemy(){
		for(int i = 0; i <enemyExplorePic.length;i++){
			enemyExplorePic[i] = Toolkit.getDefaultToolkit().getImage("image\\bomb_enemy.png");
		
		}
	}
	//��ʼ������
	public void init(int x ,int y){
		m_posX = x;
		m_posY = y;
		mAnimState = ENEMY_ALIVE_STATE;
		mPlayID = 0;
	}
	//���Ƶл�
	public void DrawEnemy(Graphics g ,JPanel i){
		//���л�״̬Ϊ�����������������������ʱ���ٻ��Ƶл�
		if(mAnimState == ENEMY_DEATH_STATE && mPlayID<6){
			g.drawImage(enemyExplorePic[mPlayID], m_posX, m_posY, (ImageObserver)i);
			mPlayID++;
			return;
		}
		//�л����״̬
		Image pic = Toolkit.getDefaultToolkit().getImage("image\\e1_0.png");
		g.drawImage(pic,m_posX,m_posY,(ImageObserver)i);
	}
	public void UpdateEnemy(){
		m_posY+=ENEMY_STEP_Y;
	}
}
