package powerpowerplayv1;

import javax.swing.JPanel;
/**
 * Class Description:
 *       A class to provide the basic methods that every child of the Player Class will use. 
 * Methods:
 * Public
 *      Freeze: acts as a cooldown function. Starts a thread which prevents any actions from being made by the player
 *      updateScores: passes score imformation to the player which the player can then use to update its TextContainer
 *      updateConesBoard and updatePossesionBoard: updates warious boards which the plpayer holds which tell it imformation about the state of the game field
 *      setCallbackCone and setCallBackBeacon: pass a Runnale to run if a cone or beacon is placed. 
 *      repaint: method to e implemented by the HumanPlayer subclass but which doesnt do anything in its current form
 *      getTextContainer: getterMethod for TextContainer Object
 *      freezeThread: helper method for the freeze method which is a while loop which tracks time elapsed and runs for a specified amount of seconds
 * 
 */
public class Player {

    //Instance VAriables

    //two callback functions depending on whether a cone or beacon is placed
    Runnable callbackCone;
    Runnable callbackBeacon;
    //two variables tracking how many cones and beacons have been placed
    int conesPlaced = 0;
    int beaconsPlaced =0;
    
    //an int tracking how many seconds left until the player is unfrozen
    int freezeSeconds = 0;

    //a boolean tracking whether the player is  currently frozen; the keyword volatile allows multiple threads to access and chaneg i
    volatile boolean frozen = false;

    //a textContainer object(which is extends the JPanel class) which will hold the scores of this player
    public TextContainer textContainer;
    
    //2-d arrays which track the cones on each junction and the possesion of each junction
    public int[][] possessionBoard = new int[5][5];
    public String[][] conesBoard = new String[5][5];

//simple constructor; takes a JPAnel argumemt but doesnt do anything with it. HUmanPlayer will be passes a Game Panel; Computer Player will be passed a TexContainer; both of those extend the JPanel class
    public Player(JPanel in){

    }


    //two methods to update the 2-d arrays; these will be called by the Game Controller class whenever a new cone or beacon is placed on teh field
    public void updateConesBoard(String[][]in){conesBoard = in;}
    public void updatePossessionBoard(int[][]in){possessionBoard = in;}

    /**
     * Freeze: this method freezes the player for the specified  amount of seconds
     * @param seconds- how many seconds to freeze the player for
     */
    public void freeze(int seconds){
        Thread freezeThread = new Thread(this::freezeThread);//creates a new thread to run concurrently to the main one; thie thread will run the freezeThread method
        //the (double colon):: operator returns a runnable which is kinda like a pointer to a method. 
        //The operator takes a class name or instance of the class on the left side and one of its methods on the right side

        freezeSeconds = seconds;//set the instance freze seconds equal to the amount of time to freeze for
        freezeThread.start();//start the freezeThread

    }
    /**
     * Freeze Thread- tracks how long the player is frozen for and updates variables corresponding to that
     */
    public void freezeThread(){
        int startTime = Main.getTime();//get the start time; uses a method in the Main class which converts System.nanTime() into seconds
        int elapsedTime = 0;//set an int elapsed time to 0
        frozen = true;//set the boolean frozen to true
        while(elapsedTime < freezeSeconds){//while the elapsedTime is less than the time we are frozen for
            elapsedTime = Main.getTime()-startTime;//elapsed time = currentTime - startTime
        }
        frozen = false;//when the loop is finished, the player is no longer frozen
    }
    public void repaint(){}//only here to allow the human player to override it later; the computer player does not need to repaint anything

    public TextContainer getTextContainer(){return textContainer;}//return the textContiner object that all Player objects have; this object holds the scores of the player

    public void setCallbackCone(Runnable callback){this.callbackCone = callback;}//specifies a method for the player to call when it places a cone
    public void setCallbackBeacon(Runnable callback){this.callbackBeacon= callback;}//specifies a method for the player to call when it places a beacon
   /**
    * updateScores-sends updated scores and data to the textContainer object
    * @param in- an int[]{#ofgroundJunctionsScores, #low, #middle, #high, #junctionsOwned}
    */
    public void updateScores(int[] in){
        textContainer.updateJunctions(in[0],in[1],in[2],in[3], in[4]);//sends info regarding the cones on junctions
        textContainer.updateJunctionsOwned(in[4]);//sends info regarding how many junctions this player owns
        textContainer.updateTotalScore();//tells the object to update the total scores
        textContainer.update();//tells the object to update the visual representation of the data.
    }

}
