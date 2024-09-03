import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class mainPanel extends JPanel implements MouseListener, KeyListener, ActionListener, Runnable{
    Boolean gameStarted = false;
    App app;
    ArrayList<Player> players;
    Queue<Card> deck = new LinkedList<Card>();
    Stack<Card> discard = new Stack<Card>();

    String gameState = "Unstarted";
    String roundState = "Unstarted";
    String instructions = "";

    ArrayList<JButton> playerButtons = new ArrayList<JButton>();

    int mousex;
    int mousey;

    Thread animationThread = new Thread(this);
    Boolean anim = false;

    Player roundTrigger = null;

    Boolean flipped = false;

    Card held = null;

    Card moving = null;
    int vectorX;
    int vectorY;

    public mainPanel(App a){
        super();
        app = a;
        
        addMouseListener(this);
        addKeyListener(this);
        setSize(1600, 1000);
        //setLayout(new BorderLayout());
        enableInputMethods(true);
        for(Integer i = 2; i<=4; i++){
            JButton b = new JButton(i.toString());
            add(b);
            playerButtons.add(b);
            b.setBounds(200+100*i, 1200+100*i, 80, 80);
            b.setPreferredSize(new Dimension(80, 80));
            b.addActionListener(this);

            
            b.setVisible(true);
            revalidate();
        }
        this.setVisible(true);
    }

    public void paint(Graphics g){
        if(gameStarted){
            try {
                g.drawImage(ImageIO.read(mainPanel.class.getResource("bkg.png")), 0, 0, 1585, 961, null);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        Font f = g.getFont();
        f = new Font(f.getName(), f.getStyle(), 20);
        g.setFont(f);
        g.drawString(players.get(0).toString(),50, 60);
        g.drawString(instructions,500, 60);


        int drawx = 170;
        int drawy = 150;
        for(ArrayList<Card> x:players.get(0).cards){
            drawx += 150;
            drawy = 150;
            for(Card c:x){
                
                drawy += 200;
                if(c!=null){
                    c.x = drawx;
                    c.y = drawy;
                    c.draw(g);  
                }
                
                
            }
        }
        deck.peek().x = 700;
        deck.peek().y = 100;
        deck.peek().draw(g);

        try {
            discard.peek().facedown = false;
            discard.peek().x = 400;
            discard.peek().y = 100;
            discard.peek().draw(g);
        } catch (Exception e) {
            // TODO: handle exception
        }

        if(held!=null){
            held.draw(g);
        }
        if(moving!=null){
            moving.draw(g);
        }
        ArrayList<Player> scoreOrder = new ArrayList<Player>();
        for(int i = 0; i<players.size(); i++){
            scoreOrder.add(new Player(-1));
        }
        Collections.copy(scoreOrder, players);
        Player.overall = true;
        Collections.sort(scoreOrder);
        Player.overall = false;

        int scorex = 1300;
        int scorey = 40;

        for(Player p:scoreOrder){
            g.drawString(p.toString()+" - "+p.getTotalScore()+" ["+p.getRoundScore()+"]", scorex, scorey+=20);
        }

        }else{
            try {
                g.drawImage(ImageIO.read(mainPanel.class.getResource("startbkg.png")), 0, 0, 1585, 961, null);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        paintComponents(g);
        //g.fillRect(mousex + 10, mousey + 10, 10, 10);
        
    }

    

    public void startGame(int numPlayers){
        gameStarted = true;

        players = app.players;
        ArrayList<Card> cards = createCards();
        for(int i = 1; i<=numPlayers; i++){
            players.add(new Player(i));
            players.getLast().addCards(cards);
        }        

        while(cards.size()>0){
            deck.add(cards.removeFirst());
        }

        discard.add(deck.remove());
        gameState = "Round";
        roundState = "TakeTwo";
        instructions = "Choose two cards to flip over";
    }

    public void startRound(){
        ArrayList<Card> cards = createCards();
        for(Player p:players){
            p.addCards(cards);
        }       
        deck = new LinkedList<Card>();
        while(cards.size()>0){
            deck.add(cards.removeFirst());
        }
        discard = new Stack<Card>();
        discard.add(deck.remove());
        gameState = "Round";
        roundState = "Take card";
        instructions = "Take a card";
        flipped = false;
        while(players.get(0)!=roundTrigger){
            players.add(players.removeFirst());
        }
        roundTrigger = null;

        for(Player p:players){
            if(p.getTotalScore()>=100){
                gameState = "Ended";
                instructions = "Game Ended";
                break;
            }
        }
        if(gameState=="Ended"){
            discard.peek().facedown = true;
        }
    }

    public ArrayList<Card> createCards(){
        ArrayList<Card> cards = new ArrayList<Card>();

        for(int i = 0; i<5; i++){
            cards.add(new Card(-2));
            cards.add(new Card(-1));
            cards.add(new Card(-1));
            cards.add(new Card(0));
            cards.add(new Card(0));
            cards.add(new Card(0));
            for(int j = 0; j<2; j++){
                for(int c = 1; c<=12; c++){
                    cards.add(new Card(c));
                }
            }
        }
        Collections.shuffle(cards);
        return cards;
    }

    public void periodic(){
        if(anim){
            animationThread.run();
        }else{
            animationThread.interrupt();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    }
    @Override
    public void keyPressed(KeyEvent e) {
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
       
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    public void endTurn(){
        if(players.get(1)==roundTrigger){
            roundState = "Ended";
            gameState = "Between";
            instructions = "Click to reveal cards";
            players.add(players.removeFirst());
        }else{
            if(roundTrigger==null&&ap().faceups()==ap().all().size()){
                roundTrigger = ap();
            }
            players.add(players.removeFirst());
            roundState = "Take card";
            instructions = "Take a card";
        }
        
        
        periodic();
    }

    public Boolean allFlippedTwo(){
        for(Player p:players){
            if(p.faceups()!=2){
                return false;
            }
        }
        return true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(gameState == "Ended"){

        }
        else if(roundState.equals("TakeTwo")){
            if(instructions=="Click anywhere to go to next player"){
                players.add(players.remove(0));
                instructions = "Choose two cards to flip over";
            }
            if(instructions=="Click anywhere to start game"){
                Collections.sort(players, Comparator.reverseOrder());
                roundState = "Take card";
                instructions = "Take a card";
            }
            if(ap().faceups()<2){
                //System.out.println(ap().faceups());
                for(Card c:ap().all()){
                    if(c.clickedOn(x, y)){
                        c.facedown = false;
                        break;
                    }
                }
                if(ap().faceups()==2){
                    instructions="Click anywhere to go to next player";
                    if(allFlippedTwo()){
                        instructions="Click anywhere to start game";
                    }
                }
            }
        }
        else if(roundState == "Take card"){//statement
            if(deck.peek().clickedOn(e.getX(), e.getY())){
                hold(deck.peek());
                deck.peek().facedown=false;
                deck.remove();
                roundState = "Place card";
                instructions = "Place your card";
            }
            try {
                if(discard.peek().clickedOn(e.getX(), e.getY())){
                    hold(discard.peek());
                    discard.peek().facedown=false;
                    discard.pop();
                    roundState = "Place card";
                    instructions = "Place your card";
                }
            } catch (Exception exception) {
                // TODO: handle exception
            }
        }
        else if(roundState=="Place card"){
            Boolean placed = false;
            if(instructions == "Click to go to next turn"){
                endTurn();
            }else if(instructions == "Select a card to flip over"){
                for(Card c:ap().all()){
                    if(c.facedown&&c.clickedOn(x, y)){
                        c.facedown = false;
                        instructions = "Click to go to next turn";
                        placed = true;
                    }
                }
            }
            for(Card c:ap().all()){
                if(!placed&&c.clickedOn(x, y)){
                    ap().addCard(held, c);
                    release();
                    moveToDiscard(c);
                    instructions = "Click to go to next turn";
                    placed = true;
                    if(ap().hasThree()!=-1){
                        ArrayList<Card> sendToDiscard = ap().cards.remove(ap().hasThree());
                        for(Card ca:sendToDiscard){
                            moveToDiscard(ca);
                        }
                    }
                    break;
                }
            }
            if(!placed&&discard.peek().clickedOn(x, y)){
                discard.add(held);
                release();
                instructions = "Select a card to flip over";
                placed = true;
            }
        }else if(roundState == "Ended"){
            
            if(!flipped){
                for(Card c:ap().all()){
                    c.facedown = false;
                }
                instructions = "Click to advance";
            }else{
                instructions = "Click to reveal cards";
                if(players.get(1)==roundTrigger){
                    roundState = "Ready";
                    instructions = "Click to start new round";
                }
                players.add(players.removeFirst());
            }
            flipped = !flipped;
        }else if(roundState == "Ready"){
            for(Player p:players){
                if(p!=roundTrigger&&p.getRoundScore()<=roundTrigger.getRoundScore()){
                    roundTrigger.Double();
                }
            }for(Player p:players){
                p.newRound();
            }
            startRound();
        }
        periodic();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(JButton b:playerButtons){
            if(e.getSource()==b){
                startGame(Integer.parseInt(b.getText()));
                for(JButton j:playerButtons){
                    j.setVisible(false);
                }
                break;
            }
        }
        anim = true;
        periodic();
    }

    void hold(Card c){
        held = c;
        anim = true;
        periodic();
    }

    void releaseTo(int resultx, int resulty){
        if(held!=null){
            held.x = resultx;
            held.y = resulty;
            held = null;
            anim = false;
        }
        //periodic();
    }

    void release(){
        held = null;
        anim = false;
        //periodic();
    }

    void moveToDiscard(Card c){
        //400, 100
        /*Double targetSeconds = 1.0;
        int x = c.x;
        int y = c.y;

        vectorX = (int)((400-x)/(targetSeconds*50));
        vectorY = (int)((100-y)/(targetSeconds*50));

        System.out.println(vectorX);
        System.out.println(vectorY);

        anim = true;
        moving = c;*/
        c.facedown = false;
        discard.add(c);        
    }

    @Override
    public void run() {
        Runnable Philip = new Runnable() {
            public void run() {
                Point point = MouseInfo.getPointerInfo().getLocation();
                //Point location = mainPanel.this.getLocation();
                mainPanel.this.mousex = (int) (point.getX());
                mainPanel.this.mousey = (int) (point.getY());
                
                if(mainPanel.this.held!=null){
                    mainPanel.this.held.x = (int) (point.getX());
                    mainPanel.this.held.y = (int) (point.getY());
                }
                if(mainPanel.this.moving!=null){
                    /*if(mainPanel.this.moving.x>390&&mainPanel.this.moving.x<410&&mainPanel.this.moving.y>90&&mainPanel.this.moving.y<110){
                        mainPanel.this.anim = false;
                        discard.add(mainPanel.this.moving);
                        mainPanel.this.moving = null;
                    }else*/{
                        mainPanel.this.moving.x += mainPanel.this.vectorX;
                        mainPanel.this.moving.y += mainPanel.this.vectorY;
                        //System.out.println("Vectors added");
                    }
                }
                mainPanel.this.repaint();

                //System.out.println(point);
            }
        };
        
        
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(Philip, 0, 20, TimeUnit.MILLISECONDS);
    }

    public Player ap(){
        return players.get(0);
    }
}
