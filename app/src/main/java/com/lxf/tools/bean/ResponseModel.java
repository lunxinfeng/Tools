package com.lxf.tools.bean;

/**
 * 服务器返回的实体
 */
public class ResponseModel<T> {

    private int error;		//业务状态码(0:正常；<0：错误)
    private String msg;		//业务状态对应提示
    private T data;	//返回内容主题，序列化的对象，JSON格式

    public ResponseModel() {

    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseModel [error=" + error + ", msg=" + msg + ", data="
                + data + "]";
    }

    public ResponseModel(int error, String msg, T data) {
        super();
        this.error = error;
        this.msg = msg;
        this.data = data;
    }

}
