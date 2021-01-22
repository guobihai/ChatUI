package trf.smt.com.netlibrary;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import trf.smt.com.netlibrary.bean.AckValue;
import trf.smt.com.netlibrary.bean.Send;
import trf.smt.com.netlibrary.utils.JsonUtils;
import trf.smt.com.netlibrary.utils.TimeTool;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void sendTxt(){
        List<AckValue> list = new ArrayList<>();
        list.add(new AckValue("wx2222","1"));
        list.add(new AckValue("wx333","14"));
        Send<List<AckValue>> send =new Send<>("ack",list,"FFFFFFF","000001");
        System.out.println("===================="+ JsonUtils.serialize(send));
    }

    @Test
    public void timeTool(){
        //2018-10-20 14:42:47
        String str = "2018-10-20 14:42:47";
        System.out.println("=====time=="+str.replaceAll("-","/"));
    }
}