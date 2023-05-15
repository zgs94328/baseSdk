package com.zgs.library_umeng.douyinapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bytedance.sdk.open.aweme.CommonConstants;
import com.bytedance.sdk.open.aweme.common.handler.IApiEventHandler;
import com.bytedance.sdk.open.aweme.common.model.BaseReq;
import com.bytedance.sdk.open.aweme.common.model.BaseResp;
import com.bytedance.sdk.open.aweme.share.Share;
import com.bytedance.sdk.open.douyin.DouYinOpenApiFactory;
import com.bytedance.sdk.open.douyin.api.DouYinOpenApi;

/**
 * 文件名：DouYinEntryActivity <br/>
 * 描述：
 *
 * @author zgs
 * @since 2023/05/11 16:45
 */
public class DouYinEntryActivity extends Activity implements IApiEventHandler {

    DouYinOpenApi douYinOpenApi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        douYinOpenApi = DouYinOpenApiFactory.create(this);
        douYinOpenApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == CommonConstants.ModeType.SHARE_CONTENT_TO_TT_RESP) {
            Share.Response response = (Share.Response) resp;
            // 抖音940以后，错误码除了response.errorCode字段，还增加了response.subErrorCode字段，帮助三方排查错误原因
            Toast.makeText(this, "分享失败,errorCode: " + response.errorCode + "subcode" + response.subErrorCode + " Error Msg : " +
                    response.errorMsg, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onErrorIntent(@Nullable Intent intent) {
        // 错误数据
        Toast.makeText(this, "Intent出错", Toast.LENGTH_LONG).show();
    }
}