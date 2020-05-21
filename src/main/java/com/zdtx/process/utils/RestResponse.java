package com.zdtx.process.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/***
 * 响应结果工具类
 * @author zdtx
 */
@ApiModel(value = "响应结果")
public class RestResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("状态码 0:失败；1成功")
    private int code;

    @ApiModelProperty("消息")
    private String message;

    @ApiModelProperty("返回数据")
    private T data;

    public RestResponse() {
        super();
    }

    public RestResponse(T data) {
        super();
        this.data = data;
    }

    public static RestResponse succuess() {
        RestResponse restResponse = new RestResponse();
        restResponse.setResultCode(ResultEnum.SUCCESS);

        return restResponse;
    }

    public RestResponse succuess(T data) {
        RestResponse restResponse = new RestResponse();
        restResponse.setResultCode(ResultEnum.SUCCESS);
        restResponse.setData(data);

        return restResponse;
    }

    public static RestResponse fail() {
        RestResponse restResponse = new RestResponse();
        restResponse.setResultCode(ResultEnum.FAIL);

        return restResponse;
    }


    public static RestResponse fail(ResultEnum resultCode) {
        RestResponse restResponse = new RestResponse();
        restResponse.setResultCode(resultCode);

        return restResponse;
    }

    public static RestResponse fail(String message) {
        RestResponse restResponse = new RestResponse();
        restResponse.setCode(ResultEnum.FAIL.getCode());
        restResponse.setMessage(message);

        return restResponse;
    }

    public RestResponse fail(Integer code, String message) {
        RestResponse restResponse = new RestResponse();
        restResponse.setCode(code);
        restResponse.setMessage(message);

        return restResponse;
    }

    public RestResponse fail(ResultEnum resultCode, T data) {
        RestResponse restResponse = new RestResponse();
        restResponse.setResultCode(resultCode);
        restResponse.setData(data);

        return restResponse;
    }

    private void setResultCode(ResultEnum resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMsg();
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
