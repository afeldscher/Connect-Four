import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JComponent;

public class Painter extends JComponent {

    private DropThread dropThread;
    private int prevMode = 0;
    private int mode = 0; //0 = player 1's turn
    private ConnectFour main = new ConnectFour();
    private String spaces[][] = new String[7][6];
    private Color clr;
    private int dropColumn;

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.darkGray);
        g2.fillRect(0, 0, main.getWindow().getWidth(), main.getWindow().getHeight());
        g2.setFont(new Font("Arial Black", Font.PLAIN, 20));
        g2.setColor(Color.WHITE);
        g2.drawString("Connect Four", main.getWindow().getWidth() / 2 - 75, 30);
        drawBoard(g2);
        mode = (main.getDrop() ? 2 : mode); //if drop is true go to mode 2 (drop mode)
        String winner = checkWinner();
        
        if (!winner.equals("")){ //Winner
            g2.setColor(Color.white);
            g2.drawString("Click to Restart", 260, 95);
            g2.setFont(new Font("Arial Black", Font.PLAIN, 40));
            g2.setColor(winner.equals("r") ? Color.red : Color.BLACK);
            g2.drawString((winner.equals("r") ? "Red" : "Black") + " Wins!", main.getWindow().getWidth() / 2 - 105, 70);
            if (main.getDrop()){
                restart();
            }
            return;
        } else if (fullBoard()){
            g2.setColor(Color.white);
            g2.drawString("Click to Restart", 260, 95);
            g2.setFont(new Font("Arial Black", Font.PLAIN, 40));
            g2.drawString("No Winner", main.getWindow().getWidth() / 2 - 105, 70);
            if (main.getDrop()){
                restart();
            }
            return;
        }
        switch (mode) {
            case 0://red
                drawPiece(g2, Color.red, new Point(pxToBoard(main.getMouseLocation()).x, -1));
                prevMode = 0;
                break;
            case 1://black
                drawPiece(g2, Color.black, new Point(pxToBoard(main.getMouseLocation()).x, -1));
                prevMode = 1;
                break;
            case 2://preDrop
                main.setDrop(false);
                dropColumn = pxToBoard(main.getMouseLocation()).x;
                clr = (prevMode == 0 ? Color.red : Color.black); //color based on previous mode
                if (dropLevel(dropColumn) == -1) {
                    mode = prevMode;
                    main.getWindow().repaint();
                    return;
                }
                dropThread = new DropThread(dropColumn, dropLevel(dropColumn));
                dropThread.start();
                mode = 3;
                break;
            case 3:
                if (!dropThread.isAlive()) {
                    mode = 4;
                    main.getWindow().repaint();
                }
                drawBoard(g2);
                drawPiece(g2, clr, dropThread.getLoc());
                break;
            case 4:
                spaces[dropColumn][dropLevel(dropColumn)] = clr.equals(Color.red) ? "r" : "b"; //if the color is red record red for the space, or black
                mode = (prevMode == 0 ? 1 : 0); //if it was red's turn now its blacks vice versa
                main.getWindow().repaint();
                break;
        }
    }
    
    private void restart(){
        spaces = new String[7][6];
        main.setDrop(false);
        mode = 0;
        main.getWindow().repaint();
    }
    
    private boolean fullBoard(){
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) {
                if (spaces[x][y] == null || spaces[x][y].equals("")){
                    return false;
                }
            }
        }
        return true;
    }

    private String checkWinner() {
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) { //for each space
                //4 different directions of possible 4 piece set. 2 down angles, down, and right
                //if statment layout:   * remember each part is evaluated in order, once one test is false the rest are not evaluated in that if. 
                // failsafes that piece will be in range. Then see if next piece is the same as current. 
                //Then if that is true check for a sequence in that direction. Then return the color if it is a win.
                
                if (x + 1 < 7 && spaces[x][y] != null && spaces[x + 1][y] != null && spaces[x + 1][y].equals(spaces[x][y]) && checkRow(x, y, 1, 0)) return spaces[x][y];  
                if (y + 1 < 6 && spaces[x][y] != null && spaces[x][y + 1] != null && spaces[x][y + 1].equals(spaces[x][y]) && checkRow(x, y, 0, 1)) return spaces[x][y];
                if (x + 1 < 7 && y + 1 < 6 && spaces[x][y] != null && spaces[x + 1][y + 1] != null && spaces[x + 1][y + 1].equals(spaces[x][y]) && checkRow(x, y, 1, 1)) return spaces[x][y];
                if (x - 1 > -1 && y + 1 < 6 && spaces[x][y] != null && spaces[x - 1][y + 1] != null && spaces[x - 1][y + 1].equals(spaces[x][y]) && checkRow(x, y, -1, 1))  return spaces[x][y];
            }
        }
        return "";
    }

    private boolean checkRow(int x, int y, int xDir, int yDir) {
        int currX, currY;
        for (int i = 1; i < 4; i++) {
            currX = x + (i * xDir);
            currY = y + (i * yDir);
            if (currX > 6 || currY > 5 || currX < 0 || currY < 0 || !spaces[x][y].equals(spaces[currX][currY])) { //if the new space does not equal the first for 4 spaces
                return false;
            }
        }
        return true; //winner
    }

    private int dropLevel(int column) { //returns the lowest available place
        for (int i = 5; i >= 0; i--) {
            if (spaces[column][i] == null || spaces[column][i].equals("")) {
                return i;
            }
        }
        return -1;
    }

    private void delay(int dur) {
        try {
            Thread.sleep(dur);
        } catch (InterruptedException ex) {
        }
    }

    private void drawPieces(Graphics2D g2) {
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) {
                if (spaces[x][y] != null && !spaces[x][y].equals("")) { //if you do have to draw a piece in that spot
                    drawPiece(g2, (spaces[x][y].equals("r") ? Color.red : Color.black), new Point(x, y));
                }
            }
        }
    }

    private void drawPiece(Graphics2D g2, Color clr, Point location) { //location is in terms of the board, 0,0 at upper left
        g2.setColor(clr);
        if (location.y == -1) {
            g2.fillOval(boardToPx(location).x - 3, 30, 66, 66);
            return;
        }
        g2.fillOval(boardToPx(location).x, boardToPx(location).y, 60, 60);
    }

    private Point boardToPx(Point in) {
        int x = 110 + (70 * in.x);
        int y = 120 + (70 * in.y);
        return new Point(x, y);
    }

    private Point pxToBoard(Point in) {
        int x = (int) Math.floor((in.x - 110) / 70);
        int y = (int) Math.floor((in.y - 120) / 70);
        if (x < 0) {
            return new Point(0, 0);
        } else if (x > 6) {
            return new Point(6, 0);
        }
        return new Point(x, y);
    }

    private void drawBoard(Graphics2D g2) {
        g2.setColor(Color.yellow);
        g2.fillRect(100, 100, 500, 450);
        g2.setColor(Color.CYAN);
        g2.fillRect(80, 250, 20, 380);
        g2.fillRect(600, 250, 20, 380);
        g2.fillRect(125, 550, 450, 10);
        g2.setColor(Color.darkGray);
        for (int i = 120; i <= 470; i += 70) {
            for (int j = 110; j <= 530; j += 70) {
                g2.fillOval(j, i, 60, 60);
            }
        }
        drawPieces(g2);
    }

}
