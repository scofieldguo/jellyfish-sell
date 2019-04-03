package com.jellyfish.sell.wechat.controller;

import com.jellyfish.sell.common.api.bean.TaskEnum;
import com.jellyfish.sell.support.xml.XmlBeanUtil;
import com.jellyfish.sell.user.entity.UserData;
import com.jellyfish.sell.user.entity.WechatUser;
import com.jellyfish.sell.user.service.IUserDataService;
import com.jellyfish.sell.user.service.IWechatUserService;
import com.jellyfish.sell.wechat.config.WxConfig;
import com.jellyfish.sell.wechat.model.RequestXml;
import com.jellyfish.sell.wechat.model.ResponseXml;
import com.jellyfish.sell.wechat.service.ConfigService;
import com.jellyfish.sell.wechat.util.StringCdata;
import com.jellyfish.sell.wechat.util.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Controller
@RequestMapping("lotto/wechat/open")
public class WeixinCoreController extends BaseController {
    private static Logger LOGGER = LoggerFactory.getLogger(WeixinCoreController.class);

    @Autowired
    private ConfigService configService;
    @Autowired
    private IWechatUserService wechatUserService;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private IUserDataService userDataService;

    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("nodify.do")
    public void notify(
            @RequestParam(value = "nonce", required = false) String nonce,
            @RequestParam(value = "timestamp", required = false) String timestamp,
            @RequestParam(value = "signature", required = false) String signature,
            @RequestParam(value = "echostr", required = false) String echostr,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            // 获得 nonce timestamp token signature echostr的值
            LOGGER.info(String.format("notify: nonce=%s, timestamp=%s, signature=%s, echostr=%s.",
                    nonce, timestamp, signature, echostr));
            // 验证是否为微信发送
            if (!WxUtil.check(nonce, timestamp, signature, wxConfig.getWxOpenAppToken())) {
                LOGGER.info("This request is not from weixin");
                this.writeXml("", response);
                return;
            }
            // 验证消息真实性接口请求
            if (checkEchostr(echostr)) {
                LOGGER.info("Check signature is SUCCESS");
                this.writeXml(echostr, response);
                return;
            }

            String body = getBody(request);
            // 验证Post信息
            if (!checkbody(body)) {
                LOGGER.info("No Post Infomation from the request");
                this.writeXml("", response);
                return;
            }

            RequestXml requestXml = XmlBeanUtil.XMLToBean(body, RequestXml.class);
            if (!"event".equals(requestXml.getMsgType())) {//不是事件类型
                //统一回复内容
                ResponseXml responseXml = new ResponseXml();
                responseXml.setToUserName(new StringCdata(requestXml.getFromUserName()).toString());
                responseXml.setFromUserName(new StringCdata(requestXml.getToUserName()).toString());
                responseXml.setMsgType(new StringCdata("text").toString());
                responseXml.setCreateTime(System.currentTimeMillis());
                responseXml.setContent(new StringCdata(wxConfig.getWxOpenAppReplyTxt()).toString());
                String result = XmlBeanUtil.BeanToXML(responseXml, ResponseXml.class);
                LOGGER.info(String.format("【用户发送信息回复】=%s", wxConfig.getWxOpenAppReply()));
                this.writeXml(result, response);
                return;
            }

            String event = requestXml.getEvent();
            if (event.equals("subscribe") || event.equals("unsubscribe")) {
                String openId = requestXml.getFromUserName();
                WechatUser user = wechatUserService.findByOpenID(openId);
                Integer flag = 0;
                if (event.equals("subscribe")) {
                    flag = 1;
                }
                if (user != null) {
                    user.setFlag(flag);
                    wechatUserService.updateWeChatUser(user);
                    LOGGER.info(String.format("Update User subscribe openid=%s,event=%s", openId, event));
                } else {
                    String access_token = configService.getAccess_token();
                    user = WxUtil.getUserInfoByUnionId(access_token, openId);
                    if (user == null) {
                        this.writeXml("", response);
                        return;
                    }
                    user.setFlag(flag);
                    wechatUserService.insertWeChatUser(user);
                    LOGGER.info(String.format("CreatUser success openid=%s", openId));
                }

                ResponseXml responseXml = new ResponseXml();
                responseXml.setToUserName(new StringCdata(requestXml.getFromUserName()).toString());
                responseXml.setFromUserName(new StringCdata(requestXml.getToUserName()).toString());
                responseXml.setMsgType(new StringCdata("text").toString());
                responseXml.setCreateTime(System.currentTimeMillis());
                if (event.equals("subscribe")) {
                    responseXml.setContent(new StringCdata(wxConfig.getWxOpenAppReply()).toString());
                }
                responseXml.setFuncFlag(0);
                String result = XmlBeanUtil.BeanToXML(responseXml, ResponseXml.class);
                LOGGER.info(String.format("【用户关注回复】%s", wxConfig.getWxOpenAppReply()));
                this.writeXml(result, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkbody(String body) {
        return (body != null && body.length() > 1 && !body
                .equalsIgnoreCase("null"));
    }

    /**
     * 获取Post xml体
     *
     * @param request
     * @return
     */
    private String getBody(HttpServletRequest request) {
        String body = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (ServletInputStream) request.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            body = sb.toString();
            LOGGER.info("Request Post Body = " + body);
        } catch (IOException e) {
            LOGGER.error("Request IO Exception cause by  " + e);
        }
        return body;
    }

    /**
     * 检测是否为验证消息接口
     *
     * @param echostr
     * @return
     */
    private boolean checkEchostr(String echostr) {
        boolean result = (echostr != null && echostr.length() > 0 && !echostr
                .equalsIgnoreCase("null"));
        LOGGER.info("Check infomation api result is " + result);
        return result;
    }

}
