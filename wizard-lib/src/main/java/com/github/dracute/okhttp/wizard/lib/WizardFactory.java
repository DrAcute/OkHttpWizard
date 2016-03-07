package com.github.dracute.okhttp.wizard.lib;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by DrAcute on 2016/1/26.
 */
public class WizardFactory {
    static WizardOkHttp instance;
    public static WizardOkHttp getDefault() {
        if (instance == null) {
            instance = new WizardOkHttp();
        }
        return instance;
    }

    public static void initDefault(OkHttpClient client, WizardConfig wizardOption) {
        instance = new WizardOkHttp(client, wizardOption);
    }
}
