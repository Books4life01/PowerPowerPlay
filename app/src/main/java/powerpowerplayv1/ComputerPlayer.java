package powerpowerplayv1;
/**
 * Class Description:
 * The Computer PLayer class extends the PLayer class to act as a virtual opponent. The computer determines where
 * to place cones by giving certain weight to specific actions. Owning a junctions is considerd a numer of points as is 
 * as is the pointage of the junctions, as well as how contested the junction has been(number of cones on junction)
 * Non Inherited Methods:
 * Public methods
 *         -Run()-a while loop which will continously place cones and then wait for the cooldown
 * Private methods
 *          -findOptimalSpot, findBestBeaconSpot, findPointsAtSpot, and findPointsAtSpotBeacon: all of which help determine the actions of the computer player
 * 
 * 
 */
public class ComputerPlayer extends Player{
   
    int selectedX = 0;
    int selectedY = 0;
    
    int [][] junctionPoints = new int[][]{//Ground2, Low3, Middle4, HIgh5
        {2,3,2,3,2},
        {3,4,5,4,3},
        {2,5,2,5,2},
        {3,4,5,4,3},
        {2,3,2,3,2}
    };

    public ComputerPlayer(TextContainer container){
        super(container);//super class has an argument of Jpanel but doesnt actually do anything with it

        this.textContainer = container;//set the player
    }
    
    /**
     * Run - a loop that repeats until the game is over, and makes decision for the computer about where to put cones
     */
    public void run(){
        
        while(Main.gameOver == false){//while the game isnt over
            if(!frozen){//if the player isnt frozen
                if(Main.elapsedTime > 80 &&beaconsPlaced<2){//if we have less than 10 seconds less and we havent placed two beacons, then lets place a beacon
                    int[] point = findBestBeaconSpot();//find the best Beacon spot
                    selectedY = point[0];
                    selectedX = point[1];
                    callbackBeacon.run();//ru the callback beacon funciton to place the beacon
                    frozen = true;//imediatly freeze the computer because by the time the GameController attempts to freeze the computer this thread might have already looped back again and tried to place anotehr beacon
                }
                else if(conesPlaced <30){
                    int[] point = findOptimalSpot();//find best spot to place a cone
                    selectedY = point[0];
                    selectedX = point[1];
                    callbackCone.run();
                    
                    frozen = true;//imediatly freeze the computer because by the time the GameController attempts to freeze the computer this thread might have already looped back again and tried to place anotehr beacon
                }
            }
        }
    }

    /**
     * Find Optimal Spot- iterates through all spots; finds the modified(the computer has an inclination to place on the humanPlayers cones even if it might not be the most points) pointValue for that spot and uses this to determine the best position
     * @return- an int[]{bestRow/bestY, bestCol/bestX}
     */
    private int[] findOptimalSpot(){
        int bestRow = 0;
        int bestCol = 0;
        int bestScore = findPointsAtSpot(0,0);
        for(int row = 0; row < 5; row++){
            for(int col = 0; col < 5; col++){
                int points = findPointsAtSpot(col, row);//find points at that spot
                if(points > bestScore){//if the points is greater than the current best
                    bestScore = points;//update best Score, row, and col
                    bestRow = row;
                    bestCol = col;
                }
            }
        }
        return new int[]{bestRow,bestCol};
    }
    /**
     * Find Best BEacon Spot - same as find Optimal Spot except the beacon uses a slightly different method to find the best spot
     * @return- an int[]{bestRow/bestY, bestCol/bestX}
     */
    private int[] findBestBeaconSpot(){
        int bestRow =0;
        int bestCol = 0;
        int bestScore = findPointAtSpotBeacon(0,0);
        for(int row = 0; row < 5; row++){
            for(int col = 0; col < 5; col++){
                int points = findPointAtSpotBeacon(col, row);//uses a different method than find Optimal Spot
                if(points > bestScore){
                    bestScore = points;
                    bestRow = row;
                    bestCol = col;
                }
            }
        }
        return new int[]{bestRow,bestCol};

    }

    /**
     * Find Points At Spot - determines how many points the computer would get if it placed a cone at this spot except with a little modifications
     * @param x - xPOs/col
     * @param y - yPos/row
     * @return int containing number of points
     */
    private int findPointsAtSpot(int x, int y){
        int points = junctionPoints[y][x];//first add the normal amount of points
        points+= (possessionBoard[y][x]==0)?3:0;//if no ones has placed on this spot then add 3
        points +=(possessionBoard[y][x]==1)?4:0;//if this junction is owned by the opposite color add 4(this makes the computer target junctions owned by the human player more)
        if(conesBoard[y][x] != null &&(conesBoard[y][x].contains("B") || conesBoard[y][x].contains("R")))points = 0;//if this junction has been capped by a beacon then the points is 0(this prevents the computer from playing on a beacon capped spot)
        return points;
    }

    /**
     * Find Points At Beacon - determines how many points the computer would get if it placed a beacon at this spot except with a little modifications
     * @param x - xPOs/col
     * @param y - yPos/row
     * @return int containing number of points
     */
    private int findPointAtSpotBeacon(int x, int y){
        int points = 0;//start with the base points for that junction
        points += possessionBoard[y][x]==1?3:0;//adds 3 points if the junction is owned by he opposite color
        points += possessionBoard[y][x]==0?2:0;
        String cones = conesBoard[y][x];

        //then it takes the string represntation of the cones on that spot and adds and integer which is half of the cones on that spot; this makes the computer target junctions which have been contested more
        //then it checks to see if there is already a beacon there andd sets points to 0 if there is
        if(cones != null){points += conesBoard[y][x].length();points = (conesBoard[y][x].contains("R") || conesBoard[y][x].contains("B"))?0:points;}
        return points;
    }
}
