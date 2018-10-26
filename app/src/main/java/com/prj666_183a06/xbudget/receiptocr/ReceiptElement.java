package com.prj666_183a06.xbudget.receiptocr;

import com.google.android.gms.vision.text.Line;

import org.w3c.dom.NamedNodeMap;

import java.util.List;

public class ReceiptElement {
    //Theory: The elements of a receipt that have importants are the objects,
    // their prices and the total cost of the receipt. You can find all the elements
    // needed by just looking at the dollar values and identify possible strings associated with
    // that number

    private Line line;
    private String value;
    private Float numValue = null;
    private Boolean isNumber;
    private String numberChar = "[\\doli]";
    private String decimalChar = "[\\,\\.]";
    List<String> possibleAssociation;

    ReceiptElement(Line nline){
        line = nline;
        value = line.getValue().toLowerCase();
        isNumber = parseNumber();
    }

    private boolean parseNumber(){

        //sorted from most likely to least likely indicator of type
        if(value.matches(decimalChar + numberChar + numberChar + "Z")) {
            //Almost Guarantee number
            return true;
        }
        //check for X.XX value
        if(value.matches(decimalChar + numberChar + numberChar)) {
            //Very Likely number
            return true;
        }
        //check if there is a number
        if(value.matches("[\\d]")){
            //possible number
            return true;
        }
        //check if there is a decemal
        if(value.matches("[\\.\\,]")) {
            //possible number
            return true;
        }
        //check if letters are common numbers
        if(value.matches("[oli]")) {
            //possible number
            return true;
        }
        //check if there are letters
        if(value.matches("[\\w]")) {
            //possible word
            return false;
        }
        //unknown
        return false;
    }

    public boolean inNumber() {
        return isNumber;
    }


}
