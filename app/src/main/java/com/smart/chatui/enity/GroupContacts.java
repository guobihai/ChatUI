package com.smart.chatui.enity;

import java.util.List;

import trf.smt.com.netlibrary.enity.Contacts;

/**
 * Created by gbh on 2018/5/25  14:18.
 *
 * @describe 分组数据
 */

public class GroupContacts {
    private int size;
    private String id;
    private String label;
    private List<Contacts> children;//分组会员
    private boolean isSelect;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Contacts> getChildren() {
        return children;
    }

    public void setChildren(List<Contacts> children) {
        this.children = children;
    }

    public boolean isVerbMenber() {
        return null == id ? true : id.equals("1111");
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
