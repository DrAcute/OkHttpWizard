package com.github.dracute.okhttp.wizard.demo.http;

import com.github.dracute.okhttp.wizard.annotations.Download;
import com.github.dracute.okhttp.wizard.annotations.Field;
import com.github.dracute.okhttp.wizard.annotations.Get;
import com.github.dracute.okhttp.wizard.annotations.Host;
import com.github.dracute.okhttp.wizard.annotations.Path;
import com.github.dracute.okhttp.wizard.annotations.Post;
import com.github.dracute.okhttp.wizard.annotations.UseQuerySymbolInUrl;
import com.github.dracute.okhttp.wizard.demo.bean.Been;
import com.github.dracute.okhttp.wizard.lib.WizardCall;
import com.github.dracute.okhttp.wizard.lib.param.DownloadParam;

import java.io.File;
import java.util.List;

/**
 * Created by DrAcute on 2016/1/5.
 */
public interface DemoHttp {

    public interface DemoInnerHttp {
        @UseQuerySymbolInUrl(true)
        @Get("?m=content&c=apidown&a=lists&number={1}")
        TestWizard<List<Been>> demo(@Path("p") int page, @Path("{1}") int number, @Path int catid);

        @Post("http://xxxx")
        void demo2(@Path int page, @Path int number, @Field String name, @Field String key);

        @Post("http://xxxx")
        WizardCall<File> demo3(@Path int page, @Download String saveName);

        @Host("")
        @Get("http://dl.maxthon.cn/mx4/mx4.4.7.3000cn.exe")
        WizardCall<File> demo4(@Download DownloadParam download);
    }

}
