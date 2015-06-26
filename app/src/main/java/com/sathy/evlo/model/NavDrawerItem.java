package com.sathy.evlo.model;

/**
 * Created by sathy on 20/6/15.
 */
public class NavDrawerItem {

    private boolean showNotify;
    private int icon;
    private String title;

    public NavDrawerItem() {
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
