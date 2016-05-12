
import java.awt.Point;

public class DropThread extends Thread {

    private Point currPoint;
    private int dropColumn, dropLevel;
    private ConnectFour main = new ConnectFour();

    public DropThread(int dropColumn, int dropLevel) {
        this.dropColumn = dropColumn;
        this.dropLevel = dropLevel;
        currPoint = new Point(dropColumn, 0);
    }

    public void run() {
        for (int i = 0; i <= dropLevel; i++) {
            delay(35);
            currPoint = new Point(dropColumn, i);
            main.getWindow().repaint();
        }
    }

    public Point getLoc() {
        return currPoint;
    }

    private void delay(int dur) {
        try {
            Thread.sleep(dur);
        } catch (InterruptedException ex) {
        }
    }
}
