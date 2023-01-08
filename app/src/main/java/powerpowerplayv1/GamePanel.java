package powerpowerplayv1;

import java.awt.*;
import javax.swing.*;
/**
 * Class Description:
 *      the GamePanel is used exclusivly by the HUmane Player to hold both a TextContainer(the scores) and the gameField
 * a computer player does not need this because it only displays a TextContainer
 * 
 */
public class GamePanel extends JPanel {//extend the JFRame class because the Game FRame will be a type of JFRame

    //Instance Variables

    //Game Field Object extends JPanel; this is the field displayed on the left
    GameField field;

    //TextContainer Object extends JPanel; this is where we will display instructions and scores
    TextContainer textContainer; 

    //Jpanel which holds both the GameField and the TextContainer
    JPanel gameContainer;

    

    public GamePanel(){
        

        //instantiate a new J Panel which will hold the entire gameField and the TexContainer
        gameContainer = new JPanel();

        //instantiate a panel which will hold the instructions and scores
        textContainer = new TextContainer();

        //instantiate a Panel which will hold the graphics
        field = new GameField();


        //set up the game container
        gameContainer.setLayout(new GridLayout(1,2));//grid layout; 1 row 2 columns
        gameContainer.add(field);//add the game field to the layout
        gameContainer.add(textContainer);//add the text Container to the layout
        setLayout(new GridLayout(1,1));
        this.add(gameContainer);
    }
}