package com.tequila.net;


import com.tequila.utils.Enums;
import com.tequila.model.BaseResult;


public interface IServiceMap extends Enums.IType {

    int NET_TASK_START = 0;
    int NET_TASKTYPE_CONTROL = NET_TASK_START;
    int NET_TASKTYPE_SECURE = NET_TASK_START + 1;
    int NET_TASKTYPE_POLL = NET_TASK_START + 2;
    int NET_TASKTYPE_MULTI = NET_TASK_START + 3;// 参数作为附件上传,支持传递纯二进制数据
    int NET_TASKTYPE_ALL = NET_TASK_START + 4;

    Class<? extends BaseResult> getClazz();

    String name();

    String getProgressMessage(IServiceMap serviceMap);
}