package com.gw.zph.base.listener;

import java.io.File;

public interface ImageCompressImp {
     void onStart() ;//TODO 压缩开始前调用，可以在方法内启动 loading UI

     void onSuccess(File file) ;// TODO 压缩成功后调用，返回压缩后的图片文件

     void onError(Throwable e) ;//// TODO 当压缩过程出现问题时调用
}
