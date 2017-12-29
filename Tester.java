import java.util.ArrayList;
public class Tester
{
    public static void main(String[] args)
    {
       //CREATION OF CARDS
       String[] suitStrings = {"hearts", "diamonds", "clubs", "spades"};
       ArrayList<Cards> deckToSort = new ArrayList<Cards>();
       String trumpSuit;
       for (int i = 0; i < 9; i++){
          for (int j = 0; j < 4; j++){
              Cards card = new Cards(i+6, suitStrings[j]); //deck from 6 to 14 (ace) with alternating suits
              deckToSort.add(card);
          }
       }
       //CREATION OF DECK
       Deck sortedDeck = new Deck(deckToSort);
       trumpSuit = sortedDeck.getTrumpSuit();
       //INITIALIZING PLAYERS
       ArrayList<Cards> init1 = sortedDeck.replenishCards(6);
       ArrayList<Cards> init2 = sortedDeck.replenishCards(6);
       Player player1 = new Player(init1, trumpSuit);
       Player player2 = new Player(init2, trumpSuit);
       System.out.println("Trump suit: " + trumpSuit);
       System.out.println();
       System.out.println("Player 1's initial cards: ");
       for (int i = 0; i < 6; i++){
           System.out.print(player1.getHand().get(i).getCardName() + " ");
           
       }
       System.out.println();
       System.out.println("Player 2's initial cards: ");
       for (int i = 0; i < 6; i++){
           System.out.print(player2.getHand().get(i).getCardName() + " ");
       }
      System.out.println();
      
      //DECIDING WHO STARTS THE GAME- based on who has the smallest trump card
      boolean player1offense;
      if (player1.returnSmallestTrump() > player2.returnSmallestTrump()){
           player1offense = false;
       }
      else{ //defaults to player 1 if neither has a trump card- basically I am player 1 and my grandma is player 2
          player1offense = true;
       }
       
      //THE GAME ITSELF
      boolean gameOver = false;
      boolean winner = false; //TRUE IF PLAYER 1 WINS, FALSE IF PLAYER 2 WINS
      int round = 1;
       while(!gameOver){ //while the game is in progress
          int drawCardsLeft = sortedDeck.getCardsLeft(); //how many cards are left in the deck
          System.out.println();
          System.out.println(Integer.toString(drawCardsLeft) + " cards left in the deck");
          ArrayList<Cards> cardsInPlay = new ArrayList<Cards>();
          System.out.println();
          System.out.println("Round " + Integer.toString(round));
          int maxCards; //maximum number of cards that can be played (put into the list of cardsInPlay)
          if(player1offense){
            System.out.println("Player 1 is on offense, player 2 is on defense");
            maxCards = player2.getCardsLeft()*2;
          }
          else{
            System.out.println("Player 2 is on offense, player 1 is on defense");
            maxCards = player1.getCardsLeft()*2;
          }
          boolean roundInProgress = true;
          boolean defenseGaveUp = false; //if defense gave up- offense strategy
          String defenseWeakness = null; //suit that made defense give up
          while (roundInProgress == true || cardsInPlay.size() == maxCards){
                //PLAYER 1 IS ON OFFENSE AND PLAYER 2 IS ON DEFENSE
                 if(player1offense){
                    System.out.println();
                    //offensive player
                    Cards offense = null;
                    if(defenseGaveUp){
                        offense = player1.offense(cardsInPlay, sortedDeck.getCardsLeft(), defenseWeakness);
                    }
                    else{
                        offense = player1.offense(cardsInPlay, sortedDeck.getCardsLeft(), null);
                    }
                    if (offense == null){ //if it has no cards left to play- round ends
                        player1offense = !player1offense;
                        System.out.println("Offense is done. Round Over.");
                        System.out.println();
                        if (player1.getCardsLeft() < 6) {
                            player1.addCards(sortedDeck.replenishCards(6-player1.getCardsLeft()));
                        }
                        System.out.println("Player 1's new cards are:");
                        for (int i =0; i<player1.getCardsLeft(); i++){
                            System.out.print(player1.getHand().get(i).getCardName() + ", ");
                        }
                        if (player2.getCardsLeft() < 6){
                            player2.addCards(sortedDeck.replenishCards(6-player2.getCardsLeft()));
                         }
                        System.out.println();
                        System.out.println("Player 2's new cards are:");
                        for (int i =0; i<player2.getCardsLeft(); i++){
                             System.out.print(player2.getHand().get(i).getCardName() + ", ");
                        }
                        cardsInPlay.clear();
                        round++;
                        roundInProgress = false;
                        break;
                    }
                    else{
                        cardsInPlay.add(offense);
                        System.out.println("Player 1 plays " + cardsInPlay.get(cardsInPlay.size()-1).getCardName());
                        //checks win
                        if (drawCardsLeft == 0){
                            if (player1.getCardsLeft() == 0){
                                winner = true;
                                gameOver = true;
                                break;
                            }
                        }
                    }
                    //defensive player:
                    if (roundInProgress){
                        Cards defend = player2.defense(cardsInPlay.get(cardsInPlay.size()-1), cardsInPlay.size(), sortedDeck.getCardsLeft());
                        if (defend == null){ //can't beat card
                            System.out.println("Player 2 gives up, accepts all played cards. Round over.");
                            defenseGaveUp = true;
                            defenseWeakness = cardsInPlay.get(cardsInPlay.size()-1).getSuit(); //suit of card that made it quit
                            player2.addCards(cardsInPlay);
                            System.out.println();
                            System.out.println("Player 2's new cards are:");
                            for (int i =0; i<player2.getCardsLeft(); i++){
                                    System.out.print(player2.getHand().get(i).getCardName()  + ", ");
                            }
                            if (player1.getCardsLeft() < 6){
                                player1.addCards(sortedDeck.replenishCards(6-player1.getCardsLeft()));
                            }
                            System.out.println();
                            System.out.println("Player 1's new cards are:");
                            for (int i =0; i<player1.getCardsLeft(); i++){
                                System.out.print(player1.getHand().get(i).getCardName() + ", ");
                            }
                            cardsInPlay.clear();
                            round++;
                            roundInProgress = false;
                            break;
                        }
                        else{ //can beat card
                           cardsInPlay.add(defend);
                           System.out.println("Player 2 responds with " + cardsInPlay.get(cardsInPlay.size()-1).getCardName());
                           //checks win
                           if (drawCardsLeft == 0){
                                if (player2.getCardsLeft() == 0){
                                    winner = false;
                                    gameOver = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                //PLAYER 2 IS ON OFFENSE, PLAYER 1 IS ON DEFENSE
                if(!player1offense){
                    System.out.println();
                    //offensive player
                    Cards offense = null;
                    if(defenseGaveUp){
                        offense = player2.offense(cardsInPlay, sortedDeck.getCardsLeft(), defenseWeakness);
                    }
                    else{
                        offense = player2.offense(cardsInPlay, sortedDeck.getCardsLeft(), null);
                    }
                    if (offense == null){ //if it has no cards left to give- bita
                        player1offense = !player1offense;
                        System.out.println("Offense is done. Round Over.");
                        if (player2.getCardsLeft() < 6){
                            player2.addCards(sortedDeck.replenishCards(6-player2.getCardsLeft()));
                        }
                        System.out.println();
                        System.out.println("Player 2's new hand is:");
                        for (int i =0; i<player2.getCardsLeft(); i++){
                            System.out.print(player2.getHand().get(i).getCardName() + ", ");
                        }
                        if (player1.getCardsLeft() < 6){
                            player1.addCards(sortedDeck.replenishCards(6-player1.getCardsLeft()));
                         }
                        System.out.println();
                        System.out.println("Player 1's new hand is:");
                        for (int i =0; i<player1.getCardsLeft(); i++){
                             System.out.print(player1.getHand().get(i).getCardName() + ", ");
                        }
                        cardsInPlay.clear();
                        round++;
                        roundInProgress = false;
                        break;
                    }
                    else{
                        cardsInPlay.add(offense);
                        System.out.println("Player 2 plays " + cardsInPlay.get(cardsInPlay.size()-1).getCardName());
                        //checks win
                        if (sortedDeck.getCardsLeft() == 0){
                            if (player2.getCardsLeft() == 0){
                                winner = false;
                                gameOver = true;
                                break;
                            }
                        }
                    }
                    //defensive player:
                    if (roundInProgress){
                        Cards defend = player1.defense(cardsInPlay.get(cardsInPlay.size()-1), cardsInPlay.size(), sortedDeck.getCardsLeft());
                        if (defend == null){ //can't beat card
                            System.out.println("Player 1 gives up, accepts all played cards. Round over.");
                            defenseGaveUp = true;
                            defenseWeakness = cardsInPlay.get(cardsInPlay.size()-1).getSuit();
                            player1.addCards(cardsInPlay);
                            System.out.println();
                            System.out.println("Player 1's new hand is:");
                            for (int i =0; i<player1.getCardsLeft(); i++){
                                    System.out.print(player1.getHand().get(i).getCardName() + ", ");
                            }
                            if (player2.getCardsLeft() < 6){
                                player2.addCards(sortedDeck.replenishCards(6-player2.getCardsLeft()));
                            }
                            System.out.println();
                            System.out.println("Player 2's new hand is:");
                            for (int i =0; i<player2.getCardsLeft(); i++){
                                System.out.print(player2.getHand().get(i).getCardName() + ", ");
                            }
                            cardsInPlay.clear();
                            round++;
                            roundInProgress = false;
                            break;
                        }
                        else{ //can beat card
                           cardsInPlay.add(defend);
                           System.out.println("Player 1 beats it back with a " + cardsInPlay.get(cardsInPlay.size()-1).getCardName());
                           //checks win
                           if (sortedDeck.getCardsLeft() == 0){
                                if (player1.getCardsLeft() == 0){
                                    winner = true;
                                    gameOver = true;
                                    break;
                                }
                            }
                        }
                    }
                }
          }
      }
      if(winner){
        System.out.println("The winner is player 1!");
        }
      if(!winner){
        System.out.println("The winner is player 2!");
        }
    }
}
