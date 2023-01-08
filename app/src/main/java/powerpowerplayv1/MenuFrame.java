package powerpowerplayv1;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 * Class Descrition
 *      the MenuFrame class is a specialized JFrame which acts as an introduction to the game. It is split up into two JPanels
 * One Of which displays an image of the game field while the other displays a long line of HTML formatted text in a JLabel introducing the game
 * and rules.
 * 
 */
public class MenuFrame extends JFrame {//extend the JFRame class because the Game FRame will be a type of JFRame
    //INstance Variables
    JPanel background;
    String link = "fieldTest_02.png";
    


    
    public MenuFrame() throws IOException{
        
        //set up dimensions based on background image
        BufferedImage backgroundImage = ImageIO.read(getClass().getResourceAsStream(link));
        int width = 2*backgroundImage.getWidth();//2* because we are using a layout
        int height = backgroundImage.getHeight();
        setBounds(100, 100, width, height);
        setResizable(false);


        Container container = getContentPane();
        container.setLayout(new GridLayout(1,2));
        background = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                // TODO Auto-generated method stub
                super.paintComponent(g);
                try{
                g.drawImage(backgroundImage,0,0,this);
                }
                catch(Exception e){}
            }
        };
        container.add(background);
        JPanel rightScreen = new JPanel();
        rightScreen.setLayout(new FlowLayout());

        //I am very rusty at the java gui and it proaly shows here

        JPanel storage = new JPanel();//storage JPanel to add JPanel containers to
        storage.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Power Power Play (created by Team 16633 Bots in Black)", SwingConstants.CENTER);
        storage.add(title, BorderLayout.NORTH);
        //Buttons for all the different game modes
        JPanel container2 = new JPanel();
        JButton startMultiplayer = new JButton("Start Multiplayer");
        startMultiplayer.addActionListener(e ->{Main.waitForStart=false; Main.gamemode = "Multiplayer";});
        container2.add(startMultiplayer);
        storage.add(container2, BorderLayout.WEST);

        JPanel container3 = new JPanel();
        JButton startComputer = new JButton("Start Computer");
        startComputer.addActionListener(e ->{Main.waitForStart=false; Main.gamemode = "Computer";});
        container3.add(startComputer);
        storage.add(container3, BorderLayout.EAST);

        JPanel container4 = new JPanel();
        JButton startStatistics = new JButton("View Statistics");
        startStatistics.addActionListener(e ->{Main.waitForStart=false; Main.gamemode = "Statistics";});
        container4.add(startStatistics);
        storage.add(container4, BorderLayout.CENTER);

        JPanel container5 = new JPanel();
        JButton sandBox = new JButton("Sandbox");
        sandBox.addActionListener(e ->{Main.waitForStart=false; Main.gamemode = "Sandbox";});
        container5.add(sandBox);
        storage.add(container5, BorderLayout.SOUTH);

        rightScreen.add(storage);

            //String in html format explaining game
        String html = "<html><center>Welcome to Power Power Play, the virtual version of the FTC 2022 Game Power Play! <br>"+
        "The purpose of this application is twofold: <br>"+
        "1.) To provide a fun experience and help teams become better aquanted with the game<br>"+
        "2.) To collect data about the various strategies used in the virtual version of the game, <br>and to use this data to formulate strategies in the real game<br><<hr>"+
        "There are three buttons above: <br>"+
        "<bold>MULTIPLAYER</bold> will allow you to play the game with another player on this device <br>"+
        "<bold>COMPUTER</bold> allows you to play against a program which uses a <br>algorithm to determine the best moves <br>"+
        "<bold>STATISTICS</bold> skips straight to the nitty gritty data this game has collected. <br><i style = \" color:red;\">This application uses the google sheets API for Java to store data <br>from games played from any computer.</i> Cool, Right?!<br>"+
        "<bold>SANDBOX</bold> allows you to plan out strategies with your alliance partner on the field<br><hr>"+
        "Okay, now onto the <b>Controls</b>: <br>"+
        "<b style = \" color:red;\">Red Controls</b>: <br>up,down,left, and right arrows: <u>move the highlighted box around</u><i> <br>This selects where you wish to put a cone/beacons</i> <br>Enter: <u>Places a cone</u> <br>/(forward slash): <u>Places a Beacon</u><br>"+
        "<b style = \" color:blue;\">Blue Controls</b>: <br>a,w,s,d : <u>moves the highlighted box around</u> <br>Shift: <u>Places a cone</u> <br>f: <u>Places a Beacon</u><br><hr>"+
        "Now, a couple of things about <b>Gameplay</b>: <br>"+
        "The game operates according to the scoring rules of Power Play with a few exceptions:<br>"+
        "1.) The game is only the Driver Control and Endgame portions of Power Play<br>"+
        "2.) Circuits do not required a cone to be placed in each terminal to score the circuit, <br>and additionaly there is no terminal scoring functionality<br>"+
        "3.) To simulate the time it takes for a robot to deliver a cone, placing cones on different junctions <br>will activate a cooldown  for a specified amount of seconds. <br><b>The closer you place a cone to your specific substation< or cones stack(colored vertical lines)<br> the less time it will take</b>"+

        "<h1><b>ENJOY THE GAME!</h1</b>"+
        "</center></html>";
        JLabel jl = new JLabel();
        jl.setText(html);
        jl.setFont(new Font("Ubuntu", Font.BOLD, 8));
        rightScreen.add(jl);
        container.add(rightScreen);


      


        
    }

    
    

   



    

}