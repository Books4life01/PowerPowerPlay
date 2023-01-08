package powerpowerplayv1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
/**
 * Class Description:
 *      TextContainer is bascially a specificly constructed JPanel which displays the scores of a Player
 * Methods
 * Public
 *      update()- updates the JLael with the current scoring imformation
 *      updateJunctions()- updates this object with the latest junction scoring data
 *      updatBEaconsPlaced()-updates this object with the latest beacon placed data
 *      updateCircuit()- updates this object with the latest circuit scored data
 *      updateJunctionsOwned() - updates this object with the latest junctions owned data
 */
public class TextContainer extends JPanel{
    int[] junctions = new int[]{0,0,0,0, 0};//array containing data aout the cones on each junctions. Format: {cones on ground, low, mid, high, junctions owned}
    private int junctionsOwned;//number of junctions Owned
    public int beaconsPlaced;//number of beacons placed
    public int conesPlaced=0;//numer of cones placed
    boolean circuitAchieved = false;
    int totalScore = 0;

    JLabel htmlTest;//lael we will put the for matting in
    

    

    public TextContainer() {
        setBackground(Color.gray);//make a
        setBorder(new LineBorder(Color.BLACK.darker(), 3));
       
        htmlTest = new JLabel();
        add(htmlTest, SwingConstants.CENTER);
        update();
    }
    /**
     * update- updates the JLael in the JPanel which holds the score table. THe JLael accepts HTML format so that is what I use to construct a table
     */
    public void update(){
        //construct an html formatted string which displays a table with the data and scores for each scoring possility displayed
        String html = "<html><head><style>table, tr, td, th{border:1px solid;}</style></head>" + 
                    "<table>" + 
                        "<tr><th>Name</th><th>Number Scored</th><th>Points Earned</th></tr>"+
                        "<tr><td>Ground Junction</td><td>" + junctions[0] + "</td><td>" + 2* junctions[0] + "</td></tr>"+
                        "<tr><td>Low Junction</td><td>" + junctions[1] + "</td><td>" + 3* junctions[1] + "</td></tr>"+
                        "<tr><td>Middle Junction</td><td>" + junctions[2] + "</td><td>" + 4* junctions[2] + "</td></tr>"+
                        "<tr><td>High Junction</td><td>" + junctions[3] + "</td><td>" + 5* junctions[3] + "</td> </tr>"+
                        "<tr><td>Junctions Owned</td><td>" + junctionsOwned + "</td><td>" + 3* junctionsOwned + "</td></tr>"+
                        "<tr><td>Beacons Scored</td><td>" + beaconsPlaced + "</td><td>" + 10* beaconsPlaced + "</td></tr>"+
                        "<tr><td>Circuits Completed</td><td>" + (circuitAchieved?1:0) + "</td><td>" + 20* (circuitAchieved?1:0) + "</td></tr>"+
                    "</table>" + 
                    "<table>" + 
                        "<tr><td>ConesPlaced</td><td>" + conesPlaced + "</td></tr>"+
                        "<tr><td>BeaconsPlaced</td><td>" + beaconsPlaced + "</td><</tr>"+
                        "<tr><td>TotalScore</td><td>" + totalScore + "</td><</tr>"+
                    "</table>" + 
                "</html>";
        htmlTest.setText(html);//set the text of the JLael to this string
    }
    /**
     * Update Junctions- updates the latest data about the amount of cones stored on each juncction
     * @param groundCones-number of cones on ground junction
     * @param lowCones- # on low
     * @param middleCones- # on mid
     * @param highCones- # on high
     * @param junctionsOwned- # junctions owned(this player's color on top) by this player
     */
    public void updateJunctions(int groundCones, int lowCones, int middleCones, int highCones, int junctionsOwned){
        junctions =new int[]{groundCones, lowCones, middleCones, highCones, junctionsOwned};//redefin the junction array

    }
    /**
     * update____- a series of functions which update boolean or int values corresponding to things happening on the field; Ex updateBeaconsPLaced upadtes the amount of eacons the player has placed
     * @param in
     */
    public void updateCircuit(boolean in){circuitAchieved = in;}
    public void updateBeacons(int in){beaconsPlaced = in;}
    public void updateJunctionsOwned(int in){junctionsOwned = in;}

    /**
     * updateTotalScore-when called this method takes all the indivisual score data about beacons, junctions, ecetera and calculates the point values for each
     */
    public void updateTotalScore(){totalScore = 2*junctions[0] + 3*junctions[1] + 4*junctions[2] +5*junctions[3] + (circuitAchieved?20:0) + 10*beaconsPlaced +3*junctionsOwned;}





}
