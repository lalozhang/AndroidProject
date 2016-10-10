package com.tequila.net;

import android.os.Handler;

public class NetworkTask {

    public boolean cancel = false;
    public final NetworkParam param;
    public final Handler handler;
	public int serverStatus = TaskStatus.SERVER_ERROR;

    public NetworkTask(NetworkParam p, Handler handler) {
        this.param = p;
        this.handler = handler;
    }

}
