package com.jellyfish.sell.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.jellyfish.sell.support.wechat.proto.ISendCustomerMessageService;
import com.jellyfish.sell.wechat.config.WxConfig;
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
@RequestMapping("lotto/customer")
public class CustomerServiceController extends BaseController {

    private static Logger LOGGER = LoggerFactory.getLogger(CustomerServiceController.class);

    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private ISendCustomerMessageService sendCustomerMessageService;

    @RequestMapping("notify.do")
    public void notify(@RequestParam(value = "nonce", required = false) String nonce,
                       @RequestParam(value = "timestamp", required = false) String timestamp,
                       @RequestParam(value = "signature", required = false) String signature,
                       @RequestParam(value = "echostr", required = false) String echostr,
            HttpServletRequest request, HttpServletResponse response) {

        try {
            // 获得 nonce timestamp token signature echostr的值
            LOGGER.info(String.format("notify: nonce=%s, timestamp=%s, signature=%s, echostr=%s. method=%s",
                    nonce, timestamp, signature, echostr,request.getMethod()));
            // 验证是否为微信发送
            if(request.getMethod().toLowerCase().equals("get")) {
                if (WxUtil.check(nonce, timestamp, signature, wxConfig.getWxOpenAppToken())){
                    LOGGER.info("This request is not from weixin");
                    this.writeJSON(echostr, response);
                    return;
                }else{
                    this.writeJSON("", response);
                    return;
                }
            }

            String body = getBody(request);
            LOGGER.info("request body="+body);
            // 验证Post信息
            if (!checkbody(body)) {
                LOGGER.info("No Post Infomation from the request");
                this.writeJSON("", response);
                return;
            }
            JSONObject requestObj = JSONObject.parseObject(body);
            String content = requestObj.getString("Content");

            if(content !=null && content.trim().equals("1")){
                this.writeJSON("success", response);
                try {
                    sendCustomerMessageService.send1BackMessage(requestObj.getString("FromUserName"));
                }catch (Exception e){
                    LOGGER.error("sendCustomerMessage is error",e);
                    e.printStackTrace();
                }
                return;
            }
            if(content !=null && content.trim().equals("贝壳集市")){
                this.writeJSON("success", response);
                try {
                    sendCustomerMessageService.send2BackMessage(requestObj.getString("FromUserName"));
                }catch (Exception e){
                    LOGGER.error("sendCustomerMessage is error",e);
                    e.printStackTrace();
                }
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ToUserName",requestObj.getString("FromUserName"));
            jsonObject.put("FromUserName",requestObj.getString("ToUserName"));
            jsonObject.put("CreateTime",requestObj.getString("CreateTime"));
            jsonObject.put("MsgType","transfer_customer_service");
            this.writeJSON(jsonObject.toJSONString(),response);
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
