package trf.smt.com.netlibrary.bean;

import trf.smt.com.netlibrary.utils.JsonUtils;

/**
 * Created by gbh on 2018/6/25  10:21.
 *
 * @describe 消息应答
 */

public class AckMsg {
    public String _fromdevicesid;//设备ID
    public String _msgid;//消息ID
    public int _ack = 1;//应答消息
    public int _ttl;//应答超时时间

    public AckMsg() {
    }

    public AckMsg(String fromdevicesid, String msgid) {
        this._fromdevicesid = fromdevicesid;
        this._msgid = msgid;
        _ack = 1;
        _ttl = 0;
    }

    public String getFromdevicesid() {
        return _fromdevicesid;
    }

    public void setFromdevicesid(String fromdevicesid) {
        this._fromdevicesid = fromdevicesid;
    }

    public String getMsgid() {
        return _msgid;
    }

    public void setMsgid(String msgid) {
        this._msgid = msgid;
    }

    public int getAck() {
        return _ack;
    }

    public void setAck(int ack) {
        this._ack = ack;
    }

    public int getTtl() {
        return _ttl;
    }

    public void setTtl(int ttl) {
        this._ttl = ttl;
    }

    @Override
    public String toString() {
        return JsonUtils.serialize(this);
    }
}
