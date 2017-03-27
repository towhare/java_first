package javagame;
import java.awt.*;
import java.awt.image.ImageObserver;
import javax.swing.JFrame;
import javax.swing.JPanel;
//子弹类
public class Bullet {
	//子弹x方向上的速度
	static final int BULLET_STEP_X = 3;
	//子弹y轴速度
	static final int BULLET_STEP_Y = 10;
	//子弹图片的宽度
	static final int BULLET_WIDTH = 80;
	static final int BULLET_LEFT_OFFSET = 10;
	static final int BULLET_UP_OFFSET = 20;
	//子弹（x,y)坐标
	public int m_posX = 0;
	public int m_posY = -20;//初始子弹在屏幕游戏外
	//是否更新绘制子弹
	boolean mFacus = true;
	private Image pic[] = null;//子弹  可以为数组
	//当前帧的ID
	private int mPlayID = 0;
	public Bullet(){
		pic = new Image[4];
		for(int i = 0;i < 4;i++){
			pic[i] = Toolkit.getDefaultToolkit().getImage("image\\bullet_"+i+".png");
		}
	}
	//初始化坐标
	public void init(int x,int y){
		m_posX = x ;
		m_posY = y ;
		mFacus = true;
	}
	public void DrawBullet(Graphics g,JPanel i){
		g.drawImage(pic[mPlayID++], m_posX, m_posY, (ImageObserver)i);//绘制子弹方法
		if(mPlayID==4)mPlayID=0;//到第四帧的时候重新变为0
	}
	public void UpdateBullet(){
		if(mFacus){
			m_posY -= BULLET_STEP_Y;
		}
	}
}
