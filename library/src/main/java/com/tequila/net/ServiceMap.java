package com.tequila.net;


import com.tequila.model.BaseResult;

public enum ServiceMap implements IServiceMap {
	;

	private final String mType;
	private final Class<? extends BaseResult> mClazz;
	private final int mTaskType;

	ServiceMap(String type, Class<? extends BaseResult> clazz) {
		this(type, clazz, NET_TASKTYPE_CONTROL);
	}

	ServiceMap(String type, Class<? extends BaseResult> clazz, int taskType) {
		this.mType = type;
		this.mClazz = clazz;
		this.mTaskType = taskType;
	}

	@Override
	public String getDesc() {
		return mType;
	}

	@Override
	public Class<? extends BaseResult> getClazz() {
		return mClazz;
	}

	@Override
	public int getCode() {
		return mTaskType;
	}

	public String getProgressMessage(IServiceMap serviceMap) {
		switch ((ServiceMap) serviceMap) {

			default:
				return "努力加载中……";

		}
	}

}