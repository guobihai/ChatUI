package trf.smt.com.netlibrary.utils;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


/**
 * Created by gbh on 2018/4/16 10:36.
 *
 * @describe UDP 客户端
 */

public final class UDPClient implements Runnable {

    private int udpPort = 62280;
    private String hostIp = "192.168.5.250";
    private DatagramSocket socket = null;
    private DatagramPacket packetSend, packetRcv;
    private boolean udpLife = true; //udp生命线程
    private byte[] msgRcv = new byte[1024 * 2]; //接收消息

    private RevCallBackInterface mRevCallBackInterface;

    public void setRevCallBackInterface(RevCallBackInterface revCallBackInterface) {
        mRevCallBackInterface = revCallBackInterface;
    }

    public UDPClient(String hostIp, int udpPort) {
        this.hostIp = hostIp;
        this.udpPort = udpPort;
    }

    //返回udp生命线程因子是否存活
    protected boolean isUdpLife() {
        if (udpLife) {
            return true;
        }

        return false;
    }

    //更改UDP生命线程因子
    protected void setUdpLife(boolean b) {
        udpLife = b;
    }

    //发送消息
    protected String send(String msgSend) throws IOException {
        InetAddress hostAddress = null;
        if (null == msgSend || msgSend.equals("")) return "";
        try {
            hostAddress = InetAddress.getByName(hostIp);
            LogUtils.sysout("hostAddress:" + hostAddress.getHostAddress() + "  udpPort:" + udpPort);

            if (null == hostAddress) return "";
            byte[] sendData = msgSend.getBytes();
            packetSend = new DatagramPacket(sendData, sendData.length, hostAddress, udpPort);
            socket.send(packetSend);
        } catch (UnknownHostException e) {
//            e.printStackTrace();
        }
        //   socket.close();
        return msgSend;
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(1000);//设置超时为3s
        } catch (SocketException e) {
            e.printStackTrace();
        }
        packetRcv = new DatagramPacket(msgRcv, msgRcv.length);
        while (udpLife) {
            try {
                socket.receive(packetRcv);
                String RcvMsg = new String(packetRcv.getData(), packetRcv.getOffset(), packetRcv.getLength());
                //将收到的消息发给主界面
                if (null != mRevCallBackInterface)
                    mRevCallBackInterface.onCallBack(RcvMsg);
            } catch (IOException e) {
//                e.printStackTrace();

            }
        }

        socket.close();
    }

    protected void close() {
        setUdpLife(false);
        if (null != socket && !socket.isClosed())
            socket.close();
    }

    public interface RevCallBackInterface {
        void onCallBack(String data);
    }
}
