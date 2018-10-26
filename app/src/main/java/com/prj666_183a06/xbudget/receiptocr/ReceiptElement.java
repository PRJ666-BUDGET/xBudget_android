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
    private Boolean isNumber = false;
    List<String> possibleAssociation;

    ReceiptElement(Line nline){
        line = nline;
        value = line.getValue();
        if(parseNumber() ){
            isNumber = true;
        }

    }

    private boolean parseNumber(){
        return false;
    }
    public boolean inNumber() {
        return isNumber;
    }


}
