import java.util.ArrayList;
import java.util.Random;
public class Deck
{
    ArrayList<Cards> Deck = new ArrayList<Cards>();
    //initial capacity: 36
    String trumpSuit;
    public Deck(ArrayList<Cards> unsortedDeck)
    {
        Deck = unsortedDeck;
        for (int i = 0; i< Deck.size(); i++){
          Random rand1 = new Random();
          int m = rand1.nextInt(36);
          int n = rand1.nextInt(36);
          Cards x = Deck.get(m);
          Cards y = Deck.get(n);
          Deck.set(n, x);
          Deck.set(m, y);
        }
        trumpSuit = Deck.get(Deck.size()-1).getSuit();
    }
    public ArrayList<Cards> getDeck(){
     return Deck;
    }
    public String getTrumpSuit()
    {
        return trumpSuit;
    }
    public int getCardsLeft(){
        return Deck.size();
    }
    public ArrayList<Cards> replenishCards(int cardsNeeded){
        if(cardsNeeded > 0){
            if (Deck.size() >= cardsNeeded){
                ArrayList<Cards> replen = new ArrayList<Cards>(Deck.subList(0, cardsNeeded));
                for (int i = 0; i < cardsNeeded; i++){
                    Deck.remove(0);
                } 
                return replen;
            }
            else{
                ArrayList<Cards> replen = new ArrayList<Cards>(Deck);
                Deck.clear();
                return replen;
            }
        }
        else{
            return null;
        }
    }
}
