package com.devdroid.snssdknew.remote;

import com.devdroid.snssdknew.model.SnssdkText;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:Gaolei  gurecn@gmail.com
 * Date:2016/10/10
 * I'm glad to share my knowledge with you all.
 */
public interface LoadListener {
    void loadLoaded(int snssdkType, List<SnssdkText> snssdks);
}
