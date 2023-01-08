package powerpowerplayv1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
/**
 * Class Description:
 *      The GameField class is a special suclass of JPanel which will represent the gamefield of Power Play.
 *  Its base will be a grey background with a 5x5 grid of varying sized yellow and lack junctions on it and epending on 
 * the contents of the conesBoard and possessionBoard will also have cones and Beacons on it. This JPanel will be constantly repainted
 * and every HUmanPlayer object will have one.
 * Methods
 * Private
 *      draw()- draws the gamfield according to the positions of the cones, beacons, and the size of the window
 * Public
 *      alert()- puts a JLabel on the screen with the String meesage parameter; used for updating the time
 *      setFrozenLabel()-updates the FrozenLabel with the time left in cooldown aafter placing a cone
 *      paintComponent()- overrided method: paints the gamefield y calling the draw method
 *      updateConesBoard()- updates the positions of the cones and beacons
 *  
 *      
 *      
 */
public class GameField extends JPanel {
    //the class GameField extends/is a JPanel and implements the MouseListener Interface

    //Fields
    
    //int double array which stores the point values asocciated with each junction
    int [][] junctionHeights = new int[][]{//Ground2, Low3, Middle4, HIgh5
        {2,3,2,3,2},
        {3,4,5,4,3},
        {2,5,2,5,2},
        {3,4,5,4,3},
        {2,3,2,3,2}
    };

    //String double array which stores Strings corresponding to each junction; the Strings will contain a combination of r and b depending 
    //on the order of blue and red cones placed on the junction 
    String [][] conesBoard = new String[5][5];

    
    //Color array for convenienve
    Color[] colorArray = new Color[]{Color.yellow, Color.red, Color.blue};

    //label for displaying text to user
    JLabel timeLabel = new JLabel("PRESS ANY KEY To START");
    JLabel frozenLabel = new JLabel("");

    int highlightX = 0;
    int highlightY = 0;

    

    


    public GameField(){
        setBackground(Color.gray);//set the backgrounf to be gray
        setBorder(new LineBorder(Color.BLACK.darker(), 3));//set a border of dark black 3 pixels thick

        //increase the font and size of the label
        timeLabel.setFont(new Font("Ubuntu", Font.BOLD, 19));
        //add the timeLabel
        add(timeLabel);
        add(frozenLabel);
        frozenLabel.setFont(new Font("Ubuntu", Font.BOLD, 19));

        //placeholder method for testing purposes; randomizes the placement of cones on the board
        //fillRandomCones();
       
    }

    /**
     * Alert - sets the text of the timeLabel to be the message
     * @param message- message to be displayed on the Game Field
     */
    public void alert(String message)
    {
        //set the text of the timeLabel to be whatever the parameter was
        timeLabel.setText(message);
    }
    public void setFrozenLabel(String message)
    {
        String out = "<html><h1 style=\"color: red;\">" + message + "</h1></html>";
        frozenLabel.setText(out);
    }

    /**
     * repaints all of the componenets in the current Component
     * @param  g - the graphics object
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);//call to super method
        this.draw(g);//draw this object
    }

    private void draw(Graphics g){
        //Find the width and height of the Panel
        int windowHeight = getHeight();
        int windowWidth = getWidth();

        //Find the spacing for width and height between the squares we will put the rets and circles in
        int hSpacing = windowHeight /6;
        int wSpacing = windowWidth/6;
        //set the padding equal to be a 20th of the iwdth and height
        int wPadding = windowWidth/20;
        int hPadding = windowHeight/20;

        int boxWidth  = wSpacing;
        int boxHeight = hSpacing;

        //Highlight
        int highLightY = hPadding + highlightY * hSpacing;
        int highLightX = wPadding + highlightX * wSpacing;
        g.setColor(Color.ORANGE);
        g.drawRect(highLightX, highLightY, boxWidth, boxHeight);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        int wDistance = windowWidth /10;
        int hDistance = windowHeight/10;
        g.setColor(Color.RED);
        g.drawLine(wDistance, 0, 0, hDistance);
        g.drawLine(windowWidth,windowHeight-hDistance, windowWidth-wDistance, windowHeight);
        g.setColor(Color.BLUE);
        g.drawLine(wDistance, windowHeight, 0, windowHeight-hDistance);
        g.drawLine(windowWidth-wDistance,0, windowWidth, hDistance);
        g2.setStroke(new BasicStroke(1));

        //draw pickup Lines on top
        int redPickupXLeft = 3*wSpacing + wPadding-boxWidth/12;
        g.setColor(Color.RED);
        g.fillRect(redPickupXLeft, 0, boxWidth/6, boxHeight);
        g.fillRect(redPickupXLeft, windowHeight-boxHeight, boxWidth/6, boxHeight);

        int bluePickupXLeft = 2*wSpacing + wPadding-boxWidth/12;
        g.setColor(Color.BLUE);
        g.fillRect(bluePickupXLeft, 0, boxWidth/6, boxHeight);
        g.fillRect(bluePickupXLeft, windowHeight-boxHeight, boxWidth/6, boxHeight);

        //draw storage areas
        g.setColor(Color.RED);
        g.fillPolygon(new int[]{windowWidth-wPadding, windowWidth, windowWidth}, new int[]{3*hSpacing -boxHeight/2 + hPadding, 3*hSpacing + hPadding, 2*hSpacing + hPadding}, 3);
        g.setColor(Color.BLUE);
        g.fillPolygon(new int[]{wPadding, 0, 0}, new int[]{3*hSpacing -boxHeight/2 + hPadding, 3*hSpacing + hPadding, 2*hSpacing + hPadding}, 3);


        for(int row = 0; row < 5; row++){
            for(int col = 0; col<5; col++){
                //determine the upper left coord of each box, along with the height and width
                int boxY = hPadding + row * hSpacing;
                int boxX = wPadding + col * wSpacing;
                //g.setColor(Color.BLACK);
                //g.drawRect(boxX, boxY, wSpacing, hSpacing);
               

                
                
                
                //find the point value of the current junction; we will use this to determine what junction it is
                int junction = junctionHeights[row][col];

                //coords for rectangle
                int rectHeight = (int)(((7*hSpacing)/8.0) * (junction )/5);//The height of the rectangle will be 
                //7/8ths of the height of the box, but the height will be modified based on if its a low, midde, or high junction
                int rectWidth = (rectHeight /8);//the rect width will be an 8th of the height
                int rectX = ((wSpacing-rectWidth)/2)+boxX;
                int rectY = ((hSpacing-rectHeight)/2)+boxY;
                

                //coords for circle
                int circleRadius = Math.min(boxWidth, boxHeight)/2;
                int circleX = ((wSpacing-circleRadius)/2)+boxX;
                int circleY = ((hSpacing-circleRadius)/2)+boxY;

                if(junction ==2){//if it is a ground junction
                    
                    g.setColor(Color.BLACK);//color is balck
                    g.fillOval(circleX, circleY, circleRadius, circleRadius);//fill circle

                   
                }else{
                    //make a rectangle
                    g.setColor(Color.YELLOW);
                    g.fillRect(rectX,rectY,rectWidth,rectHeight);

                }

                //Now lets add the cones for this junction
                String cones = conesBoard[row][col];
                
                if(cones != null && cones.length() != 0){//if there are cones on thie junction
                    //determine the coords for the triangle
                    int triangleLeftX = rectX-(rectWidth);
                    int triangleRightX = rectX+ (2*rectWidth);
                    int triangleLeftY = rectY+rectHeight;
                    int triangleRightY = triangleLeftY;
                    int triangleUpX = rectX + rectWidth/2;
                    int triangleUpY = rectY + rectHeight/2;

                    if(junction ==2){//if the junction is a triangle we have to modify the coords a bit
                        triangleLeftX = circleX+(int)(.25*circleRadius);
                        triangleRightX = circleX+(int)(.75*circleRadius);
                        triangleRightY = circleY+(int)(.75*circleRadius);
                        triangleLeftY = triangleRightY;
                        triangleUpY = circleY ;
                    }
                
                    //convert the conesString to a char array
                    char[] ch = cones.toCharArray();

                    int spacing = (int)((rectHeight/2.0)/cones.length());//determine the spcaing between the cones based on how mnay are there
                    if(spacing==0)spacing =1;
                    int counter = 0;//counter tracks how many cones we have placed


                    loop: for(Character possession: ch)//for each cone
                    {
                        int spacingAdder = counter*spacing;//determine the y coordinate by multiplying the spaceing by how many cones we have placed already
                        if(possession == 'r')//if the cone is red
                        {
                            g.setColor(Color.RED);
                            g.fillPolygon(new int[]{triangleLeftX, triangleUpX, triangleRightX}, new int[]{triangleLeftY-spacingAdder, triangleUpY-spacingAdder, triangleRightY-spacingAdder}, 3);

                        }
                        else if(possession == 'b')//if the cone is blue
                        {
                            g.setColor(Color.BLUE);
                            g.fillPolygon(new int[]{triangleLeftX, triangleUpX, triangleRightX}, new int[]{triangleLeftY-spacingAdder, triangleUpY-spacingAdder, triangleRightY-spacingAdder}, 3);

                        }
                        else if(possession =='B'){//if it is a blue beacons
                            int wSpace = triangleRightX-triangleLeftX;
                            int hSpace = triangleLeftY - triangleUpY; 
                            g.setColor(Color.BLUE);
                            g.fillOval(triangleUpX-(wSpace/2), triangleUpY-spacingAdder, wSpace, hSpace);
                            break loop;
                        }
                        else if(possession =='R'){//if it is a red beacon
                            int wSpace = triangleRightX-triangleLeftX;
                            int hSpace = triangleLeftY - triangleUpY; 
                            g.setColor(Color.RED);
                            g.fillOval(triangleUpX-(wSpace/2), triangleUpY-spacingAdder, wSpace, hSpace);
                            break loop;
                        }
                        counter++;//increse the counter
                    }
             }
            }
        }

    }

    /**
     * updates the conesBoard variable
     * @param board
     */
    public void updateConesBoard(String [][] board)
    {
        conesBoard = board;
    }
}