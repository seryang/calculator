/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.riotgames.recruit.simple;

import com.riotgames.recruit.simple.service.CalculateService;
import com.riotgames.recruit.simple.service.PostOrderService;
import com.riotgames.recruit.simple.service.SplitService;
import com.riotgames.recruit.simple.service.ValidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for {@link RiotSeryangApplication}.
 *
 * @author Dave Syer
 * @author Phillip Webb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RiotSeryangApplication.class)
public class CalculateTests {

    @Autowired
    public ValidationService validationService = new ValidationService();

    @Autowired
    public SplitService splitService = new SplitService();

    @Autowired
    public CalculateService calculateService = new CalculateService();

    @Autowired
    public PostOrderService postOrderService = new PostOrderService();

    @Autowired
    private Environment environment;

    private String question;
    private List<String> splitOutput;
    private List<String> postOrderOutput;

    @Before
    public void init(){
        question = "(3+2)/2";
        splitOutput = Arrays.asList("(","3","+","2",")","/","2");
        postOrderOutput = Arrays.asList("3","2","+","2","/");
    }

    @Test
    public void validationTestOk() throws Exception {
        validationService.validCheck(question);
        validationService.validCheck(")3+2(");
    }


    @Test(expected = Exception.class)
    public void validationTestException() throws Exception {
        validationService.validCheck("(a+2)");
    }

    @Test
    public void splitDataTest() throws Exception {
        assertArrayEquals(splitService.splitInputQuestion(question).toArray(), splitOutput.toArray());
        List<String> answer = Arrays.asList(")","3","+","2","(");
        assertArrayEquals(splitService.splitInputQuestion(")3+2(").toArray(), answer.toArray());
    }

    @Test
    public void postOrderTest() throws Exception{
        assertArrayEquals(postOrderService.convertToPostOrder(splitOutput).toArray(), postOrderOutput.toArray());
    }

	@Test
	public void calculateTest() {
        Map<String, String> map = new HashMap<>();
        for(int i = 1 ; i < 34 ; i++){
            String caseNo = environment.getProperty("case"+i);
            String [] q = caseNo.split(" = ");
            map.put(q[0], q[1]);
        }

        Iterator<String> ite = map.keySet().iterator();
        while(ite.hasNext()){
            String key = ite.next();
            assertEquals(calculateService.getAnswer(StringUtils.trimAllWhitespace(key)), StringUtils.trimAllWhitespace(map.get(key)));
        }
        assertEquals(calculateService.getAnswer(StringUtils.trimAllWhitespace(")3+2(")), "ERROR");
	}
}
