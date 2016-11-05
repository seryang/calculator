package com.riotgames.recruit.simple.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Stack;

/**
 * Created by Seryang on 2016. 11. 4..
 */
@Service
public class CalculateService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String FAIL = "ERROR";

    @Autowired
    private TypeCheckService typeCheckService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private SpliterService spliterService;

    @Autowired
    private PostOrderService postOrderService;

    public String getAnswer(String question){
        String answer;

        try{
            if(validationService.validCheck(question)) {
                List<String> splitDataList = spliterService.splitData(question);
                logger.info("* In-Order : " + splitDataList);

                List<String> postOrderData = postOrderService.getPostOrder(splitDataList);
                logger.info("* Post-Order : " + postOrderData);
                answer = getCalculate(postOrderData);
            }else{
                return FAIL;
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            return FAIL;
        }
        return answer;
    }

    private String getCalculate(List<String> wow) throws Exception {

        Stack<String> stack = new Stack<>();

        for(int i = 0 ; i < wow.size() ; i++){
            String go  = wow.get(i);
            if(typeCheckService.isOperator(go)){
                if(i == wow.size() - 1 && stack.size() == 1 && go.equals("-")){
                    stack.push("-"+stack.pop());
                    break;
                }
                double num2 = Double.parseDouble(stack.pop());
                double num1 = Double.parseDouble(stack.pop());

                switch(go){
                    case "^" :
                        stack.push(String.valueOf(Math.pow(num1, num2)));
                        break;
                    case "+" :
                        stack.push(String.valueOf(num1+num2));
                        break;
                    case "-" :
                        stack.push(String.valueOf(num1-num2));
                        break;
                    case "*" :
                        stack.push(String.valueOf(num1*num2));
                        break;
                    case "/" :
                        if (num2 == 0) {
                            throw new Exception("Cannot devide with zero");
                        }
                        stack.push(String.valueOf(num1/num2));
                        break;
                }
            }else{
                stack.push(go);
            }
        }
        if(stack.size() != 1) throw new Exception("Riot heart");
        return answerFormat(stack.pop());
    }



    private String answerFormat(String answer){
        if(Double.parseDouble(answer) == 0){
            answer = "0";
        }

        return String.format("%.6f", Double.parseDouble(answer));
    }
}
