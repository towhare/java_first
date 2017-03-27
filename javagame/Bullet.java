package javagame;
import java.awt.*;
import java.awt.image.ImageObserver;
import javax.swing.JFrame;
import javax.swing.JPanel;
//�ӵ���
public class Bullet {
	//�ӵ�x�����ϵ��ٶ�
	static final int BULLET_STEP_X = 3;
	//�ӵ�y���ٶ�
	static final int BULLET_STEP_Y = 10;
	//�ӵ�ͼƬ�Ŀ��
	static final int BULLET_WIDTH = 80;
	static final int BULLET_LEFT_OFFSET = 10;
	static final int BULLET_UP_OFFSET = 20;
	//�ӵ���x,y)����
	public int m_posX = 0;
	public int m_posY = -20;//��ʼ�ӵ�����Ļ��Ϸ��
	//�Ƿ���»����ӵ�
	boolean mFacus = true;
	private Image pic[] = null;//�ӵ�  ����Ϊ����
	//��ǰ֡��ID
	private int mPlayID = 0;
	public Bullet(){
		pic = new Image[4];
		for(int i = 0;i < 4;i++){
			pic[i] = Toolkit.getDefaultToolkit().getImage("image\\bullet_"+i+".png");
		}
	}
	//��ʼ������
	public void init(int x,int y){
		m_posX = x ;
		m_posY = y ;
		mFacus = true;
	}
	public void DrawBullet(Graphics g,JPanel i){
		g.drawImage(pic[mPlayID++], m_posX, m_posY, (ImageObserver)i);//�����ӵ�����
		if(mPlayID==4)mPlayID=0;//������֡��ʱ�����±�Ϊ0
	}
	public void UpdateBullet(){
		if(mFacus){
			m_posY -= BULLET_STEP_Y;
		}
	}
}
