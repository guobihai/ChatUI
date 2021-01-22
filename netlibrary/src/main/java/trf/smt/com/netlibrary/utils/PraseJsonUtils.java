package trf.smt.com.netlibrary.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import trf.smt.com.netlibrary.bean.HisMessageInfo;
import trf.smt.com.netlibrary.enity.MessageInfo;

/**
 * Created by gbh on 2018/5/15  17:20.
 *
 * @describe
 */

public class PraseJsonUtils {

    public static HisMessageInfo praseMsgInfo(String json, String headUrl) {
        HisMessageInfo hisMessageInfo = new HisMessageInfo();
        List<MessageInfo> infoList = new ArrayList<>();
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(json).getAsJsonObject();
            String code = jsonObject.get("code").getAsString();
            if (code.equals("0000")) {
                hisMessageInfo.setCode(code);
                HisMessageInfo.DataBeanX dataBeanX = new HisMessageInfo.DataBeanX();
                boolean isObj = jsonObject.get("data").isJsonObject();
                if (!isObj) return hisMessageInfo;
                jsonObject = jsonObject.get("data").getAsJsonObject();
                int total = jsonObject.get("total").getAsInt();
                dataBeanX.setTotal(total);
                boolean isArray = jsonObject.get("data").isJsonArray();
                if (!isArray) return hisMessageInfo;
                JsonArray array = jsonObject.get("data").getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {
                    jsonObject = array.get(i).getAsJsonObject();
                    MessageInfo info = new MessageInfo();
//                    info.setId(jsonObject.get("id").getAsLong());
                    info.setIsSend(jsonObject.get("isSend").getAsInt());
                    info.setContent(jsonObject.get("content").getAsString());
                    info.setMsgType(jsonObject.get("type").getAsInt());
                    info.setFromUser(jsonObject.get("fromUser").getAsString());
                    info.setToUser(jsonObject.get("toUser").getAsString());
                    info.setTime(jsonObject.get("time").getAsString());
                    info.setHeader(info.getIsSend() == 1 ?
                            "http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg" : headUrl);
                    switch (info.getMsgType()) {
                        case MsgType.SEND_TYPE_IMG:
                        case MsgType.SEND_TYPE_EMOJI:
                            info.setImageUrl(info.getContent());
                            break;
                        case MsgType.SEND_TYPE_VOICE:
                            info.setFilepath(info.getContent());
                            info.setVoiceTime(3);
                            break;
                    }
                    info.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                    info.setType(info.getIsSend() == 1 ? Constants.CHAT_ITEM_TYPE_RIGHT : Constants.CHAT_ITEM_TYPE_LEFT);
                    infoList.add(info);
                }
                dataBeanX.setData(infoList);
                hisMessageInfo.setData(dataBeanX);
            }

        } catch (JsonSyntaxException e) {
//            e.printStackTrace();
        }

        return hisMessageInfo;
    }
}
