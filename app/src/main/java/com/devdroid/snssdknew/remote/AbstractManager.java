package com.devdroid.snssdknew.remote;

/**
 * 
 * <br>
 * 类描述:数据层基本接口 <br>
 * 功能详细描述:
 * 
 * @author chaoziliang
 * @date [2015-1-19]
 */
public abstract class AbstractManager {

	protected volatile boolean mLoad = false;

	public AbstractManager() {
	}

	/**
	 * 子类需实现此接口，完成加载
	 */
	public abstract void onStartLoader();

	/**
	 * 子类需实现此接口，数据加载完毕
	 */
	public abstract void onLoadFininsh();

	/**
	 * 子类需实现此接口，加载完毕后发布消息
	 */
	public abstract void onPostMsg();

	/**
	 * 数据是否已加载完成.<br>
	 * 
	 * @return
	 */
	public final boolean isLoadDone() {
		return mLoad;
	}

	/**
	 * 启动加载.<br>
	 * 在非主线程调用.<br>
	 */
	public final void startLoader() {
		mLoad = false;
		onStartLoader();
	}

	/**
	 * 停止加载.<br>
	 * 在非主线程调用.<br>
	 */
	public final void loadFininsh() {
		mLoad = true;
		onLoadFininsh();
		onPostMsg();
	}
}
