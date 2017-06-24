package Learning.Nancy;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class Win
{
    public static boolean isplaying=true;//成员变量内部类也可以使用

    public static void main(String[] args)
    {

        GameWin frame=new GameWin();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


}

class GameWin extends JFrame
{
    public static final int Default_X=500;
    public static final int Default_Y=630;
    private Left jpLeft=new Left();  //原jp2
    private Right jpRight=new Right() ;//原jp3
    private JSplitPane jpy1=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jpLeft,jpRight);

    public GameWin()
    {
        this.setFocusable(true);
        getContentPane().requestFocus();
        this.setAlwaysOnTop(true);
        setTitle("Tetris");
        setBounds(350,90,Default_X,Default_Y);
        setResizable(false);
        add(jpy1);
        jpy1.setDividerLocation(304);
        jpy1.setDividerSize(4);
        addKeyListener(jpLeft);
        Thread thread=new Thread(jpLeft);
        thread.start();
    }


}

class Right extends JPanel implements ActionListener
{
    private String[] level={"1","2","3","4","5"};
    private String[] color={"浅绿","浅黄","黑色"};
    private JComboBox[] jcArray={new JComboBox(level),new JComboBox(color)};
    private JLabel[] jlArray={new JLabel("游戏等级"),new JLabel("空间背景")};
    private JButton[] jbArray={new JButton("开始游戏"),new JButton("暂停游戏")
            ,new JButton("结束游戏"),new JButton("退出游戏")};
    public Right()
    {
        initialframe();
        initialListener();
    }

    public void initialframe()
    {
        setLayout(null);
        add(jlArray[0]);
        jlArray[0].setBounds(30,60,70,30);
        jlArray[0].setFocusable(false);
        add(jlArray[1]);
        jlArray[1].setBounds(30,140,70,30);
        jlArray[1].setFocusable(false);
        add(jcArray[0]);
        jcArray[0].setBounds(100,60,70,30);
        jcArray[0].setFocusable(false);
        add(jcArray[1]);
        jcArray[1].setBounds(100,140,70,30);
        jcArray[1].setFocusable(false);
        add(jbArray[0]);
        jbArray[0].setBounds(50,240,100,35);
        jbArray[0].setFocusable(false);
        add(jbArray[1]);
        jbArray[1].setBounds(50,310,100,35);
        jbArray[1].setFocusable(false);
        add(jbArray[2]);
        jbArray[2].setBounds(50,380,100,35);
        jbArray[2].setFocusable(false);
        add(jbArray[3]);
        jbArray[3].setBounds(50,450,100,35);
        jbArray[3].setFocusable(false);
    }

    public void initialListener()
    {
        for(int i=0;i<4;i++)
            jbArray[i].addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==jbArray[0])   //开始   修改
        {
            Win.isplaying=true;
            //invalidate();
            repaint();
        }
        else if(e.getSource()==jbArray[1])//暂停  修改
        {
            Win.isplaying=false;
            invalidate();
        }
        else if(e.getSource()==jbArray[2])//结束  修改
        {
            Win.isplaying=false;
        }
        else if(e.getSource()==jbArray[3])//退出
        {
            System.exit(0);
        }
    }


}

class Left extends JComponent implements KeyListener,Runnable     //线程
{
    private RussionBlockGame game;
    public static final int boxSize=30;
    int yPosition,xPosition;
    public Left()
    {
        game=new RussionBlockGame();
        yPosition=game.gety();
        xPosition=game.getx();
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2=(Graphics2D)g;
        super.paintComponent(g);
        double width=300,height=600;//b
        Rectangle2D rect=new Rectangle2D.Double(0,0,width,height);
        g2.setColor(Color.green);//定义上下文颜色,没有实际意义
        g2.draw(rect);           //画，但还没展现
        g2.setColor(Color.PINK);//将画布内的图形的颜色设为粉色
        g2.fill(rect);            //将rect充满整个画布
        g2.setColor(Color.cyan);//边框
        //g2.draw(rect);
        for(int i=0;i<xPosition;i++)   //20和10 是怎么来的
            for(int j=0;j<yPosition;j++)
            {
                if(game.judge(i,j)==true)  //如果有小方块
                {
                    Rectangle2D rect3=new Rectangle2D.Double(j*boxSize,i*boxSize,boxSize,boxSize);
                    g2.setColor(Color.black);//没啥意义？
                    g2.draw(rect3);          //边框？
                    g2.setColor(Color.ORANGE);  //矩形的颜色设置
                    g2.fill(rect3);          //g2画图是叠加的吗？画上去
                    g2.setColor(Color.CYAN);//小矩形边框颜色
                    g2.draw(rect3);        //??
                }
            }
        game.notsure();//让block_box的占有空间区域都记为0
    }

    public void keyTyped(KeyEvent e)
    {}

    public void keyPressed(KeyEvent e)
    {
        if(Win.isplaying==false)
            return;
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_LEFT:                                       //LEFT
                game.moveleft();
                movejudge();
                break;
            case KeyEvent.VK_UP:                                         //UP
                game.turnright();
                movejudge();
                break;
            case KeyEvent.VK_RIGHT:                                      //RIGHT
                game.moveright();
                movejudge();
                break;
            case KeyEvent.VK_DOWN:                                       //DOWN
                game.movedown();
                movejudge();
                break;
        }
    }

    public void keyReleased(KeyEvent e)
{}

    public void movejudge()
    {
        if(game.iscanmoveto()==true)
        {
            game.sure();
            repaint();
        }
        else if(game.ishitbottom()==true)
        {
            game.CheckAndCutLine();
            game.makenewblock();
            repaint();
            if(game.isGameOver()==true)
                Win.isplaying=false;
        }
    }

    public void run()
    {
        try
        {
            while(Win.isplaying)
            {
                Thread.sleep(500);
                invalidate();
                game.movedown();
                movejudge();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}


