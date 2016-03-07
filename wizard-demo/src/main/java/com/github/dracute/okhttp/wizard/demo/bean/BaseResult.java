package com.github.dracute.okhttp.wizard.demo.bean;

/**
 * Created by Lurker on 2015/3/10.
 */
public class BaseResult<T> {
    BaseResult.State state;
    T data;

    public BaseResult.State getState() {
        return state;
    }

    public void setState(BaseResult.State state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static class State {

        int code;
        String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
