package com.smart.chatui.test;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import trf.smt.com.netlibrary.utils.JsonUtils;

/**
 * Created by gbh on 2018/8/30  17:54.
 *
 * @describe
 */
public class TestJsonTest {
    @Test
    public void testJson() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("bb", "还是你厉害".getBytes());
        String s = JsonUtils.serialize(map);
        System.out.println("============"+s);
    }

}