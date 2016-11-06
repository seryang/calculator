package com.riotgames.recruit.simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Seryang on 2016. 11. 6..
 */
@Service
public class PostOrderService {

    @Autowired
    private TypeCheckService typeCheckService;



    /**
     * 중위표기법을 후위표기법으로 변환
     *
     * @param splitDataList
     * @return postOrder data list
     * @throws Exception (ERROR)
     */
    public List<String> convertToPostOrder(List<String> splitDataList) throws Exception {

        Stack<String> stack = new Stack<>();
        List<String> postOrderList = new ArrayList<>();

        for (String data : splitDataList) {
            switch(data) {
                case "(" :
                    stack.push(data);
                    break;

                case ")" :
                    while(typeCheckService.isBracket(stack.peek()) == false){
                        postOrderList.add(stack.pop());
                    }

                    stack.pop();
                    break;

                case "+" :
                case "-" :
                case "/" :
                case "*" :
                    int priorityNum = typeCheckService.getPriority(data);

                    while(stack.isEmpty() == false && typeCheckService.isBracket(data) == false
                                                 && typeCheckService.isOperator(stack.peek()) == true) {
                        if(typeCheckService.getPriority(stack.peek()) >= priorityNum){
                            postOrderList.add(stack.pop());
                        }else{
                            break;
                        }
                    }

                    stack.push(data);
                    break;

                case "^" :
                    stack.push(data);
                    break;

                default :
                    postOrderList.add(data);
            }
        }

        while (stack.isEmpty() == false) {
            postOrderList.add(stack.pop());
        }

        return postOrderList;
    }
}