import java.util.ArrayList;
import java.util.HashSet;

public class Player implements Comparable<Player>{
    public ArrayList<ArrayList<Card>> cards = new ArrayList<ArrayList<Card>>();
    public int num;
    public int score;

    public Player(int number){
        num = number;
    }

    public void addCards(ArrayList<Card> cardList){
        for(int i = 0; i<4; i++){
            cards.add(new ArrayList<Card>());
            for(int j = 0; j<3; j++){
                this.cards.get(i).add(cardList.remove(0));
            }
        }
    }

    public HashSet<Card> all(){
        HashSet<Card> all = new HashSet<Card>();
        for(ArrayList<Card> x:cards){
            for(Card c:x){
                all.add(c);
            }
        }
        return all;
    }

    public String toString(){
        return "Player "+num;
    }

    public Integer faceups(){
        int i = 0;
        for(Card c:all()){
            if(c.facedown==false){
                i++;
            }
        }
        return i;
    }

    public int getRoundScore(){
        int i = 0;
        for(Card c:all()){
            if(!c.facedown){
                i += c.number;
            }
        }
        return i;
    }

    public void addCard(Card incoming, Card outgoing){
        for(ArrayList<Card> x:cards){
            for(Card c:x){
                if(c==outgoing){
                    x.set(x.indexOf(outgoing), incoming);
                }
            }
        }
    }

    @Override
    public int compareTo(Player o) {
        return getRoundScore() - o.getRoundScore();
    }

    public int hasThree(){
        
        for(ArrayList<Card> x:cards){
            Boolean allFaceUp = !x.get(0).facedown&&!x.get(1).facedown&&!x.get(2).facedown;
            if(allFaceUp&&x.get(0).number==x.get(1).number&&x.get(1).number==x.get(2).number){
                return cards.indexOf(x);
            }
        }
        return -1;
    }


}
