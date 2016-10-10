package com.tequila.net.task;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.tequila.cache.ResponseMemCache;
import com.tequila.model.BaseParam;
import com.tequila.model.BaseResult;
import com.tequila.net.IServiceMap;
import com.tequila.net.NetworkParam;
import com.tequila.net.NetworkTask;
import com.tequila.net.TaskListener;
import com.tequila.net.TaskStatus;
import com.tequila.utils.CheckUtils;
import com.tequila.cache.DiskCacheHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class BaseTask extends AsyncTask<Void, Integer, BaseResult> {

	private static final String TAG = BaseTask.class.getSimpleName();
	
	protected TaskListener listener = null;
	public NetworkTask networkTask = null;

	public BaseTask(NetworkTask netTask, TaskListener aListener) {
		networkTask = netTask;
		listener = aListener;
	}

	public boolean cancelWithHandler(Handler handler) {
		if (networkTask.handler == handler && networkTask.param.cancelAble) {
			networkTask.cancel = true;
			cancel(true);
			return true;
		}
		return false;
	}

	public boolean cancelWithKey(IServiceMap key) {
		if (networkTask.param.key == key && networkTask.param.cancelAble) {
			networkTask.cancel = true;
			cancel(true);
			return true;
		}
		return false;
	}

	public boolean cancelWithType(int type) {
		// type为all时可以结束cancelable的类型
		if (type == IServiceMap.NET_TASKTYPE_ALL) {
			networkTask.cancel = true;
			cancel(true);
			return true;
		} else if (networkTask.param.key.getCode() == type && networkTask.param.cancelAble) {
			networkTask.cancel = true;
			cancel(true);
			return true;
		}
		return false;
	}

	public boolean cancelWithUrl(String url) {
		if (networkTask.param.url.equals(url) && networkTask.param.cancelAble) {
			networkTask.cancel = true;
			cancel(true);
			return true;
		}
		return false;
	}

	public boolean cancelWithParam(NetworkParam param) {
		if (networkTask.param.equals(param) && networkTask.param.cancelAble) {
			networkTask.cancel = true;
			cancel(true);
			return true;
		}
		return false;
	}

	public NetworkParam getNetworkParam() {
		return networkTask == null ? null : networkTask.param;
	}

	public static void findDiskCache(NetworkParam networkParam, Handler uiHandler) {
		if (uiHandler == null) {
			return ;
		}

		BaseParam param = networkParam.param;
		String key = param.newCacheKey();
		if(param != null && !TextUtils.isEmpty(key)){
			String cacheKey = DiskCacheHelper.hashKeyForDisk(key);
			BaseResult result = DiskCacheHelper.getDataWithCacheKeySafe(cacheKey,networkParam.key.getClazz());
			if(result != null){
				if(networkParam.result == null) {
					synchronized (networkParam) {
						if(networkParam.result == null) {
							networkParam.result = result;
							try {
								//创建networkParam的快照,默认clone只会做浅克隆
								NetworkParam snapshot = networkParam.clone();
								Message msg = uiHandler.obtainMessage(TaskStatus.CACHE_HITTED,snapshot);
								uiHandler.sendMessage(msg);
							} catch (CloneNotSupportedException e) {

							}
						}
					}
				}
			}
		}
	}

	protected void putCacheToDisk(BaseParam param,BaseResult result) {
		if(result == null){
			return ;
		}

		if(networkTask.serverStatus != TaskStatus.SUCCESS){
			return ;
		}

		String cacheKey = DiskCacheHelper.hashKeyForDisk(param.newCacheKey());

		if(!CheckUtils.isEmpty(cacheKey)){
			DiskCacheHelper.putDataToDiskSafe(cacheKey,result);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (!networkTask.cancel) {
			doPrepare();
		} else {
			cancel(false);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (listener != null) {
			listener.onTaskCancel(networkTask);
		}
	}

	@Override
	protected BaseResult doInBackground(Void... params) {
		if (!networkTask.cancel) {
			//默认认为是服务异常
			networkTask.serverStatus = TaskStatus.SERVER_ERROR;
			BaseResult result = getResult();
			if (result != null && getNetworkParam().memCache && result.code == 0) {
				ResponseMemCache.put(getNetworkParam(), result);
			}

			/**
			 * 缓存数据
			 */
			if (getNetworkParam().diskCache) {
				putCacheToDisk(getNetworkParam().param,result);
			}

			return result;
		}
		cancel(true);
		return null;
	}

	protected abstract BaseResult getResult() ;

	@Override
	protected void onPostExecute(BaseResult result) {
		if (!networkTask.cancel) {
			synchronized (networkTask.param) {
				networkTask.param.result = result;
			}
			if (networkTask.handler != null) {
				Message msg ;
				if (networkTask.param.result != null && networkTask.serverStatus == TaskStatus.SUCCESS) {
					msg = networkTask.handler.obtainMessage(TaskStatus.SUCCESS, networkTask.param);
				} else {
					msg = networkTask.handler.obtainMessage(networkTask.serverStatus, networkTask.param);
				}
				networkTask.handler.sendMessage(msg);
			}
			if (listener != null) {
				listener.onTaskComplete(networkTask);
			}
		} else {
			if (listener != null) {
				listener.onTaskCancel(networkTask);
			}
		}
	}

	protected  void doPrepare() {

	}

	public final Map<String,String> param2Map(BaseParam param){
		Map<String, Object> tmp = (Map<String, Object>) JSON.toJSON(param);
		if(tmp==null||tmp.isEmpty()){
			return new HashMap<String,String>();
		}
		Set<Map.Entry<String, Object>> entrySet = tmp.entrySet();
		Iterator<Map.Entry<String, Object>> set = entrySet.iterator();
		Map<String,String> map = new HashMap<String,String>();
		while (set.hasNext()){
			Map.Entry<String, Object> var = set.next();
			String key = var.getKey();
			String value = String.valueOf(var.getValue());
			map.put(key,value);
		}
		return map;
	}
}
