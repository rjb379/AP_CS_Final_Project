public class Cards
{
    private int number;
    private String suit;
    public Cards(int inputNumber, String inputSuit){
        number = inputNumber;
        suit = inputSuit;
    }
    public int getValue(){
        return number;
    }
    public String getSuit(){
        return suit;
    }
    public String getCardName(){
        if ((number >= 6) && (number <=10)){
        return String.valueOf(number) + " of " + suit;
        }
        else if (number == 11){
            return "Jack of " + suit;
        }
        else if (number == 12){
            return "Queen of " + suit;
        }
        else if (number == 13){
            return "King of " + suit;
        }
        return "Ace of " + suit;
    }
}
