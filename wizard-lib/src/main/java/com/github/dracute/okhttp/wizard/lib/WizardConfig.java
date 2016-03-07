package com.github.dracute.okhttp.wizard.lib;

/**
 * Created by DrAcute on 2016/1/26.
 */
public class WizardConfig {

    final String host;
    final boolean useQuerySymbol;

    private WizardConfig(WizardConfigBuilder builder) {
        host = builder.host;
        useQuerySymbol = builder.useQuerySymbol;
    }

    public String getHost() {
        return host;
    }

    public boolean isUseQuerySymbol() {
        return useQuerySymbol;
    }

    public static WizardConfigBuilder newBuilder() {
        return new WizardConfigBuilder();
    }

    public static class WizardConfigBuilder {

        private String host = "";
        private boolean useQuerySymbol = true;

        public WizardConfigBuilder setHost(String host) {
            this.host = host;
            return this;
        }

        public WizardConfigBuilder setUseQuerySymbolInUrl(boolean flag) {
            this.useQuerySymbol = flag;
            return this;
        }

        public WizardConfig build() {
            return new WizardConfig(this);
        }
    }
}
