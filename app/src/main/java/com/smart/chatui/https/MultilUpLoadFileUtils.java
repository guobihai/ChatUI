package com.smart.chatui.https;


import com.smart.chatui.https.entry.MultilEntry;
import com.smart.chatui.https.listener.impl.UIProgressListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gbh on 17/5/5  下午6:13.
 *
 * @describe 多文件上传
 */

public class MultilUpLoadFileUtils {
    private List<MultilEntry> mEntryList;
    private ExecutorService mExecutorService;
    private int nThreadCount = 5;//线程数量
    private String mFloder;//下载存放文件目录

    public MultilUpLoadFileUtils() {
        mEntryList = new ArrayList<>();
        init();
    }


    /**
     * 设置文件存储目录
     *
     * @param floder
     */
    public void setFloder(String floder) {
        mFloder = floder;
    }

    /**
     * 设置线程的数量
     *
     * @param nThreadCount
     */
    public void setnThreadCount(int nThreadCount) {
        this.nThreadCount = nThreadCount;
    }

    private void init() {
        mExecutorService = Executors.newFixedThreadPool(nThreadCount);
    }

    /**
     * 获取当前下载条数
     *
     * @return
     */
    public List<MultilEntry> getEntryList() {
        return mEntryList;
    }

    /**
     * 获取当前下载数量
     */
    public int getSize() {
        return mEntryList.size();
    }

    /**
     * 批量增加下载
     *
     * @param entryList
     */
    public void addAllListEntry(List<MultilEntry> entryList) {
        if (null == entryList) new NullPointerException("entryList is not null");
        mEntryList.addAll(entryList);
        for (int i = 0; i < mEntryList.size(); i++) {
            final MultilEntry entry = mEntryList.get(i);
            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    DownLoadFileUtils.startDownLoadFile(entry.getPath(), mFloder, new UIProgressListener() {
                        @Override
                        public void onUIProgress(long currentBytes, long contentLength, boolean done) {
                            entry.setProgress(currentBytes);
                            entry.setContentLenght(contentLength);
                            entry.setDone(done);
                        }
                    });
                }
            });
        }
    }

    /**
     * 单个实体
     *
     * @param multilEntry
     */
    public void addMutitlEntry(final MultilEntry multilEntry) {
        if (null == multilEntry) new NullPointerException("multilEntry is not null");
        mEntryList.add(multilEntry);
        //加入线程池，进行下载
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                DownLoadFileUtils.startDownLoadFile(multilEntry.getPath(), mFloder, new UIProgressListener() {
                    @Override
                    public void onUIProgress(long currentBytes, long contentLength, boolean done) {
                        multilEntry.setProgress(currentBytes);
                        multilEntry.setContentLenght(contentLength);
                        multilEntry.setDone(done);
                    }
                });
            }
        });

    }
}
