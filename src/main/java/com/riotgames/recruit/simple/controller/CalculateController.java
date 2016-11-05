package com.riotgames.recruit.simple.controller;

import com.riotgames.recruit.simple.service.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;

/**
 * Created by Seryang on 2016. 11. 4..
 */

@RestController
public class CalculateController {

    @Autowired
    private CalculateService calculateService;

    @RequestMapping(name = "/calculate", method = RequestMethod.POST/*, produces = "application/x-www-form-urlencoded"*/)
    public String question(@RequestBody String question){
        /**
         * 우선 순위
         * 1: ^(거듭 제곱)   => 4^2 = 16
         * 2: * /
         * 3: - +
         */
        String q = URLDecoder.decode(question).replace("=","");
        return calculateService.getAnswer(StringUtils.trimAllWhitespace(q));
    }
}
