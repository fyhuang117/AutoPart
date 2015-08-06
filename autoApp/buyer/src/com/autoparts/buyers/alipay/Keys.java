/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.autoparts.buyers.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {



    //商户私钥，需要用命令行openssl去生成"
    // public static final String PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAN76CMYvp11gI6FaXxL/8ZFdx/ntCdr92NZgoabFsQzwzcH+G/cixAlhfKOatMC2pDOLtXq+DogZVGz29nd/+HWimJu3ZJGrM3ll7NksTypeT5dMG5st4lBkh9/t6dDMVehIGzawqvO0LtaBshknbdZu3+eEshJTXIN8MZ9E58g9AgMBAAECgYEA2j2d8uTXiiQ8G4SJg94xAZfp0Gw6djZ5UrJ35fSe60ySB2sA0i4b9BaP2M3mVq0tICqBMSbusDUilhhOef4uHpk3AeHUparB4Y1KsiavYx8r9EDZbbnG/5MF7iaQhyl7fdTJBMu54Oe1wMmfYk6eteqtSNPipzLnHuL94jrZxJ0CQQD40LoOCgYvoJOPQhJwoooVXPHa1dHlmv7UgIQH+JJ1rZ76DHYZboY7KT81z27uKQbsk7qUWbgXHjB9TnP7nlm3AkEA5WpOG8W4VNv9sBTewtoWf7cnxJrmLRHAJNyNH+yddoO7Xo+S+aJo+Ps2aAyFuvmQG2jspNKC6baRkMD6SEj9qwJBANS/fGbmsVVmAuX6g9PlVxZfK7mHHHsr5XsEDGDYbTrL4uRBZXXnaEULxq2XGW3wjKgaxO12LY8u1B1AyLE8O88CQBYetN9KpiZQe9pxeLUS8B5qW4RCZzUrQ/qJ5XWRi/E0bGmGsGYvea2gEzPfGTnY/EMYT9Dol0TPnKN3Z+p2VzcCQAbBcsC9mftReC63srZb84xSWCUofB0rxXR7Xm48HCdB+/1Y2uynTogGecxFkb2Qk3CjvKRHe1xPkLKLZ+dy8DQ=";
    //支付宝公钥
    public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    ///合作身份者id，以2088开头的16位纯数字
    public static final String PARTNER = "2088021017264632";
    //
    //收款支付宝账号
    public static final String SELLER = "cl@ooole.cn";

    //支付宝私钥
    public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMyqA/QINIS3wqCv\n" +
            "UBvqdPQPrUdumiVLKfRT1nKeQ+CPD/JRqM1ou5LdmY+wozDecZ+BU9KwPhksVhtc\n" +
            "rg2dY5Wt4zZAmCG0z2XgLj8gzzqJf/IIBtl9m8m2ef+vXEfvC1jDPfyvr1uFbqlC\n" +
            "x4lPCqJXPpifAdNH/1PYB2BgaevlAgMBAAECgYAcgj5EuvPd3N8OlP0atmEOMjTX\n" +
            "828jbMHuV9NSaftYh3UYMwnJeyJNxfKDEn7Y6tN2/YPYoFY2GXfxHcrc+KSLUETK\n" +
            "vApKQDoprP12n1ec54ogVE6J0Lxyq98f/eyAVtLkvgrTA6QAQFsMKLCOafnkC7hj\n" +
            "4biQs1JA482VN3j5oQJBAOcN9dwfIe3wFfJFjChIcu9NF8U/oVlz2KhqT2gZyN1+\n" +
            "TiDII5Xa5H8WgSENtNhNa1ebBQ76BDmkEPKKbQK5y7kCQQDiwqjFeJIBBVcwJDdW\n" +
            "POipARSwdMVbBx7W4R0jUs9J/nUkTy0E4btpEtlsfsT/WohcXQEtegOcZJ8ga5BR\n" +
            "e++NAkBkyP8krGPVcTNxgYq4i5mu7pbe0HmmFlU8aVsH0q9+hji4LfTvyhJ/qSZc\n" +
            "b6kh7OtUxGb5eG9dYUaPNXiFhtvpAkBHsukOV0tDC19AF5Nvrx6ZoX/bM3DbkWrM\n" +
            "4q0F6D8m1FBRxL8lVgKd/JnG1maPnx6Spc2jhXryzlYKODiPqzJZAkBTgeittfFG\n" +
            "HN5ksH7kp+scO6hqW7rCji413bWLe7xmylTSIVodk6DBn7ONSaYOSoGNQwjrGWs4\n" +
            "Q/HDHLoRoLCd";



}
