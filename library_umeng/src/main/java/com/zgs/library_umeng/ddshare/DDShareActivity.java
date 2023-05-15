package com.zgs.library_umeng.ddshare;

/**
 * 文件名：DDShareActivity <br/>
 * 描述：
 *
 * @author zgs
 * @since 2023/05/11 16:39
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.dingtalk.share.ddsharemodule.IDDAPIEventHandler;
import com.android.dingtalk.share.ddsharemodule.IDDShareApi;
import com.android.dingtalk.share.ddsharemodule.message.BaseReq;
import com.android.dingtalk.share.ddsharemodule.message.BaseResp;
import com.umeng.socialize.UMShareAPI;

/**
 *  注意   BToast.showText(); 是我自己封装的Toast
 * 	      Logger也是一个封装   可以改成Log
 *
 */

public class DDShareActivity extends Activity implements IDDAPIEventHandler {

    private IDDShareApi mIDDShareApi;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lzc", "onCreate==========>");

    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d("lzc", "onReq=============>");
    }

    @Override
    public void onResp(BaseResp baseResp) {
    }
}

