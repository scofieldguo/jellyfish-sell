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


    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    private static Logger accessLog = LoggerFactory.getLogger("accesslog");
    @Autowired
    private IAddrDataService addrDataService;
    @Autowired
    private IWeChatService weChatService;
    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    private IUserFormService userFormService;
    @Autowired
    private IAppShareMaterialService appShareMaterialService;

    @Value("${aes.iv}")
    private String iv;
    @Value("${aes.key}")
    private String key;

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "userValidate.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String userValidate(@RequestParam("code") String code, @RequestParam("iv") String iv,
                               @RequestParam("signature") String signatrue,
                               @RequestParam(name = "fromId", defaultValue = "1") Integer fromId,
                               @RequestParam(value = "inviteId", required = false) String inviteId) {
        try {
            String result = weChatService.decryptData(signatrue, iv, code);
            logger.info("userValidate userinfo=" + result);
            if (result == null) {
                return ResultUtil.builderErrorResult("", "未获取到用户登录信息");
            }
            String open_id = JSONObject.parseObject(result).getString("openId");
            Date now = new Date();
            UserData userData = userDataService.findUserDataByOpenId(open_id);
            Long inviteIdL = null;
            if (userData == null) {
                JSONObject object = JSONObject.parseObject(result);
                userData = new UserData();
                userData.setName(WXEmojiFilterUtil.filterEmoji(object.getString("nickName")));
                userData.setHeadImg(object.getString("avatarUrl"));
                userData.setLoginTime(now);
                userData.setRegistTime(now);
                userData.setGender(object.getInteger("gender"));
                userData.setProvince(object.getString("province"));
                userData.setCity(object.getString("city"));
                userData.setOpenId(object.getString("openId"));
                userData.setFromId(fromId);
                userData.setNewFlag(0);
                userData.setStatus(1);
                userData.setMasterId(inviteIdL);
                userData.setUnionId(object.getString("unionId"));
                userData.setNewBuy(UserData.NEW_BUY_NO);
                userData = userDataService.addUserData(userData);
            }/* else {
                userData.setLoginTime(now);
                userDataService.updateUserDataByOpenId(userData);
            }*/
            AccessLogBean accessLogBean = new AccessLogBean();
            accessLogBean.setUserId(userData.getId());
            accessLogBean.setaTime(now);
            if (DateUtils.diffDay(now, userData.getRegistTime()) == 0) {
                accessLogBean.setFlag(AccessLogBean.NEW);
            } else {
                accessLogBean.setFlag(AccessLogBean.OLD);
            }
            accessLogBean.setFromId(userData.getFromId());
            accessLogBean.setAtype(AccessLogBean.ACCESS);
            accessLog.info(accessLogBean.toString());
            userData.setUserId(AES.encrypt(userData.getId().toString(), globalConfig.getKey(), globalConfig.getIv()));
            userData.setId(0L);
            return ResultUtil.builderSuccessResult(userData, "登陆成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.builderErrorResult("", "登陆失败");
    }

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "userValidateNew.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String userValidateNew(@RequestParam("code") String code, @RequestParam("iv") String iv,
                               @RequestParam("signature") String signatrue,
                               @RequestParam(name = "fromId", defaultValue = "1") Integer fromId,
                               @RequestParam(value = "inviteId", required = false) Long inviteId) {
        try {
            String result = weChatService.decryptData(signatrue, iv, code);
            logger.info("userValidate userinfo=" + result);
            if (result == null) {
                return ResultUtil.builderErrorResult("", "未获取到用户登录信息");
            }
            String open_id = JSONObject.parseObject(result).getString("openId");
            Date now = new Date();
            UserData userData = userDataService.findUserDataByOpenId(open_id);
            if (userData == null) {
                JSONObject object = JSONObject.parseObject(result);
                logger.info("userInfo==="+object.toJSONString());
                userData = new UserData();
                userData.setName(WXEmojiFilterUtil.filterEmoji(object.getString("nickName")));
                userData.setHeadImg(object.getString("avatarUrl"));
                userData.setLoginTime(now);
                userData.setRegistTime(now);
                userData.setOpenId(object.getString("openId"));
                userData.setGender(object.getInteger("gender"));
                userData.setProvince(object.getString("province"));
                userData.setCity(object.getString("city"));
                userData.setFromId(fromId);
                userData.setNewFlag(0);
                userData.setStatus(1);
                userData.setMasterId(inviteId);
                userData.setUnionId(object.getString("unionId"));
                userData = userDataService.addUserData(userData);
            } /*else {
                userData.setLoginTime(now);
                userDataService.updateUserDataByOpenId(userData);
            }*/
            AccessLogBean accessLogBean = new AccessLogBean();
            accessLogBean.setUserId(userData.getId());
            accessLogBean.setaTime(now);
            if (DateUtils.diffDay(now, userData.getRegistTime()) == 0) {
                accessLogBean.setFlag(AccessLogBean.NEW);
            } else {
                accessLogBean.setFlag(AccessLogBean.OLD);
            }
            accessLogBean.setFromId(userData.getFromId());
            accessLogBean.setAtype(AccessLogBean.ACCESS);
            accessLog.info(accessLogBean.toString());
            userData.setUserId(AES.encrypt(userData.getId().toString(), globalConfig.getKey(), globalConfig.getIv()));
            return ResultUtil.builderSuccessResult(userData, "登陆成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.builderErrorResult("", "登陆失败");
    }




    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "address.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String addressList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        try {
            UserData userData = userDataService.findUserDataById(userId);
            if (userData == null) {
                return ResultUtil.builderErrorResult("null", "用户不存在");
            }
            List<AddrData> addrDataList = addrDataService.findAddrDataByUserId(userId);
            return ResultUtil.builderSuccessResult(addrDataList, "成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.builderErrorResult("", "成功");

    }


    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "editAddress.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String editAddress(HttpServletRequest request, @RequestParam("addressId") Long addressId,
                              @RequestParam(value = "direction", required = false) String direction,
                              @RequestParam(value = "recipient", required = false) String recipient,
                              @RequestParam(value = "area", required = false) String area,
                              @RequestParam(value = "province", required = false) String province,
                              @RequestParam(value = "city", required = false) String city,
                              @RequestParam(value = "phone", required = false) String phone,
                              @RequestParam(value = "post_code", required = false) String post_code,
                              @RequestParam(value = "status", required = false) Integer status) {
        Long userId = (Long) request.getAttribute("userId");
        UserData userData = userDataService.findUserDataById(userId);
        if (userData == null) {
            return ResultUtil.builderErrorResult("null", "用户不存在");
        }
        try {
            AddrData addrData = new AddrData();
            addrData.setId(addressId);
            addrData.setUserId(userId);
            addrData.setArea(area);
            addrData.setDirection(direction);
            addrData.setCity(city);
            addrData.setProvice(province);
            addrData.setInsertTime(new Date());
            addrData.setPhone(phone);
            addrData.setRecipient(recipient);
            addrData.setPostCode(post_code);
            addrData.setStatus(status);
            if (status == AddrData.STATUS_DEFAULT) {
                AddrData updateAddrData = new AddrData();
                updateAddrData.setUserId(userId);
                updateAddrData.setStatus(AddrData.STATUS_ON);
                addrDataService.updateAddrData(updateAddrData);
            }
            boolean flag = addrDataService.updateAddrData(addrData);
            if (flag) {
                return ResultUtil.builderSuccessResult(addrData, "成功");
            }
            return ResultUtil.builderErrorResult("", "失败");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.builderErrorResult("", "添加失败");
    }

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "addressOne.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String delAddress(@RequestParam(value = "addrId") Long addrId) {
        AddrData addrData = addrDataService.getById(addrId);
        addrData.setUserId(0L);
        return ResultUtil.builderSuccessResult(addrData, "成功");
    }

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "addForm.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String addForm(HttpServletRequest request,@RequestParam(value = "formId") String formId) {
        Long userId = (Long) request.getAttribute("userId");
        UserData userData = userDataService.findUserDataById(userId);
        if (userData == null) {
            return ResultUtil.builderErrorResult("null", "用户不存在");
        }
        userFormService.addUserFormId(userId,formId);
        return ResultUtil.builderSuccessResult(null, "成功");
    }



    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "addAddress.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String addAddress(HttpServletRequest request, @RequestParam("direction") String direction,
                             @RequestParam("recipient") String recipient, @RequestParam("area") String area,
                             @RequestParam("province") String province, @RequestParam("city") String city,
                             @RequestParam("phone") String phone, @RequestParam(value = "post_code", required = false) String
                                     post_code,
                             @RequestParam(value = "status", required = false) Integer status) {
        Long userId = (Long) request.getAttribute("userId");
        UserData userData = userDataService.findUserDataById(userId);
        if (userData == null) {
            return ResultUtil.builderErrorResult("null", "用户不存在");
        }
        try {
            AddrData addrData = new AddrData();
            addrData.setUserId(userId);
            addrData.setArea(area);
            addrData.setDirection(direction);
            addrData.setInsertTime(new Date());
            addrData.setPhone(phone);
            addrData.setRecipient(recipient);
            addrData.setPostCode(post_code);
            addrData.setProvice(province);
            addrData.setCity(city);
            if (status != null) {
                addrData.setStatus(status);
                if (status == AddrData.STATUS_DEFAULT) {
                    AddrData updateAddrData = new AddrData();
                    updateAddrData.setUserId(userId);
                    updateAddrData.setStatus(AddrData.STATUS_ON);
                    addrDataService.updateAddrData(updateAddrData);
                }
            } else {
                addrData.setStatus(AddrData.STATUS_ON);
            }
            addrData = addrDataService.addAddrData(addrData);
            return ResultUtil.builderSuccessResult(addrData, "成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.builderErrorResult("", "添加失败");
    }



    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "subSign.do", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String subSing(HttpServletRequest request,@RequestParam("subFlag")Integer flag){
        Long userId = (Long) request.getAttribute("userId");
        UserData userData=userDataService.findUserDataById(userId);
        if (userData==null){
            return ResultUtil.builderErrorResult("","用户错误");
        }
        try {
            userData.setSubFlag(flag);
            userDataService.updateUserDataByOpenId(userData);
            return ResultUtil.builderSuccessResult("","成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.builderErrorResult("","失败");
        }

    }


    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "wxCode.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
    public String wxCode(@RequestParam(value = "scene") String scene,
                         @RequestParam(value = "page")String page,
                         @RequestParam(value = "width")Integer width,
                         @RequestParam(value = "codeKey")String key) {
        return weChatService.getWxACode(scene,page,width,key);
    }

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "wxHeadImage.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
    public String wxHeadImage(HttpServletRequest request,@RequestParam(value = "codeKey")String key){

        Long userId = (Long) request.getAttribute("userId");
        UserData userData=userDataService.findUserDataById(userId);
        if(userData==null || userData.getHeadImg()==null || "".equals(userData.getHeadImg())){
            return ResultUtil.builderSuccessResult("wxmini/avatar/bk_logo.png","成功");
        }
        String path = "wxmini/avatar/" + key + ".png";
        InputStream inputStream =null;
        try {
            CloudStorageService aliyunSto = OssFactory.build();
            if(aliyunSto.isHaveFile(path)){
                return ResultUtil.builderSuccessResult(path,"成功");
            }
            inputStream = new URL(userData.getHeadImg()).openStream();
            String result = aliyunSto.upload(inputStream,path);
            return ResultUtil.builderSuccessResult(result,"成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.builderErrorResult("","失败");
        }finally {
            try {
                if(inputStream !=null) {
                    inputStream.close();
                }
            }catch (Exception e1){
            }
        }
    }

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "openApp.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
    public String openApp(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserData userData=userDataService.findUserDataById(userId);
        if(userData !=null) {
            userData.setLoginTime(new Date());
            userDataService.updateById(userData);
        }
        return ResultUtil.builderSuccessResult(userData.getId(),"成功");
    }



    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "appShareMeterial.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
    public String appShareMeterial(HttpServletRequest request){
        List<AppShareMaterial> appShareMaterials = appShareMaterialService.findShareMaterialsByStatus(AppShareMaterial.STATUS_ON);
        return ResultUtil.builderSuccessResult(appShareMaterials,"成功");
    }

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "sendMobileCode.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
    public String sendMobileCode(HttpServletRequest request,@RequestParam("mobileNum") String mobileNum){
        Long  userId = (Long) request.getAttribute("userId");
        UserData userData = userDataService.findUserDataById(userId);
        if(userData.getMobileNum() != null && "".equals(userData.getMobileNum())){
            return ResultUtil.builderErrorResult(null,"已帮过手机号");
        }
        Boolean aBoolean = false;
        logger.info(String.format("send mobile code mobileNum=%s,userId=%s",mobileNum,userId));
        if(mobileNum.length()!=11){
            return ResultUtil.builderErrorResult(null,"手机号不正确");
        }
        if(userDataService.mobileNumIsExist(mobileNum)){
            return ResultUtil.builderErrorResult(null,"手机号已经被占用");
        }
        try {
            String scode = getCode();
            boolean codeRedis = userDataService.insertMobileCode(userId,mobileNum,scode);
            if (!codeRedis) {
                return ResultUtil.builderErrorResult(null,"不要重复获取验证码，有效期3分钟！");
            }

            aBoolean = MDSendMobileCode.sendMobileCode(mobileNum, scode);
            if (!aBoolean) {
                userDataService.deleteMobileCode(userId,mobileNum);
                return ResultUtil.builderErrorResult(null,"发送失败，请求重试");
            }
            logger.info("SendMobileCode{mobileNum:" + mobileNum + ",Code:" + scode + "}");
            return ResultUtil.builderSuccessResult(null,"发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.builderErrorResult(null,"发送失败，请求重试");
        }
    }

    private String getCode() {
        Integer code = (int) ((Math.random() * 9 + 1) * 100000);
        return code.toString();
    }

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "bingMobile.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
    public String bingMobile(HttpServletRequest request,@RequestParam("mobileNum") String mobileNum,@RequestParam("code") String code) {
        Long  userId = (Long) request.getAttribute("userId");
        if(userDataService.mobileNumIsExist(mobileNum)){
            return ResultUtil.builderErrorResult(null,"手机号已经被占用");
        }
        String redisCode = userDataService.findMobileCode(userId,mobileNum);
        if(redisCode !=null && !"".equals(redisCode.trim()) && redisCode.equals(code)){
            if(userDataService.bingMobile(userId,mobileNum)){
                return ResultUtil.builderSuccessResult(mobileNum,"成功");
            }
        }
        return ResultUtil.builderErrorResult(null,"绑定失败");
    }

    @ResponseBody
    @CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
    @RequestMapping(value = "wxBingMobile.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
    public String wxBingMobile(HttpServletRequest request,@RequestParam("signature") String signatrue,
                               @RequestParam("code") String code, @RequestParam("iv") String iv) {
        Long  userId = (Long) request.getAttribute("userId");
        String mobileNum = null;
        try {
            logger.info(String.format("bingMobileNum signature=%s,code=%s,iv=%s",signatrue,code,iv));
            String deData = weChatService.decryptData(signatrue,iv,code);
            logger.info("bingMobileNum ==="+deData);
            mobileNum = JSON.parseObject(deData).getString("purePhoneNumber");
            logger.info(String.format("bingMobileNum userId=%s,mobile=%s",userId,mobileNum));
            if(mobileNum == null || "".equals(mobileNum)){
                return ResultUtil.builderErrorResult(null,"绑定失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.builderErrorResult(null,"没有获取到手机号");
        }
        if(userDataService.mobileNumIsExist(mobileNum)){
            return ResultUtil.builderErrorResult(null,"手机号已经被占用");
        }
        try {
            UserData userData = userDataService.findUserDataById(userId);
            if (userData != null) {
                userData.setMobileNum(mobileNum);
                userDataService.updateById(userData);
                return ResultUtil.builderSuccessResult(mobileNum, "成功");
            }
//            if (userDataService.bingMobile(userId, mobileNum)) {
//                logger.info("bingMobile true");

//            }
        }catch (Exception e){
            logger.info("bingMobile exception");
            e.printStackTrace();
            return ResultUtil.builderErrorResult(null,"绑定失败");
        }
        return ResultUtil.builderErrorResult(null,"绑定失败");
    }
}
