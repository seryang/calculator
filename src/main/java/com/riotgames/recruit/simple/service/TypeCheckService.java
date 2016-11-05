package com.riotgames.recruit.simple.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Seryang on 2016. 11. 6..
 */
@Service
public class TypeCheckService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final List<String> BRACKET = Arrays.asList("(", ")");
    private static final List<String> OPERATOR = Arrays.asList("+", "-", "*", "/", "^");
    private static final Map<String, Integer> PRIORITY = new HashMap<String, Integer>() {{
        put("+", 1);
        put("-", 1);
        put("*", 2);
        put("/", 2);
        put("^", 3);
    }};

    public boolean isBracket(String s) {
        return BRACKET.contains(s);
    }

    public boolean isOperator(String s) {
        return OPERATOR.contains(s);
    }

    public int getPriority(String k){
        return PRIORITY.get(k);
    }

    public boolean isNum(String s) {
        try {
            Double.parseDouble(s);
            return true;
        }catch(NumberFormatException n){
            logger.info(n.getMessage());
        }
        return false;
    }
}
