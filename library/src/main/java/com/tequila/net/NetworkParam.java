package com.tequila.net;

import com.tequila.model.BaseParam;
import com.tequila.model.BaseResult;
import java.io.Serializable;

public class NetworkParam implements Serializable,Cloneable {

    private static final long serialVersionUID = 1L;
    public static final String PARAM = "param";

    public IServiceMap key;
    public String url;
    public String hostPath = "";
    public boolean block = false;
    public boolean cancelAble = false;
    public boolean memCache = false;
    public String progressMessage = "";
    public int addType = Request.NET_ADD_ONORDER;
    public BaseParam param;
    public BaseResult result;

    /** 本地用的参数 */
    public Serializable ext;

	/**
	 * 是否使用本地缓存
	 */
	public boolean diskCache = false;


    public NetworkParam(BaseParam param, IServiceMap serviceType) {
        try {
            this.key = serviceType;
            // urlSource = JSON.toJSONString(param);// param.toJsonObject().toString();
            this.param = param != null ? param : new BaseParam() {
                private static final long serialVersionUID = 1L;
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public NetworkParam clone() throws CloneNotSupportedException {
		return (NetworkParam) super.clone();
	}

    /**
     * @param name 参数名
     * @param value 参数值
     * @return 加密后的字段
     */
//    public static String convertPollParameter(String name, String value) {
//        if (name == null || TextUtils.isEmpty(value)) {
//            return "";
//        }
//        String tmp;
//        try {
//            tmp = Goblin.ePoll(value);// 移到try中是防止load库出错
//            tmp = java.net.URLEncoder.encode(tmp, "utf-8");
//        } catch (Throwable e) {
//            e.printStackTrace();
//            tmp = "";
//        }
//        return name + "=" + tmp;
//    }

    /**
     * @param value 参数值
     * @param key 加密key
     * @return 加密后的字段
     */
//    public static String convertValue(String value, String key) {
//        if (TextUtils.isEmpty(value)) {
//            return "";
//        }
//        String tmp;
//        try {
//            tmp = Goblin.e(value, key == null ? "" : key);// 移到try中是防止load库出错
//            tmp = java.net.URLEncoder.encode(tmp, "utf-8");
//        } catch (Throwable e) {
//            e.printStackTrace();
//            tmp = "";
//        }
//        return tmp;
//    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.key == null ? 0 : this.key.hashCode());
        result = prime * result + (this.param == null ? 0 : this.param.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NetworkParam other = (NetworkParam) obj;
        if (!this.key.equals(other.key)) {
            return false;
        }
        if (this.param == null) {
            if (other.param != null) {
                return false;
            }
        } else if (!this.param.equals(other.param)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("NetworkParam [key=%s, this=%d]", this.key, System.identityHashCode(this));
    }

}
