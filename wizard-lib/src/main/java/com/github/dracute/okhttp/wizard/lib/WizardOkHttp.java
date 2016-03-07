package com.github.dracute.okhttp.wizard.lib;

import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Created by DrAcute on 2016/1/7.
 */
public class WizardOkHttp {


    final OkHttpClient okHttpClient;
    final WizardConfig wizardConfig;
    final HashMap<String, Object> httpImpls;

    WizardOkHttp() {
        this.okHttpClient = new OkHttpClient();
        this.httpImpls = new HashMap<>();
        this.wizardConfig = WizardConfig.newBuilder().build();
    }

    WizardOkHttp(OkHttpClient client, WizardConfig wizardConfig) {
        this.okHttpClient = client;
        this.httpImpls = new HashMap<>();
        this.wizardConfig = wizardConfig;
    }

    WizardOkHttp(WizardOkHttp wizardOkHttp) {
        this.okHttpClient = wizardOkHttp.okHttpClient;
        this.httpImpls = wizardOkHttp.httpImpls;
        this.wizardConfig = wizardOkHttp.wizardConfig;
    }


    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> c) {
        String class_name = c.getName();

        if (httpImpls.containsKey(class_name)) {
            return (T) httpImpls.get(class_name);
        }

        try {
            Class<?> c_service = Class.forName(class_name + "$$Impl");
            T t = (T) c_service.<Constructor<T>>getConstructor(OkHttpClient.class, WizardConfig.class).newInstance(okHttpClient, wizardConfig);
            httpImpls.put(class_name, t);
            return t;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
