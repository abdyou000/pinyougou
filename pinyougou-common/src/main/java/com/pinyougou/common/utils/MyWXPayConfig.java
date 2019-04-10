package com.pinyougou.common.utils;

import cn.hutool.core.io.FileUtil;
import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.InputStream;

public class MyWXPayConfig extends WXPayConfig {
    @Value("{appid}")
    private String appid;
    @Value("{mchid}")
    private String mchid;
    @Value("{partnerkey}")
    private String partnerkey;
    @Value("{certpath}")
    private String certpath;
    @Override
    public String getAppID() {
        return appid;
    }

    @Override
    public String getMchID() {
        return mchid;
    }

    @Override
    public String getKey() {
        return partnerkey;
    }

    @Override
    public InputStream getCertStream() {
        return FileUtil.getInputStream(certpath);
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String s, long l, Exception e) {
            }

            @Override
            public DomainInfo getDomain(WXPayConfig wxPayConfig) {
                return new DomainInfo(WXPayConstants.DOMAIN_API,true);
            }
        };
    }
}
