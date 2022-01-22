import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    /***
     * размер поля
     */
    private final int SIZE = 320;
    /***
     * размер в пикселях сколько будет занимать ячейка самой змейки и яблока
     */
    private final int DOT_SIZE = 16;
    /***
     * параметр указывающий сколько игровых едениц может уместиться на поле
     */
    private final int ALL_DOTS = 400;
    /***
     * рисунок секции змеи
     */
    private Image dot;
    /***
     * рисунок яблока
     */
    private Image apple;
    /***
     * позиции яблока
     */
    private int appleX;
    private int appleY;

    /***
     * два массива для сохранения положения змейки
     */
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];

    private int dots;

    private Timer timer;

    /***
     * переменные для перемещения змейки
     */
    private boolean left;
    private boolean right = true;
    private boolean up;
    private boolean down;
    private boolean inGame = true;

    public GameField() {
        setBackground(Color.BLACK);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    /***
     * Метод для загрузки картинок
     */
    public void loadImages() {
        ImageIcon iia = new ImageIcon("src/assets/apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("src/assets/dot.png");
        dot = iid.getImage();
    }

    /***
     * Метод который инициализирует начало игры
     */
    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(250, this);
        timer.start();
        createApple();

    }

    /***
     * Метод создает новое яблоко
     */
    public void createApple() {
        appleX = new Random().nextInt(20) * DOT_SIZE;
        appleY = new Random().nextInt(20) * DOT_SIZE;
    }

    /***
     * Метод инициализирующий двидение змейки
     */
    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    /***
     * Метод проверяющий встретили мы яблоко
     */
    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            createApple();
        }
    }

    /***
     * Метод проверяющий столкнулись ли мы с бордюром или с самим собой
     */
    public void checkCollisions() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (x[0] > SIZE) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (y[0] > SIZE) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollisions();
            move();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        } else {
            String end = "Game Over";
            g.setColor(Color.WHITE);
            g.drawString(end, 125, SIZE / 2);
        }
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && !down) {
                left = false;
                up = true;
                right = false;
            }
            if (key == KeyEvent.VK_DOWN && !up) {
                left = false;
                down = true;
                right = false;
            }
        }
    }
}
