package com.riotgames.recruit.simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seryang on 2016. 11. 6..
 */
@Service
public class SplitService {

    @Autowired
    private TypeCheckService typeCheckService;

    /**
     * 데이터를 괄호, 연산자, 숫자, 정수로 구분 후 쪼갠 값을 List 담아서 리턴
     *
     * @param inputQuestion
     * @return input question split list
     * @throws Exception (ERROR)
     */
    public List<String> splitInputQuestion(String inputQuestion) throws Exception {
        List<String> splitDataList = new ArrayList<>();

        // list에 들어갈 개별 데이터
        String inputElement = "";

        for (int i = 0 ; i < inputQuestion.length() ; i++) {
            int inputElementSize = inputElement.length();
            String currentChar = String.valueOf(inputQuestion.charAt(i));

            if (typeCheckService.isOperator(currentChar) || typeCheckService.isBracket(currentChar)) {

                if ("-".equals(currentChar) && (i == 0 || typeCheckService.isOperator(inputQuestion.charAt(i - 1))
                                                       || typeCheckService.isBracket(inputQuestion.charAt(i - 1)))) {
                    if (i == 0 && "-".equals(String.valueOf(inputQuestion.charAt(i+1)))) {
                        i++;
                    } else {
                        inputElement = inputElement.concat(currentChar);
                    }
                } else {
                    if (inputElementSize != 0) {
                        splitDataList.add(inputElement);
                        inputElement = "";
                    }

                    splitDataList.add(currentChar);
                }
            } else {
                if ("p".equals(currentChar) || "e".equals(currentChar)) {

                    if (inputElementSize != 0) {
                        splitDataList.add(inputElement);
                        inputElement = "";
                    }

                    switch (currentChar){
                        case "p" :
                            splitDataList.add(String.valueOf(Math.PI));
                            i++;
                            break;
                        case "e" :
                            splitDataList.add(String.valueOf(Math.E));
                            break;
                    }

                } else {
                    inputElement = inputElement.concat(currentChar);

                    if (i == inputQuestion.length() - 1) {
                        splitDataList.add(inputElement);
                    }
                }
            }
        }
        return splitDataList;
    }
}