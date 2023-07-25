package com.wagonloader.app.workers;

import static org.mockito.ArgumentMatchers.booleanThat;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;


public class WagonParsers {
    ObjectMapper mapper = new ObjectMapper();

    public static String removeSpaceBeforeDigits(String s) {
        StringBuilder withoutspaces = new StringBuilder(""); 
        boolean foundDigits = false;
        for (int i = 0; i < s.length(); i++) {
            char currentChar = s.charAt(i);
            

            if (!foundDigits){
                if (currentChar != ' ' && currentChar != ',' ){
                    foundDigits = true;
                    withoutspaces.append(currentChar);
                }
            } else{
                withoutspaces.append(currentChar);
            }
        }
        return withoutspaces.toString();
    }

    
    public ArrayNode SplitParser(String sentence){
        char ch;
        int pCount = 0;
        int beginIndex = 0;
        String param = null;
        List<Integer> outerCommas = new ArrayList<>();
        ArrayNode multipleParams = mapper.createArrayNode();
        for (int i = 0; i < sentence.length(); i++) {
            ch = sentence.charAt(i);
            if (ch =='(')
                pCount += 1;
            else if(ch ==')')
                pCount -= 1;
            
            if (ch==','){
                if (pCount == 0){
                    outerCommas.add(i);
                }
            }   
        }    
        outerCommas.add(sentence.length()); // In order to get the last element
        
        for (int index: outerCommas){
            param = removeSpaceBeforeDigits(sentence.substring(beginIndex, index));
            multipleParams.add(param);
            beginIndex = index;
        }

        return multipleParams;
    }
}
