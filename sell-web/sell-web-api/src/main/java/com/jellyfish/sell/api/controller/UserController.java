package com.jellyfish.sell.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jellyfish.sell.api.config.GlobalConfig;
import com.jellyfish.sell.common.api.entity.AppShareMaterial;
import com.jellyfish.sell.common.api.service.IAppShareMaterialService;
import com.jellyfish.sell.common.api.service.IUserFormService;
import com.jellyfish.sell.support.DateUtils;
import com.jellyfish.sell.support.ResultUtil;
import com.jellyfish.sell.support.aes.AES;
import com.jellyfish.sell.support.oss.CloudStorageService;
import com.jellyfish.sell.support.oss.OssFactory;
import com.jellyfish.sell.support.sms.MDSendMobileCode;
import com.jellyfish.sell.support.stat.AccessLogBean;
import com.jellyfish.sell.support.wechat.WXEmojiFilterUtil;
import com.jellyfish.sell.support.wechat.proto.IWeChatService;
import com.jellyfish.sell.user.entity.AddrData;
import com.jellyfish.sell.user.entity.UserData;
import com.jellyfish.sell.user.service.IAddrDataService;
import com.jellyfish.sell.user.service.IUserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private IUserDataService userDataService;
    @Autowired
    private IWeChatService weChatService;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    private static Logger accessLog = LoggerFactory.getLogger("accesslog");
    @Autowired
    private GlobalConfig globalConfig;


    @Value("${aes.iv}")
    private String iv;
    @Value("${aes.key}")
    private String key;

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "userValidate.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String userValidate(@RequestParam("code") String code,
                               @RequestParam(name = "fromId", required = false) Integer fromId) {
        try {
            String openId = weChatService.getOpenIdnFromWechat(code);
            logger.info("userValidate userinfo=" + openId);
            if (openId == null) {
                return ResultUtil.builderErrorResult("", "未获取到用户登录信息");
            }
            if(fromId != null){
                userDataService.wirteUserFromId(openId,fromId,12*3600L);
            }
            return ResultUtil.builderSuccessResult("", "登陆成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.builderErrorResult("", "登陆失败");
    }

}
