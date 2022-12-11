package com.daoblunt.sokoban;

import javax.swing.*;

/**
 * @author: daoblunt
 * @description:
 * @create: 2022-11-25 23:47
 */
public class SokobanGame {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("推箱子");
        jFrame.setBounds(100,50,780,780);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.add(new SokobanPenal());
        jFrame.setVisible(true);
    }
}
