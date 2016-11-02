package com.devdroid.snssdknew.eventbus;

/**
 * Created with IntelliJ IDEA.
 * User:Gaolei  gurecn@gmail.com
 * Date:2016/10/11
 * I'm glad to share my knowledge with you all.
 */
public class OnSnssdkLoadedEvent {
    private int position = 0;

    public OnSnssdkLoadedEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
