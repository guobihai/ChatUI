package trf.smt.com.netlibrary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import trf.smt.com.netlibrary.interfaces.ResultCallBack;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("trf.smt.com.netlibrary.test", appContext.getPackageName());
    }

    @Test
    public void sendText() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        UDPClientUtils udpClientUtils = new UDPClientUtils(appContext, "192.168.5.250", 62280, "FFFFFFFF");
        udpClientUtils.setResultCallBack(new ResultCallBack() {
            @Override
            public void onSuccess(String response) {
                System.out.println("=======res====" + response);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        udpClientUtils.login();
    }
}
