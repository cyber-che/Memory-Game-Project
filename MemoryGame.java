//////////////////////////////////////////
//
// Title: Memory Game
//
// Author: Che John
//Credit: UW Madison
///////////////////////// /////////////////////////

import java.io.File;
import processing.core.PApplet;
import processing.core.PImage;

public class MemoryGame {


  // Congratulations message
  private final static String CONGRA_MSG = "CONGRATULATIONS! YOU WON!";
  // Cards not matched message
  private final static String NOT_MATCHED = "CARDS NOT MATCHED. Try again!";
  // Cards matched message
  private final static String MATCHED = "CARDS MATCHED! Good Job!";
  // 2D-array which stores cards coordinates on the window display
  private final static float[][] CARDS_COORDINATES =
      new float[][] {{170, 170}, {324, 170}, {478, 170}, {632, 170}, {170, 324}, {324, 324},
          {478, 324}, {632, 324}, {170, 478}, {324, 478}, {478, 478}, {632, 478}}; // rows x columns
  // Array that stores the card images filenames
  private final static String[] CARD_IMAGES_NAMES = new String[] {"ball.png", "redFlower.png",
      "yellowFlower.png", "apple.png", "peach.png", "shark.png"};
  

  private static PApplet processing; // PApplet object that represents
  // the graphic display window
  private static Card[] cards; // one dimensional array of cards

  private static PImage[] images; // array of images of the different cards
  private static Card selectedCard1; // First selected card
  private static Card selectedCard2; // Second selected card
  private static boolean winner; // boolean evaluated true if the game is won,
  // and false otherwise
  private static int matchedCardsCount; // number of cards matched so far
  // in one session of the game
  private static String message; // Displayed message to the display window


  public static void main(String[] args) {
    // TODO Auto-generated method stub
    Utility.startApplication();
  }

  /**
   * Defines the initial environment properties of this game as the program starts The setup()
   * method receives an argument of type PApplet that is passed to it from the Utility class. It's
   * used to define initial environment properties such as screen size and to load background images
   * and fonts as the program starts. There can only be one setup() method in the whole program and
   * it is run once when the program starts.
   */
  public static void setup(PApplet processing) {
    
    MemoryGame.processing = processing;
    images = new PImage[CARD_IMAGES_NAMES.length];
    
    for (int i = 0; i < images.length; ++i) {
      images[i] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[i]);
    }
    startNewGame();// startGame called once
  }

  /**
   * Initializes the Game 
   */
  public static void startNewGame() {
    MemoryGame.selectedCard1 = null;
    MemoryGame.selectedCard2 = null;
    MemoryGame.matchedCardsCount = 0;
    MemoryGame.winner = false;
    MemoryGame.message = "";
    MemoryGame.cards = new Card[CARDS_COORDINATES.length];
    
    int[] mixedUp = Utility.shuffleCards(CARDS_COORDINATES.length);

    for (int i = 0; i < CARDS_COORDINATES.length; ++i) {
      cards[i] = new Card(images[mixedUp[i]], CARDS_COORDINATES[i][0], CARDS_COORDINATES[i][1]);
      cards[i].setVisible(false);
    }
  }

  /**
   * Callback method called each time the user presses a key
   */
  public static void keyPressed() {
    if (processing.key == 'N' || processing.key == 'n') {
      startNewGame();
    }
  }


  /**
   * Callback method draws continuously this application window display. Also  sets
   * the color used for the background of the Processing window.
   */

  public static void draw() {
    
    processing.background(245, 255, 250); // Mint cream color

    for (int i = 0; i < cards.length; ++i) {
      cards[i].draw();
    }
    displayMessage(message);
  }

  /**
   * Displays a given message to the display window
   * 
   * @param message to be displayed to the display window
   */
  public static void displayMessage(String message) {
    processing.fill(0);
    processing.textSize(20);
    processing.text(message, processing.width / 2, 50);
    processing.textSize(12);
  }

  /**
   * Checks whether the mouse is over a given Card
   * @param card to be checked
   * @return true if the mouse is over the storage list, false otherwise
   */
  public static boolean isMouseOver(Card card) {

    if (absdiff(card.getX(), processing.mouseX) <= 70
        && absdiff(card.getY(), processing.mouseY) <= 70) {
      return true;
    }
    return false;
  }

  /**
   * Helper method. Helps calculate absolute value for the 
   * difference between the mouse and image coordinates given values
   * 
   * @param val1 first given coordinate
   * @param val2 second given coordinate
   * @return difference as absolute value
   */
  public static double absdiff(float val1, float val2) {
    if (val2 > val1) {
      return val2 - val1;
    }
    return val1 - val2;
  }


  /**
   * Callback method called each time the user presses the mouse
   */
  public static void mousePressed() {
    // select and flip a card

    if (!winner) {

      if (selectedCard2 != null) { // clears handler variable from previous cycle 
        selectedCard1.deselect();
        selectedCard2.deselect();
        selectedCard1.setVisible(false);
        selectedCard2.setVisible(false);
        selectedCard1 = null;
        selectedCard2 = null;
      }
      
      // checks which card was selected
      for (int i = 0; i < cards.length; ++i) {
        if (isMouseOver(cards[i])) {

          cards[i].setVisible(true);
          cards[i].select();

          if (selectedCard1 == null) {
            selectedCard1 = cards[i];
            return; // return to top to get second Mouse Press
          } else {
            selectedCard2 = cards[i];
          }
          break; // leave if selected card has been found.
        }
      }


      if (selectedCard1 != null && selectedCard2 != null) {
        if (matchingCards(selectedCard1, selectedCard2)) {// checks to see if cards matched
          selectedCard1.setMatched(true);
          selectedCard2.setMatched(true);

          selectedCard1.deselect();
          selectedCard2.deselect();

          selectedCard1 = null;
          selectedCard2 = null;
          matchedCardsCount = matchedCardsCount + 1;
        }

      }
      // looks for winner
      if (matchedCardsCount == 6) {
        winner = true; 
        return;// set winner as true if all cards match.
      }


    }
  }

  /**
   * Checks whether two cards match or not. Two cards match if they possess the same image.
   * 
   * @param card1 reference to the first card
   * @param card2 reference to the second card
   * @return true if card1 and card2 image references are the same, false otherwise
   */
  public static boolean matchingCards(Card card1, Card card2) {
    PImage im1 = card1.getImage();
    PImage im2 = card2.getImage();

    if (im1.equals(im2)) {
      return true;
    }

    return false;
  }
}
