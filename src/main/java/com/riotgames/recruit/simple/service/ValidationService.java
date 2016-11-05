package com.riotgames.recruit.simple.service;

import org.springframework.stereotype.Service;

/**
 * Created by Seryang on 2016. 11. 6..
 */
@Service
public class ValidationService {

    public boolean validCheck(String question) throws Exception{

        int openBracketCount = 0;
        int closeBracketCount = 0;

        for(int i = 0 ; i < question.length() ; i++){
            String data = String.valueOf(question.charAt(i));

            if("(".equals(data)){
                openBracketCount++;
            }else if(")".equals(data)){
                closeBracketCount++;
            }
        }

        if(openBracketCount == closeBracketCount) {
            question = question.replaceAll("pi", String.valueOf(Math.PI))
                    .replaceAll("e", String.valueOf(Math.E))
                    .replaceAll("\\(", "")
                    .replaceAll("\\)", "")
                    .replaceAll("\\+", "")
                    .replaceAll("-", "")
                    .replaceAll("\\*", "")
                    .replaceAll("\\^", "")
                    .replaceAll("/", "");
            Double.parseDouble(question);
            return true;
        }else{
            return false;
        }
    }
}
