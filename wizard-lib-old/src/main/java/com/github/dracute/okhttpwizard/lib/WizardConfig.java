package com.github.dracute.okhttpwizard.lib;

import com.github.dracute.okhttpwizard.lib.param.IParam;
import com.github.dracute.okhttpwizard.lib.param.TextParam;

import java.util.HashMap;

/**
 * Created by DrAcute on 2015/12/23.
 */
public class WizardConfig {

    public static final String FORM_DATA = "FORM_DATA";
    public static final String X_WWW_FORM_URLENCODED = "X_WWW_FORM_URLENCODED";

    final UrlBuilder urlBuilder;
    final RequestBodyBuilder requestBodyBuilder;

    WizardConfig(WizardConfigBuilder builder) {
        this.urlBuilder = builder.urlBuilder;
        this.requestBodyBuilder = builder.requestBodyBuilder;
    }

    public static WizardConfigBuilder newBuilder() {
        return new WizardConfigBuilder();
    }

    public static class WizardConfigBuilder {

        UrlBuilder urlBuilder;
        RequestBodyBuilder requestBodyBuilder;

        private WizardConfigBuilder() {
            urlBuilder = new UrlBuilder();
            urlBuilder.font = "utf-8";
            requestBodyBuilder = new RequestBodyBuilder();
        }

        private WizardConfigBuilder(UrlBuilder urlBuilder, RequestBodyBuilder requestBodyBuilder) {
            this.urlBuilder = new UrlBuilder(urlBuilder);
            this.requestBodyBuilder = new RequestBodyBuilder(requestBodyBuilder);
        }

        public WizardConfigBuilder setUseQuerSymbol(boolean flag) {
            this.urlBuilder.setUseQuerySymbol(flag);
            return this;
        }

        public WizardConfigBuilder setHost(String host) {
            this.urlBuilder.setHost(host);
            return this;
        }

        public WizardConfigBuilder setFont(String font) {
           this.urlBuilder.setFont(font);
            return this;
        }

        public WizardConfigBuilder setBodyType(String type) {
            this.requestBodyBuilder.setBodyType(type);
            return this;
        }

        public WizardConfigBuilder addUrlParam(String name, String value) {
            this.urlBuilder.add(name, value);
            return this;
        }

        public WizardConfigBuilder addRequestParam(String name, String value) {
            this.requestBodyBuilder.add(name, value);
            return this;
        }

        public WizardConfig build() {
            return new WizardConfig(this);
        }
    }
}
