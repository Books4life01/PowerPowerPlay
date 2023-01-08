package powerpowerplayv1;

import java.util.ArrayList;
import java.util.stream.Collectors;
/**
 * Class Description
 *      The MatchData class will compress the basic results of a match(only records results about a singular PLayer) into  a singular String and insert it into a databse
 * using the SheetsQuickstart class. 
 * Methods
 * Public 
 * compress()- compresses data about a match into a singular String; this compressed String can e used as an argument for a constructor as well
 * getTotalScore()- gets the total score from the match
 * 
 * 
 */
public class MatchData {

    //static variable
    static ArrayList<MatchData> matches = new ArrayList<MatchData>();//list of all MatchData objects created
    static int GROUND = 0;
    static int LOW = 1;
    static int MIDDLE = 2;
    static int HIGH = 3;
    static int OWNED = 4;



    


    //instance vraibles
    char color; 
    //junction info :how many cones on Each
    int ground;
    int low; 
    int middle; 
    int high;
    int owned;
    int[] junctionInfo;

    //other info
    boolean circuitAchieved;
    boolean wasWinner;
    int conesUsed; 
    int beaconsUsed;

    public MatchData(char color, boolean wasWinner, int[] junctionInfo, boolean circuitAchieved, int conesUsed, int beaconsUsed) {
        this.ground = junctionInfo[0];
        this.low = junctionInfo[1];
        this.middle = junctionInfo[2];
        this.high = junctionInfo[3];
        this.owned = junctionInfo[4];
        this.wasWinner = wasWinner;
        this.color=color;
        this.circuitAchieved=circuitAchieved;
        junctionInfo = new int[]{ground,low,middle,high,owned};
        this.conesUsed = conesUsed;
        this.beaconsUsed = beaconsUsed;
        //add it to csv
        SheetsQuickstart.addToDatabase(this);
    }
    public MatchData(String compressedString){
        //System.out.println(compressedString);
        String[] split = compressedString.split(" ",0);
        //System.out.println(Arrays.toString(split));

        this.color = split[0].charAt(0);
        this.wasWinner = Boolean.parseBoolean(split[1]);
        this.circuitAchieved = Boolean.parseBoolean(split[2]);
        String[] split2 = split[3].split(":",0);
        this.conesUsed = Integer.parseInt(split2[0]);
        this.beaconsUsed = Integer.parseInt(split2[1]);
        this.ground = Integer.parseInt(split2[2]);
        this.low = Integer.parseInt(split2[3]);
        this.middle = Integer.parseInt(split2[4]);
        this.high = Integer.parseInt(split2[5]);
        this.owned = Integer.parseInt(split2[6]);
        junctionInfo = new int[]{ground,low,middle,high,owned};
        //add it to matches array
        matches.add(this);

    }
    /**
     * Compress- takes all the data about the match and compresses it into a single string which can be parsed to retrieve it later
     * @return
     */
    public String compress(){
        String out = "";
        out+=color + " " + wasWinner + " "+circuitAchieved+" " + conesUsed + ":"+beaconsUsed+":"+ground+":"+low+":"+middle+":"+high+":"+owned;
        return out;
    }
    /**
     * returns the total score of this match
     * @return
     */
    public int getTotalScore()
    {
        return circuitAchieved?20:0 +ground*2+low*3+middle*4+high*5+owned*3+beaconsUsed*10;
    }

    //Static Methods- these are used for computing averages from all matches using the static list of MatchData called matches
    public static double getAverageJunction(ArrayList<MatchData> matches, int junction){//0 ground 1 low 2middle 3high 4 junctionsOwned
        int sum = 0;
        for(MatchData match: matches){    
            sum += match.junctionInfo[junction];
        }
        return (double)sum /matches.size();
    }
    public static double getAverageJunction(int junction){
        return getAverageJunction(matches, junction);
    }
    public static double getAverageConesPlaced(ArrayList<MatchData> matches){
        int sum = 0;
        for(MatchData match: matches){
            sum += match.conesUsed;
        }
        return (double)sum /matches.size();
    }
    public static double getAverageConesPlaced(){
        return getAverageConesPlaced(matches);
    }
    public static double getAverageBeaconsPlaced(ArrayList<MatchData> matches){
        int sum = 0;
        for(MatchData match: matches){
            sum += match.beaconsUsed;
        }
        return (double)sum /matches.size();
    }
    public static double getAverageBeaconsPlaced(){
        return getAverageBeaconsPlaced(matches);
    }
    public static double getPercentCircuitAchieved(ArrayList<MatchData> matches){
        int sum = 0;
        for(MatchData match: matches){
            sum += match.circuitAchieved?1:0;
        }
        return ((double)sum /matches.size())*100;
    }
    public static double getPercentCircuitAchieved(){
        return getPercentCircuitAchieved(matches);
    }
    public static double getAverageScore(ArrayList<MatchData> matches){
        int sum = 0;
        for(MatchData match: matches){
            sum += match.getTotalScore();
        }
        return ((double)sum /matches.size());
    }
    public static double getAverageScore(){
        return getAverageScore(matches);
    }


    public static ArrayList<MatchData> onlyWinners(){
        return new ArrayList<MatchData>(matches.stream().filter(s -> s.wasWinner).collect(Collectors.toList()));
    }
}
