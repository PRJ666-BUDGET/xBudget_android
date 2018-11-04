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

    public Float getNumValue() {
        return numValue;
    }

    private Float numValue = null;
    private Boolean isNumber;
    private String numberChar = "[\\doli]";
    private String decimalChar = "[\\,\\.]";
    List<String> possibleAssociation;

    ReceiptElement(Line nline){
        line = nline;
        value = line.getValue().toLowerCase();
        isNumber = parseNumber();
        if(isNumber){
            value.replaceAll(",","."); //make decimal
            numValue = Float.parseFloat(value.replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
        }
    }

    private boolean parseNumber(){
        //sorted from most liakely to least likely indicator of type
        if(Pattern.compile(decimalChar + ".*" + decimalChar).matcher(value).find()) {
            //multiple decimals
            return false;
        }
        if(Pattern.compile(decimalChar + numberChar + numberChar + "Z").matcher(value).find()) {
            //Almost Guarantee number
            return true;
        }
        if(Pattern.compile(decimalChar + numberChar + numberChar).matcher(value).find()) {
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
