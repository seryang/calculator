package com.riotgames.recruit.simple.service;

import org.springframework.stereotype.Service;

/**
 * Created by Seryang on 2016. 11. 6..
 */
@Service
public class ValidationService {

    /**
     * 정상적인 계산식인지 체크
     *
     * @param inputQuestion
     * @throws Exception (ERROR)
     */
    public void validCheck(String inputQuestion) throws Exception {
        int openBracketCount = 0;
        int closeBracketCount = 0;

        for(int i = 0 ; i < inputQuestion.length() ; i++){
            String data = String.valueOf(inputQuestion.charAt(i));

            if("(".equals(data)){
                openBracketCount++;
            }else if(")".equals(data)){
                closeBracketCount++;
            }
        }

        if(openBracketCount != closeBracketCount){
            throw new Exception("Invalid number of brackets.");
        }

        inputQuestion = inputQuestion.replaceAll("pi", String.valueOf(Math.PI))
                                    .replaceAll("e", String.valueOf(Math.E))
                                    .replaceAll("\\(", "")
                                    .replaceAll("\\)", "")
                                    .replaceAll("\\+", "")
                                    .replaceAll("-", "")
                                    .replaceAll("\\*", "")
                                    .replaceAll("/", "")
                                    .replaceAll("\\^", "");

        // 연산자와 괄호를 모두 제거했는데 그 값이 숫자가 아닐 경우, Exception 발생
        try{
            Double.parseDouble(inputQuestion);
        }catch (Exception e){
            throw new Exception("Cannot parse of the data. not Num");
        }
    }
}