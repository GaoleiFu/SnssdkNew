package com.devdroid.snssdknew.constant;

/**
 * Created with IntelliJ IDEA.
 * User:Gaolei  gurecn@gmail.com
 * Date:2016/10/10
 * I'm glad to share my knowledge with you all.
 */
public class ApiConstant {
    public static final String LOG_DIR = "/snssdk/log/";
    //下拉获取更新
    public static final String GET_SNSSDK_URL_FRESH = "http://ic.snssdk.com/neihan/stream/mix/v1/?mpic=1&content_type=-102&count=30&iid=5804114303&device_id=%s";
    //图片接口
    public static final String GET_SNSSDK_IMAGE_URL_FRESH = "http://iu.snssdk.com/neihan/stream/category/data/v2/?mpic=1&webp=1&category_id=12&level=6&count=20&iid=11285516691&device_id=%s";
}
