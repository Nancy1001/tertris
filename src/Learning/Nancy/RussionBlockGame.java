package Learning.Nancy;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.math.*;

public class RussionBlockGame
{
    private int aa=0;  //aa是什么:blocktype的编号
    private int ic=0;  //ic是什么:aa*10+1
    private final int sp_width=10;                               //游戏界面宽格
    private final int sp_height=20;                              //游戏界面高格
    private final int types[][][]={                              //游戏方块-19种形状
            {{-1,0},{0,0},{1,0},{2,0}},                          //长条0
            {{0,-1},{0,0},{0,1},{0,2}},
            {{-1,0},{0,0},{1,0},{1,1}},                          //直角（右）2
            {{0,1},{0,0},{0,-1},{1,-1}},
            {{1,0},{0,0},{-1,0},{-1,-1}},
            {{0,-1},{0,0},{0,1},{-1,1}},
            {{-1,0},{0,0},{0,1},{1,0}},                          //直角（中）6
            {{0,1},{0,0},{1,0},{0,-1}},
            {{1,0},{0,0},{0,-1},{-1,0}},
            {{0,-1},{0,0},{-1,0},{0,1}},
            {{-1,1},{-1,0},{0,0},{1,0}},                         //直接（左）10
            {{1,1},{0,1},{0,0},{0,-1}},
            {{1,-1},{1,0},{0,0},{-1,0}},
            {{-1,-1},{0,-1},{0,0},{0,1}},
            {{0,-1},{0,0},{1,0},{1,1}},//14
            {{-1,0},{0,0},{0,-1},{1,-1}},//15
            {{0,1},{0,0},{1,0},{1,-1}},//16
            {{1,0},{0,0},{0,-1},{-1,-1}},
            {{0,0},{0,1},{1,0},{1,1}}                            //正方形18
    };

    private int[][] block_box=new int[4][2];                     //四个方块的空间位置
    private int[][] block_box_tt=new int[4][2];
    private int block_x=0,block_y=0;                             //游戏方块在游戏界面中的坐标
    private int block_type=0;                                    //方块类别
    private int[][] game_space=new int[sp_height][sp_width];     //空间数据 gamespace[x][y]
    private int movetype=0;
    private int scroe=0;  //*计分
    private int speed=5; //速度

    public RussionBlockGame()     //constructor
    {
        clearspace();
        makenewblock();
    }

    public int gety(){
        return sp_width;
    }
    public int getx(){
        return sp_height;
    }


    public void clearspace()                                     //初始化空间数据-初始化为0
    {
        for(int i=0;i<sp_height;i++)
            for(int j=0;j<sp_width;j++)
                game_space[i][j]=0;
    }


    public void makenewblock()                                   //随机出现方块
    {
        aa=(int)(Math.random()*100%7+1);
        ic=aa*10+1;//(11 21 31 41 51 61 71)
        switch(aa)
        {
            case 1:
                block_type=0;
                break;
            case 2:
                block_type=2;
                break;
            case 3:
                block_type=6;
                break;
            case 4:
                block_type=10;
                break;
            case 5:
                block_type=14;
                break;
            case 6:
                block_type=16;
                break;
            case 7:
                block_type=18;
                break;
        }
        block_x=1;
        block_y=sp_width/2;
        for(int i=0;i<4;i++) //四个方块坐标-一整块
        {
          //  block_box[i][0]=block_x-types[block_type][i][1]; //确定每个小方块的x坐标：1-y的值  types[block_type][i][0]
          //  block_box[i][1]=block_y+types[block_type][i][0];//y坐标：sp_width/2 +  x 的值   types[block_type][i][1]对应的值
            block_box[i][0]=types[block_type][i][0]+block_x;
            block_box[i][1]=types[block_type][i][1]+block_y;
        }
    }

    public void movedown()
    {
        block_x++;
        for(int i=0;i<4;i++)
        {
          //  block_box[i][0]=block_x-types[block_type][i][1];
            block_box[i][0]=block_x+types[block_type][i][0];
        }
        movetype=1;  //movetype是什么
    }

    public void moveleft()
    {
        block_y--;
        for(int i=0;i<4;i++)
        {
           // block_box[i][1]=block_y+types[block_type][i][0];
            block_box[i][1]=block_y+types[block_type][i][1];
        }
        movetype=2;
    }

    public void moveright()
    {
        block_y++;
        for(int i=0;i<4;i++)
        {
          //  block_box[i][1]=block_y+types[block_type][i][0];
            block_box[i][1]=block_y+types[block_type][i][1];
        }
        movetype=3;
    }

    public void changeShape()  //这个方法干啥的-改变形状（原turnright ）
    {
        int[][] block_box_temp=new int[4][2];
        int ic_temp=ic;
        int block_type_temp=block_type;
        int id=ic%10;//(取余数),ic 有啥用
        for(int i=0;i<4;i++)
        {
            block_box_temp[i][0]=block_box[i][0];
            block_box_temp[i][1]=block_box[i][1];
        }
        if(aa==7)
            return;  //正方形
        else if(aa==1||aa==5||aa==6)
        {
            if(id==2)   //?
            {
                block_type--;
                ic--;   //为什么ic要减（11-1=10）
            }
            else
            {
                block_type++;
                ic++;  //11+1=12
            }
        }
        else  //aa=2 3 4
        {
            if(id==4)  //为什么会有那么多id的值
            {
                block_type=block_type-3;
                ic=ic-3;
            }
            else
            {
                block_type++;
                ic++;
            }
        }
        for(int i=0;i<4;i++)
        {
            block_box[i][0]=block_x-types[block_type][i][1];
            block_box[i][1]=block_y+types[block_type][i][0];
        }

        if(iscanmoveto()==false)   //不能再移
        {
            ic=ic_temp;
            block_type=block_type_temp;//不要也可以
            for(int i=0;i<4;i++)
            {
                block_box[i][0]=block_box_temp[i][0];
                block_box[i][1]=block_box_temp[i][1];
            }
        }
    }

    public void moveback()   //为什么要moveback-如果判断出不能这样走
    {
        if(movetype==1)  //下
        {
            block_x--;
            for(int i=0;i<4;i++)
            {
                //block_box[i][0]=block_x-types[block_type][i][1];
                block_box[i][0]=block_x+types[block_type][i][0];
            }
        }
        else if(movetype==2)  //左
        {
            block_y++;
            for(int m=0;m<4;m++)
            {
             //   block_box[m][1]=block_y+types[block_type][m][0];
                block_box[m][1]=block_y+types[block_type][m][1];
            }
        }
        else if(movetype==3)    //右
        {
            block_y--;
            for(int n=0;n<4;n++)
            {
               // block_box[n][1]=block_y+types[block_type][n][0];
                block_box[n][1]=block_y+types[block_type][n][1];
            }

        }
    }

    public boolean iscanmoveto()
    {
        for(int i=0;i<4;i++)
        {
            if(block_box[i][0]<0||block_box[i][0]+1>sp_height)  //x坐标上的位置,为啥要-1？可能最后一个位置就是sp_-1
            {
                moveback();   //既然+1为什么要moveback
                return false;
            }
            else if(block_box[i][1]<0||block_box[i][1]+1>sp_width)//y坐标上的位置
            {
                moveback();
                return false;
            }
            else if(game_space[block_box[i][0]][block_box[i][1]]==1)  //已经有小方块了
            {
                moveback();
                return false;
            }
        }
        return true;
    }

    public boolean ishitbottom()
    {
        //情况一
        for(int i=0;i<4;i++)
        {
            if(block_box[i][0]+1>=sp_height) //为啥要+1-下一步触底-那不是应该把+1去掉吗?可能底部的最后一层是sp_height-1
            {
                for(int m=0;m<4;m++)
                {   //获得小方块对应的x值和对应的y值，gamespace=1，小方块不动了，这块区域有小方块
                    game_space[block_box[m][0]][block_box[m][1]]=1;
                    block_box_tt[m][0]=block_box[m][0];  //为什么要有block_box_tt[][]-存储触底的方块位置
                    block_box_tt[m][1]=block_box[m][1];
                    block_box[m][0]=0;//为什么要将block_box[][]坐标全部设成0-只有一个block_box
                    block_box[m][1]=0;
                }
                return true;
            }
        }

        //情况二
        for(int i=0;i<4;i++)
        {
            if(game_space[block_box[i][0]+1][block_box[i][1]]==1)   //x+1有东西了，就是下方有小方块了
            {
                for(int m=0;m<4;m++)
                {
                    game_space[block_box[m][0]][block_box[m][1]]=1;//那么就停在这里
                    block_box_tt[m][0]=block_box[m][0];  //block_box_tt又记住坐标
                    block_box_tt[m][1]=block_box[m][1];
                    block_box[m][0]=0;  //释放
                    block_box[m][1]=0;
                }
                return true;
            }
        }
        return false;
    }

    public void CheckAndCutLine()    //在这里加分数
    {   //数组a存放四个小方格坐标的第一个数（x值），共四个数,可是为什么不是y值。。。不是一行吗?
        int a[]={block_box_tt[0][0],block_box_tt[1][0],block_box_tt[2][0],block_box_tt[3][0]};
        int b[]={30,30,30,30};  //b是什么
        int temp=0;            //是什么
        int temp1=0;           //是什么
        int count=0;           //是什么
        int ss=0;              //ss是什么
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<sp_width;j++)
            {
                if(game_space[a[i]][j]==1)  //先竖后横
                    temp++;
            }


            if(temp==sp_width)                    //如果整一行都有小方块
            {
                for(int m=0;m<4;m++)
                    if(b[m]==a[i])                //b[]=每个小方块的x值
                    {
                        break;
                    }
                    else
                        ss++;                     //s+1
                if(ss==4)                         //如果没有一个没有一个小方块的x值等于30（本来就没有）
                {
                    b[count]=a[i];                //那么b[0]=a[0],b[i]对应的x值是要消掉的行数。
                    count++;                      //count+1
                }
            }
            temp=0;  //?
            ss=0;  //?
        }

        for(int i=0;i<3;i++)    //排序,使b[0]-b[3]依次增加
            for(int j=i+1;j<4;j++)
            {
                if(b[i]>b[j])
                {
                    temp1=b[i];
                    b[i]=b[j];
                    b[j]=temp1;
                }
            }

        for(int n=0;n<4;n++)
        {
            if(b[n]==30)
                break;
            else
            {
                for(int aa=b[n]-1;aa>=0;aa--)
                {
                    for(int bb=0;bb<sp_width;bb++)
                    {
                        game_space[aa+1][bb]=game_space[aa][bb];  //全部向下一行
                    }
                }
                for(int cc=0;cc<sp_width;cc++)
                    game_space[0][cc]=0;//x=0,第0行
            }
        }
    }

    public boolean isGameOver()    //触顶
    {
        boolean flag=false;
        for(int i=0;i<sp_width;i++)
        {
            if(game_space[0][i]==1)  //第0行有一个被占了
            {
                flag=true;
                break;
            }
        }
        return flag;
    }

    public void sure()
    {
        for(int i=0;i<4;i++)
            game_space[block_box[i][0]][block_box[i][1]]=1;  //block_box的位置空间占用
    }

    public void notsure()
    {
        for(int i=0;i<4;i++)
            game_space[block_box[i][0]][block_box[i][1]]=0; // 不占有空间
    }

    public boolean judge(int i,int j)
    {
        if(game_space[i][j]==1)
            return true;  //判断（i,j）位置有没有小方块
        else
            return false;
    }
}



