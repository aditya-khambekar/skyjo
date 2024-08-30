import javax.swing.JFrame;

public class App extends JFrame{
    public static void main(String[]args){
        App app = new App("Skyjo");
        app.setSize(1600, 1000);
        app.setVisible(true);
    }

    public App(String framename){
        super(framename);
    }
}
