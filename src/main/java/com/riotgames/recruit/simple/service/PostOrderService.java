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

    /** 우선순위와 괄호 고려
     * 1. 여는 괄호 '('를 만나면 스택 푸시
     * 2. 닫는 괄호 ')'를 만나면, 스택에서 '('가 나올때까지 팝하여 리스트에 넣음. '('는 스택에서 팝하여 버림
     * 3. 피연산자는 바로 리스트에 add
     * 4. 연산자는 스택에서 그 연산자보다 높은 우선순위의 연산자를 만나기전까지 팝하여 리스트에 add 후에 자신을 푸시
     * 5. 루프가 끝나고 스택에 있는 연산자들을 모두 pop하여 list에 add.
     */

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