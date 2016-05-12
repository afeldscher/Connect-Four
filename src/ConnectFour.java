import java.awt.Container;
import java.awt.Point;
import javax.swing.JFrame;

public class ConnectFour {

    static JFrame window = new JFrame("Connect Four");
    static Painter paint = new Painter();
    static Clicks click = new Clicks();

    public static void main(String[] args) {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        Container contentPane = window.getContentPane();
        contentPane.setLayout(null);
        paint.setBounds(0, 0, 700, 650);
        window.add(paint);
        window.setBounds(550, 130, 700, 650);
        window.setVisible(true);
        window.addMouseListener(click);
        window.addMouseMotionListener(click);
    }

    public JFrame getWindow() {
        return window;
    }

    public Point getMouseLocation() {
        return click.getMouseLocation();
    }

    public boolean getDrop() {
        return click.getDrop();
    }

    public void setDrop(boolean drop) {
        click.setDrop(drop);
    }
    public void postDrop(){
        click.setDrop(false);
        window.repaint();
    }
    public void restart(){
        main(null);
        //System.exit(0);
    }

}
