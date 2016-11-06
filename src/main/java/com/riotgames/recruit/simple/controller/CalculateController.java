package com.riotgames.recruit.simple.controller;

import com.riotgames.recruit.simple.service.CalculateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Seryang on 2016. 11. 4..
 */

@RestController
public class CalculateController {

    @Autowired
    private CalculateService calculateService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 계산식을 입력 받아서, 결과 Text 리턴
     *
     * @param question
     * @return (계산 결과 or ERROR text)
     */
    @RequestMapping(name = "/calculate", method = RequestMethod.POST)
    public String question(@RequestBody String question) {
        try {
            String inputQuestion = URLDecoder.decode(question, "UTF-8").replace("=","");
            return calculateService.getAnswer(StringUtils.trimAllWhitespace(inputQuestion));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            return "ERROR";
        }
    }
}
