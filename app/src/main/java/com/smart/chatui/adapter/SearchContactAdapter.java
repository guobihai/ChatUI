package com.smart.chatui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.smart.chatui.adapter.holder.SearchContactViewHolder;
import trf.smt.com.netlibrary.enity.Contacts;

import java.util.ArrayList;

/**
 * 通讯录适配器
 */
public class SearchContactAdapter extends RecyclerArrayAdapter<Contacts> {

    private onItemClickListener onItemClickListener;
    private boolean isEdit;

    public SearchContactAdapter(Context context, boolean isEdit) {
        super(context);
        this.isEdit = isEdit;
    }


    /**
     * 获取选择的
     * @return
     */
    public ArrayList<String> getSelectListUserName() {
        ArrayList<String> stringList = new ArrayList<>();
        for (Contacts contacts : getAllData()) {
            if (contacts.isSelect())
                stringList.add(contacts.getUserName());
        }
        return stringList;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        viewHolder = new SearchContactViewHolder(parent, isEdit, onItemClickListener);
        return viewHolder;
    }

    @Override
    public int getViewType(int position) {
        return getAllData().get(position).getType();
    }

    public void addItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onHeaderClick(int position);

        void onItemClick(View view, int position);

    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return getAllData().get(position).getLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = getAllData().get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
