package com.riotgames.recruit.simple.service;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Seryang on 2016. 11. 4..
 */
@Service
public class CalculateService {

    final static String FAIL = "ERROR";
    final static List<String> BRACKET = Arrays.asList("(", ")");
    final static List<String> OPERATOR = Arrays.asList("+", "-", "*", "/", "^");
    final static Map<String, Integer> PRIORITY = new HashMap<String, Integer>() {{
        put("+", 1);
        put("-", 1);
        put("*", 2);
        put("/", 2);
        put("^", 3);
    }};

    public String getAnswer(String question){
        String answer = "";

        try{
            if(validCheck(question)) {
                List<String> list = splitData(question);
                System.out.println("* 중위 오더");
                System.out.println(list);
                List<String> wow = getPostOrder(list);
                System.out.println("* 후위 오더");
                System.out.println(wow);
                answer = getCalculate(wow);
            }else{
                return FAIL;
            }
        }catch(Exception e){
            e.printStackTrace();
            return FAIL;
        }
        return answer;
    }

    private String getCalculate(List<String> wow) throws Exception {
        Stack<String> stack = new Stack<>();


        // 1. 피연산자는 스택에 다 푸쉬 푸쉬 베이베 나를
        // 2. 연사자를 만나면 팝 2번 하고 연산기호에 따른 연산실행 후 푸쉬
        for(int i = 0 ; i < wow.size() ; i++){
            String go  = wow.get(i);
            if(isOperator(go)){
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

    private boolean validCheck(String question) throws Exception{

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


    // 데이터를 쪼개서 리스트에 담았음.
    private List<String> splitData(String answer) throws Exception {
        List<String> list = new ArrayList<>();

        String temp = "";
        for(int i = 0 ; i < answer.length() ; i++){

            int tempLength = temp.length();

            String s = String.valueOf(answer.charAt(i));

            if(isOperator(s)){
                if(s.equals("-")) {
                    if (i == 0 || isOperator(String.valueOf(answer.charAt(i - 1))) || isBracket(String.valueOf(answer.charAt(i - 1)))) {
                        if(i == 0 && "-".equals(String.valueOf(answer.charAt(i+1)))){
                            i++;
                        }else {
                            temp = temp.concat(s);
                        }
                    } else {
                        if (tempLength != 0) list.add(String.valueOf(temp));
                        list.add(s);
                        temp = "";
                    }
                }else{
                    if(tempLength != 0) list.add(String.valueOf(temp));
                    list.add(s);
                    temp = "";
                }
            }else if(s.equals("p")) {
                String next = String.valueOf(answer.charAt(i+1));
                if(next.equals("i")) {
                    if (tempLength != 0) {
                        list.add(String.valueOf(temp));
                        temp = "";
                    }
                    list.add(String.valueOf(Math.PI));
                    i++;
                }

            }else if(s.equals("e")){
                if (tempLength != 0) {
                    list.add(String.valueOf(temp));
                    temp = "";
                }
                list.add(String.valueOf(Math.E));

            }else if(isBracket(s)){
                if(s.equals("-")) {
                    if (i == 0 || isOperator(String.valueOf(answer.charAt(i - 1))) || isBracket(String.valueOf(answer.charAt(i - 1)))) {
                        temp = temp.concat(s);
                    } else {
                        if(tempLength != 0) list.add(String.valueOf(temp));
                        list.add(s);
                        temp = "";
                    }
                }else{
                    if(tempLength != 0) list.add(String.valueOf(temp));
                    list.add(s);
                    temp = "";
                }

            }else if(s.equals(".")){
                if(i == answer.length() -1){
                    temp = temp.concat(s);
                    list.add(temp);
                    temp = "";
                }
                temp = temp.concat(s);

            }else if(isNum(s)) {
                if (i == answer.length() - 1) {
                    list.add(String.valueOf(temp.concat(s)));
                }
                temp = temp.concat(s);

            }else{
                return new ArrayList<>();
            }

        }
        return list;
    }

    // 우선순위와 괄호 고려
    private List<String> getPostOrder(List<String> answer) throws Exception{

//        1. '(' 를 만나면 스택에 푸시한다.
//        2. ')' 를 만나면 스택에서 '('가 나올 때까지 팝하여 출력하고 '(' 는 팝하여 버린다.
//        3. 연산자를 만나면 스택에서 그 연산자보다 낮은 우선순위의 연산자를 만날 때까지 팝하여 출력한 뒤에 자신을 푸시한다.
//        4. 피연산자는 그냥 출력한다.
//        5. 모든 입력이 끝나면 스택에 있는 연산자들을 모두 팝하여 출력한다.

        Stack<String> stack = new Stack<>();
        List<String> newList = new ArrayList<>();
        for(String s : answer){
            switch(s){
                case "(" :
                    stack.push(s);
                    break;
                case ")" :
                    while(!(isBracket(stack.peek()))){
                        newList.add(stack.pop());
                    }
                    stack.pop();
                    break;
                case "+" :
                case "-" :
                case "/" :
                case "*" :
                    int priorityNum = PRIORITY.get(s);
                    while( !stack.isEmpty() && !isBracket(s) && isOperator(stack.peek())) {
                        if(PRIORITY.get(stack.peek()) >= priorityNum){
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

    private boolean isNum(String s) {
        try {
            for (int i = 0; i < 10; i++) {
                if (Integer.parseInt(s) == i) {
                    return true;
                }
            }
        }catch(NumberFormatException n){
            n.printStackTrace();
            return false;
        }
        return false;
    }

    private boolean isBracket(String s) {
        return BRACKET.contains(s);
    }

    private boolean isOperator(String s) {
        return OPERATOR.contains(s);
    }

    private String answerFormat(String answer){
        System.out.println("answer format before : " + answer);
        if(Double.parseDouble(answer) == 0) answer = "0";
        return String.format("%.6f", Double.parseDouble(answer));
    }
}
