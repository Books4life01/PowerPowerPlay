package powerpowerplayv1;

import java.awt.event.KeyEvent;
/**
 * Class Description: 
 *      The Human Player class extend the Player class to add extra fuctionality specifically keyboard input functions 
 * and the GamePanel and Field objects which are used for a visual display of the gamefield.
 * NonInherited or Overrided methods:
 * Public
 *      setKeys-sets the keys that are to be used for up,down,left,right,and place funcionality
 *      checkKey-when a key is pressed a KeyBoard listner object back in the main class listens and calls this function which dechipers what the key is and what action to do based upon its identity
 *      freezeThread-ovverided to provide a graphics addition(see explanation in commenting)
 *      repaint-overrided to add functionality to repaint the GamePanel every time it is called. Whereas a Computer PLayer wouldnt have to repaint anything
 *      updatePossesionoard and updateConesBoard - overrided to send the updated 2d arrays to the GameField display
 *      
 */
public class HumanPlayer extends Player{

    //the human player takes input from the keys; these ints hold characters or ints corresponding to the keys the user has press to perform certain actinos
    int keyUp;//moves highlighted box up
    int keyDown;//moves highlighted box down
    int keyRight;//moves highlighted box right
    int keyLeft;//moves highlighted box left
    int keyEnter;//sets a cone at the highlighted position
    int keyBeacon;//sets a beacon at the highlighte position

    GamePanel panel;//a game panel which holds a visual represnation of the field and a textcontainer
    public HumanPlayer(GamePanel panel){//one argument; that being the game panel the human player is linked to
        super(panel);//doesnt actually do anything

        this.panel = panel;  
        this.textContainer = panel.textContainer;  //the panel holds the textcotainer for the player
    }

    /**
     * Set Keys- ints holding characters or integer values which the KeyEvent class corrseponds to the pressing of certain keys
     * @param keyUp- key which will make the highlgighted box move up
     * @param keyDown - key which will make the highlighted box move down
     * @param keyRight - key which will make the highlighted box move right
     * @param keyLeft - key which will make the highlighted box move left
     * @param keyEnter - key which will place a cone
     * @param keyBeacon - key which will place a beacon
     */
    public void setKeys(int keyUp, int keyDown, int keyRight, int keyLeft, int keyEnter, int keyBeacon)
    {
        this.keyDown= keyDown;
        this.keyUp = keyUp;
        this.keyLeft = keyLeft;
        this.keyRight = keyRight;
        this.keyEnter = keyEnter;
        this.keyBeacon = keyBeacon;
    }
    

    /**
     * Check Key - detrmines what key was pressed and perform the specified action; this method is called by a KeyListener object in the main class
     * @param e - a KeyEvent object which holds imformation containing which key was pressed
     */
    public void checkKey(KeyEvent e) {
        if(!Main.gameOver){
            int c = e.getKeyChar();//get the character corrseponding
            int k  =e.getKeyCode();//get the integer corresponding
            //it a letter key is pressed the keyEvent will have a keyChar with that letter; however, is a miscellanous key is pressed, eg. eneter, tab, shift, it will return an integer key code
            if((k>=37&&k<=40) || k ==16 || k==10 || k ==47){c = k;}//if the key code is between 37 and 40(the up left right and down arrows), or 16(shift), or 10(enter key) or 47(forward slash)

            //check to see which action is called and makes sure the actions wont cause the highlightec box to go out of bounds
            if(c == keyUp && panel.field.highlightY != 0)panel.field.highlightY --;
            if(c == keyDown && panel.field.highlightY != 4)panel.field.highlightY ++;
            if(c == keyRight && panel.field.highlightX != 4)panel.field.highlightX ++;
            if(c == keyLeft && panel.field.highlightX != 0)panel.field.highlightX --;
            //repaints the field with the highlgights box in a different position
            repaint();
            if(c == keyEnter && !frozen  && conesPlaced < 30){callbackCone.run();}//checks to see if he player isnt frozen and hasnt placed more than 30 cones, if all conditions are ture than run the conePlace callbak function and increase the amount of cones PLaces
            if(c == keyBeacon && !frozen  && (Main.elapsedTime > 60 ||GameController.gameMode.equals("Sandbox"))){callbackBeacon.run();};//if the player isnt frozen, has not placed more than 2 cones, and we are in the last 30 seconds, than increase beaconsPLaced and run the beaconsPLaced callback function
        }
    }

    /**
     * @Override - the method is overrided to add printing to the game field; all added lines have comments next to them
     */
    @Override
    public void freezeThread(){
        int startTime = Main.getTime();
        int elapsedTime = 0;
        frozen = true;
        while(elapsedTime < freezeSeconds){
            panel.field.setFrozenLabel("ROBOT IS DELIVERING CONE. WAIT TIME: " + (freezeSeconds -elapsedTime));//adds a label stating the seconds left frozen

            elapsedTime = Main.getTime()-startTime;
        }
        panel.field.setFrozenLabel("");//resets the message to nothing
        frozen = false;
    }
    
    @Override
    public void repaint(){//noramlly this functions does nothing but since a human player actually has a visual represnation we need to repaint the panel which holds it.
        panel.field.repaint();
    }

    //have to override these methods so we can also update the gamefields possesion and cones Board
    @Override
    public void updatePossessionBoard(int[][] in){
        possessionBoard = in;
    }
    @Override
    public void updateConesBoard(String[][] in){
        conesBoard = in;
        panel.field.conesBoard = in;
    }
    
}
