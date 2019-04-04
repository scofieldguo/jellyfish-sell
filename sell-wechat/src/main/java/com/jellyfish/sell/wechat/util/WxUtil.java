package com.jellyfish.sell.wechat.util;

import com.alibaba.fastjson.JSONObject;
import com.jellyfish.sell.support.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.*;

public class WxUtil {
    private static Logger LOG = LoggerFactory.getLogger(WxUtil.class);
    private static final String CHARSET = "UTF-8";

    /**
     * 获取微信公众号的access_token
     *
     * @param appid
     * @param secrent
     * @return
     */
    public static String getAccessToken(String appid, String secrent) {
        String access_token = null;
        LOG.info(String.format("appid=%s secrent=%s",appid,secrent));
        StringBuffer sb = new StringBuffer("https://api.weixin.qq.com/cgi-bin/token?");
        sb.append("grant_type=").append("client_credential").append("&").append("appid=").append(appid).append("&")
                .append("secret=").append(secrent);
        JSONObject jsonObject = HttpUtil.httpGetUrl(sb.toString());
        LOG.info("getAccessToken="+jsonObject.toJSONString());
        if (jsonObject.get("access_token") == null) {
            return null;
        }
        access_token = jsonObject.get("access_token").toString();
        return access_token;
    }

    /**
     * 通过code获取网页授权access_token.
     *
     * @param code
     * @param appid
     * @param secret
     * @return
     */
    public static Map<String, Object> getAuthAccessToken(String code, String appid, String secret) {
        String openid = null;
        String access_token = null;
        StringBuffer sb = new StringBuffer("https://api.weixin.qq.com/sns/oauth2/access_token?");
        sb.append("appid=").append(appid).append("&").append("secret=").append(secret)
                .append("&").append("code=").append(code).append("&").append("grant_type=").append("authorization_code");
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject jsonObject = HttpUtil.httpGetUrl(sb.toString());

        LOG.info(String.format("get oauth2 errcode=%s", jsonObject.getIntValue("errcode")));
        if (jsonObject.getIntValue("errcode") != 0) {
            return null;
        }
        openid = jsonObject.getString("openid");
        access_token = jsonObject.getString("access_token");
        map.put("openId", openid);
        map.put("access_token", access_token);
        LOG.info(String.format("code=%s,openid=%s,access_token=%s.", code, openid, access_token));
        return map;
    }

//    /**
//     * 拉取用户信息
//     * 网页授权作用域：snsapi_userinfo
//     *
//     * @param access_token
//     * @param openid
//     * @return
//     */
//    public static WechatUser getUserInfo(String access_token, String openid) {
//        StringBuffer sb = new StringBuffer("https://api.weixin.qq.com/sns/userinfo?");
//        sb.append("access_token=").append(access_token)
//                .append("&").append("openid=").append(openid)
//                .append("&").append("lang=").append("zh_CN");//lang:返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
//
//        JSONObject json = HttpUtil.httpGetUrl(sb.toString());
//        if (json.getIntValue("errcode") != 0) {
//            LOG.error("get weixin userinfo error:" + json.getIntValue("errcode"));
//            return null;
//        }
//        WechatUser user = new WechatUser();
//        user.setOpenId(openid);
//        user.setNickname(WxUtil.urlEnodeUTF8(json.getString("nickname")));
//        String sex = json.getString("sex");
//        user.setSex("1".equals(sex) ? WechatUser.SEX_MALE : ("2".equals(sex) ? WechatUser.SEX_FEMALE : WechatUser.SEX_UNKNOWN));
//        user.setCountry(json.getString("country"));
//        user.setProvince(json.getString("province"));
//        user.setCity(json.getString("city"));
//        user.setHeadimgurl(json.getString("headimgurl"));
//        user.setIntime(new Date());
//        return user;
//    }
//
//    /**
//     * UnionID机制获取用户基本信息
//     */
//    public static WechatUser getUserInfoByUnionId(String access_token, String openid) {
//        StringBuffer sb = new StringBuffer("https://api.weixin.qq.com/cgi-bin/user/info?");
//        sb.append("access_token=").append(access_token)
//                .append("&").append("openid=").append(openid)
//                .append("&").append("lang=").append("zh_CN");//lang:返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
//        JSONObject json = HttpUtil.httpGetUrl(sb.toString());
//        LOG.info("wechatUser==="+json.toJSONString());
//        if (json.getIntValue("errcode") != 0) {
//            LOG.info("get weixin userinfo error:" + json.getIntValue("errcode"));
//            return null;
//        }
//        WechatUser user = new WechatUser();
//        user.setOpenId(openid);
//        user.setUnionId(json.getString("unionid"));
//        if (null != json.getString("nickname") && !"".equals(json.getString("nickname").trim())) {
//            user.setNickname(WxUtil.urlEnodeUTF8(json.getString("nickname")));
//        }
//        String sex = json.getString("sex");
//        user.setSex("1".equals(sex) ? WechatUser.SEX_MALE : ("2".equals(sex) ? WechatUser.SEX_FEMALE : WechatUser.SEX_UNKNOWN));
//        user.setCountry(json.getString("country"));
//        user.setProvince(json.getString("province"));
//        user.setCity(json.getString("city"));
//        user.setHeadimgurl(json.getString("headimgurl"));
//        Integer subscribe = json.getInteger("subscribe");
//        user.setFlag(subscribe == 1 ? WechatUser.FLAG_SUBSCRIBE : WechatUser.FLAG_UNSUBSCRIBE);
//        user.setIntime(new Date());
//        return user;
//    }

    /**
     * 验证SHA1签名
     *
     * @param nonce    数据
     * @param signature SHA1签名
     * @return 若成功返回ture，否则返回false
     */
    public static boolean check(String nonce, String timestamp, String signature, String token) {
        if (signature == null || signature.isEmpty()) return false;
        List<String> list = new ArrayList<String>();
        list.add(nonce);
        list.add(timestamp);
        list.add(token);

        Collections.sort(list);

        // 将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(str);
        }
        String sha1 = SHA1.getString(sb.toString());
        //开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        return signature.equalsIgnoreCase(sha1);
    }

    /**
     * 随机字符串
     *
     * @param length 随机数长度
     * @return
     */
    public static String getNoncestr(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            char bt = (char) (97 + i);
            char mt = (char) (65 + i);
            sb.append(bt).append(mt);
        }

        String str = sb.toString();
        sb.delete(0, str.length());

        Random ran = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(str.charAt(ran.nextInt(52)));
        }

        return sb.toString();
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    public static long getTimestamp() {
        Date date = new Date();
        return date.getTime() / 1000;
    }

    public static String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
