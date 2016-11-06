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

    @Autowired
    private TypeCheckService typeCheckService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private SplitService splitService;

    @Autowired
    private PostOrderService postOrderService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String FAIL = "ERROR";

    public String getAnswer(String inputQuestion) {
        try {
            validationService.validCheck(inputQuestion);
            List<String> splitDataList = splitService.splitInputQuestion(inputQuestion);
            List<String> postOrderDataList = postOrderService.convertToPostOrder(splitDataList);

            logger.info("* In-Order : " + splitDataList + " -> " + "Post-Order : " + postOrderDataList);
            return calculate(postOrderDataList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return FAIL;
        }
    }

    /**
     * 후위표기법으로 담긴 list를, stack을 이용하여 계산
     *
     * @param postOrderDataList
     * @return calculate result
     * @throws Exception (ERROR)
     */
    private String calculate(List<String> postOrderDataList) throws Exception {
        Stack<String> stack = new Stack<>();

        for (int i = 0 ; i < postOrderDataList.size() ; i++) {
            String data  = postOrderDataList.get(i);

            if (typeCheckService.isOperator(data)) {
                if (i == postOrderDataList.size() - 1 && stack.size() == 1 && "-".equals(data)) {
                    stack.push("-" + stack.pop());
                    break;
                }

                double num2 = Double.parseDouble(stack.pop());
                double num1 = Double.parseDouble(stack.pop());

                switch (data) {
                    case "^" :
                        stack.push(String.valueOf(Math.pow(num1, num2)));
                        break;

                    case "+" :
                        stack.push(String.valueOf(num1 + num2));
                        break;

                    case "-" :
                        stack.push(String.valueOf(num1 - num2));
                        break;

                    case "*" :
                        stack.push(String.valueOf(num1 * num2));
                        break;

                    case "/" :
                        if (num2 == 0) {
                            throw new Exception("Cannot divide with zero");
                        }

                        stack.push(String.valueOf(num1 / num2));
                        break;
                }
            } else {
                stack.push(data);
            }
        }

        if (stack.size() != 1) {
            throw new Exception();
        }

        return answerFormat(stack.pop());
    }

    private String answerFormat(String answer) throws Exception {
        if (Double.parseDouble(answer) == 0) {
            answer = "0";
        }

        return String.format("%.6f", Double.parseDouble(answer));
    }
}