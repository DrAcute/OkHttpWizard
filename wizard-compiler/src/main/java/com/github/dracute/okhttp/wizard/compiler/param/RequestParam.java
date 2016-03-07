package com.github.dracute.okhttp.wizard.compiler.param;

/**
 * Created by DrAcute on 2016/1/5.
 */
public class RequestParam {
    public String requestMethod;
    public String path;
    public String host;
    public int useQuerySymbol;

    public RequestParam(String requestMethod, String path) {
        this.requestMethod = requestMethod;
        this.path = path;
    }
}
