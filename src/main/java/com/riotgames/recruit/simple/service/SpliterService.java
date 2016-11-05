package com.riotgames.recruit.simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seryang on 2016. 11. 6..
 */
@Service
public class SpliterService{

    @Autowired
    private TypeCheckService typeCheckService;

    // 데이터를 쪼개서 리스트에 담았음.
    public List<String> splitData(String answer) throws Exception {
        List<String> list = new ArrayList<>();

        String temp = "";
        for(int i = 0 ; i < answer.length() ; i++){

            int tempLength = temp.length();

            String s = String.valueOf(answer.charAt(i));

            if(typeCheckService.isOperator(s)){
                if(s.equals("-")) {
                    if (i == 0 || typeCheckService.isOperator(String.valueOf(answer.charAt(i - 1))) || typeCheckService.isBracket(String.valueOf(answer.charAt(i - 1)))) {
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

            }else if(typeCheckService.isBracket(s)){
                if(s.equals("-")) {
                    if (i == 0 || typeCheckService.isOperator(String.valueOf(answer.charAt(i - 1))) || typeCheckService.isBracket(String.valueOf(answer.charAt(i - 1)))) {
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

            }else if(typeCheckService.isNum(s)) {
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
}
