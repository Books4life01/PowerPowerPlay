package powerpowerplayv1;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import static javax.swing.JOptionPane.showMessageDialog;
/**
 * Class Description
 *      The the class that runs when the application is started. First it presents the MenuFrame. THen based on uttons clicked on the menuFrame
 * It will set up  a certain gamemode and start the GameController, and finally when the gme ends it will present the
 * Statistics Frame.
 */
public class Main {
    public static boolean waitForStart = true;
    private static MenuFrame menu;
    static String gamemode = "null";

    static GameController gameController;
    static HumanPlayer blue;
    static HumanPlayer red;
    static ComputerPlayer blueComputer;
    static TextContainer computerText;
    static JFrame frame;
    volatile static boolean gameOver = true;
    volatile static int elapsedTime;
   static String [][] conesBoard = new String[5][5];
   static boolean pressKeyToSTart = true;
     public static void main(String[] args)
    {
        //create Menu Frame
        try{

            menu = new MenuFrame();
            menu.setDefaultCloseOperation(MenuFrame.EXIT_ON_CLOSE);
            menu.setVisible(true);
        }
        catch(Exception e){e.printStackTrace();}
        
        while(waitForStart){
            System.out.print("");
            //wait for one of the buttons on menuFrame to e clicked
        }
        menu.setVisible(false);
        
        //the gamemode String will change based on the button pressed on the MenuFrame
        
        if(!gamemode.equals("Statistics"))//if the gamemode isnt statistics
        {
            //Create Frame
            frame = new JFrame();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 

            //create one HUman PLayer with its own GamePanel pass it some callack functions for placing cones and beacons and pass it the keys for the movements
            GamePanel redPlayer = new GamePanel();
            redPlayer.gameContainer.setBorder(new LineBorder(Color.RED.darker(), 5));
            redPlayer.setBorder(new LineBorder(Color.LIGHT_GRAY.darker(), 5));

            red = new HumanPlayer(redPlayer);
            red.setCallbackCone(Main::callbackFunctionRedCone);
            red.setCallbackBeacon(Main::callbackFunctionRedBeacon);
            red.setKeys(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT,KeyEvent.VK_ENTER, KeyEvent.VK_SLASH);

            //then add the humanPlayer's Gamepanel to the contentPane
            Container contentPane = frame.getContentPane();
            contentPane.setLayout(new GridLayout(2,1));
            contentPane.add(redPlayer);

            //If its multiplayer or Sandbox mode then we will create another human player
            if(gamemode.equals("Multiplayer") || gamemode.equals("Sandbox")){
                //create a blue PLayer with its own GamePanel and pass it the key functions necessary as well as the necessary callback functions
                GamePanel bluePlayer = new GamePanel();
                bluePlayer.gameContainer.setBorder(new LineBorder(Color.BLUE.darker(), 5));
                bluePlayer.setBorder(new LineBorder(Color.LIGHT_GRAY.darker(), 5));

                blue = new HumanPlayer(bluePlayer);
                blue.setCallbackCone(Main::callbackFunctionBlueCone);
                blue.setCallbackBeacon(Main::callbackFunctionBlueBeacon);
                blue.setKeys('w', 's', 'd','a',KeyEvent.VK_SHIFT, 'f');
                contentPane.add(bluePlayer);// add the blue players screen as the second view
            }
            //else if it is computer mode
            else if(gamemode.equals("Computer"))
            {
                //create a Computer PLayer and its TextContiner
                computerText = new TextContainer();
                blueComputer = new ComputerPlayer(computerText);
                blueComputer.setCallbackCone(Main::callbackFunctionComputerCone); 
                blueComputer.setCallbackBeacon(Main::callbackFunctionComputerBeacon);
                contentPane.add(computerText);//add the second view as the computer's text container because we dont need to see its screen
            }
            KeyListener listener = new KeyListener(){//create a key listener anonymous class

                @Override
                public void keyPressed(KeyEvent e) {
                    pressKeyToSTart=false;
                    
                }
                @Override
                public void keyReleased(KeyEvent e) {//listens for specific key presses 
                    pressKeyToSTart = false;
                    int k = e.getKeyCode();
                    if((k>=37&&k<=40) || k ==16 || k == 47)callbackFunction(e);//listens for arrow keys forward slash and shift
                }
                @Override
                public void keyTyped(KeyEvent e) {callbackFunction(e);}
            };
            frame.addKeyListener(listener);//ad the listener to the game frame and ensure the gameframe has the focus
            frame.setFocusable(true);
            frame.setVisible(true);
            frame.toFront();
            frame.requestFocusInWindow();
            //if multiplayer or sandbox pass two human players 
            if(gamemode.equals("Multiplayer")|| gamemode.equals("Sandbox"))gameController  = new GameController(blue, red);
            else gameController = new GameController(blueComputer, red);//else if its computer pass a computer player and a human player
            Main.gameOver = false;//game Over is false
            while(pressKeyToSTart){//wait for keypress to start timer
                System.out.print("");
            }
            GameController.gameMode=gamemode;
            if(!gamemode.equals("Sandbox")){//if it isnt sandbox mode then start the timer
                Thread timeLoop = new Thread(Main::runTimeLoop);
                timeLoop.start();
            }
            else{
                blue.panel.field.alert("Sandbox");//else set the JLael in the GameFIelds of the player objects to say Sandbox
                red.panel.field.alert("Sandbox");
            }

            if(gamemode.equals("Computer"))new Thread(blueComputer::run).start();//if its computer start the computer logic thread
            boolean noTimer=false;
            if(gamemode.equals("Sandbox"))noTimer = true;
            gameController.runLoop(noTimer);//start the gameController Loop


            //after the game is over
            int redScore = red.textContainer.totalScore;
            int blueScore;
            //get the scores
            if(gamemode.equals("Multiplayer"))blueScore = blue.textContainer.totalScore;
            else blueScore = blueComputer.textContainer.totalScore;
            //create match data objects based off the scores
            MatchData redMatch = new MatchData('r', (redScore>blueScore?true:false),red.textContainer.junctions,red.textContainer.circuitAchieved, red.conesPlaced, red.beaconsPlaced);
            MatchData blueMatch ;
            if(gamemode.equals("Multiplayer")) blueMatch = new MatchData('b', (redScore<blueScore?true:false),blue.textContainer.junctions,blue.textContainer.circuitAchieved, blue.conesPlaced, blue.beaconsPlaced);
            else blueMatch = new MatchData('b', (redScore<blueScore?true:false),blueComputer.textContainer.junctions,blueComputer.textContainer.circuitAchieved, blueComputer.conesPlaced, blueComputer.beaconsPlaced);
            //send a dialog box telling who the winner is
            showMessageDialog(null, "The Winner is " + (redScore>blueScore?"Red!":"Blue!"));

        }
        //now show stastics
        SheetsQuickstart.readDatabase();//read all the previous matches

        showStatisticsFrame();//show the stastics fram


    }
    public static void showStatisticsFrame(){
        JFrame statFrame = new JFrame("Statistics");//create a new frame
        statFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        statFrame.setVisible(true);

        JPanel statPanel = new JPanel();
        statFrame.add(statPanel);
        statPanel.setBackground(Color.GRAY);//add a grey JPAnel

        ArrayList<MatchData> winsOnly =MatchData.onlyWinners();
        //create an html table comparing the average stastics from the matches of winners vs of all matches 
        String str = "<html><center><bold>Statistics From All Matches Played(Anywhere)</bold></center><br>"+
        "<table >"+
        "<tr><th>Catergory</th><th>All</th><th>WinsOnly</th></tr>"+
        "<tr><td>Average Cones Placed on Ground Junctions</td><td>" + round(MatchData.getAverageJunction(0)) +"</td><td>" + round(MatchData.getAverageJunction(winsOnly,0))+"</td></tr>"+
        "<tr><td>Average Cones Placed on Low Junctions</td><td>" + round(MatchData.getAverageJunction(1)) +"</td><td>" + round(MatchData.getAverageJunction(winsOnly,1))+"</td></tr>"+
        "<tr><td>Average Cones Placed on Middle Junctions</td><td>" + round(MatchData.getAverageJunction(2)) +"</td><td>" + round(MatchData.getAverageJunction(winsOnly,2))+"</td></tr>"+
        "<tr><td>Average Cones Placed on High Junctions</td><td>" + round(MatchData.getAverageJunction(3)) +"</td><td>" + round(MatchData.getAverageJunction(winsOnly,3))+"</td></tr>"+
        "<tr><td>Average Junctions Owned</td><td>" + round(MatchData.getAverageJunction(4)) +"</td><td>" + round(MatchData.getAverageJunction(winsOnly,4))+"</td></tr>"+
        "<tr><td>Average Cones Placed</td><td>" + round(MatchData.getAverageConesPlaced()) +"</td><td>" + round(MatchData.getAverageConesPlaced(winsOnly))+"</td></tr>"+
        "<tr><td>Average Beacons Placed </td><td>" + round(MatchData.getAverageBeaconsPlaced()) +"</td><td>" + round(MatchData.getAverageBeaconsPlaced(winsOnly))+"</td></tr>"+
        "<tr><td>Percent Circuit Achieved</td><td>" + round(MatchData.getPercentCircuitAchieved()) +"</td><td>" + round(MatchData.getPercentCircuitAchieved(winsOnly))+"</td></tr>"+
        "<tr><td>Average Final Score</td><td>" + round(MatchData.getAverageScore()) +"</td><td>" + round(MatchData.getAverageScore(winsOnly))+"</td></tr>"+"</table></html>";
        JLabel stats = new JLabel();//put this string a JLael
        stats.setText(str);
        stats.setBorder(new LineBorder(Color.yellow.brighter(), 5));//make a yellow border
        statPanel.add(stats);



    }
    //calback function to be passed to PLayer objects
    public static void callbackFunction(KeyEvent k){
        if(gamemode.equals("Multiplayer")||gamemode.equals("Sandbox"))blue.checkKey(k);
        red.checkKey(k);
    }
    public static void callbackFunctionBlueCone(){
        gameController.addCone(blue.panel.field.highlightX, blue.panel.field.highlightY, 'b');
    }
    public static void callbackFunctionBlueBeacon(){
        gameController.addCone(blue.panel.field.highlightX, blue.panel.field.highlightY, 'B');
    }
    public static void callbackFunctionRedCone(){
        gameController.addCone(red.panel.field.highlightX, red.panel.field.highlightY, 'r');

    }
    public static void callbackFunctionRedBeacon(){
        gameController.addCone(red.panel.field.highlightX, red.panel.field.highlightY, 'R');
    }
    public static void callbackFunctionComputerCone(){
        gameController.addCone(blueComputer.selectedX,blueComputer.selectedY,'b');
    }
    public static void callbackFunctionComputerBeacon(){
        gameController.addCone(blueComputer.selectedX,blueComputer.selectedY,'B');
    }
//loop which tracks time and updates the TimeLabels on the gameFields
    public static void runTimeLoop(){
        Main.gameOver = false;
        int startTime = getTime();
        elapsedTime = 0;
        String message = "";
        while(elapsedTime < 90){
            elapsedTime = getTime()-startTime;
             message = "Time Remaining: " + (90-elapsedTime);
            if(!gamemode.equals("Sandbox"))red.panel.field.alert(message);

            if(gamemode.equals("Multiplayer"))blue.panel.field.alert(message);

        }
        Main.gameOver = true;
    }
    //gets the time
    public static int getTime(){
        return (int)(System.nanoTime()/Math.pow(10,9));
    }
    //rounding helper method
    public static double round(double in){
        return((int)(in*100))/100.0;
    }    
}
