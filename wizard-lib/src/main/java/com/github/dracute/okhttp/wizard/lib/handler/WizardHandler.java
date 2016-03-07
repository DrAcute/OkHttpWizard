package com.github.dracute.okhttp.wizard.lib.handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import com.github.dracute.okhttp.wizard.lib.callback.ProgressCallback;
import com.github.dracute.okhttp.wizard.lib.callback.WizardCallback;
import com.github.dracute.okhttp.wizard.lib.parser.IParser;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by DrAcute on 2016/1/23.
 */
public class WizardHandler<T> extends Handler implements Callback, ProgressCallback {

    private static final int CODE_RESPONSE = 0x100;
    private static final int CODE_FAILURE = 0x101;
    private static final int CODE_PROGRESS = 0x102;

    WeakReference<WizardCallback<T>> wizardCallWeakReference;
    IParser<T> parser;

    public WizardHandler(WeakReference<WizardCallback<T>> call, IParser<T> parser) {
        this.wizardCallWeakReference = call;
        this.parser = parser;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        Message message = new Message();
        message.what = CODE_FAILURE;
        message.obj = new Pair<>(request, e);
        sendMessage(message);
    }

    @Override
    public void onResponse(Response response) throws IOException {
        try {
            Pair<Integer, T> result = parser.parser(response);
            Message message = new Message();
            message.what = CODE_RESPONSE;
            message.obj = result;
            sendMessage(message);
        } catch (Exception e) {
            Message message = new Message();
            message.what = CODE_FAILURE;
            message.obj = new Pair<>(response.request(), e);
            sendMessage(message);
        }
    }

    @Override
    public void onProgress(long bytesRead, long contentLength, boolean done) {
        IParser.ProgressEntity progressEntity = parser.progress(bytesRead, contentLength, done);
        if (progressEntity != null) {
            Message message = new Message();
            message.what = CODE_PROGRESS;
            message.obj = progressEntity;
            sendMessage(message);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleMessage(Message msg) {
        if (wizardCallWeakReference != null && wizardCallWeakReference.get() != null) {
            switch (msg.what) {
                case CODE_RESPONSE:
                    Pair<Integer, T> result = (Pair<Integer, T>) msg.obj;
                    wizardCallWeakReference.get().onSuccess(result.first, result.second);
                    break;
                case CODE_FAILURE:
                    Pair<Request, Exception> exception = (Pair<Request, Exception>) msg.obj;
                    wizardCallWeakReference.get().onFailure(exception.first, exception.second);
                    break;
                case CODE_PROGRESS:
                    IParser.ProgressEntity progressEntity = (IParser.ProgressEntity) msg.obj;
                    wizardCallWeakReference.get().onProgress(progressEntity.bytesRead, progressEntity.contentLength, progressEntity.done);
                    break;
            }
        }
    }
}
