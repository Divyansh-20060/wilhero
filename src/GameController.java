import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
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
import javafx.scene.transform.Rotate;
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
    public ArrayList<double[]> Chest_p = new ArrayList<double[]>();
    public ArrayList<double[]> Falling_p = new ArrayList<double[]>();
    public double[] Sword = {0,0};
    public double[] Boss = {0,0};
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

        for(int i = 0; i< G1.Chests.size(); i++){
            double[] p = {G1.Chests.get(i).Node.getLayoutX(),G1.Chests.get(i).Node.getLayoutY(),G1.Chests.get(i).Drop};
            Chest_p.add(p);
        }

        for(int i = 0; i < G1.FS_L.size(); i++){
            double[] p = {G1.FS_L.get(i).Node.getLayoutX(),G1.FS_L.get(i).Node.getLayoutY()};
            Falling_p.add(p);
        }

        Boss[0] = G1.boss.Node.getLayoutX();
        Boss[1] = G1.boss.Node.getLayoutY();

        Sword[0] = G1.hero.hammer.Node.getLayoutX();
        Sword[1] = G1.hero.hammer.Node.getLayoutY();

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
class Falling_Step extends gameobjcts{
    public Falling_Step(double x,double y){
        Node = new ImageView("FallingStep.png");
        Node.setLayoutX(x);
        Node.setLayoutY(y);

    }
}

class Orc extends gameobjcts{
	public RotateTransition die_Rotate;
	public TranslateTransition die_Fall;
	
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
	public void play_Die_Rotate() {
		this.die_Rotate.play();
	}
	
	public void play_Die_Fall() {
		this.die_Fall.play();
	}
    
    public void setDie_Rotate(RotateTransition die_Rotate) {
    	this.die_Rotate = die_Rotate;
    }
    public void setDie_Fall(TranslateTransition die_Fall) {
    	this.die_Fall = die_Fall;
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
class Boss extends gameobjcts{
    public Boss(double x, double y){
        Node = new ImageView("OrcBoss.png");
        Node.setLayoutX(x);
        Node.setLayoutY(y);
    }
}

class Chest extends gameobjcts{
	boolean isOpen;
	
    public int Drop;

    public Chest(double x, double y){
        Random rand = new Random();
        int drop = rand.nextInt(3);
        Node = new ImageView("ChestClosed.png");
        Node.setLayoutX(x);
        Node.setLayoutY(y);
        Drop = drop;
    }
}

class Hero extends gameobjcts{
	Hammer hammer;
	Shuriken shuriken;
	
	Timeline up_Timeline;
	Timeline down_Timeline;
	Timeline right_Timeline;
	Timeline left_Timeline;
	
    public Hero(String st){
//    	hammer = new Hammer(110, 180);
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

class Shuriken extends gameobjcts{
	boolean isEquiped;
	int level;
	RotateTransition rotate_ani;
	TranslateTransition trans_ani;
	ImageView Node;
	Shuriken(double x , double y){
		Node = new ImageView("WeaponAxe1.png");
		Node.setLayoutX(x);
		Node.setLayoutY(y);
	}
	public void rot() {
		this.rotate_ani.play();
		this.trans_ani.play();
	}
}

class Hammer extends gameobjcts{
	boolean isEquiped;
	int level;
	RotateTransition rotate_ani;
	ImageView Node;
	Hammer(double x , double y){
		Node = new ImageView("WeaponSword2.png");
		Node.setLayoutX(x);
		Node.setLayoutY(y);
	}
	public void rot() {
		this.rotate_ani.play();
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
    public Boss boss;
    
    public ArrayList<islands> Islands = new ArrayList<islands>();
    public ArrayList<Orc> Orcs = new ArrayList<Orc>();
    public ArrayList<Coin> Coins = new ArrayList<Coin>();
    public ArrayList<Chest> Chests = new ArrayList<Chest>();
    public ArrayList<Falling_Step> FS_L = new ArrayList<Falling_Step>();

    public Game(String st){
        hero = new Hero(st);
        hero.hammer = new Hammer(110, 180);
        hero.shuriken = new Shuriken(42, 162);
        hero.hammer.Node.setOpacity(0);
        hero.shuriken.Node.setOpacity(0);
        
        islands i1 = new islands(40,300);
        Islands.add(i1);
        for (int i = 0; i<49;i++){
        IslandSpawner();
        }

        for(int i = 0; i<5;i++){
            Random rand = new Random();
            int Is = rand .nextInt(50);
            ChestSpawner(Islands.get(Is).Node.getLayoutX()+100,Islands.get(Is).Node.getLayoutY() - 55);
        }

        BossSpawner();

    }

    public void ChestSpawner(double x, double y){
        Chest c = new Chest(x,y);
        Chests.add(c);
    }
    
    public void pauseGame() {
    	this.isPaused = true;
    	
    	
    	if (hero.up_Timeline != null) {
    		hero.up_Timeline.pause();
    	}
    	if(hero.down_Timeline != null) {
    		hero.down_Timeline.pause();
    	}
    	if(hero.right_Timeline != null) {
    		hero.right_Timeline.pause();
    	}
    	if(hero.left_Timeline != null) {
    		hero.left_Timeline.pause();
    	}
    	for (int i = 0; i < Orcs.size(); i++) {
    		if(Orcs.get(i).down_Timeline != null) {
    			Orcs.get(i).down_Timeline.pause();
    		}
    		if(Orcs.get(i).up_Timeline != null) {
    			Orcs.get(i).up_Timeline.pause();
    		}
    		if(Orcs.get(i).right_Timeline != null) {
    			Orcs.get(i).right_Timeline.pause();
    		}
    	}
    }
    
    public void resumeGame() {
    	this.isPaused = false;
    	
    	if (hero.up_Timeline != null &&  hero.up_Timeline.getStatus() == Animation.Status.PAUSED) {
    		hero.up_Timeline.play();
    	}
    	if (hero.down_Timeline != null &&  hero.down_Timeline.getStatus() == Animation.Status.PAUSED) {
    		hero.down_Timeline.play();
    	}
    	if (hero.right_Timeline != null &&  hero.right_Timeline.getStatus() == Animation.Status.PAUSED) {
    		hero.right_Timeline.play();
    	}
    	if (hero.left_Timeline != null &&  hero.left_Timeline.getStatus() == Animation.Status.PAUSED) {
    		hero.left_Timeline.play();
    	}
    	for (int i = 0; i < Orcs.size(); i++) {
    		if (Orcs.get(i).down_Timeline != null && Orcs.get(i).down_Timeline.getStatus() == Animation.Status.PAUSED) {
    			Orcs.get(i).down_Timeline.play();
    		}
    		if (Orcs.get(i).up_Timeline != null &&  Orcs.get(i).up_Timeline.getStatus() == Animation.Status.PAUSED) {
    			Orcs.get(i).up_Timeline.play();
    		}
    		
    		if (Orcs.get(i).right_Timeline != null &&  Orcs.get(i).right_Timeline.getStatus() == Animation.Status.PAUSED) {
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
                				(hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() < hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY()) &&
                				(hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() < orc.Node.getBoundsInParent().getMaxY() - hero.Node.getBoundsInParent().getMinY()) &&
                				(hero.Node.getBoundsInParent().getMaxX() >= orc.Node.getBoundsInParent().getMinX()) &&
                				(hero.Node.getBoundsInParent().getMinX() <= orc.Node.getBoundsInParent().getMinX() )
                				  )  {
                			
                			//hero hits the side of orc
                			
                			
                			orc.right_Timeline.setCycleCount(120);
                			orc.right_Timeline.play();
                			hero.right_Timeline.stop();
                			hero.down_Timeline.play();
                		}
                		
                		
                		else if(hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) && 
                				(hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMinY() ) && 
                				(hero.Node.getBoundsInParent().getMinY() <= orc.Node.getBoundsInParent().getMinY()) &&
                				(hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() >= hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY()) &&
                				(orc.Node.getBoundsInParent().getMaxX() - hero.Node.getBoundsInParent().getMinX() >= hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY())
                				) {
                			
                			//hero hits the top of orc
                	    	hero.up_Timeline.setCycleCount(56);
                	    	
                	    	if(hero.up_Timeline.getStatus() != Animation.Status.RUNNING) {
                	    		hero.up_Timeline.play();
                	    	}
                	    	
                	        
                	    	hero.up_Timeline.setOnFinished(e -> hero.down_Timeline.play());
                	    	
                	    	
                		}
                		

                		else if (hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) && 
                				(hero.Node.getBoundsInParent().getMinY() <= orc.Node.getBoundsInParent().getMaxY() ) && 
                				(hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMaxY() ) &&
                				(hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() >= orc.Node.getBoundsInParent().getMaxY() - hero.Node.getBoundsInParent().getMinY()) &&
                				(orc.Node.getBoundsInParent().getMaxX() - hero.Node.getBoundsInParent().getMinX() >= orc.Node.getBoundsInParent().getMaxY() - hero.Node.getBoundsInParent().getMinY())
                				) {
                			orc.down_Timeline.stop();
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
                				(hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() < hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY()) &&
                				(hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() < orc.Node.getBoundsInParent().getMaxY() - hero.Node.getBoundsInParent().getMinY()) &&
                				(hero.Node.getBoundsInParent().getMaxX() >= orc.Node.getBoundsInParent().getMinX()) &&
                				(hero.Node.getBoundsInParent().getMinX() <= orc.Node.getBoundsInParent().getMinX() )
                				  
                				    )  {
                			
                			//hero hits the side of orc
                			hero.right_Timeline.stop();
                			hero.down_Timeline.play();
                			
                			orc.right_Timeline.setCycleCount(120);
                			orc.right_Timeline.play();
                		}

                    	else if(hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) && 
                				(hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMinY() ) && 
                				(hero.Node.getBoundsInParent().getMinY() <= orc.Node.getBoundsInParent().getMinY()) &&
                				(hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() >= hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY()) &&
                				(orc.Node.getBoundsInParent().getMaxX() - hero.Node.getBoundsInParent().getMinX() >= hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY())
                				 ) {
                			
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
                				(hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMaxY() ) &&
                				(hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() >= orc.Node.getBoundsInParent().getMaxY() - hero.Node.getBoundsInParent().getMinY()) &&
                				(orc.Node.getBoundsInParent().getMaxX() - hero.Node.getBoundsInParent().getMinX() >= orc.Node.getBoundsInParent().getMaxY() - hero.Node.getBoundsInParent().getMinY())
                				) {
                			orc.up_Timeline.stop();
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
                	
//                	orc.right_Timeline.setOnFinished(t -> orc.down_Timeline.play());
                	if(hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) &&
            				(hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() < hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY()) &&
            				(hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() < orc.Node.getBoundsInParent().getMaxY() - hero.Node.getBoundsInParent().getMinY()) &&
            				(hero.Node.getBoundsInParent().getMaxX() >= orc.Node.getBoundsInParent().getMinX()) &&
            				(hero.Node.getBoundsInParent().getMinX() <= orc.Node.getBoundsInParent().getMinX() )
            				  
            				    )  {
            			
            			//hero hits the side of orc
            			hero.right_Timeline.stop();
            			hero.down_Timeline.play();
            			
            			orc.right_Timeline.setCycleCount(120);
            			orc.right_Timeline.play();
            		}

                	else if(hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) && 
            				(hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMinY() ) && 
            				(hero.Node.getBoundsInParent().getMinY() <= orc.Node.getBoundsInParent().getMinY()) &&
            				(hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() >= hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY()) &&
            				(orc.Node.getBoundsInParent().getMaxX() - hero.Node.getBoundsInParent().getMinX() >= hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY())
            				 ) {
            			
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
            				(hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMaxY() ) &&
            				(hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() >= orc.Node.getBoundsInParent().getMaxY() - hero.Node.getBoundsInParent().getMinY()) &&
            				(orc.Node.getBoundsInParent().getMaxX() - hero.Node.getBoundsInParent().getMinX() >= orc.Node.getBoundsInParent().getMaxY() - hero.Node.getBoundsInParent().getMinY())
            				) {
            			orc.right_Timeline.stop();
            			pauseGame();
            			
            		}

                	
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

    public void BossSpawner(){
        double y = Islands.get(Islands.size() - 1).Node.getLayoutY();
        double x = Islands.get(Islands.size() - 1).Node.getLayoutX() + 250;

        for(int i = 0; i<18; i++){
            Falling_Step FS = new Falling_Step(x,y);
            FS_L.add(FS);
            x = x+79;
        }
        islands I = new islands(x + 126,y);
        boss = new Boss( FS_L.get(9).Node.getLayoutX() , y - 150);

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
        		G1.hero.hammer.Node.setLayoutY(G1.hero.hammer.Node.getLayoutY() + 1 );
        		G1.hero.shuriken.Node.setLayoutY(G1.hero.shuriken.Node.getLayoutY() + 1 );
        		
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
            for(int i = 0; i < G1.Chests.size(); i++ ) {
            	if (!G1.Chests.get(i).isOpen) {
            		if (G1.hero.Node.getBoundsInParent().intersects(G1.Chests.get(i).Node.getBoundsInParent())) {
            			G1.Chests.get(i).isOpen = true;
            			G1.Chests.get(i).Node.setImage(new Image(getClass().getResourceAsStream("ChestOpen.png"))); 
            			if(G1.Chests.get(i).Drop == 0) {
            				G1.coinsCollected += 10;
            				coinCounter.setText(Integer.toString(Integer.parseInt(coinCounter.getText())+10));
            			}
            			
            			else if(G1.Chests.get(i).Drop == 1) {
            				//weapon1
            				if(G1.hero.hammer.isEquiped) {
            					G1.hero.hammer.level++;
            				}
            				else {
            					G1.hero.hammer.level++;
            					G1.hero.hammer.isEquiped = true;
            					G1.hero.shuriken.isEquiped = false;
            					
            					G1.hero.hammer.Node.setOpacity(1);
            					G1.hero.shuriken.Node.setOpacity(0);
            				}
            				
            			}
            			
            			else if(G1.Chests.get(i).Drop == 2) {
            				// weapon2
            				if(G1.hero.shuriken.isEquiped) {
            					G1.hero.shuriken.level++;
            					G1.hero.shuriken.Node.setImage(new Image(getClass().getResourceAsStream("WeaponAxe" + G1.hero.shuriken.level + ".png")));
            				}
            				else {
            					G1.hero.shuriken.level++;
            					G1.hero.shuriken.isEquiped = true;
            					G1.hero.hammer.isEquiped = false;
            					G1.hero.shuriken.Node.setImage(new Image(getClass().getResourceAsStream("WeaponAxe" + G1.hero.shuriken.level + ".png")));
            					G1.hero.shuriken.Node.setOpacity(1);
            					G1.hero.hammer.Node.setOpacity(0);
            				}
            			}
            		}
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
        	G1.hero.hammer.Node.setLayoutY(G1.hero.hammer.Node.getLayoutY() - 1 );
        	G1.hero.shuriken.Node.setLayoutY(G1.hero.shuriken.Node.getLayoutY() - 1 );
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
            for(int i = 0; i < G1.Chests.size(); i++ ) {
            	if (!G1.Chests.get(i).isOpen) {
            		if (G1.hero.Node.getBoundsInParent().intersects(G1.Chests.get(i).Node.getBoundsInParent())) {
            			G1.Chests.get(i).isOpen = true;
            			G1.Chests.get(i).Node.setImage(new Image(getClass().getResourceAsStream("ChestOpen.png"))); 
            			if(G1.Chests.get(i).Drop == 0) {
            				G1.coinsCollected += 10;
            				coinCounter.setText(Integer.toString(Integer.parseInt(coinCounter.getText())+10));
            			}
            			
            			else if(G1.Chests.get(i).Drop == 1) {
            				//weapon1
            				if(G1.hero.hammer.isEquiped) {
            					G1.hero.hammer.level++;
            				}
            				else {
            					G1.hero.hammer.level++;
            					G1.hero.hammer.isEquiped = true;
            					G1.hero.shuriken.isEquiped = false;
            					
            					G1.hero.hammer.Node.setOpacity(1);
            					G1.hero.shuriken.Node.setOpacity(0);
            				}
            				
            			}
            			
            			else if(G1.Chests.get(i).Drop == 2) {
            				// weapon2
            				if(G1.hero.shuriken.isEquiped) {
            					G1.hero.shuriken.level++;
            					G1.hero.shuriken.Node.setImage(new Image(getClass().getResourceAsStream("WeaponAxe" + G1.hero.shuriken.level + ".png")));
            				}
            				else {
            					G1.hero.shuriken.level++;
            					G1.hero.shuriken.isEquiped = true;
            					G1.hero.hammer.isEquiped = false;
            					G1.hero.shuriken.Node.setImage(new Image(getClass().getResourceAsStream("WeaponAxe" + G1.hero.shuriken.level + ".png")));
            					G1.hero.shuriken.Node.setOpacity(1);
            					G1.hero.hammer.Node.setOpacity(0);
            				}
            			}
            		}
            	}
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

        	
        	GD.Score = -(int)(G1.Islands.get(0).Node.getLayoutX() / 200);
    		Score.setText(String.valueOf(GD.Score));
    		
        	if (G1.hero.Node.getLayoutX() >= 60 && G1.hero.Node.getLayoutX() <= 250) {

        		G1.hero.Node.setLayoutX(G1.hero.Node.getLayoutX() - 1 );
        		G1.hero.hammer.Node.setLayoutX(G1.hero.hammer.Node.getLayoutX() - 1 );
        		G1.hero.shuriken.Node.setLayoutX(G1.hero.shuriken.Node.getLayoutX() - 1 );
        		
        		for (int i = 0; i < G1.Islands.size(); i++) {
        			G1.Islands.get(i).Node.setLayoutX(G1.Islands.get(i).Node.getLayoutX() - 1 );
        		}
        		for (int i = 0; i < G1.Orcs.size(); i++) {
        			G1.Orcs.get(i).Node.setLayoutX(G1.Orcs.get(i).Node.getLayoutX() - 1 );

                }

                for (int i = 0; i < G1.Coins.size(); i++) {
                    G1.Coins.get(i).Node.setLayoutX(G1.Coins.get(i).Node.getLayoutX() -1 );
                }
                for (int i = 0; i < G1.FS_L.size(); i++) {
                	G1.FS_L.get(i).Node.setLayoutX(G1.FS_L.get(i).Node.getLayoutX() -1);
                }
                for (int i = 0; i < G1.Chests.size(); i++) {
                	G1.Chests.get(i).Node.setLayoutX(G1.Chests.get(i).Node.getLayoutX() -1);
                }
                G1.boss.Node.setLayoutX(G1.boss.Node.getLayoutX() -1);

        	}
        	else if(G1.hero.Node.getLayoutX() >250) {
        		G1.hero.Node.setLayoutX(G1.hero.Node.getLayoutX() - 1 );
        		G1.hero.hammer.Node.setLayoutX(G1.hero.hammer.Node.getLayoutX() - 1 );
        		G1.hero.shuriken.Node.setLayoutX(G1.hero.shuriken.Node.getLayoutX() - 1 );
        		for (int i = 0; i < G1.Islands.size(); i++) {
        			G1.Islands.get(i).Node.setLayoutX(G1.Islands.get(i).Node.getLayoutX() -1.8 );
        		}
        		for (int i = 0; i < G1.Orcs.size(); i++) {
        			G1.Orcs.get(i).Node.setLayoutX(G1.Orcs.get(i).Node.getLayoutX() -1.8 );

                }

                for (int i = 0; i < G1.Coins.size(); i++) {
                    G1.Coins.get(i).Node.setLayoutX(G1.Coins.get(i).Node.getLayoutX() -1.8 );
                }
                for (int i = 0; i < G1.FS_L.size(); i++) {
                	G1.FS_L.get(i).Node.setLayoutX(G1.FS_L.get(i).Node.getLayoutX() -1.8);
                }
                for (int i = 0; i < G1.Chests.size(); i++) {
                	G1.Chests.get(i).Node.setLayoutX(G1.Chests.get(i).Node.getLayoutX() -1.8);
                }
                G1.boss.Node.setLayoutX(G1.boss.Node.getLayoutX() -1.8);
                
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
        			G1.hero.hammer.Node.setLayoutX(G1.hero.hammer.Node.getLayoutX() + 1 );
        			G1.hero.shuriken.Node.setLayoutX(G1.hero.shuriken.Node.getLayoutX() + 1 );
        		}
        		for (int i = 0; i < G1.Coins.size(); i++) {
        			if (G1.hero.Node.getBoundsInParent().intersects(G1.Coins.get(i).Node.getBoundsInParent())) {
        				anchorPane.getChildren().removeAll(G1.Coins.get(i).Node);
        				G1.Coins.remove(i);
        				G1.coinsCollected += 1;
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
                
                for (int i = 0; i < G1.Orcs.size(); i++) {
                	if ( ( Math.pow((G1.hero.Node.getBoundsInParent().getMaxX() - G1.Orcs.get(i).Node.getBoundsInParent().getMinX()),2) + Math.pow((G1.hero.Node.getBoundsInParent().getCenterY() - G1.Orcs.get(i).Node.getBoundsInParent().getCenterY()),2) ) <= 5000) {
                		if(G1.hero.hammer.isEquiped) {
	                		if (G1.hero.hammer.rotate_ani.getStatus() != Animation.Status.RUNNING) {
	                			G1.hero.hammer.rot();
	                			G1.Orcs.get(i).down_Timeline.stop();
	                			G1.Orcs.get(i).up_Timeline.stop();
	                			G1.Orcs.get(i).right_Timeline.stop();
	                			
	                			if(G1.Orcs.get(i).die_Rotate.getStatus() != Animation.Status.RUNNING) {
	                				G1.Orcs.get(i).play_Die_Rotate();
	                			}
	                			
	                			if(G1.Orcs.get(i).die_Fall.getStatus() != Animation.Status.RUNNING) {
	                				G1.Orcs.get(i).play_Die_Fall();
	                			}
	                			G1.Orcs.remove(i);
	
	                		}
                		}
                		
                		if(G1.hero.shuriken.isEquiped) {
                			if (G1.hero.shuriken.rotate_ani.getStatus() != Animation.Status.RUNNING && G1.hero.shuriken.trans_ani.getStatus() != Animation.Status.RUNNING) {
                				G1.hero.shuriken.rot();
	                			G1.Orcs.get(i).down_Timeline.stop();
	                			G1.Orcs.get(i).up_Timeline.stop();
	                			G1.Orcs.get(i).right_Timeline.stop();
	                			
	                			if(G1.Orcs.get(i).die_Rotate.getStatus() != Animation.Status.RUNNING) {
	                				G1.Orcs.get(i).play_Die_Rotate();
	                			}
	                			
	                			if(G1.Orcs.get(i).die_Fall.getStatus() != Animation.Status.RUNNING) {
	                				G1.Orcs.get(i).play_Die_Fall();
	                			}
	                			G1.Orcs.remove(i);
                			}
                		}
                	}
                }

                
        	}	
        	
            for(int i = 0; i < G1.Chests.size(); i++ ) {
            	if (!G1.Chests.get(i).isOpen) {
            		if (G1.hero.Node.getBoundsInParent().intersects(G1.Chests.get(i).Node.getBoundsInParent())) {
            			G1.Chests.get(i).isOpen = true;
            			G1.Chests.get(i).Node.setImage(new Image(getClass().getResourceAsStream("ChestOpen.png"))); 
            			if(G1.Chests.get(i).Drop == 0) {
            				G1.coinsCollected += 10;
            				coinCounter.setText(Integer.toString(Integer.parseInt(coinCounter.getText())+10));
            			}
            			
            			else if(G1.Chests.get(i).Drop == 1) {
            				//weapon1
            				if(G1.hero.hammer.isEquiped) {
            					G1.hero.hammer.level++;
            				}
            				else {
            					G1.hero.hammer.level++;
            					G1.hero.hammer.isEquiped = true;
            					G1.hero.shuriken.isEquiped = false;
            					
            					G1.hero.hammer.Node.setOpacity(1);
            					G1.hero.shuriken.Node.setOpacity(0);
            				}
            				
            			}
            			
            			else if(G1.Chests.get(i).Drop == 2) {
            				// weapon2
            				if(G1.hero.shuriken.isEquiped) {
            					G1.hero.shuriken.level++;
            					G1.hero.shuriken.Node.setImage(new Image(getClass().getResourceAsStream("WeaponAxe" + G1.hero.shuriken.level + ".png")));
            				}
            				else {
            					G1.hero.shuriken.level++;
            					G1.hero.shuriken.isEquiped = true;
            					G1.hero.hammer.isEquiped = false;
            					G1.hero.shuriken.Node.setImage(new Image(getClass().getResourceAsStream("WeaponAxe" + G1.hero.shuriken.level + ".png")));
            					G1.hero.shuriken.Node.setOpacity(1);
            					G1.hero.hammer.Node.setOpacity(0);
            				}
            			}
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
            anchorPane.getChildren().addAll(G1.hero.hammer.Node);
            anchorPane.getChildren().addAll(G1.hero.shuriken.Node);
            anchorPane.getChildren().addAll(G1.hero.Node,G1.boss.Node);

            for(int i = 0; i < G1.FS_L.size(); i++){
            	anchorPane.getChildren().addAll(G1.FS_L.get(i).Node);
            }
            
            //shuriken_rot_ani
            G1.hero.shuriken.rotate_ani = new RotateTransition();
            G1.hero.shuriken.rotate_ani.setNode(G1.hero.shuriken.Node);
            G1.hero.shuriken.rotate_ani.setDuration(Duration.millis(200));
            G1.hero.shuriken.rotate_ani.setCycleCount(3);
            G1.hero.shuriken.rotate_ani.setInterpolator(Interpolator.LINEAR);
            G1.hero.shuriken.rotate_ani.setByAngle(360);
            G1.hero.shuriken.rotate_ani.setAxis(Rotate.Z_AXIS);
            
            //shuriken_trans_ani
            G1.hero.shuriken.trans_ani = new TranslateTransition();
            G1.hero.shuriken.trans_ani.setNode(G1.hero.shuriken.Node);
            G1.hero.shuriken.trans_ani.setDuration(Duration.millis(200));
            G1.hero.shuriken.trans_ani.setCycleCount(2);
            G1.hero.shuriken.trans_ani.setByX(190);
            G1.hero.shuriken.trans_ani.setAutoReverse(true);
            
            
            
            //hammer_rot_ani
    		G1.hero.hammer.rotate_ani = new RotateTransition();
    		G1.hero.hammer.rotate_ani.setNode(G1.hero.hammer.Node);
    		G1.hero.hammer.rotate_ani.setDuration(Duration.millis(150));
    		G1.hero.hammer.rotate_ani.setCycleCount(2);
    		G1.hero.hammer.rotate_ani.setAutoReverse(true);
    		G1.hero.hammer.rotate_ani.setInterpolator(Interpolator.LINEAR);
    		G1.hero.hammer.rotate_ani.setByAngle(180);
    		G1.hero.hammer.rotate_ani.setAxis(Rotate.Z_AXIS);
            
            System.out.println(G1.hero.Node.getImage());


            for (int i = 0; i<G1.Islands.size();i++){
                anchorPane.getChildren().addAll(G1.Islands.get(i).Node);
            }

            for (int i = 0; i<G1.Chests.size();i++){
                anchorPane.getChildren().addAll(G1.Chests.get(i).Node);
            }

            for (int i = 0; i<G1.Orcs.size();i++){
            	
                anchorPane.getChildren().addAll(G1.Orcs.get(i).Node);
                
                G1.Orcs.get(i).die_Rotate = new RotateTransition();
                G1.Orcs.get(i).die_Rotate.setNode(G1.Orcs.get(i).Node);
                G1.Orcs.get(i).die_Rotate.setDuration(Duration.millis(1000));
                G1.Orcs.get(i).die_Rotate.setCycleCount(1);
                G1.Orcs.get(i).die_Rotate.setInterpolator(Interpolator.LINEAR);
                G1.Orcs.get(i).die_Rotate.setByAngle(360);
                G1.Orcs.get(i).die_Rotate.setAxis(Rotate.Z_AXIS);
                
                G1.Orcs.get(i).die_Fall = new TranslateTransition(); 
                
                G1.Orcs.get(i).die_Fall.setNode(G1.Orcs.get(i).Node);
                G1.Orcs.get(i).die_Fall.setDuration(Duration.millis(1000));
                G1.Orcs.get(i).die_Fall.setCycleCount(1);
//                G1.Orcs.get(i).die_Fall.setInterpolator(Interpolator.LINEAR);
                G1.Orcs.get(i).die_Fall.setToY(1000);
                
                
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
                Orc orc = new Orc(0,0);
                
                Timeline down_Timeline = new Timeline(new KeyFrame(Duration.millis(2.6), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                    	if (orc.up_Timeline.getStatus() != Animation.Status.RUNNING && 
                    			orc.right_Timeline.getStatus() != Animation.Status.RUNNING) {
                    		
                    		orc.Node.setLayoutY(orc.Node.getLayoutY() + 1);
                    		
                    		 if(G1.hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) &&
                    				(G1.hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() < G1.hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY()) &&
                    				(G1.hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() < orc.Node.getBoundsInParent().getMaxY() - G1.hero.Node.getBoundsInParent().getMinY()) &&
                    				(G1.hero.Node.getBoundsInParent().getMaxX() >= orc.Node.getBoundsInParent().getMinX()) &&
                    				(G1.hero.Node.getBoundsInParent().getMinX() <= orc.Node.getBoundsInParent().getMinX() )
                    				  )  {
                    			
                    			//hero hits the side of orc
                    			
                    			
                    			orc.right_Timeline.setCycleCount(120);
                    			orc.right_Timeline.play();
                    			G1.hero.right_Timeline.stop();
                    			G1.hero.down_Timeline.play();
                    		}
                    		
                    		
                    		else if(G1.hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) && 
                    				(G1.hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMinY() ) && 
                    				(G1.hero.Node.getBoundsInParent().getMinY() <= orc.Node.getBoundsInParent().getMinY()) &&
                    				(G1.hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() >= G1.hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY()) &&
                    				(orc.Node.getBoundsInParent().getMaxX() - G1.hero.Node.getBoundsInParent().getMinX() >= G1.hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY())
                    				) {
                    			
                    			//hero hits the top of orc
                    			G1.hero.up_Timeline.setCycleCount(56);
                    	    	
                    	    	if(G1.hero.up_Timeline.getStatus() != Animation.Status.RUNNING) {
                    	    		G1.hero.up_Timeline.play();
                    	    	}
                    	    	
                    	        
                    	    	G1.hero.up_Timeline.setOnFinished(e -> G1.hero.down_Timeline.play());
                    	    	
                    	    	
                    		}
                    		

                    		else if (G1.hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) && 
                    				(G1.hero.Node.getBoundsInParent().getMinY() <= orc.Node.getBoundsInParent().getMaxY() ) && 
                    				(G1.hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMaxY() ) &&
                    				(G1.hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() >= orc.Node.getBoundsInParent().getMaxY() - G1.hero.Node.getBoundsInParent().getMinY()) &&
                    				(orc.Node.getBoundsInParent().getMaxX() - G1.hero.Node.getBoundsInParent().getMinX() >= orc.Node.getBoundsInParent().getMaxY() - G1.hero.Node.getBoundsInParent().getMinY())
                    				) {
                    			orc.down_Timeline.stop();
                    			G1.pauseGame(); 
                    			
                    		}
                    		

                    	}
                        for (int i = 0;i < G1.Islands.size(); i++){
                            if(orc.Node.getBoundsInParent().intersects(G1.Islands.get(i).Node.getBoundsInParent())){
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
                        	
                    		if(G1.hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) &&
                    				(G1.hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() < G1.hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY()) &&
                    				(G1.hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() < orc.Node.getBoundsInParent().getMaxY() - G1.hero.Node.getBoundsInParent().getMinY()) &&
                    				(G1.hero.Node.getBoundsInParent().getMaxX() >= orc.Node.getBoundsInParent().getMinX()) &&
                    				(G1.hero.Node.getBoundsInParent().getMinX() <= orc.Node.getBoundsInParent().getMinX() )
                    				  
                    				    )  {
                    			
                    			//hero hits the side of orc
                    			G1.hero.right_Timeline.stop();
                    			G1.hero.down_Timeline.play();
                    			
                    			orc.right_Timeline.setCycleCount(120);
                    			orc.right_Timeline.play();
                    		}

                        	else if(G1.hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) && 
                    				(G1.hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMinY() ) && 
                    				(G1.hero.Node.getBoundsInParent().getMinY() <= orc.Node.getBoundsInParent().getMinY()) &&
                    				(G1.hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() >= G1.hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY()) &&
                    				(orc.Node.getBoundsInParent().getMaxX() - G1.hero.Node.getBoundsInParent().getMinX() >= G1.hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY())
                    				 ) {
                    			
                    			//hero hits the top of orc
                        		G1.hero.up_Timeline.setCycleCount(90);
                    	    	if(G1.hero.up_Timeline.getStatus() != Animation.Status.RUNNING) {
                    	    		G1.hero.up_Timeline.play();
                        	    }
                    	    	orc.up_Timeline.stop();
                    	    	orc.down_Timeline.play();
                    	    	G1.hero.up_Timeline.setOnFinished(e -> G1.hero.down_Timeline.play());	
                    		}

                    		else if (G1.hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) && 
                    				(G1.hero.Node.getBoundsInParent().getMinY() <= orc.Node.getBoundsInParent().getMaxY() ) && 
                    				(G1.hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMaxY() ) &&
                    				(G1.hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() >= orc.Node.getBoundsInParent().getMaxY() - G1.hero.Node.getBoundsInParent().getMinY()) &&
                    				(orc.Node.getBoundsInParent().getMaxX() - G1.hero.Node.getBoundsInParent().getMinX() >= orc.Node.getBoundsInParent().getMaxY() - G1.hero.Node.getBoundsInParent().getMinY())
                    				) {
                    			orc.up_Timeline.stop();
                    			G1.pauseGame();
                    			
                    		}

                    		
                        }
                    }
                }
                ));
                
                Timeline right_Timeline = new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                    	
                    	
                    	orc.Node.setLayoutX(orc.Node.getLayoutX() + 1.5);
                    	
//                    	orc.right_Timeline.setOnFinished(t -> orc.down_Timeline.play());
                    	if(G1.hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) &&
                				(G1.hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() < G1.hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY()) &&
                				(G1.hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() < orc.Node.getBoundsInParent().getMaxY() - G1.hero.Node.getBoundsInParent().getMinY()) &&
                				(G1.hero.Node.getBoundsInParent().getMaxX() >= orc.Node.getBoundsInParent().getMinX()) &&
                				(G1.hero.Node.getBoundsInParent().getMinX() <= orc.Node.getBoundsInParent().getMinX() )
                				  
                				    )  {
                			
                			//hero hits the side of orc
                    		G1.hero.right_Timeline.stop();
                    		G1.hero.down_Timeline.play();
                			
                			orc.right_Timeline.setCycleCount(120);
                			orc.right_Timeline.play();
                		}

                    	else if(G1.hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) && 
                				(G1.hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMinY() ) && 
                				(G1.hero.Node.getBoundsInParent().getMinY() <= orc.Node.getBoundsInParent().getMinY()) &&
                				(G1.hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() >= G1.hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY()) &&
                				(orc.Node.getBoundsInParent().getMaxX() - G1.hero.Node.getBoundsInParent().getMinX() >= G1.hero.Node.getBoundsInParent().getMaxY() - orc.Node.getBoundsInParent().getMinY())
                				 ) {
                			
                			//hero hits the top of orc
                	    	G1.hero.up_Timeline.setCycleCount(90);
                	    	if(G1.hero.up_Timeline.getStatus() != Animation.Status.RUNNING) {
                	    		G1.hero.up_Timeline.play();
                    	    }
                	    	orc.up_Timeline.stop();
                	    	orc.down_Timeline.play();
                	    	G1.hero.up_Timeline.setOnFinished(e -> G1.hero.down_Timeline.play());	
                		}

                		else if (G1.hero.Node.getBoundsInParent().intersects(orc.Node.getBoundsInParent()) && 
                				(G1.hero.Node.getBoundsInParent().getMinY() <= orc.Node.getBoundsInParent().getMaxY() ) && 
                				(G1.hero.Node.getBoundsInParent().getMaxY() >= orc.Node.getBoundsInParent().getMaxY() ) &&
                				(G1.hero.Node.getBoundsInParent().getMaxX() - orc.Node.getBoundsInParent().getMinX() >= orc.Node.getBoundsInParent().getMaxY() - G1.hero.Node.getBoundsInParent().getMinY()) &&
                				(orc.Node.getBoundsInParent().getMaxX() - G1.hero.Node.getBoundsInParent().getMinX() >= orc.Node.getBoundsInParent().getMaxY() - G1.hero.Node.getBoundsInParent().getMinY())
                				) {
                			orc.right_Timeline.stop();
                			G1.pauseGame();
                			
                		}

                    	
                    }
                }
                ));
                
                
                
                
                orc.setDown_Timeline(down_Timeline);
                orc.setUp_Timeline(up_Timeline);
                orc.setRight_Timeline(right_Timeline);
                orc.down_Timeline.pause();
                orc.up_Timeline.pause();
                orc.right_Timeline.pause();
                
                anchorPane.getChildren().addAll(orc.Node);
                
                orc.die_Rotate = new RotateTransition();
                orc.die_Rotate.setNode(orc.Node);
                orc.die_Rotate.setDuration(Duration.millis(1000));
                orc.die_Rotate.setCycleCount(1);
                orc.die_Rotate.setInterpolator(Interpolator.LINEAR);
                orc.die_Rotate.setByAngle(360);
                orc.die_Rotate.setAxis(Rotate.Z_AXIS);
                
                orc.die_Fall = new TranslateTransition(); 
                
                orc.die_Fall.setNode(orc.Node);
                orc.die_Fall.setDuration(Duration.millis(1000));
                orc.die_Fall.setCycleCount(1);
//                G1.Orcs.get(i).die_Fall.setInterpolator(Interpolator.LINEAR);
                orc.die_Fall.setToY(1000);
               
                
                G1.Orcs.add(orc);
                
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

        for(int i = 0; i < G1.Chests.size(); i++){
            G1.Chests.get(i).Node.setLayoutX(GD.Chest_p.get(i)[0]);
            G1.Chests.get(i).Node.setLayoutY(GD.Chest_p.get(i)[1]);
            G1.Chests.get(i).Drop = (int) GD.Chest_p.get(i)[2];
        }

        for(int i = 0; i< G1.FS_L.size(); i++){
            G1.FS_L.get(i).Node.setLayoutX(GD.Falling_p.get(i)[0]);
            G1.FS_L.get(i).Node.setLayoutY(GD.Falling_p.get(i)[1]);
        }

        G1.boss.Node.setLayoutX(GD.Boss[0]);
        G1.boss.Node.setLayoutY(GD.Boss[1]);

        G1.hero.hammer.Node.setLayoutX(GD.Sword[0]);
        G1.hero.hammer.Node.setLayoutY(GD.Sword[1]);

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
