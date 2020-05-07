package com.example.olio_ht;
/* The vault - android banking application
 *  Author: Akseli Aula 0545267
 *  Object Oriented programming course final project
 *  2020 */

//Only for generating cardNumber
public class CreateCard {
    public CreateCard() {
    }

    //This method returns a cardnumber based on account number
    //AccountNumber 123412341234 -> CardNumber 1234-1234-1234
    public String generateNewCard(String accNumber) {
        StringBuilder sb = new StringBuilder();
        String cardNmbr;
        for (int i = 0; i < accNumber.length(); i++) {
            char c = accNumber.charAt(i);
            sb.append(c);
            if (i == 3 || i == 7) {
                sb.append("-");
            }
        }
        cardNmbr = sb.toString();
        return cardNmbr;
    }
}
