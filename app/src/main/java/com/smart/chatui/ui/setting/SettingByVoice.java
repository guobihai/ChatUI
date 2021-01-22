package com.smart.chatui.ui.setting;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.smart.chatui.R;

/**
 * Created by gbh on 2018/5/17  16:12.
 *
 * @describe
 */

public class SettingByVoice extends Preference {
    public SettingByVoice(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.setting_voice_layout);

    }
}
