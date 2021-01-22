package trf.smt.com.netlibrary.enums;

/**
 * Created by gbh on 2018/4/8  12:26.
 *
 * @describe
 */

public enum TypeEnum {
    TXT(1,"文本消息"), IMG(3,"图片信息"),VOICE(34,"语音信息");
    int type;
    String desc;

    TypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
