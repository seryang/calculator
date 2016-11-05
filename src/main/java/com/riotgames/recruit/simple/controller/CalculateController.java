package com.riotgames.recruit.simple.controller;

import com.riotgames.recruit.simple.service.CalculateService;
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

    @RequestMapping(name = "/calculate", method = RequestMethod.POST)
    public String question(@RequestBody String question){
        String q = null;
        try {
            q = URLDecoder.decode(question, "UTF-8").replace("=","");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return calculateService.getAnswer(StringUtils.trimAllWhitespace(q));
    }
}
