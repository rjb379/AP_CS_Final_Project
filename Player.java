import java.util.ArrayList;
public class Player
{
    ArrayList<Cards> hand = new ArrayList<Cards>();
    ArrayList<Cards> unorganizedHand = new ArrayList<Cards>();
    String trumpSuited;
    public Player(ArrayList<Cards> initialCards, String trumpSuit)
    {
        String[] suitStrings = {"hearts", "diamonds", "clubs", "spades"};
        //find where the trump suit is in the suitStrings, puts it in the back
        trumpSuited = trumpSuit;
        unorganizedHand = initialCards;
        int indexTrump = 0;
        for (int i =0; i< 4; i++){
            if (suitStrings[i] == trumpSuit){
               indexTrump = i;
            }
        }
        //switches the last one with the trump suit
        suitStrings[3] = suitStrings[indexTrump];
        suitStrings[indexTrump] = "spades";
        while (hand.size() < 6){
            //goes through each non-Trump suit, finds cards of the trump suits in order
            for (int i = 0; i < 3; i++){
                String suitToOrder = suitStrings[i]; //specific suit
                int numSuit = 0; //variable: # cards of that specific suit, counter
                //loop: finds how many cards of that suit
                for (int k = 0; k < unorganizedHand.size(); k++){
                     if (unorganizedHand.get(k).getSuit() == suitToOrder){
                         numSuit++;
                     }
                }
 
                //loop: finds and orders all cards of that suit, puts them into ArrayList hand
                if (numSuit > 0){
                    int cardsAdded = 0;
                    while (cardsAdded != numSuit){
                        //loop: finds lowest card of that suit
                        int highest = 15;
                        Cards lowestCard = initialCards.get(0); //dummy card to not get an error
                        int index = 0;
                        for (int j = 0; j<initialCards.size(); j++){
                                if (unorganizedHand.get(j).getSuit() == suitToOrder){
                                    if(unorganizedHand.get(j).getValue() < highest){
                                        highest = unorganizedHand.get(j).getValue();
                                        lowestCard = unorganizedHand.get(j);
                                        index = j;
                                    }
                                }
                        }
                        hand.add(lowestCard);
                        unorganizedHand.remove(index);
                        cardsAdded++;
                        
                     }
                }
            }
            int numTrumpCards = 0; //variable: # cards of trump suit, counter
            //loop: finds how many cards of the trump suit suit
            for (int i = 0; i < initialCards.size(); i++){
                 if (initialCards.get(i).getSuit() == trumpSuit){
                      numTrumpCards++;
                 }
            }
            if (numTrumpCards > 0){
                 int cardsAdded = 0;
                 while (cardsAdded != numTrumpCards){
                        //loop: finds lowest card of that suit
                        int lowest = 5;
                        Cards highestCard = initialCards.get(0); //dummy card to not get an error
                        int index = 0;
                        for (int j = 0; j<initialCards.size(); j++){
                                if (unorganizedHand.get(j).getSuit() == trumpSuit){
                                    if(unorganizedHand.get(j).getValue() > lowest){
                                        lowest = unorganizedHand.get(j).getValue();
                                        highestCard = unorganizedHand.get(j);
                                        index = j;
                                    }
                                }
                        }
                        hand.add(highestCard);
                        unorganizedHand.remove(index);
                        cardsAdded++;
                 }
            }
        }
    }
    public ArrayList<Cards> getHand(){
        return hand;
    }
    public int getCardsLeft(){
        return hand.size();
    }
    public int returnSmallestTrump(){
        if(hand.get(hand.size()-1).getSuit() == trumpSuited){
         return hand.get(hand.size()-1).getValue();
       }
       else{
        return 15;
       }
    }
    ArrayList<Cards> cardsInPlay = new ArrayList<Cards>();
    public Cards offense(ArrayList<Cards> cardsPlayed, int deckLeft, String weakSuit){
       cardsInPlay = cardsPlayed;
       if (cardsPlayed.size() == 0){ //player begins the game: finds lowest non-trump card
           //FINDS LOWEST CARD IN THE DECK
           Cards lowestCard = null;
           int index = 0;
           int lowestVal = 15;
           for (int i = 0; i<hand.size(); i++){
                 if (hand.get(i).getValue() < lowestVal && hand.get(i).getSuit() != trumpSuited){
                      lowestCard = hand.get(i);
                      index = i;
                      lowestVal = hand.get(i).getValue();
                 }
           }
           if (weakSuit == null){ //defensive player did not forfeit previous round
               if (hand.get(0).getSuit() != trumpSuited){ //the hand contains non trump suit cards- plays lowest one
                    hand.remove(index);
                    return lowestCard;
               }
               else{ //only trump suits in hand- plays lowest one
                   Cards cardToGive = hand.get(hand.size()-1);
                   hand.remove(hand.size()-1);
                   return cardToGive;
               }
           }
           else{ //defensive player gave up- can we exploit that weakness?
               //finds average value of normal cards
               int totalValHand = 0;
               int counter = 0;
               //WHILE LOOP: adds up all the values until it hits a trump card or until it reaches the end of the deck
               while(counter<hand.size() && hand.get(counter).getSuit() != trumpSuited){
                   totalValHand += hand.get(counter).getValue();
               }
               int avgValHand = totalValHand/hand.size();
               Cards lowestCardWeakSuit = null; //will contain the lowest card of the suit that the defense is vulnerable to
               int indexWeakSuit = 0; //index of that card
               for (int i = 0; i< hand.size(); i++){
                   if(hand.get(i).getSuit() == weakSuit){
                       indexWeakSuit = i;
                       lowestCardWeakSuit = hand.get(i);
                       break;
                    }
               }
               if(lowestCardWeakSuit.getValue() < avgValHand){
                   hand.remove(indexWeakSuit);
                   return lowestCardWeakSuit;
               }
               else{
                    hand.remove(index);
                    return lowestCard;
               }
           }
       }
        else{
            boolean hasCard = false;
            int index = 0;
            Cards toGive = null;
            int numTrumpCards = 0; //number of trump cards in hand
            //for loop: finds number of trump cards, to later test if that's the only card in hand
            for (int i =0; i<hand.size(); i++){
                if (hand.get(i).getSuit() == trumpSuited){
                    numTrumpCards++;
                }
            }
            if(numTrumpCards < hand.size()){ //if other cards that 
                for (int i = 0; i<cardsInPlay.size(); i++){
                    for(int j = 0; j<hand.size()-numTrumpCards; j++){ //loops to the end of the hand
                        if (hand.get(j).getValue() == cardsInPlay.get(i).getValue()){
                            if (toGive==null){//first card found
                                hasCard = true;
                                toGive= hand.get(j);
                                index = j;
                            }
                            else if(hand.get(j).getValue() < toGive.getValue()){ //the value is lower than what already is there
                                toGive = hand.get(j);
                                index = j;
                            }
                        }
                    }
                }
            }
            else{ //ONLY TRUMP CARDS IN HAND
                for (int i =0; i<cardsPlayed.size(); i++){ 
                    for (int j=hand.size()-1; j>=0; j--){
                        if (hand.get(j).getValue() == cardsInPlay.get(i).getValue() && deckLeft <10 && hand.get(j).getValue() <10){
                            toGive = hand.get(j); //lowest trump card that matches
                            index = j; //index of that card
                            hasCard = true;
                        }
                    }
                }
            }
            if (hasCard == true){
                    hand.remove(index);
                    return toGive;
            }
            else{
                    return null;
            }
            /*
            while (done == false){
                    for (int i =0; i<cardsInPlay.size(); i++){
                        //finds if card at index i of cardsInPlay matches any of the cards in the hand
                        //value to value
                        for (int j = 0; j<hand.size(); j++){
                            if ((hand.get(j).getValue() == cardsInPlay.get(i).getValue()) && (hand.get(j).getSuit() != trumpSuited)){
                                index = j;
                                toGive = hand.get(index);
                                hasCard = true;
                                done = true;
                                break;
                            }
                            //if trump cards are all that's left in the and and they match the played cards and there isn't much of the deck left
                            else if(numTrumpCards == hand.size() && (hand.get(j).getValue() == cardsInPlay.get(i).getValue()) && deckLeft <10 && hand.get(j).getValue() <10){
                                index = j;
                                toGive = hand.get(index);
                                hasCard = true;
                                done = true;
                                break;
                            }
                        }
                        if (done){
                            break;
                        }
                    }
                    if (done){
                            break;
                    }
                    done = true;
            }*/
        }
    }
    public Cards defense(Cards toBeat, int pileSize, int deckSize){
        boolean hasNormalCard = false; //is there a normal card that can beat toBeat?
        int hasNormalIndex = 0; //index of normal card, it's 0 to keep the compiler happy- this variable won't be used unless the trump card exists
        boolean hasTrumpCard = false; //is there a trump card?
        int numTrumpCards = 0; //how many trump cards there are
        
        //FOR LOOP: finds if there are normal cards that can beat it, and if so where that normal card is
        for (int i = 0; i<hand.size(); i++){
            if (hand.get(i).getSuit() == toBeat.getSuit() && hand.get(i).getValue() > toBeat.getValue()){
                hasNormalIndex = i;
                hasNormalCard = true;
                break;
            }
        }
        //WHILE LOOP: goes through hand and finds if there are trump cards, and how many
        //cuts out either when it reachers the beginning of the deck or when the suit of the card at index counter stops being a trump suit
        int counter = hand.size()-1;
        while (counter >= 0 && hand.get(counter).getSuit() == trumpSuited){ 
            hasTrumpCard = true;
            numTrumpCards++;
            counter--;
        }
        //sets a new object Cards to these if they exist
        //initially set to null so scope isn't an issue and won't be touched unless the corresponding boolean is true
        Cards normalCardThatBeats = null;
        if (hasNormalCard){
            normalCardThatBeats = hand.get(hasNormalIndex);
        }
        Cards trumpCardThatBeats = null;
        if (hasTrumpCard){
            trumpCardThatBeats = hand.get(hand.size()-1);
        }
        //WHAT TO DO WHEN THERE IS A NORMAL CARD BUT NO TRUMP CARD
        if (hasNormalCard == true && hasTrumpCard == false){
            if(normalCardThatBeats.getValue() - toBeat.getValue() < 5){ //if the card that beats is approppriately close
                hand.remove(hasNormalIndex);
                return normalCardThatBeats;
            }
            else{ //depends largely on desperation if you play a card that is pretty high
                if(hand.size() + pileSize > 10){ //you accept to many cards otherwise
                    hand.remove(hasNormalIndex);
                    return normalCardThatBeats;
                }
                else if(deckSize <5){ //not much of the game left- you don't want to take cards
                    hand.remove(hasNormalIndex);
                    return normalCardThatBeats;
                }
                else{ // gives up because it's not late in the game and the hand size if they give up isn't overwhelmingly yuge
                    return null; 
                }
            }
        }
        //WHAT TO DO WHEN THERE IS BOTH A NORMAL CARD AND A TRUMP CARD
        if(hasNormalCard && hasTrumpCard){ //if true and true because I think I know how booleans work
            //would only use trump card if it's low (<10) and equal to the card played
            int i = hand.size()-1; //will loop through from the back
            while (i >= 0 && hand.get(i).getSuit() == trumpSuited){
                if(hand.get(i).getValue() == toBeat.getValue() && hand.get(i).getValue() < 10){
                    Cards trumpCardEqual = hand.get(i);
                    hand.remove(i);
                    return trumpCardEqual;
                }
                else{
                    i--;
                }
            }
            hand.remove(hasNormalIndex);
            return normalCardThatBeats;
        }
        //WHAT TO DO WHEN THERE IS A TRUMP CARD BUT NO NORMAL CARDS
        if(hasTrumpCard && !hasNormalCard){
            if (deckSize >10){
                if(hand.size() + pileSize < 9){
                    return null;
                }
                else{
                    if(trumpCardThatBeats.getValue() < 10){
                        hand.remove(hand.size()-1); //removes last card in deck- the trump card to be played
                        return trumpCardThatBeats;
                    }
                    else if(hand.size() == 1){ //only one card left in your hand and that one is the trump card
                       hand.remove(hand.size()-1); //removes last card in deck- the trump card to be played
                       return trumpCardThatBeats;
                    }
                    else if(hand.size() + pileSize > 12 && trumpCardThatBeats.getValue() < 13) {
                        hand.remove(hand.size()-1);
                        return trumpCardThatBeats;
                    }
                    else{
                        return null;
                    }
                }
            }
            else{
                if(deckSize > 0){
                    if(trumpCardThatBeats.getValue() < 10){
                        hand.remove(hand.size()-1);
                        return trumpCardThatBeats;
                    }
                    else{
                        if(hand.size() + pileSize > 10 && trumpCardThatBeats.getValue()<13){
                            hand.remove(hand.size()-1);
                            return trumpCardThatBeats;
                        }
                        else{
                            return null;
                        }
                    }
                }
                else{ //trump cards will be played at the end of the game- you do not want to accept cards at the very end
                    hand.remove(hand.size()-1);
                    return trumpCardThatBeats;
                }
            }
        }
        else{
            return null;
        }
    }
    public void addCards(ArrayList<Cards> addCards){
       ArrayList<Cards> cardsToAdd = new ArrayList<Cards>();
       cardsToAdd  = addCards;
       //credit to Mr. Young for coming up with the while loop idea
       
       for (int i = 0; i < cardsToAdd.size(); i++){
           String suit = cardsToAdd.get(i).getSuit();
           int val = cardsToAdd.get(i).getValue();
           boolean suitExists = false;
           int indexSuit = 0;
           for (int j = 0; j<hand.size(); j++){ //FOR LOOP: finds if there are other cards of that suit
                if (hand.get(j).getSuit() == suit){
                    suitExists = true;
                    indexSuit = j;
                    break;
                }
            }
           if(cardsToAdd.get(i).getSuit() != trumpSuited){
              if (suitExists){ //finds where to put it if there is another card of that type
                  while (indexSuit < hand.size() && hand.get(indexSuit).getSuit() == suit && hand.get(indexSuit).getValue() <  val){
                    indexSuit++;
                  }
                  hand.add(indexSuit, cardsToAdd.get(i));
              }
              else{
                  hand.add(0, cardsToAdd.get(i));
              }
           }
           else{//if the card is a trump card
                if(suitExists){ //there is at least one other trump card in the deck
                    int index = hand.size() -1; //last card in deck
                    while(index > 0 && suit == hand.get(index).getSuit() && val > hand.get(index).getValue()){
                        index--;
                    }
                    hand.add(index+1, cardsToAdd.get(i));
                }
                else{ //no other trump cards- this one gets put in the back of the deck
                    hand.add(hand.size(), cardsToAdd.get(i));
                    //System.out.println(cardsToAdd.get(i).getCardName());
                }
            }
       }
    }
}
