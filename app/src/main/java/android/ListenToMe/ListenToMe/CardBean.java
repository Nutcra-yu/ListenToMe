package android.ListenToMe.ListenToMe;

import android.ListenToMe.R;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

/**
 * 卡片类
 */
public class CardBean {

    private String engStr;
    private String name;

    public CardBean(String engStr, String name) {
        this.engStr = engStr;
        this.name = name;
    }

    public String getEngStr() {
        return engStr;
    }

    public String getName() {
        return name;
    }
}
