package com.daoblunt.sokoban;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * @author: daoblunt
 * @description:
 * @create: 2022-11-25 23:47
 */
public class SokobanPenal extends JPanel implements KeyListener, ActionListener {
    ArrayList<Node> walls;//存放墙的坐标
    ArrayList<Node> points;//存放点的坐标
    ArrayList<Node> boxList;//存放箱子坐标
    Node people;//放小人的坐标
    boolean[] flag = new boolean[4];//是否通关
    boolean isComplete;//是否通关
    Timer timer = new Timer(20,this);//计时器,每20ms进行一次事件监听，执行一次actionPerformed()方法
    int number;//记步数
    //构造器
    public SokobanPenal(){
        init();
        this.setFocusable(true);//设置次画板可以成为焦点
        this.addKeyListener(this);//添加键盘监听事件
    }

    //初始化方法
    public void init(){
        walls = new ArrayList<>();
        points = new ArrayList<>();
        boxList = new ArrayList<>();
        initWall();
        initPoint();
        initBox();
        people = new Node(360,270);
        isComplete = false;
        for (int i = 0; i < flag.length; i++) {
            flag[i]=false;
        }
        timer.start();
        number=0;
    }

    //画画
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        //画墙
        for (int i = 0; i < walls.size(); i++) {
            Data.WALL.paintIcon(this,g,walls.get(i).getX(),walls.get(i).getY());
        }
        //画点
        for (int i = 0; i < points.size(); i++) {
            Data.POINT.paintIcon(this,g,points.get(i).getX(),points.get(i).getY());
        }
        //画箱子
        for (int i = 0; i < boxList.size(); i++) {
            Data.BOX_LAND.paintIcon(this,g,boxList.get(i).getX(),boxList.get(i).getY());
        }
        //画小人
        Data.PEOPLE.paintIcon(this,g,people.getX(),people.getY());
        //画完成的箱子
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < boxList.size(); j++) {
                if(points.get(i).getX()==boxList.get(j).getX() && points.get(i).getY()==boxList.get(j).getY()){
                    Data.BOX_POINT.paintIcon(this,g,points.get(i).getX(),points.get(i).getY());
                    flag[i] =true;
                }
            }
        }
        //画开始结束提示
        if (isComplete){
            g.setColor(Color.BLACK);
            g.setFont(new Font("微软雅黑",Font.BOLD,40));
            g.drawString("过关！！！",270,405);
        }
        //画计步器
        g.setColor(Color.BLACK);
        g.setFont(new Font("微软雅黑",Font.BOLD,20));
        g.drawString("步数:"+number,45,45);
        //画游戏规则
        g.setColor(Color.BLACK);
        g.setFont(new Font("微软雅黑",Font.BOLD,20));
        g.drawString("游戏规则：",520,45);
        g.drawString("上下左右移动",540,90);
        g.drawString("空格重新开始",540,135);
    }

    //键盘监听
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (!isComplete){
            if(keyCode==KeyEvent.VK_UP){
                moveUp();
            }else if(keyCode==KeyEvent.VK_DOWN){
                moveDown();
            }else if(keyCode==KeyEvent.VK_RIGHT){
                moveRight();
            }else if(keyCode==KeyEvent.VK_LEFT){
                moveLeft();
            }
        }
        if(keyCode==KeyEvent.VK_SPACE){
            init();
        }
    }

    //事件监听
    @Override
    public void actionPerformed(ActionEvent e) {
        //判断是否通关
        if (flag[0]&&flag[1]&&flag[2]&&flag[3]){
            isComplete=true;
        }
        repaint();
    }
    //上移逻辑,先移动，再解决撞墙问题
    public void moveUp(){
        int y1=people.getY();
        people.setY(people.getY()-90);//移动人
        for (int i = 0; i < boxList.size(); i++) {
            //移动箱子
            if(boxList.get(i).getX()==people.getX() && boxList.get(i).getY()==people.getY()){
               boxList.get(i).setY(boxList.get(i).getY()-90);
            }

            for (int j = 0; j < walls.size(); j++) {
                //解决箱子穿墙
                if(boxList.get(i).getX()==walls.get(j).getX() && boxList.get(i).getY()==walls.get(j).getY()){
                    boxList.get(i).setY(boxList.get(i).getY()+90);
                    people.setY(people.getY()+90);
                }
                //解决人穿墙
                if (walls.get(j).getX()==people.getX() && walls.get(j).getY()==people.getY()){
                    people.setY(people.getY()+90);
                }
            }
        }
        int y2 = people.getY();
        if(y1!=y2){
            number++;
        }
    }
    //下移逻辑
    public void moveDown(){
        int y1=people.getY();
        people.setY(people.getY()+90);//移动人
        for (int i = 0; i < boxList.size(); i++) {
            //移动箱子
            if(boxList.get(i).getX()==people.getX() && boxList.get(i).getY()==people.getY()){
                boxList.get(i).setY(boxList.get(i).getY()+90);
            }

            for (int j = 0; j < walls.size(); j++) {
                //解决箱子穿墙
                if(boxList.get(i).getX()==walls.get(j).getX() && boxList.get(i).getY()==walls.get(j).getY()){
                    boxList.get(i).setY(boxList.get(i).getY()-90);
                    people.setY(people.getY()-90);
                }
                //解决人穿墙
                if (walls.get(j).getX()==people.getX() && walls.get(j).getY()==people.getY()){
                    people.setY(people.getY()-90);
                }
            }
        }
        int y2=people.getY();
        if (y1!=y2){
            number++;
        }

    }
    //右移逻辑
    public void moveRight(){
        int x1=people.getX();
        people.setX(people.getX()+90);//移动人
        for (int i = 0; i < boxList.size(); i++) {
            //移动箱子
            if(boxList.get(i).getX()==people.getX() && boxList.get(i).getY()==people.getY()){
                boxList.get(i).setX(boxList.get(i).getX()+90);
            }

            for (int j = 0; j < walls.size(); j++) {
                //解决箱子穿墙
                if(boxList.get(i).getX()==walls.get(j).getX() && boxList.get(i).getY()==walls.get(j).getY()){
                    boxList.get(i).setX(boxList.get(i).getX()-90);
                    people.setX(people.getX()-90);
                }
                //解决人穿墙
                if (walls.get(j).getX()==people.getX() && walls.get(j).getY()==people.getY()){
                    people.setX(people.getX()-90);
                }
            }
        }
        int x2=people.getX();
        if (x1!=x2){
            number++;
        }
    }
    //左移逻辑
    public void moveLeft(){
        int x1=people.getX();
        people.setX(people.getX()-90);//移动人
        for (int i = 0; i < boxList.size(); i++) {
            //移动箱子
            if(boxList.get(i).getX()==people.getX() && boxList.get(i).getY()==people.getY()){
                boxList.get(i).setX(boxList.get(i).getX()-90);
            }

            for (int j = 0; j < walls.size(); j++) {
                //解决箱子穿墙
                if(boxList.get(i).getX()==walls.get(j).getX() && boxList.get(i).getY()==walls.get(j).getY()){
                    boxList.get(i).setX(boxList.get(i).getX()+90);
                    people.setX(people.getX()+90);
                }
                //解决人穿墙
                if (walls.get(j).getX()==people.getX() && walls.get(j).getY()==people.getY()){
                    people.setX(people.getX()+90);
                }
            }
        }
        int x2=people.getX();
        if (x1!=x2){
            number++;
        }
    }


    //初始化墙
    public void initWall(){
        for (int i = 0; i < 3; i++) {
            walls.add(new Node(180+i*90,0));
        }
        for (int i = 0; i < 3; i++) {
            walls.add(new Node(180,90+i*90));
        }
        walls.add(new Node(90,270));
        for (int i = 0; i < 3; i++) {
            walls.add(new Node(0,270+i*90));
        }
        for (int i = 0; i < 3; i++) {
            walls.add(new Node(90+i*90,450));
        }
        walls.add(new Node(270,540));
        for (int i = 0; i < 3; i++) {
            walls.add(new Node(270+i*90,630));
        }
        walls.add(new Node(360,90));
        for (int i = 0; i < 4; i++) {
            walls.add(new Node(360+i*90,180));
        }
        walls.add(new Node(630,270));
        for (int i = 0; i < 3; i++) {
            walls.add(new Node(450+i*90,360));
        }
        for (int i = 0; i < 2; i++) {
            walls.add(new Node(450,450+i*90));
        }
    }
    //初始化点
    public void initPoint(){
        points.add(new Node(270,90));
        points.add(new Node(90,360));
        points.add(new Node(540,270));
        points.add(new Node(360,540));
    }

    //初始化箱子
    public void initBox(){
        boxList.add(new Node(270,270));
        boxList.add(new Node(270,360));
        boxList.add(new Node(450,270));
        boxList.add(new Node(360,450));
    }
    //初始化小人
    public void initPeople(){
        people = new Node(360,270);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}