package com.smart.chatui;

import org.junit.Test;

import trf.smt.com.netlibrary.utils.LogUtils;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void text(){
        String str = "";
        if(str.lastIndexOf(",")>0)
        LogUtils.sysout(str.substring(0,str.lastIndexOf(",")));
    }

    @Test
    public void addList(){

     String str = "http://113.106.222.250:9001";
     System.out.println(str.substring(0,str.lastIndexOf("/")));
//     byte[] base = Base64.decode(str,Base64.DEFAULT);
    }

    @Test
    public void subText(){
        String str = "wxid_9228942288022:\nhttp:///";
        System.out.println("========="+str.substring(str.indexOf("http"),str.length()));
    }
}