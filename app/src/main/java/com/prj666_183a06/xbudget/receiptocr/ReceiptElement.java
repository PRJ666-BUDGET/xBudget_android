package com.prj666_183a06.xbudget.receiptocr;

import com.google.android.gms.vision.text.Line;

import org.w3c.dom.NamedNodeMap;

import java.util.List;
import java.util.regex.Pattern;

public class ReceiptElement {
    //Theory: The elements of a receipt that have importants are the objects,
    // their prices and the total cost of the receipt. You can find all the elements
    // needed by just looking at the dollar values and identify possible strings associated with
    // that number

    public Line getLine() {
        return line;
    }

    private Line line;

    public String getValue() {
        return value;
    }

    private String value;

    public Double getNumValue() {
        return numValue;
    }

    private Double numValue = null;
    private Boolean isNumber;
    private String numberChar = "[\\dolig]"; //TODO 9 can be interpreted as g
    private String decimalChar = "[\\,\\.]";
    List<String> possibleAssociation;

    ReceiptElement(Line nline){
        line = nline;
        value = line.getValue().toLowerCase();
        isNumber = parseNumber();
        if(isNumber){
            value = value.replaceAll(",","."); //make decimal
            value = value.replaceAll("[li]","1"); //li -> 1
            value = value.replaceAll("g","9"); //g -> 9
            value = value.replaceAll("o","0"); //o -> 0
            value = value.replaceAll("\\s",""); //remove spaces
            value = value.replaceAll(".*[^\\d.]", ""); //remove non numeric characters left of first char

            numValue = Double.parseDouble(value);
        }
    }

    private boolean parseNumber(){
        //sorted from most liakely to least likely indicator of type
        if(Pattern.compile(decimalChar + ".*" + decimalChar).matcher(value).find()) {
            //multiple decimals
            return false;
        }
        if(Pattern.compile(decimalChar + numberChar + numberChar + "$").matcher(value).find()) {
            //Almost Guarantee number
            return true;
        }
        if(Pattern.compile(".*" + decimalChar + numberChar + numberChar + '$').matcher(value).find()) {
            //Almost Guarantee number
            return true;
        }
        //check for X.XX value
        if(value.contains(decimalChar + numberChar + numberChar)) {
            //Very Likely number
            //return true;
        }
        //check if there is a number
        if(Pattern.compile("[\\d]").matcher(value).find()) {
            //possible number
            //return true;
        }
        //check if there is a decemal
        if(value.contains("[\\.\\,]")) {
            //possible number
            //return true;
        }
        //check if letters are common numbers
        if(value.contains("[oli]")) {
            //possible number
            //return true;
        }
        //check if there are letters
        if(value.contains("[\\w]")) {
            //possible word
            //return false;
        }
        //unknown
        return false;
    }

    public boolean isTotal(){
        if(Pattern.compile("total").matcher(value).find()) {
            //possible total
            return true;
        }
        return false;
    }

    public boolean inNumber() {
        return isNumber;
    }


}
