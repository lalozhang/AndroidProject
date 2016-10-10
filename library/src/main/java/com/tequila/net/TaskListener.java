package com.tequila.net;

public interface TaskListener {
    void onTaskComplete(NetworkTask task);

    void onTaskCancel(NetworkTask task);

}
