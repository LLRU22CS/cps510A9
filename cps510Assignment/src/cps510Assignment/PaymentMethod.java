/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cps510Assignment;

/**
 *
 * @author Nathan
 */
public class PaymentMethod {
    
    private int paymentMethodID;
    private String cardNum;
    private String cardType;
    private String cardExp;
    private String firstName;
    private String lastName;
    
    public PaymentMethod(int paymentMethodID, String cardNum, String cardType, String cardExp, String firstName, String lastName) {
        this.paymentMethodID = paymentMethodID;
        this.cardNum = cardNum;
        this.cardType = cardType;
        this.cardExp = cardExp;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public int getPaymentMethodID() {
        return paymentMethodID;
    }

    public String getCardNum() {
        return cardNum;
    }

    public String getCardType() {
        return cardType;
    }

    public String getCardExp() {
        return cardExp;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
}
