import java.awt.Graphics;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Card {
    public Integer number;
    public Boolean facedown;
    public Card(int num){
        number = num;
        facedown = true;
    }

    int x = 0;
    int y = 0;
    int w = 116;
    int h = 180;

    public void draw(Graphics g){
        if(facedown){
            try {
                g.drawImage(ImageIO.read(Card.class.getResource("/Images/CardBack.png")), x, y, w, h, null);
                //System.out.println("Card drawn "+x+" "+y);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            try {
                g.drawImage(ImageIO.read(Card.class.getResource("/Images/"+number.toString()+".png")), x, y, w, h, null);
                //System.out.println("Card drawn "+x+" "+y);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }

    public Boolean clickedOn(int clickx, int clicky){
        return clickx>=x&&clickx<=x+w&&clicky>=y&&clicky<=y+h;
        //return false;
    }
}
