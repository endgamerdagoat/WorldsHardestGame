//2nd level - worlds hardest game 1, lvl 14


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Rectangle;

public class WorldsHardestGame extends JPanel implements KeyListener,Runnable
{
    //instance variables should be declared here
    //so that they work in all methods
    //you will need to add more as you go
    private int x,y, x2, y2, startX, startY, startX2, startY2, deaths, levelNum;
    private double theta;
    private int[] enemyX, enemyY, enemyX2, enemyY2, plusX, plusY, originalX, originalY, gridX, gridY, gridX2, gridY2;
    private int heroDim, enemyDim;
    private Rectangle hero;
    private Rectangle[] enemies, enemies2;
    private Polygon level, level2, checker, checker2;
    private JFrame frame;
    private Thread t;
    private boolean gameOn, right, up, down, left, pause;
    boolean[] enemyDirs, enemyDirs2;
    private Font f;
    private Color color;

    public WorldsHardestGame()
    {
        //this is the constructor
        //leave the stuff alone that you don't recognize
        //it is necessary to make the motion happen
        //be sure to initialize any object type variables
        //in this section (rectangles, polygons, etc)
        //or they will be null and things won't work

        frame=new JFrame();

        levelNum = 1;

        x = 50;
        y = 150;


        startX = x;
        startY = y;

        x2 = 100;
        y2 = 350;


        startX2 = x2;
        startY2 = y2;


        gameOn = true;
        heroDim = 20;
        enemyDim = 15;

        //setup for a polygon, coordinates in order
        int[] xPoints = {25, 125, 125, 175, 175, 575, 575, 775, 775, 675, 675, 625, 625, 225, 225, 25};
        int[] yPoints = {75, 75, 325, 325, 125, 125, 75, 75, 375, 375, 125, 125, 325, 325, 375, 375};
        level = new Polygon(xPoints, yPoints, xPoints.length);
        xPoints = new int[]{125, 175, 175, 575, 575, 675, 675, 625, 625, 225, 225, 125};
        yPoints = new int[]{325, 325, 125, 125, 75, 75, 125, 125, 325, 325, 375, 375};
        gridX = new int[] {175, 175, 175, 225, 225, 275, 275, 325, 325, 375, 375, 425, 425, 475, 475, 525, 525, 575, 575, 625};
        gridY = new int[] {325, 225, 125, 175, 275, 225, 125, 175, 275, 225, 125, 175, 275, 225, 125, 175, 275, 225, 125, 75};
        checker = new Polygon(xPoints, yPoints, xPoints.length);
        enemyX = new int[]{610, 175, 610, 175};
        enemyY = new int[]{150, 200, 240, 290};
        enemyDirs = new boolean[]{true, false, true, false};
        enemies = new Rectangle[enemyX.length];

        for(int i = 0; i<enemies.length; i++)
            enemies[i] = new Rectangle(enemyX[i],enemyY[i],15,15);

        xPoints = new int[]{25, 675, 675, 825, 825, 725, 725, 825, 825, 225, 225, 175, 175, 25};
        yPoints = new int[]{300, 300, 100, 100, 250, 250, 300, 300, 450, 450, 350, 350, 450, 450};
        level2 = new Polygon(xPoints, yPoints, xPoints.length);
        xPoints = new int[] {175, 675, 675, 725, 725, 825, 825, 225, 225, 175};
        yPoints = new int[] {300, 300, 250, 250, 300, 300, 450, 450, 350, 350};
        checker2 = new Polygon(xPoints, yPoints, xPoints.length);

        gridX2 = new int[] {225, 225, 275, 325, 325, 375, 425, 425, 475, 525, 525, 575, 625, 625, 675, 675, 725, 725, 775};
        gridY2 = new int[] {300, 400, 350, 300, 400, 350, 300, 400, 350, 300, 400, 350, 300, 400, 350, 250, 300, 400, 350};

        enemyX2 = new int[]{245, 445, 645};
        enemyY2 = new int[]{435, 300, 435};

        plusX = new int[]{342, 342, 342, 342, 320, 305, 290, 364, 379, 394, 540, 540, 540, 540, 518, 503, 488, 562, 577, 592, 740, 740, 740, 740, 718, 703, 688, 762, 777, 792};
        plusY = new int[]{370, 390, 410, 430, 365, 350, 335, 365, 350, 335, 370, 390, 410, 430, 365, 350, 335, 365, 350, 335, 370, 390, 410, 430, 365, 350, 335, 365, 350, 335};

        originalX = new int[plusX.length];
        originalY = new int[plusY.length];
        for (int x = 0; x < plusX.length; x++) {
            originalX[x] = plusX[x];
            originalY[x] = plusY[x];
        }

        enemyDirs2 = new boolean[]{true, false, true};
        enemies2 = new Rectangle[enemyX2.length+plusX.length];

        for(int i = 0; i<enemyX2.length; i++)
            enemies2[i] = new Rectangle(enemyX2[i],enemyY2[i],enemyDim,enemyDim);

        for(int i = enemyX2.length; i<enemies2.length; i++)
            enemies2[i] = new Rectangle(plusX[i-enemyX2.length], plusY[i-enemyX2.length], enemyDim, enemyDim);




        //can change font type/size as necessary
        f=new Font("SANS SERIF",Font.BOLD,30);
        //don't change anything below here except maybe...
        frame.addKeyListener(this);
        frame.add(this);
        //the size of the frame (xWidth, yWidth)
        frame.setSize(850,500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("THE WORLD'S HARDEST GAME");
        t=new Thread(this);
        t.start();
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        //all painting (AND ONLY PAINTING) happens here!
        //Don't use this method to deal with mathematics
        //The painting imps aren't fond of math

        //background
        g2d.setColor(new Color(180,180,254));
        g2d.fillRect(0,0,850,500);

        //menu


        //scoreboard
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,850,50);
        g2d.setColor(Color.WHITE);
        g2d.setFont(f);
        g2d.drawString("MENU",0,30);
        g2d.drawString(levelNum+"/30",380,30);
        g2d.drawString("Deaths: "+deaths,680,30);


        if(levelNum==1)
        {
            //level
            g2d.setColor(new Color(181, 254, 180));
            g2d.fill(level);

            g2d.setColor(new Color(247, 247, 255));
            g2d.fill(checker);

            g2d.setColor(new Color(230,230,255));
            for(int i = 0; i<gridX.length; i++)
            {
                g2d.fillRect(gridX[i],gridY[i],50,50);
            }

            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.BLACK);
            g2d.draw(level);


            //character
            g2d.setColor(Color.RED);
            g2d.fillRect(x, y, heroDim, heroDim);
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(new Color(127, 0, 0));
            g2d.drawRect(x, y, heroDim, heroDim);

            //enemy
            for(int i = 0; i < enemyX.length; i++) {
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(5));
                g2d.drawOval(enemyX[i], enemyY[i], enemyDim, enemyDim);
                g2d.setColor(Color.BLUE);
                g2d.fillOval(enemyX[i], enemyY[i], enemyDim, enemyDim);
            }
        }

        if(levelNum==14)
        {
            //level
            g2d.setColor(new Color(181, 254, 180));
            g2d.fill(level2);

            g2d.setColor(new Color(247, 247, 255));
            g2d.fill(checker2);

            g2d.setColor(new Color(230,230,255));
            for(int i = 0; i<gridX2.length; i++)
            {
                g2d.fillRect(gridX2[i],gridY2[i],50,50);
            }



            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.BLACK);
            g2d.draw(level2);


            //character
            g2d.setColor(Color.RED);
            g2d.fillRect(x2, y2, heroDim, heroDim);
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(new Color(127, 0, 0));
            g2d.drawRect(x2, y2, heroDim, heroDim);

            //enemy
            for(int i = 0; i < enemyX2.length; i++)
            {
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(5));
                g2d.drawOval(enemyX2[i], enemyY2[i], enemyDim, enemyDim);
                g2d.setColor(Color.BLUE);
                g2d.fillOval(enemyX2[i], enemyY2[i], enemyDim, enemyDim);
            }

            for(int i = 0; i<plusX.length; i++)
            {
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(5));
                g2d.drawOval(plusX[i], plusY[i], enemyDim, enemyDim);
                g2d.setColor(Color.BLUE);
                g2d.fillOval(plusX[i], plusY[i], enemyDim, enemyDim);
            }

        }


        if(!pause && levelNum==14)
        {
            g2d.setColor(Color.RED);
            g2d.drawString("Collisions On",10,100);
            g2d.drawString("Press P to turn off",10,130);
        }

        if(pause && levelNum==14)
        {
            g2d.setColor(Color.RED);
            g2d.drawString("Collisions Off",10,100);
            g2d.drawString("Press P to turn on",10,130);
        }

        if(!pause && levelNum==1)
        {
            g2d.setColor(Color.RED);
            g2d.drawString("Collisions On",10,410);
            g2d.drawString("Press P to turn off",10,440);
        }

        if(pause && levelNum==1)
        {
            g2d.setColor(Color.RED);
            g2d.drawString("Collisions Off",10,410);
            g2d.drawString("Press P to turn on",10,440);
        }


        //game End
        if(!gameOn)
        {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("SANS SERIF",Font.BOLD,50));
            g2d.drawString("YOU WIN!",250,250);
        }


    }
    public void run()
    {
        while(true)
        {
            if(gameOn)
            {
                //MATH HAPPENS HERE!!!!


                //if the right key is pressed down it will move right
                //you can use boolean variables to determine the
                //direction of the movement of your character





                //intersection is true if even one point is shared
                //this can be used to determine collisions with obstacles
                //or movement into the winning portion of the level



                if(levelNum==1)
                {
                    if (right && level.contains(new Rectangle(x+2,y,heroDim,heroDim)))
                        x+=2;
                    if(left && level.contains(new Rectangle(x-2,y,heroDim,heroDim)))
                        x-=2;
                    if(up && level.contains(new Rectangle(x,y-2,heroDim,heroDim)))
                        y-=2;
                    if(down && level.contains(new Rectangle(x,y+2,heroDim,heroDim)))
                        y+=2;

                    hero = new Rectangle(x,y,heroDim,heroDim);


                    for (int i = 0; i < enemyX.length; i++) {
                        if (enemyDirs[i])
                            enemyX[i] -= 4;
                        else
                            enemyX[i] += 4;

                        if (enemyX[i] >= 610 || enemyX[i] <= 175)
                            enemyDirs[i] = !enemyDirs[i];


                        enemies[i] = new Rectangle(enemyX[i], enemyY[i], enemyDim, enemyDim);

                        if(!pause)
                        {
                            if (hero.intersects(enemies[i])) {
                                x = startX;
                                y = startY;
                                deaths++;
                            }
                        }
                    }
                }

                if(levelNum==14)
                {

                    if (right && level2.contains(new Rectangle(x2+2,y2,heroDim,heroDim)))
                        x2+=2;
                    if(left && level2.contains(new Rectangle(x2-2,y2,heroDim,heroDim)))
                        x2-=2;
                    if(up && level2.contains(new Rectangle(x2,y2-2,heroDim,heroDim)))
                        y2-=2;
                    if(down && level2.contains(new Rectangle(x2,y2+2,heroDim,heroDim)))
                        y2+=2;



                    hero = new Rectangle(x2,y2,heroDim,heroDim);



                    for (int i = 0; i < enemies2.length; i++) {
                        if (i<enemyX2.length)
                        {
                            if(enemyDirs2[i])
                                enemyY2[i] -= 2;
                            else
                                enemyY2[i] += 2;

                            if(enemyY2[i] >=435 || enemyY2[i]<=300)
                                enemyDirs2[i] = !enemyDirs2[i];

                            enemies2[i] = new Rectangle(enemyX2[i], enemyY2[i], enemyDim, enemyDim);
                        }



                        if(i>=enemyX2.length)
                        {

                            double centerX = 342;
                            double centerY = 370;

                            if(i<=enemyX2.length+9) {
                                centerX = 342;
                                double dx = originalX[i-enemyX2.length] - centerX;
                                double dy = originalY[i-enemyX2.length] - centerY;
                                plusX[i-enemyX2.length] = (int)(centerX + dx * Math.cos(theta) + dy * Math.sin(theta));
                                plusY[i-enemyX2.length] = (int)(centerY - dx * Math.sin(theta) + dy * Math.cos(theta));
                            }
                            else if (i!=enemyX2.length && i<=enemyX2.length+19) {
                                centerX = 540;
                                double dx = originalX[i-enemyX2.length] - centerX;
                                double dy = originalY[i-enemyX2.length] - centerY;
                                plusX[i - enemyX2.length] = (int)(centerX + dx * Math.cos(theta) - dy * Math.sin(theta));
                                plusY[i - enemyX2.length] = (int)(centerY + dx * Math.sin(theta) + dy * Math.cos(theta));

                            }
                            else if(i!=enemyX2.length+19) {
                                centerX = 740;
                                double dx = originalX[i-enemyX2.length] - centerX;
                                double dy = originalY[i-enemyX2.length] - centerY;
                                plusX[i-enemyX2.length] = (int)(centerX + dx * Math.cos(theta) + dy * Math.sin(theta));
                                plusY[i-enemyX2.length] = (int)(centerY - dx * Math.sin(theta) + dy * Math.cos(theta));
                            }



                            enemies2[i] = new Rectangle(plusX[i-enemyX2.length], plusY[i-enemyX2.length], enemyDim, enemyDim);

                        }

                        if(!pause)
                        {
                            if (hero.intersects(enemies2[i])) {
                                x2 = startX2;
                                y2 = startY2;
                                deaths++;
                            }
                        }
                    }

                    theta+=0.040;
                    theta%=2*Math.PI;
                }


                //must be entirely inside for contains to be true
                //polygons are useful for shapes that are irregular
                //such as the outline of the level
                //you can use the method contains to assure your character
                //remains inside the outline of the level


                //win logic
                if(x>=675-heroDim && levelNum==1)
                {
                    levelNum = 14;
                    //WorldsHardestGame app=new WorldsHardestGame();

                }

                if(y2<=255 && levelNum==14)
                {
                    gameOn = false;
                }

                //this is the code for delay
                try
                {
                    t.sleep(10);
                } catch(InterruptedException e)
                {
                }
                repaint();
            }
        }
    }

    public void keyPressed(KeyEvent ke)
    {
        //this method will do stuff if you press/hold a key down

        //this will print keycodes (hint, hint)
        System.out.println(ke.getKeyCode());


        //39 is right arrow key
        if(ke.getKeyCode()==39 || ke.getKeyCode()==68)
            right = true;
        if(ke.getKeyCode()==38 || ke.getKeyCode()==87)
            up = true;
        if(ke.getKeyCode()==37 || ke.getKeyCode()==65)
            left = true;
        if(ke.getKeyCode()==40 || ke.getKeyCode()==83)
            down = true;

        if(ke.getKeyCode()==80)
            pause=!pause;

    }
    public void keyReleased(KeyEvent ke)
    {
        //this will do stuff if you let go of a key
        if(ke.getKeyCode()==39 || ke.getKeyCode()==68)
            right = false;
        if(ke.getKeyCode()==38 || ke.getKeyCode()==87)
            up = false;
        if(ke.getKeyCode()==37 || ke.getKeyCode()==65)
            left = false;
        if(ke.getKeyCode()==40 || ke.getKeyCode()==83)
            down = false;

    }
    public void keyTyped(KeyEvent ke)
    {
        //prob don't use this as it requires a key to be
        //pressed and released to do anything
    }


    public static void main(String args[])
    {
        WorldsHardestGame app=new WorldsHardestGame();
    }
}
