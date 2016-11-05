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
     *   1. '(' 를 만나면 스택에 푸시한다.
     *   2. ')' 를 만나면 스택에서 '('가 나올 때까지 팝하여 출력하고 '(' 는 팝하여 버린다.
     *   3. 연산자를 만나면 스택에서 그 연산자보다 낮은 우선순위의 연산자를 만날 때까지 팝하여 출력한 뒤에 자신을 푸시한다.
     *   4. 피연산자는 그냥 출력한다.
     *   5. 모든 입력이 끝나면 스택에 있는 연산자들을 모두 팝하여 출력한다.
     */

    public List<String> getPostOrder(List<String> answer) throws Exception{

        Stack<String> stack = new Stack<>();
        List<String> newList = new ArrayList<>();
        for(String s : answer){
            switch(s){
                case "(" :
                    stack.push(s);
                    break;
                case ")" :
                    while(!(typeCheckService.isBracket(stack.peek()))){
                        newList.add(stack.pop());
                    }
                    stack.pop();
                    break;
                case "+" :
                case "-" :
                case "/" :
                case "*" :
                    int priorityNum = typeCheckService.getPriority(s);
                    while( !stack.isEmpty() && ! typeCheckService.isBracket(s) && typeCheckService.isOperator(stack.peek())) {
                        if(typeCheckService.getPriority(stack.peek()) >= priorityNum){
                            newList.add(stack.pop());
                        }else{
                            break;
                        }
                    }
                    stack.push(s);
                    break;
                case "^" :
                    stack.push(s);
                    break;
                default:
                    newList.add(s);
            }
        }

        while(!stack.isEmpty()){
            newList.add(stack.pop());
        }
        return newList;
    }
}
