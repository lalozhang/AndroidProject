package com.tequila.net.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tequila.model.BaseResult;
import com.tequila.net.IServiceMap;
import com.tequila.net.NetworkTask;
import com.tequila.net.TaskListener;
import com.tequila.net.TaskStatus;
import com.tequila.utils.OkHttpManager;

import java.io.IOException;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class CommonTask extends BaseTask {


	public CommonTask(NetworkTask netTask, TaskListener aListener) {
		super(netTask, aListener);
	}

	@Override
	protected BaseResult getResult() {
		if(networkTask.param.param!=null&&
				!networkTask.cancel&&
				networkTask.param.key!=null){
			IServiceMap key = networkTask.param.key;
			Map<String, String> paramMap = param2Map(networkTask.param.param);
			try {
				Response response=OkHttpManager.getInstance().execute("",paramMap);
				if(response.isSuccessful()){
					String json = response.body().string();
					BaseResult result = JSON.parseObject(json, key.getClazz());
					networkTask.serverStatus = TaskStatus.SUCCESS;
					return result;
				}else{
					//response.code() <200 或 >=300
					networkTask.serverStatus = TaskStatus.SERVER_ERROR;
				}

			} catch (IOException e) {
				//崩溃解析异常或无网络
				networkTask.serverStatus = TaskStatus.ERROR;
			}

		}
		return null;
	}




}
