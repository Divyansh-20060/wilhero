import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.fxml.Initializable;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javafx.animation.ScaleTransition;
import javafx.animation.Interpolator;


class game_data{
    public String helmet = "hero.png";
    public int Score = 0;
}

class gameobjcts{
    public ImageView Node;
    //private void Collision(){}
}

class islands extends gameobjcts {
    public int coins = 0;
    public islands(double x, double y){
        Node = new ImageView("island.png");
        Node.setLayoutX(x);
        Node.setLayoutY(y);
    }
}

class Orc extends gameobjcts{
	public Timeline down_Timeline;
	public Timeline up_Timeline;
	public Timeline right_Timeline;
    public Orc (double x, double y){
        String orcT[] = {"Orc1.png","RedOrc1.png"};
        Random rand = new Random();
        int OT = rand.nextInt(2);
        Node = new ImageView(orcT[OT]);
        Node.setLayoutX(x);
        Node.setLayoutY(y);
    }
    public void setDown_Timeline(Timeline down_Timeline){
    	this.down_Timeline = down_Timeline;
    }
    public void setUp_Timeline(Timeline up_Timeline){
    	this.up_Timeline = up_Timeline;
    }
    public void setRight_Timeline(Timeline right_Timeline){
    	this.right_Timeline = right_Timeline;
    }
    
    

    
}

class Hero extends gameobjcts{
	Timeline up_Timeline;
	Timeline down_Timeline;
	Timeline right_Timeline;
	Timeline left_Timeline;
	
    public Hero(String st){
        Node = new ImageView(st);
        Node.setLayoutX(60);
        Node.setLayoutY(200);

    }
    
    public void setUp_Timeline(Timeline up_Timeline) {
    	this.up_Timeline = up_Timeline;
    }
    public void setDown_Timeline(Timeline down_Timeline) {
    	this.down_Timeline = down_Timeline;
    }
    public void setRight_Timeline(Timeline right_Timeline) {
    	this.right_Timeline = right_Timeline;
    }
    public void setLeft_Timeline(Timeline left_Timeline) {
    	this.left_Timeline = left_Timeline;
    }
    
    
}

class Coin extends gameobjcts{
    public Coin(double x, double y){
        Node = new ImageView("Coin.png");
        Node.setLayoutX(x+90);
        Node.setLayoutY(y);
    }
}

class Game implements Serializable{
	boolean isPaused;
    public int[] isl = {0,0};
    public int coinsCollected;
    public Hero hero;
    
    public ArrayList<islands> Islands = new ArrayList<islands>();
    public ArrayList<Orc> Orcs = new ArrayList<Orc>();
    public ArrayList<Coin> Coins = new ArrayList<Coin>();
    public Game(String st){
        hero = new Hero(st);
        
        islands i1 = new islands(40,300);
        Islands.add(i1);
        for (int i = 0; i<49;i++){
        IslandSpawner();
        }
    }
    
    public void pauseGame() {
    	this.isPaused = true;
    	
    	
    	
    	hero.up_Timeline.pause();
    	hero.down_Timeline.pause();
    	hero.right_Timeline.pause();
    	hero.left_Timeline.pause();
    	for (int i = 0; i < Orcs.size(); i++) {
    		Orcs.get(i).down_Timeline.pause();
    		Orcs.get(i).up_Timeline.pause();
    		Orcs.get(i).right_Timeline.pause();
    	}
    }
    
    public void resumeGame() {
    	if (hero.up_Timeline.getStatus() == Animation.Status.PAUSED) {
    		hero.up_Timeline.play();
    	}
    	if (hero.down_Timeline.getStatus() == Animation.Status.PAUSED) {
    		hero.down_Timeline.play();
    	}
    	if (hero.right_Timeline.getStatus() == Animation.Status.PAUSED) {
    		hero.right_Timeline.play();
    	}
    	if (hero.left_Timeline.getStatus() == Animation.Status.PAUSED) {
    		hero.left_Timeline.play();
    	}
    	for (int i = 0; i < Orcs.size(); i++) {
    		if (Orcs.get(i).down_Timeline.getStatus() == Animation.Status.PAUSED) {
    			Orcs.get(i).down_Timeline.play();
    		}
    		if (Orcs.get(i).up_Timeline.getStatus() == Animation.Status.PAUSED) {
    			Orcs.get(i).up_Timeline.play();
    		}
    		
    		if (Orcs.get(i).right_Timeline.getStatus() == Animation.Status.PAUSED) {
    			Orcs.get(i).right_Timeline.play();
    		}
    	}
    	
    	
    }

    public void IslandSpawner(){
        double New_Y[] = {-50,0,50};
        Random rand = new Random();
        int y = rand.nextInt(3);
        boolean OV = rand.nextBoolean();
        islands i = new islands((Islands.get(Islands.size()-1).Node.getLayoutX()+ 420),
                (Islands.get(Islands.size()-1).Node.getLayoutY() + New_Y[y]));
        if (i.Node.getLayoutY() > 450){
            i.Node.setLayoutY(450);
        }

        if (i.Node.getLayoutY() < 200){
            i.Node.setLayoutY(200);
        }
        Islands.add(i);
        if (OV){
            Orc orc = new Orc(i.Node.getLayoutX() + 90,i.Node.getLayoutY() - 55);
            
            Timeline down_Timeline = new Timeline(new KeyFrame(Duration.millis(2.6), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                	if (orc.up_Timeline.getStatus() != Animation.Status.RUNNING && 
                			orc.right_Timeline.getStatus() != Animation.Status.RUNNING) {
                		
                		orc.Node.setLayoutY(orc.Node.getLayoutY() + 1);
                		
                		 if(hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) &&
                				(hero.Node.getBoundsInParent().getMaxY()  >= orc.Node.getBoundsInParent().getMinY()) && 
                				(hero.Node.getBoundsInParent().getMinY()  <= orc.Node.getBoundsInParent().getMaxY()) && 
                				(hero.Node.getBoundsInParent().getMaxX()  >= orc.Node.getBoundsInParent().getMinX() ) && 
                				(hero.Node.getBoundsInParent().getMinX() <= orc.Node.getBoundsInParent().getMinX()) && 
                				(hero.down_Timeline.getStatus() != Animation.Status.RUNNING) && 
                				(hero.up_Timeline.getStatus() != Animation.Status.RUNNING)  )  {
                			
                			//hero hits the side of orc
                			hero.right_Timeline.stop();
                			hero.down_Timeline.play();
                			
                			orc.right_Timeline.setCycleCount(120);
                			orc.right_Timeline.play();
                		}
                		
                		
                		else if(hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) && 
                				(hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMinY() ) && 
                				(hero.Node.getBoundsInParent().getMinY() <= orc.Node.getBoundsInParent().getMinY())) {
                			
                			//hero hits the top of orc
                	    	hero.up_Timeline.setCycleCount(56);
                	    	
                	    	if(hero.up_Timeline.getStatus() != Animation.Status.RUNNING) {
                	    		hero.up_Timeline.play();
                	    	}
                	    	
                	        
                	    	hero.up_Timeline.setOnFinished(e -> hero.down_Timeline.play());
                	    	
                	    	
                		}
                		

                		else if (hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) && 
                				(hero.Node.getBoundsInParent().getMinY() <= orc.Node.getBoundsInParent().getMaxY() ) && 
                				( hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMaxY() )) {
                			
                			pauseGame(); 
                			
                		}
                		

                	}
                    for (int i = 0;i < Islands.size(); i++){
                        if(orc.Node.getBoundsInParent().intersects(Islands.get(i).Node.getBoundsInParent())){
                        orc.down_Timeline.stop();
                        

                        orc.up_Timeline.setCycleCount(160);
                        orc.up_Timeline.play();
                        orc.up_Timeline.setOnFinished(ev -> orc.down_Timeline.play());
                        }

                    }
                }
            }
            ));
            
            Timeline up_Timeline = new Timeline(new KeyFrame(Duration.millis(2.6), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                	if ( orc.right_Timeline.getStatus() != Animation.Status.RUNNING) {
                		
                    	orc.Node.setLayoutY(orc.Node.getLayoutY() - 1);
                    	
                		if(hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) &&
                				(hero.Node.getBoundsInParent().getMaxY()  >= orc.Node.getBoundsInParent().getMinY()) && 
                				(hero.Node.getBoundsInParent().getMinY()  <= orc.Node.getBoundsInParent().getMaxY()) && 
                				(hero.Node.getBoundsInParent().getMaxX()  >= orc.Node.getBoundsInParent().getMinX() ) && 
                				(hero.Node.getBoundsInParent().getMinX() <= orc.Node.getBoundsInParent().getMinX()) && 
                				(hero.down_Timeline.getStatus() != Animation.Status.RUNNING) && 
                				(hero.up_Timeline.getStatus() != Animation.Status.RUNNING)  )  {
                			
                			//hero hits the side of orc
                			hero.right_Timeline.stop();
                			hero.down_Timeline.play();
                			
                			orc.right_Timeline.setCycleCount(120);
                			orc.right_Timeline.play();
                		}

                    	else if(hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) && 
                				(hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMinY() ) && 
                				(hero.Node.getBoundsInParent().getMinY() <= orc.Node.getBoundsInParent().getMinY()) ) {
                			
                			//hero hits the top of orc
                	    	hero.up_Timeline.setCycleCount(90);
                	    	if(hero.up_Timeline.getStatus() != Animation.Status.RUNNING) {
                	    		hero.up_Timeline.play();
                    	    }
                	    	orc.up_Timeline.stop();
                	    	orc.down_Timeline.play();
                	    	hero.up_Timeline.setOnFinished(e -> hero.down_Timeline.play());	
                		}

                		else if (hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) && 
                				(hero.Node.getBoundsInParent().getMinY() <= orc.Node.getBoundsInParent().getMaxY() ) && 
                				( hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMaxY() )) {
                			
                			pauseGame();
                			
                		}

                		
                    }
                }
            }
            ));
            
            Timeline right_Timeline = new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                	
                	
                	orc.Node.setLayoutX(orc.Node.getLayoutX() + 1);
                	
                	orc.right_Timeline.setOnFinished(t -> orc.down_Timeline.play());
                }
            }
            ));
            
            
            
            
            orc.setDown_Timeline(down_Timeline);
            orc.setUp_Timeline(up_Timeline);
            orc.setRight_Timeline(right_Timeline);
            
            Orcs.add(orc);
        }

        else{
            int No_C = rand.nextInt(4);
            if(No_C != 0){
                i.coins = No_C;
                double DX = i.Node.getLayoutX() + 30;
                for(int j = 0; j<2;j ++){
                    double DY = i.Node.getLayoutY() - 140;
                    for(int k = 0; k <No_C; k++){
                        
                        Coins.add(new Coin(DX,DY));
                        DY = DY - 40;
                    }
                    DX = DX - 50;
                }
            }
        }

        //return i;
    }

    public void save(Game G1) throws IOException {
        isl[0] = 200;
        isl[1] = 200;
        String st = "GAME34.ser";
        FileOutputStream fileout = new FileOutputStream(st);
        ObjectOutputStream out = new ObjectOutputStream(fileout);
        out.writeObject(G1.isl);
        out.close();
        fileout.close();
        System.out.println("Saved!");
    }

    public int[] load() throws IOException, ClassNotFoundException {
        int g1[] = null;
        FileInputStream filein = new FileInputStream("GAME34.ser");
        ObjectInputStream in = new ObjectInputStream(filein);
        g1 = (int[]) in.readObject();
        in.close();
        filein.close();
        System.out.println("loaded");
        return g1;
    }
}

public class GameController implements Initializable{

    @FXML
    private Label Score;

    @FXML
    private ImageView coinIcon;

//  HELMET CHANGING
    @FXML
    private ImageView h1;

    @FXML
    private ImageView h2;

    @FXML
    private ImageView exit;

    @FXML
    private AnchorPane helmets;

    @FXML
    private Label Helm_Lab;

    @FXML
    private Button Helmet;
//  HELMET CHANGING
	
	@FXML
    private Label coinCounter;
    
	@FXML
    private Text WILLHERO;
	
	@FXML
    private AnchorPane anchorPane;
    @FXML
    private Button save;

    @FXML
    private Button Start;

    @FXML
    private ImageView BG;

    @FXML
    private Button Stop;


    @FXML
    private Button dash;
    
    private boolean hasStarted;
    
    public Game G1;

    public TranslateTransition transition = new TranslateTransition();

    private boolean DT = false;
    public game_data GD = new game_data();
    
    public void onPauseClick() {
    	G1.pauseGame();
    }
    
    public void onResumeClick() {
    	G1.resumeGame();
    }
    
    //HELMET CHANGING
    public void onH1click(){
        if (GD.helmet != "hero.png"){
            GD.helmet = "hero.png";
            helmets.setVisible(false);
            helmets.setDisable(true);
            Helm_Lab.setText("Choose Helmet");
        }
        else{
            Helm_Lab.setText("Helmet already equipped!");
        }
    }

    public void onH2click(){

        if (GD.helmet != "hero2.png"){
            GD.helmet = "hero2.png";
            helmets.setVisible(false);
            helmets.setDisable(true);
            Helm_Lab.setText("Choose Helmet");
        }
        else{
            Helm_Lab.setText("Helmet already equipped!");
        }
    }

    public void onHelmetBClick(ActionEvent event){
        helmets.setVisible(true);
        helmets.setDisable(false);
    }

    public void CancelB(){
        System.out.println("h1");
        helmets.setDisable(true);
        System.out.println("h2");
        helmets.setVisible(false);
    }

    //HELMET CHANGING

    Timeline T1 = new Timeline(new KeyFrame(Duration.millis(2.6), new EventHandler<ActionEvent>() {
        
        @Override
        public void handle(ActionEvent event) {
        	if (G1.hero.up_Timeline.getStatus() != Animation.Status.RUNNING && 
        			G1.hero.right_Timeline.getStatus() != Animation.Status.RUNNING) {
        		
        		G1.hero.Node.setLayoutY(G1.hero.Node.getLayoutY() + 1);
        		for (int i = 0; i < G1.Coins.size(); i++) {
        			if (G1.hero.Node.getBoundsInParent().intersects(G1.Coins.get(i).Node.getBoundsInParent())) {
        				
        				anchorPane.getChildren().removeAll(G1.Coins.get(i).Node);
        				G1.Coins.remove(i);
        				G1.coinsCollected++;
        				coinCounter.setText(Integer.toString(Integer.parseInt(coinCounter.getText())+1));
        				
        				
        			}
        		}
        	}
            for (int i = 0;i < G1.Islands.size(); i++){
                if(G1.hero.Node.getBoundsInParent().intersects(G1.Islands.get(i).Node.getBoundsInParent())){
                G1.hero.down_Timeline.stop();
                G1.hero.right_Timeline.stop();

                
                jump(G1.hero.Node);
                }
            }
        }
    }
    ));
    

    
    Timeline jump = new Timeline(new KeyFrame(Duration.millis(2.6), new EventHandler<ActionEvent>() {
        
        @Override
        
        public void handle(ActionEvent event) {
        	if ( G1.hero.right_Timeline.getStatus() != Animation.Status.RUNNING) {
        	G1.hero.Node.setLayoutY(G1.hero.Node.getLayoutY() - 1);
    		for (int i = 0; i < G1.Coins.size(); i++) {
    			if (G1.hero.Node.getBoundsInParent().intersects(G1.Coins.get(i).Node.getBoundsInParent())) {
    				
    				anchorPane.getChildren().removeAll(G1.Coins.get(i).Node);
    				G1.Coins.remove(i);
    				G1.coinsCollected++;
    				coinCounter.setText(Integer.toString(Integer.parseInt(coinCounter.getText())+1));
    				
    				
    			}
    		}
        	}
        	else {
        		G1.hero.up_Timeline.stop();
        	}
        }
    }));
    
    Timeline DashBack = new Timeline(new KeyFrame(Duration.millis(2), new EventHandler<ActionEvent>() {
        
        @Override
        
        public void handle(ActionEvent event) {


            //////////buggy code starts

//            boolean leftI = G1.Islands.get(0).Node.getBoundsInParent().getMaxX() <= BG.getBoundsInParent().getMinX();
//            boolean leftO;
//            if(G1.Orcs.size() != 0){
//            leftO = G1.Orcs.get(0).Node.getBoundsInParent().getMaxX() <= BG.getBoundsInParent().getMinX();
//            }
//            else{
//                leftO = false;
//            }
//            if(leftI){
//                anchorPane.getChildren().removeAll(G1.Islands.get(0).Node);
//                G1.Islands.remove(0);
//                G1.IslandSpawner();
//                anchorPane.getChildren().addAll(G1.Islands.get(G1.Islands.size() - 1).Node);
//                for(int i = 0; i < G1.Orcs.size(); i++){
//                    if(!anchorPane.getChildren().contains(G1.Orcs.get(i).Node)){
//                        anchorPane.getChildren().addAll(G1.Orcs.get(i).Node);
//                    }
//                }
//
//                for(int i = 0; i < G1.Coins.size(); i++){
//                    if(!anchorPane.getChildren().contains(G1.Coins.get(i).Node)){
//                        anchorPane.getChildren().addAll(G1.Coins.get(i).Node);
//                    }
//                }
//            }
//            if(leftO){
//                System.out.println("ayo");
//                anchorPane.getChildren().removeAll(G1.Orcs.get(0).Node);
//                G1.Orcs.remove(0);
//            }

            /////////clean code starts

        	if (G1.hero.Node.getLayoutX() >= 60 && G1.hero.Node.getLayoutX() <= 250) {

        		G1.hero.Node.setLayoutX(G1.hero.Node.getLayoutX() - 1 );
        		for (int i = 0; i < G1.Islands.size(); i++) {
        			G1.Islands.get(i).Node.setLayoutX(G1.Islands.get(i).Node.getLayoutX() - 1 );
        		}
        		for (int i = 0; i < G1.Orcs.size(); i++) {
        			G1.Orcs.get(i).Node.setLayoutX(G1.Orcs.get(i).Node.getLayoutX() - 1 );

                }

                for (int i = 0; i < G1.Coins.size(); i++) {
                    G1.Coins.get(i).Node.setLayoutX(G1.Coins.get(i).Node.getLayoutX() -1 );
                }

        	}
        	else if(G1.hero.Node.getLayoutX() >250) {
        		G1.hero.Node.setLayoutX(G1.hero.Node.getLayoutX() - 1 );
        		for (int i = 0; i < G1.Islands.size(); i++) {
        			G1.Islands.get(i).Node.setLayoutX(G1.Islands.get(i).Node.getLayoutX() -1.8 );
        		}
        		for (int i = 0; i < G1.Orcs.size(); i++) {
        			G1.Orcs.get(i).Node.setLayoutX(G1.Orcs.get(i).Node.getLayoutX() -1.8 );

                }

                for (int i = 0; i < G1.Coins.size(); i++) {
                    G1.Coins.get(i).Node.setLayoutX(G1.Coins.get(i).Node.getLayoutX() -1.8 );
                }
        	}
        	
        	
        }
    }));
    

    
    Timeline rest = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
        
        @Override
        public void handle(ActionEvent event) {
        }
    }));
    
   
    Timeline Dash = new Timeline(new KeyFrame(Duration.millis(0.6), new EventHandler<ActionEvent>() {
        
        @Override
        
        public void handle(ActionEvent event) {
        	
        	if (G1.hero.Node.getLayoutX() <= 250) {
        		if (true) {
        			G1.hero.Node.setLayoutX(G1.hero.Node.getLayoutX() + 1);
        		}
        		for (int i = 0; i < G1.Coins.size(); i++) {
        			if (G1.hero.Node.getBoundsInParent().intersects(G1.Coins.get(i).Node.getBoundsInParent())) {
        				anchorPane.getChildren().removeAll(G1.Coins.get(i).Node);
        				G1.Coins.remove(i);
        				G1.coinsCollected++;
        				coinCounter.setText(Integer.toString(Integer.parseInt(coinCounter.getText())+1));
        				
        			}
        		}
                for (int i = 0;i < G1.Islands.size(); i++){
                    if((G1.hero.Node.getBoundsInParent().intersects(G1.Islands.get(i).Node.getBoundsInParent())) && 
                    		G1.hero.Node.getBoundsInParent().getMaxY() > (G1.Islands.get(i).Node.getBoundsInParent()).getMinY()  ){
                    	
                    G1.hero.down_Timeline.stop();
                    G1.hero.right_Timeline.stop();

                    
                    jump(G1.hero.Node);
                    }
                    

                }
        	}	
        	
        }
    }));


    public GameController() throws IOException {
    }

    public void Dash(){
    	G1.hero.up_Timeline.stop(); 
    	G1.hero.down_Timeline.stop();
        G1.hero.right_Timeline.setCycleCount(210);  
        G1.hero.left_Timeline.setCycleCount(Animation.INDEFINITE); 
        G1.hero.left_Timeline.play();
        
       
        
        G1.hero.right_Timeline.play();
        
        Dash.setOnFinished(e -> {
            boolean left = G1.Islands.get(0).Node.getBoundsInLocal().getMaxX() <= BG.getBoundsInLocal().getMinX();
            if(left){
                anchorPane.getChildren().remove(G1.Islands.get(0).Node);
                G1.Islands.remove(0);
                G1.IslandSpawner();
                anchorPane.getChildren().addAll(G1.Islands.get(G1.Islands.size() -1 ).Node);
            }
//
//            if(!G1.Orcs.get(0).Node.getBoundsInParent().intersects(BG.getBoundsInParent())){
//                anchorPane.getChildren().removeAll(G1.Orcs.get(0).Node);
//                G1.Orcs.remove(0);
//            }

            G1.hero.down_Timeline.play();
        }
        );
        

        

    }
    
    public void jump(Node pp){
//	ScaleTransition scale = new ScaleTransition();
//    	scale.setNode(pp);
//    	scale.setDuration(Duration.millis(80));
//    	scale.setCycleCount(0);
//    	scale.setInterpolator(Interpolator.LINEAR);
//    	scale.setToY(1);
////    	scale.setAutoReverse(true);
//    	scale.play();
    	
    	G1.hero.up_Timeline.setCycleCount(160);
    	
//    	Dash.setOnFinished(event -> rest.stop());
    	G1.hero.up_Timeline.play();
    	G1.hero.up_Timeline.setOnFinished(e -> G1.hero.down_Timeline.play());
//        TranslateTransition transition = new TranslateTransition();
//        transition.setDuration(Duration.seconds(0.5));
//        transition.setAutoReverse(false);
//        transition.setCycleCount(0);
//        //while(System.get)
//        transition.setByY(-180);
//        transition.setNode(pp);
//        transition.play();
//        transition.setOnFinished(e->T1.play());
    	
    }

    public void onStartButtonClick() {
    	if(!hasStarted) {

            G1 = new Game(GD.helmet);
            anchorPane.getChildren().addAll(G1.hero.Node);
            System.out.println(G1.hero.Node.getImage());


            for (int i = 0; i<G1.Islands.size();i++){
                anchorPane.getChildren().addAll(G1.Islands.get(i).Node);
            }

            for (int i = 0; i<G1.Orcs.size();i++){
                anchorPane.getChildren().addAll(G1.Orcs.get(i).Node);
            }

            for (int i = 0; i<G1.Coins.size();i++){
                anchorPane.getChildren().addAll(G1.Coins.get(i).Node);
            }
    		hasStarted = true;
	    	TranslateTransition transition = new TranslateTransition();
	    	transition.setNode(WILLHERO);
	    	transition.setDuration(Duration.millis(250));
	    	transition.setByY(-230);
	    	transition.play();

    	}
    	
        DT = true;
//        T2.setCycleCount(Animation.INDEFINITE);
//        T2.play();
        for (int i = 0; i < G1.Orcs.size(); i++) {
        	G1.Orcs.get(i).down_Timeline.setCycleCount(Animation.INDEFINITE);
        	G1.Orcs.get(i).down_Timeline.play();
        }
        G1.hero.setDown_Timeline(T1);
        G1.hero.setLeft_Timeline(DashBack);
        G1.hero.setRight_Timeline(Dash);
        G1.hero.setUp_Timeline(jump);
        G1.hero.down_Timeline.setCycleCount(Animation.INDEFINITE);
        G1.hero.down_Timeline.play();
//        T1.setCycleCount(Animation.INDEFINITE);
//        T1.play();
        //anime(hero);
    }

    public void onSaveB(ActionEvent event) throws IOException {
        G1.save(G1);
        System.out.println("SAVED!");
    }

    public void onQuitButtonClick (){
        System.exit(0);
    }

    public void onDashButtonClick(ActionEvent event){
        if(DT){
            GD.Score++;
            Score.setText(String.valueOf(GD.Score));
            if (!G1.isPaused ) {
            	Dash();
            }
        }
    }

    public void onLoadB() throws IOException, ClassNotFoundException {

        G1.isl = G1.load();
        G1.Islands.get(0).Node.setLayoutX(G1.isl[0]);
        G1.Islands.get(0).Node.setLayoutY(G1.isl[1]);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        anchorPane.getChildren().addAll(G1.hero.Node);
//        System.out.println(G1.hero.Node.getImage());
//
//
//        for (int i = 0; i<G1.Islands.size();i++){
//            anchorPane.getChildren().addAll(G1.Islands.get(i).Node);
//        }
//
//        for (int i = 0; i<G1.Orcs.size();i++){
//            anchorPane.getChildren().addAll(G1.Orcs.get(i).Node);
//        }
//
//        for (int i = 0; i<G1.Coins.size();i++){
//            anchorPane.getChildren().addAll(G1.Coins.get(i).Node);
//        }
        
        
    }
}


//min y = 100
//max y = 450
//shift x = 300
//shift y = 50
