package com.smart.chatui.adapter.groups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smart.chatui.R;
import com.smart.chatui.enity.ContactsInfo;
import com.smart.chatui.enity.GroupContacts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import trf.smt.com.netlibrary.enity.Contacts;

/**
 * Created by gbh on 2018/5/23  10:33.
 *
 * @describe 二级目录
 */

public class RecyclerAdapter extends SecondaryListAdapter<RecyclerAdapter.GroupItemViewHolder, RecyclerAdapter.SubItemViewHolder> {


    private Context context;

    private List<DataTree<GroupContacts, Contacts>> dts = new ArrayList<>();
    private onItemClickListener onItemClickListener;
    private boolean isEdit;

    public RecyclerAdapter(Context context, boolean isEdit) {
        this.context = context;
        this.isEdit = isEdit;
    }

    public void setData(List datas) {
        dts = datas;
        notifyNewData(dts);
    }

    public void addAll(List datas) {
        dts.clear();
        dts.addAll(datas);
        notifyNewData(dts);
        notifyDataSetChanged();
    }

    /**
     * 获取所有的单个通讯录
     *
     * @return
     */
    public List<Contacts> getAllItemConcacts() {
        List<Contacts> contactsList = new ArrayList<>();
        Set<Contacts> contactsSet = new HashSet<>();
        for (DataTree<GroupContacts, Contacts> dt : dts) {
            if (null != dt.getSubItems())
                contactsSet.addAll(dt.getSubItems());
        }
        contactsList.addAll(contactsSet);
        return contactsList;
    }

    /**
     * 获取选中的人员
     *
     * @return
     */
    public Set<String> getSelectListContact() {
        Set<String> contactsList = new HashSet<>();
        for (DataTree<GroupContacts, Contacts> dt : dts) {
            for (Contacts contacts : dt.getSubItems()) {
                if (contacts.isSelect())
                    contactsList.add(contacts.getUserName());
            }
        }
        return contactsList;
    }

    @Override
    public RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_verb_group_layout, parent, false);
        return new GroupItemViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder subItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_verb_child_layout, parent, false);

        return new SubItemViewHolder(v);
    }

    @Override
    public void onGroupItemBindViewHolder(final RecyclerView.ViewHolder holder, final int groupItemIndex) {

        final GroupContacts contacts = dts.get(groupItemIndex).getGroupItem();
        ((GroupItemViewHolder) holder).tvGroup.setText(contacts.getLabel());
        ((GroupItemViewHolder) holder).tvCount.setText("(" + contacts.getSize() + ")");
        ((GroupItemViewHolder) holder).mCheckBox.setChecked(contacts.isSelect());
        ((GroupItemViewHolder) holder).mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View compoundButton) {
                for (Contacts p : contacts.getChildren()) {
                    p.setSelect(!contacts.isSelect());
                }
                if (null != onItemClickListener)
                    onItemClickListener.onItemSelectContacts(holder, groupItemIndex, 0);
                contacts.setSelect(!contacts.isSelect());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onSubItemBindViewHolder(final RecyclerView.ViewHolder holder, final int groupItemIndex,
                                        final int subItemIndex) {
        final Contacts data = dts.get(groupItemIndex).getSubItems().get(subItemIndex);
        ((SubItemViewHolder) holder).tvSub.setText(data.getNickName());
        ((SubItemViewHolder) holder).mCheckBox.setChecked(data.isSelect());
        if (!TextUtils.isEmpty(data.getIconUrl())) {
            Glide.with(context).load(data.getIconUrl())
                    .placeholder(R.drawable.ic_image_loading)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.person_72).into(((SubItemViewHolder) holder).imgUrl);
        } else {
            ((SubItemViewHolder) holder).imgUrl.setImageResource(R.drawable.person_72);
        }
        ((SubItemViewHolder) holder).mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setSelect(!data.isSelect());
                notifyDataSetChanged();
                if (null != onItemClickListener)
                    onItemClickListener.onItemSelectContacts(holder, groupItemIndex, subItemIndex);
            }
        });

    }

    @Override
    public void onGroupItemClick(Boolean isExpand, GroupItemViewHolder holder, int groupItemIndex) {
        ((GroupItemViewHolder) holder).imgDropState.setImageResource(isExpand ? R.drawable.ic_chevron_right_white_24dp :
                R.drawable.ic_expand_more_black_24dp);
    }

    @Override
    public void onSubItemClick(SubItemViewHolder holder, int groupItemIndex, int subItemIndex) {
        if (null != onItemClickListener)
            onItemClickListener.onItemClick(holder, groupItemIndex, subItemIndex);
    }

    public class GroupItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvGroup;
        TextView tvCount;
        ImageView imgDropState;
        CheckBox mCheckBox;


        public GroupItemViewHolder(View itemView) {
            super(itemView);

            tvGroup = (TextView) itemView.findViewById(R.id.tvGroupName);
            tvCount = (TextView) itemView.findViewById(R.id.tvCount);
            imgDropState = (ImageView) itemView.findViewById(R.id.imgDropState);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            mCheckBox.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        }
    }

    public class SubItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvSub;
        ImageView imgUrl;
        CheckBox mCheckBox;

        public SubItemViewHolder(View itemView) {
            super(itemView);
            tvSub = (TextView) itemView.findViewById(R.id.tvUserName);
            imgUrl = (ImageView) itemView.findViewById(R.id.imgHeart);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            mCheckBox.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        }
    }

    public void addItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onHeaderClick(int position);

        void onItemClick(RecyclerView.ViewHolder view, int groupItemIndex, int subItemIndex);

        void onItemSelectContacts(RecyclerView.ViewHolder view, int position, int subItemIndex);
    }
}
