package trf.smt.com.netlibrary.bean;

/**
 * Created by gbh on 2018/4/10  13:46.
 *
 * @describe
 */

public class AckValue {
    private Object ids;
    private String tag;

    public AckValue(String tag, String ids) {
        this.tag = tag;
        this.ids = ids;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
