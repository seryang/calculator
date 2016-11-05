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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link RiotSeryangApplication}.
 *
 * @author Dave Syer
 * @author Phillip Webb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RiotSeryangApplication.class)
public class CalculateTests {

	public CalculateService calculateService = new CalculateService();

    @Autowired
    private Environment environment;

    private Map<String, String> map = new HashMap<>();

    @Before
    public void init(){
        for(int i = 1 ; i < 34 ; i++){
            String question = environment.getProperty("case"+i);
            System.out.println(question);
            String [] q = question.split(" = ");
            map.put(q[0], q[1]);
        }
    }
	@Test
	public void test() throws Exception {

        Iterator<String> ite = map.keySet().iterator();
        while(ite.hasNext()){
            String key = ite.next();
            assertEquals(calculateService.getAnswer(StringUtils.trimAllWhitespace(key)), StringUtils.trimAllWhitespace(map.get(key)));
        }
	}
}
