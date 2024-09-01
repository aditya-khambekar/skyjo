import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;

public class App extends JFrame implements MouseListener{
    Boolean started = false;
    public ArrayList<Player> players = new ArrayList<Player>();
    public mainPanel mainPanel;
    public static void main(String[]args){
        App app = new App("Skyjo");
        app.setVisible(true);
    }

    public App(String framename){
        super(framename);
        setSize(1600, 1000);
        this.setLayout(null);
        addMouseListener(this);
        mainPanel m = new mainPanel(this);
        mainPanel = m;
        m.setBounds(0, 0, getWidth(), getHeight());
        add(m);
        setResizable(false);
        createPlayers(3);
    }

    public void createPlayers(int num){
        /*ArrayList<Player> list = new ArrayList<Player>();
        for(int i = 1; i<=num; i++){
            list.add(new Player(i, this));
        }

        players = list;*/
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}
