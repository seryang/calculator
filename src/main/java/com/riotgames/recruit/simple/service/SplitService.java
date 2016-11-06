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

        for(int i = 0 ; i < inputQuestion.length() ; i++){
            int currentElementSize = inputElement.length();
            String currentChar = String.valueOf(inputQuestion.charAt(i));

            if(typeCheckService.isOperator(currentChar)){
                if("-".equals(currentChar)){
                    if(i == 0 || typeCheckService.isOperator(String.valueOf(inputQuestion.charAt(i - 1)))
                              || typeCheckService.isBracket(String.valueOf(inputQuestion.charAt(i - 1)))){
                        if(i == 0 && "-".equals(String.valueOf(inputQuestion.charAt(i+1)))){
                            i++;
                        }else {
                            inputElement = inputElement.concat(currentChar);
                        }
                    }else{
                        if(currentElementSize != 0){
                            splitDataList.add(String.valueOf(inputElement));
                        }

                        splitDataList.add(currentChar);
                        inputElement = "";
                    }
                }else{
                    if(currentElementSize != 0){
                        splitDataList.add(String.valueOf(inputElement));
                    }

                    splitDataList.add(currentChar);
                    inputElement = "";
                }
            }else if("p".equals(currentChar)){
                if (currentElementSize != 0) {
                    splitDataList.add(String.valueOf(inputElement));
                    inputElement = "";
                }

                splitDataList.add(String.valueOf(Math.PI));
                i++;
            }else if("e".equals(currentChar)){
                if (currentElementSize != 0) {
                    splitDataList.add(String.valueOf(inputElement));
                    inputElement = "";
                }
                splitDataList.add(String.valueOf(Math.E));
            }else if(typeCheckService.isBracket(currentChar)){
                if(currentElementSize != 0){
                    splitDataList.add(String.valueOf(inputElement));
                }
                splitDataList.add(currentChar);
                inputElement = "";
            }else if(".".equals(currentChar)){
                if(i == inputQuestion.length() -1){
                    inputElement = inputElement.concat(currentChar);
                    splitDataList.add(inputElement);
                    inputElement = "";
                }

                inputElement = inputElement.concat(currentChar);
            }else if(typeCheckService.isNum(currentChar)){
                if(i == inputQuestion.length() - 1){
                    splitDataList.add(String.valueOf(inputElement.concat(currentChar)));
                }else{
                    inputElement = inputElement.concat(currentChar);
                }
            }else{
                return new ArrayList<>();
            }
        }
        return splitDataList;
    }
}