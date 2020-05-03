package com.example.olio_ht;

public class CreateCard {
    public CreateCard(){}

    //This method returns a cardnumber based on account number
    //AccountNumber 123412341234 -> CardNumber 1234-1234-1234
    public String generateNewCard(String accNumber) {
        StringBuilder sb = new StringBuilder();
        String cardNmbr;
        for (int i = 0; i < accNumber.length(); i++) {
            char c = accNumber.charAt(i);
            sb.append(c);
             if(i == 3 || i == 7){
                sb.append("-");
             }
        }
        cardNmbr = sb.toString();
        return cardNmbr;
    }
}
