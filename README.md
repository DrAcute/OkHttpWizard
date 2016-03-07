## 一个okhttp编译时注解快速开发框架

***注意！实验项目***

### 特点

使用编译时注解来构建http访问代码。

利用编译时能访问到泛型具体类型的特点，拼装具体的Type类型，并反序列化对象。
适于用于json中带有返回基本信息如`{'state':{'code':1, 'msg':''}, 'data':{}}`的http访问，
可自定义返回，做简单的逻辑处理，并抽出data对象

支持上传和下载 (断点下载)

支持FORM_DATA和X_WWW_FORM_URLENCODED两种RequestBody格式

### gradle

```
provided project(':wizard-annotations')
provided project(':wizard-compiler')
compile project(':wizard-lib')
```

### 使用

Interface

支持内部接口
```java
public interface DemoHttp {

    public interface DemoInnerHttp {
        @UseQuerySymbolInUrl(true)
        @Get("?m=content&c=apidown&a=lists&number={1}")
        // 可自定义WizardCall类型，完成初步的BaseResult操作
        TestWizard<List<Been>> demo(@Path("p") int page, @Path("{1}") int number, @Path int catid);

        @Host("")
        @Get("http://dl.maxthon.cn/mx4/mx4.4.7.3000cn.exe")
        WizardCall<File> demo2(@Download DownloadParam download);
    }

}
```

MainActivity

```java
WizardConfig config = WizardConfig.newBuilder().setHost("Your http host").setUseQuerySymbolInUrl(true).build();
WizardFactory.initDefault(new OkHttpClient(), config);
WizardFactory.getDefault().getService(DemoHttp.DemoInnerHttp.class).demo(1, 20, 32).enqueue(new WizardCallback<BaseResult<List<Been>>>() {
    @Override
    public void onSuccess(int code, BaseResult<List<Been>> listBaseResult) {
        // in main thread
        Log.d("Tag", code + " === " + listBaseResult.getData().size());
    }

    @Override
    public void onFailure(Request request, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onProgress(long bytesRead, long contentLength, boolean done) {

    }
});
WizardFactory.getDefault().getService(DemoHttp.DemoInnerHttp.class).demo2(new DownloadParam(Environment.getExternalStorageDirectory().getAbsolutePath(), "temp", true)).enqueue(new WizardCallback<File>() {
    @Override
    public void onSuccess(int code, File file) {
        Log.d("Tag", code + " ");
    }

    @Override
    public void onFailure(Request request, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onProgress(long bytesRead, long contentLength, boolean done) {
        Log.d("TAG", bytesRead + " --- " + contentLength + " --- " + done);
    }
});
```
