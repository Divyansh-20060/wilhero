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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javafx.animation.ScaleTransition;
import javafx.animation.Interpolator;


class game_data implements Serializable{
    public String helmet = "hero.png";
    public int Score = 0;
    public ArrayList<double[]> Island_P = new ArrayList<double[]>();
    public ArrayList<double[]> Orc_P = new ArrayList<double[]>();
    public ArrayList<double[]> Coin_P = new ArrayList<double[]>();
    public double[] Hero_P = {0,0};
    public int CoinsC = 0;
    public void CopyData (Game G1){
        for(int i = 0; i < G1.Islands.size(); i++){
            double[] P = {G1.Islands.get(i).Node.getLayoutX(),G1.Islands.get(i).Node.getLayoutY()};
            Island_P.add(P);
        }

        for(int i = 0; i < G1.Orcs.size(); i++){
            double[] P = {G1.Orcs.get(i).Node.getLayoutX(),G1.Orcs.get(i).Node.getLayoutY()};
            Orc_P.add(P);
        }

        for(int i = 0; i < G1.Coins.size(); i++){
            double[] P = {G1.Coins.get(i).Node.getLayoutX(),G1.Coins.get(i).Node.getLayoutY()};
            Coin_P.add(P);
        }

        Hero_P[0] = G1.hero.Node.getLayoutX();
        Hero_P[1] = G1.hero.Node.getLayoutY();
    }
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
    	this.isPaused = false;
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
                	
                	
                	orc.Node.setLayoutX(orc.Node.getLayoutX() + 1.5);
                	
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

    public void save(game_data GD) throws IOException{
        GD.CopyData(this);
        String dir = System.getProperty("user.dir");
        String dest = dir+File.separator+"save games";
        File FTL = new File(dest);
        String[] saves = FTL.list();

        String st = "GAME" + saves.length +".ser";
        FileOutputStream fileout = new FileOutputStream(st);
        ObjectOutputStream out = new ObjectOutputStream(fileout);
        out.writeObject(GD);
        out.close();
        fileout.close();

        String fileS = "";
        String fileD = "";

        fileS = dir+File.separator+st;
        fileD = dir+File.separator+"save games"+File.separator+st;
        File Sourcefile = new File(fileS);
        File Destinationfile = new File(fileD);

        Files.copy(Sourcefile.toPath(),Destinationfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Sourcefile.delete();
        System.out.println("Saved!");
    }

    public game_data load(String st) throws IOException, ClassNotFoundException {


        game_data gd = null;
        FileInputStream filein = new FileInputStream(st);
        ObjectInputStream in = new ObjectInputStream(filein);
        gd = (game_data) in.readObject();
        in.close();
        filein.close();
        System.out.println("loaded");
        return gd;
    }
}

public class GameController implements Initializable,Serializable{

    @FXML
    private Label Score;

    @FXML
    private ImageView coinIcon;

////////////////HELMET CHANGING NODES////////////////////////////
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
/////////////////////////  HELMET CHANGING NODES////////////////////////////
	
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
    //public int NSAVES = 0;
    public game_data GD = new game_data();
    

    	
    
    //////////////////////////////////HELMET CHANGING////////////////////////////////////////////////////////////////
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
        helmets.setDisable(true);
        helmets.setVisible(false);
    }

    /////////////////////////////////////ANIMATION TIMELINES///////////////////////////////////////////////////////////

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
        				GD.CoinsC++;
                        int p = GD.CoinsC;
        				coinCounter.setText(String.valueOf(p));
        				
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

    ///////////////////////////////////////BUTTON ASSIGNMENTS//////////////////////////////////////////////////////////

    public void onPauseClick() {
        G1.pauseGame();
    }

    public void onResumeClick() {

        G1.resumeGame();

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
        G1.save(GD);
        System.out.println("SAVED!");
        for(int i = 0 ; i < G1.Islands.size(); i++){
            System.out.println("---------------" +"\n");

            System.out.println(G1.Islands.get(i).Node.getLayoutX());
            System.out.println(GD.Island_P.get(i)[0]);

        }
    }

    public void onQuitButtonClick () throws IOException {
        System.exit(0);
    }

    public void onDashButtonClick(ActionEvent event){
        if(DT){
//            GD.Score++;
//            Score.setText(String.valueOf(GD.Score));
            if (!G1.isPaused ) {
            	Dash();
            }
        }
    }

    public void onLoadB() throws IOException, ClassNotFoundException {

        String fileS = "";
        String dir = System.getProperty("user.dir");

        fileS = dir+File.separator+"save games";
        File saveL = new File(fileS);

        System.out.println("Choose Save file" + "\n");
        String[] saves = saveL.list();
        for(int i = 0; i<saves.length; i++){
            System.out.println(saves[i]);
        }

        Scanner sc = new Scanner(System.in);
        String st = sc.nextLine();
        fileS = fileS + File.separator + st;
        GD = G1.load(fileS);

        if(GD.Island_P.size() > G1.Islands.size()){
            while (GD.Island_P.size() > G1.Islands.size()){
                islands is = new islands(0,0);
                G1.Islands.add(is);
            }
        }

        else if (GD.Island_P.size() < G1.Islands.size()){
            while(GD.Island_P.size() < G1.Islands.size()){
                G1.Islands.remove(G1.Islands.size() -1 );
            }
        }

        if(GD.Orc_P.size() > G1.Orcs.size()){
            while(GD.Orc_P.size() > G1.Orcs.size()){
                Orc or = new Orc(0,0);
                G1.Orcs.add(or);
            }
        }

        else if (GD.Orc_P.size() < G1.Orcs.size()){
            while(GD.Orc_P.size() < G1.Orcs.size()){
                G1.Orcs.remove(G1.Orcs.size() -1);
            }
        }

        if(GD.Coin_P.size() > G1.Coins.size()){
            while(GD.Coin_P.size() > G1.Coins.size()){
                Coin coin = new Coin(0,0);
                G1.Coins.add(coin);
            }
        }

        else if (GD.Coin_P.size() < G1.Coins.size()){
            while (GD.Coin_P.size() < G1.Coins.size()){
                G1.Coins.remove(G1.Coins.size() - 1);
            }
        }

        for(int i = 0; i < G1.Islands.size(); i++){
            G1.Islands.get(i).Node.setLayoutX(GD.Island_P.get(i)[0]);
            G1.Islands.get(i).Node.setLayoutY(GD.Island_P.get(i)[1]);
        }

        for(int i = 0; i < G1.Orcs.size(); i++){
            G1.Orcs.get(i).Node.setLayoutX(GD.Orc_P.get(i)[0]);
            G1.Orcs.get(i).Node.setLayoutY(GD.Orc_P.get(i)[1]);
        }

        for(int i = 0; i < G1.Coins.size(); i++){
            G1.Coins.get(i).Node.setLayoutX(GD.Coin_P.get(i)[0]);
            G1.Coins.get(i).Node.setLayoutY(GD.Coin_P.get(i)[1]);
        }
        G1.hero.Node.setLayoutX(GD.Hero_P[0]);
        G1.hero.Node.setLayoutY(GD.Hero_P[1]);
        int p = GD.CoinsC;
        coinCounter.setText(String.valueOf(p));
    }



    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {

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

//yes
//min y = 100
//max y = 450
//shift x = 300
//shift y = 50
