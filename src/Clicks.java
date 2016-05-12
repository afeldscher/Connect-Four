

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Clicks implements MouseListener, MouseMotionListener {

    private Point mouseLocation = new Point(0, 0);
    private ConnectFour main = new ConnectFour();
    private boolean drop;

    public void mouseReleased(MouseEvent me) {
        Point location = me.getPoint();
        drop = true;
        main.getWindow().repaint();
    }

    public void mouseMoved(MouseEvent me) {
        Point location = me.getPoint();
        mouseLocation = location;
        main.getWindow().repaint();
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }

    public void mouseClicked(MouseEvent me) {
    }

    public void mousePressed(MouseEvent me) {
    }

    public void mouseDragged(MouseEvent me) {

    }

    public Point getMouseLocation() {
        return mouseLocation;
    }

    public boolean getDrop() {
        return drop;
    }
    public void setDrop(boolean drop){
        this.drop = drop;
    }

}
