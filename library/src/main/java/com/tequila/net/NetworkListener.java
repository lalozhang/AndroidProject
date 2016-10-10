package com.tequila.net;

public interface NetworkListener {

    void onNetStart(NetworkParam param);

    void onNetEnd(NetworkParam param);

    void onNetError(NetworkParam param, int errCode);

    void onMsgSearchComplete(NetworkParam param);

	void onCacheHit(NetworkParam param);

    void onNetCancel();

    void onShowProgress(NetworkParam param);

    void onCloseProgress(NetworkParam param);

}
