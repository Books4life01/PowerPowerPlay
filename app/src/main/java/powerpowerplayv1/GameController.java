package powerpowerplayv1;
import java.util.ArrayList;
/**
 * Class Description:
 *      The Game Controller class controls the flow of the game. It is the main thread that the game runs on.
 * It is responsible for recieving imformation(like where a beacon or cone is placed) from the Player objects(COmputer or HUmane)
 * acting upon that imformation and then updating the graphisc.
 * Methods
 * Public
 *      runLoop()- runs the main gameLoop
 *      updateScores()-sends updated scores to both player objects which then updates their TextContainers
 *      addCone()-called by Player object when a cone is placed. the Game COntroller object then places the cone on the official
 *      cone Board and freezes the PLayer who placed it as a cooldown
 *  Private
 *      updateScores()-updates Scores in the PlayerObjects
 *      calculateScores()- calculateScores from the possesion and Cones boards ocurences of a character in a string; used to count cones in conesBoard
 *      countCharacters()- helper method for calculateScores which counts the
 *      static circuitComplete()- modified BFS algorithm used to see if a circuit has been achieved by either color
 *      
 *          
 *      
 */
public class GameController {
    boolean noTimer = false;
    static String gameMode = "";


    int  possessionBoard[][]= new int [5][5];//1 is red 2 is blue
    String  conesBoard[][]= new String [5][5];
    int[][] coolDownBlue = new int[][]{
        {3,2,2,3,4},
        {3,3,3,3,4},
        {2,3,4,4,5},
        {3,3,3,3,4},
        {3,2,2,3,4}
    };
    int[][] coolDownRed = new int[][]{
        {4,3,2,2,3},
        {4,3,3,3,3},
        {5,4,4,3,2},
        {4,3,3,3,3},
        {4,3,2,2,3}
    };
    int [][] junctionHeights = new int[][]{//Ground2, Low3, Middle4, HIgh5
        {2,3,2,3,2},
        {3,4,5,4,3},
        {2,5,2,5,2},
        {3,4,5,4,3},
        {2,3,2,3,2}
    };
     Player blue;
     Player red;
    String gamemode;
 
    public GameController(Player one, Player two)
    {
        this.blue = one;
        this.red = two;
        gamemode = Main.gamemode;

    }
    /**
     * runLoop- runs the main GameLoop
     * @param noTimer- paramter used for Sandbox mode
     */
    public void runLoop(boolean noTimer)
    {
        this.noTimer = noTimer;
        while(!Main.gameOver || noTimer)
        {
            //updates player boards and scores
            blue.updatePossessionBoard(possessionBoard);
            blue.updateConesBoard(conesBoard);
            red.updatePossessionBoard(possessionBoard);
            red.updateConesBoard(conesBoard);
            updateScores();
            

            
        }
        updateScores();
        
    }
    /**
     * updates the scores in the player objects
     */
    private void updateScores(){
        int[] redScores = calculateScore('r');
        int[] blueScores =calculateScore('b');
        blue.updateScores(blueScores);
        blue.textContainer.updateCircuit(circuitAchieved(possessionBoard, 2));
        blue.repaint();
        red.updateScores(redScores);
        red.textContainer.updateCircuit(circuitAchieved(possessionBoard, 1));
        red.repaint();
    }
    /**
     *  function called by Player object which places a cone  or beacon onto a certain space
     * @param col
     * @param row
     * @param activePlayer
     */
    public void addCone(int col, int row, char activePlayer)
    {
        if(conesBoard[row][col] == null || !(conesBoard[row][col].contains("B") || conesBoard[row][col].contains("R")))
        {
            if(conesBoard[row][col] == null)
            {
                conesBoard[row][col]=String.valueOf(activePlayer);
            }
            else {
                conesBoard[row][col]+=String.valueOf(activePlayer);
            }
            if(activePlayer=='b')//only want thsi int array to count possession by cone
            {
                possessionBoard[row][col]=2;
                if(!noTimer)blue.freeze(coolDownBlue[row][col]);
                blue.conesPlaced++;
                blue.textContainer.conesPlaced++;     
            }
            else if(activePlayer == 'B'){
                if(!noTimer)blue.freeze(coolDownBlue[row][col]);
                possessionBoard[row][col]=2;
                blue.beaconsPlaced++;
                blue.textContainer.beaconsPlaced++;        }
            else if(activePlayer == 'R'){
                if(!noTimer)red.freeze(coolDownRed[row][col]);
                possessionBoard[row][col]=1;
                red.beaconsPlaced++;
                red.textContainer.beaconsPlaced++;
            }
            else
            {
                possessionBoard[row][col]=1;
                if(!noTimer)red.freeze(coolDownRed[row][col]);
                red.conesPlaced++;
                red.textContainer.conesPlaced++;     
            }
        } 
    }
    /**
     * calculates scores using possesion and cones boards
     * @param forPlayer
     * @return
     */
    private int [] calculateScore(char forPlayer)
    {
        //parentheses spaghetti
        String beacon=String.valueOf(Character.toUpperCase(forPlayer));
        
        //define numbers
        int numGround=0;
        int numLow=0;
        int numMid=0;
        int numHigh=0;
        int numJunctions=0;
        int totalScore=0;
        //for loop spaghetti
        for(int i=0; i<5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                //if statement spaghetti
                //for each square in posession board, check if it is a beacon controlled by for player or a cone controlled by forPlayer
                if(possessionBoard[i][j] == ((forPlayer=='r')?1:2) && conesBoard[i][j] != null &&(!(conesBoard[i][j].contains("B") || conesBoard[i][j].contains("R"))))
                {
                    numJunctions++;
                }
                //now the tricky part
                //find the value of the current junction
                //this thing with the array is more readable but overcomplicated
                String [] heights=new String[]{"ground", "low", "middle", "high"};
                String currentHeight="";
                int currentScoringValue=junctionHeights[i][j];
                //subtracting two from the score should give you the correct index in the heights array
                //i.e. 2 points -2 = index 0
                currentHeight=heights[currentScoringValue-2];
                int conesPlaced = 0;
                if(conesBoard[i][j] != null) conesPlaced=countCharacters(conesBoard[i][j],forPlayer);
                if(currentHeight.equals("ground"))numGround+=conesPlaced;
                if(currentHeight.equals("low"))numLow+=conesPlaced;
                if(currentHeight.equals("middle"))numMid+=conesPlaced;
                if(currentHeight.equals("high"))numHigh+=conesPlaced;
            }
        }
        totalScore+=2*numGround+3*numLow+4*numMid+5*numHigh + 3*numJunctions;
        return new int[]{numGround, numLow, numMid, numHigh, numJunctions, totalScore};

    }
    /**
     * Counts occurences of a specific character in a String
     * @param in
     * @param lookingFor
     * @return
     */
    private int countCharacters(String in, char lookingFor)
    {
        int count=0;
        for(int i=0; i<in.length(); i++)
        {
            if(in.charAt(i)==lookingFor || Character.toUpperCase(in.charAt(i))==lookingFor)
            {
                count++;
            }
        }
        return count;
    }
    /**
     * circuitAcheived- a modified BFS algorithm which checks to see if there is a line of cones from one corner of the field to the other
     * @param possessionBoard
     * @param value
     * @return
     */
    private static boolean circuitAchieved(int[][] possessionBoard, int value){
        
        ArrayList<Point> visited = new ArrayList<Point>();
        ArrayList<Point> toVisit = new ArrayList<Point>();
        ArrayList<Point> nextToVisit = new ArrayList<Point>();
        Point exit1=null;
        Point exit2=null;
        Point exit3 = null;
        Point start1=null;
        Point start2=null;
        Point start3=null;
        //Define Start and Exit POsitions determined by the color we are checking
        if(value ==1){
             exit1 = new Point(4,4);
             exit2 = new Point(3,4);
             exit3 = new Point(4,3);
            start1 = new Point(0,0);
             start2 = new Point(0,1);
             start3 = new Point(1,0);
        }
         else if(value ==2){
            exit1 = new Point(4,0);
            exit2 = new Point(3,0);
            exit3 = new Point(4,1);
            start1 = new Point(0,4);
            start2 = new Point(1,4);
            start3 = new Point(0,3);
         }
        toVisit.add(start1);
        toVisit.add(start2);
        toVisit.add(start3);
        while(true){
            //return false if there are no paths to visit
            //return true if one of the paths in toVisit is an end destination
            if(toVisit.size()==0)return false;
            for(Point point: toVisit){//iterate through points to visit

                if(point.equals(exit1)||point.equals(exit2)||point.equals(exit3))return true;//check for an exit condition
                if(possessionBoard[point.row][point.col]==value){//only here for the start otherwise a later condition checks for this
                    for(int r=-1; r<=1;r++){//find neghbors
                        for(int c =-1; c<=1; c++)
                        {
                            Point possibleNeighbor = new Point(point.row+r, point.col+c);
                            if(!possibleNeighbor.outB()&&possessionBoard[possibleNeighbor.row][possibleNeighbor.col]==value&&!Point.inList(visited, possibleNeighbor)&&!Point.inList(toVisit, possibleNeighbor) &&!Point.inList(nextToVisit, possibleNeighbor))
                            {//if we havent visited and it is a vali spot add it to nextToZVisit list
                                nextToVisit.add(possibleNeighbor);
                            }
                        }
                    }
                }
                visited.add(point);
            }
            toVisit=new ArrayList<Point>();//empty the toVisit list
            for(Point point: nextToVisit)//add the nextTOVisit points to the toVisit list
            {
                toVisit.add(point);
            }
            nextToVisit=new ArrayList<Point>();   //empty the nextToVisit ledt    
        }
    }
 }
 //class to be used with BFS algorithm circuitComplete()
class Point
    {
        int row;
        int col;
        
        
        public Point(int row, int col){
            this.row=row;
            this.col=col;
            
        }
        public boolean outB(){
            if(row<0||col<0||row>=5||col>=5)return true;
            else return false;
        }
        
        public static boolean inList(ArrayList<Point> points, Point point){
            for(Point element: points){
                if(element.equals(point))return true;
            }
            return false;
        }
        public boolean equals(Point other){
            if(other.row==this.row&&other.col==this.col)return true;
            return false;
        }
        public String toString(){
            return "("+row+","+col+")";
        }
    }
 