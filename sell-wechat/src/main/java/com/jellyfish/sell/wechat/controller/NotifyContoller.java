package com.jellyfish.sell.wechat.controller;

import com.jellyfish.sell.support.DateUtils;
import com.jellyfish.sell.support.Md5EncryptUtil;
import com.jellyfish.sell.support.wechat.proto.IWeChatService;
import com.jellyfish.sell.support.xml.XmlBeanUtil;
import com.jellyfish.sell.wechat.bean.NotifyBean;
import com.jellyfish.sell.wechat.bean.ReturnBean;
import com.jellyfish.sell.wechat.config.WxConfig;
import com.jellyfish.sell.wechat.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("lotto/wechat")
public class NotifyContoller {

    private static Logger orderLog= LoggerFactory.getLogger("orderlog");
    private static Logger logger= LoggerFactory.getLogger(NotifyContoller.class);

    @Autowired
    private IWeChatService weChatService;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private OrderService orderService;

    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";


    @RequestMapping("test.do")
    public void  test(){
        String out_trade_no = "jellyfish" + DateUtils.formatDate(new Date(), DateUtils.DatePattern.PATTERN_ALL_NOSPLIT_EXTENDS.getPattern());
        String nonceStr = UUID.randomUUID().toString().replace("-", "").trim();
        weChatService.getPrepayId(10.0,"O20181127180504472794","商品","oHDwv5f5zKFOj7JlYSJQLoDtI9to","192.168.1.44",out_trade_no,nonceStr);
    }

    @RequestMapping("notify.do")
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        ReturnBean returnBean = new ReturnBean();
        returnBean.setReturn_code("SUCCESS");
        PrintWriter out;
        try {
            int totalbytes = request.getContentLength();
            byte[] dataOrgin = new byte[totalbytes];
            DataInputStream in = new DataInputStream(request.getInputStream());
            in.readFully(dataOrgin);
            in.close();
            String reqContent = new String(dataOrgin);
            logger.info("wx notify request content="+reqContent);
            NotifyBean bean = XmlBeanUtil.XMLToBean(reqContent, NotifyBean.class);
            String out_trade_no = bean.getOut_trade_no();
                boolean flag = orderService.doHandleNotify(bean);
                if(!flag) {
                    returnBean.setReturn_code("FAIL");
                    returnBean.setReturn_msg("校验错误");
                }
        } catch (Exception e) {
            returnBean.setReturn_code("FAIL");
            returnBean.setReturn_msg("校验错误");
        }
        try {
            out = response.getWriter();
            out.write(XmlBeanUtil.BeanToXML(returnBean, ReturnBean.class));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("refundNotify.do")
    public void refundNotify(HttpServletRequest request,HttpServletResponse response){
//        ReturnBean returnBean = new ReturnBean();
//        returnBean.setReturn_code("SUCCESS");
//        returnBean.setReturn_msg("OK");
//        PrintWriter out;
//        try {
//            int totalbytes = request.getContentLength();
//            byte[] dataOrgin = new byte[totalbytes];
//            DataInputStream in = new DataInputStream(request.getInputStream());
//            in.readFully(dataOrgin);
//            in.close();
//            String reqContent = new String(dataOrgin);
//            logger.info("wx notify request content="+reqContent);
//            RefundNotifyBean bean = XmlBeanUtil.XMLToBean(reqContent, RefundNotifyBean.class);
//            Date now = new Date();
//            if (bean.getReturn_code().equals("SUCCESS")) {
//                String reqInfo=decodeReqInfo(bean.getReq_info());
//                RefundResultBean refundResultBean=XmlBeanUtil.XMLToBean(reqInfo,RefundResultBean.class,"root");
//                String refund_status=refundResultBean.getRefund_status();
//                RefundOrder oldRefundOrder=refundOrderService.findById(refundResultBean.getOut_refund_no());
//                RefundOrder refundOrder=new RefundOrder();
//                refundOrder.setNotifyTime(now);
//                refundOrder.setWxRefundId(refundResultBean.getRefund_id());
//                refundOrder.setRefundNo(refundResultBean.getOut_refund_no());
//                refundOrder.setUserId(oldRefundOrder.getUserId());
//                if (refund_status.equals("SUCCESS")){
//                    refundOrder.setStatus(RefundOrder.STATUS_SUCCESS);
//                }else{
//                    refundOrder.setStatus(RefundOrder.STATUS_FAIL);
//                }
//                refundOrder.setMsg(refund_status);
//                RefundOrderData refundOrderData = new RefundOrderData();
//                refundOrderData.setId(refundResultBean.getOut_refund_no().split("_")[1]);
//                refundOrderData.setUserId(oldRefundOrder.getUserId());
//                refundOrderData.setRefundTime(new Date());
//                boolean flag=refundOrderService.updateRefundOrder(refundOrder,refundOrderData);
//                System.out.println("update Order flag ="+flag);
//                if (!flag){
//                    returnBean.setReturn_code("FAIL");
//                    returnBean.setReturn_msg("校验错误");
//                }
//                sendTemplateService.sendRefundTemplate(refundResultBean.getOut_refund_no().split("_")[1]);
//                System.out.println("update send templat");
//            }
//        } catch (Exception e) {
//            returnBean.setReturn_code("FAIL");
//            returnBean.setReturn_msg("校验错误");
//        }
//        try {
//            System.out.println("response wechat success");
//            out = response.getWriter();
//            out.write(XmlBeanUtil.BeanToXML(returnBean, ReturnBean.class));
//            out.flush();
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @RequestMapping("notifyTest.do")
    public void notifyTest(@RequestParam("orderId") String orderId) {
    }

    @RequestMapping("refundNotifytest.do")
    public void refundNotify(){
    }

    private String decodeReqInfo(String reqInfo){
        try {
        SecretKeySpec key=new SecretKeySpec(Md5EncryptUtil.md5Enc(wxConfig.getMchKey(),"utf-8").toLowerCase().getBytes(),"AES");
        Cipher cipher=Cipher.getInstance(ALGORITHM_MODE_PADDING);
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] bytes=Base64.getDecoder().decode(reqInfo);
        return new String(cipher.doFinal(bytes));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
//        WxUtil.getAccessToken()

//        try {
//        String refundNo="jellyrefund_R201812261959031732114";
//            String nonceStr = UUID.randomUUID().toString().replaceAll("-","");
//            Map<String, Object> paramsMap = new HashMap<String, Object>();
//            paramsMap.put("mch_billno", "R201812261959031732114");
//            paramsMap.put("mch_id", "1524093801");
//            paramsMap.put("nonce_str",nonceStr);
//            paramsMap.put("wxappid","wx05975fb4d3b4dd09");
//            paramsMap.put("send_name","beibei");
//            paramsMap.put("re_openid", "oiP4r5GlMVl0PWjaU9HsbmNaEPP8");
//            paramsMap.put("total_amount", 1000);
//            paramsMap.put("total_num", 1);
//            paramsMap.put("wishing","thank");
//            paramsMap.put("client_ip","45.251.22.122");
//            paramsMap.put("act_name","choss");
//            paramsMap.put("remark","choss");
//            CashBean bean=new CashBean();
//
//            bean.setMch_billno("R201812261959031732114");
//            bean.setMch_id("1524093801");
//            bean.setNonce_str(nonceStr);
//            bean.setWxappid("wx05975fb4d3b4dd09");
//            bean.setSend_name("beibei");
//            bean.setRe_openid("oiP4r5GlMVl0PWjaU9HsbmNaEPP8");
//            bean.setTotal_amount(1000);
//            bean.setTotal_num(1);
//            bean.setWishing("thank");
//            bean.setClient_ip("45.251.22.122");
//            bean.setAct_name("choss");
//            bean.setRemark("choss");
//            bean.setSign(WXSignUtil.sign(paramsMap,"eb4c09247e19860901c02edce69f6a4f"));
//
//            String reqStr = XmlBeanUtil.BeanToXML(bean, CashBean.class);
//            logger.info(String.format("[WX]支付请求：%s", reqStr));
//            CloseableHttpClient httpClient = WXHttpClient.getHttpClient("D:\\apiclient_cert.p12","1524093801");
//            String url="https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
//            String result = WXHttpClient.post(httpClient,url, reqStr, "UTF-8");
//            System.out.println(result);
//        }catch (Exception e){
//        e.printStackTrace();}
//    }

        try {
            String a = "<xml><appid><![CDATA[wx16ced9ae9187ffe9]]></appid>\n" +
                    "<bank_type><![CDATA[SPDB_CREDIT]]></bank_type>\n" +
                    "<cash_fee><![CDATA[480]]></cash_fee>\n" +
                    "<coupon_count><![CDATA[1]]></coupon_count>\n" +
                    "<coupon_fee>500</coupon_fee>\n" +
                    "<coupon_fee_0><![CDATA[500]]></coupon_fee_0>\n" +
                    "<coupon_id_0><![CDATA[4818944612]]></coupon_id_0>\n" +
                    "<fee_type><![CDATA[CNY]]></fee_type>\n" +
                    "<is_subscribe><![CDATA[N]]></is_subscribe>\n" +
                    "<mch_id><![CDATA[1524093801]]></mch_id>\n" +
                    "<nonce_str><![CDATA[6e5925c1d432446b8f049916410ad70f]]></nonce_str>\n" +
                    "<openid><![CDATA[opyaQ4o40DVoC9m786k8LaSeqnko]]></openid>\n" +
                    "<out_trade_no><![CDATA[jellyfish20190209233708170]]></out_trade_no>\n" +
                    "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
                    "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "<sign><![CDATA[8D80451ABA737AE75A80BEF5FB6252D4]]></sign>\n" +
                    "<time_end><![CDATA[20190209233717]]></time_end>\n" +
                    "<total_fee>980</total_fee>\n" +
                    "<trade_type><![CDATA[JSAPI]]></trade_type>\n" +
                    "<transaction_id><![CDATA[4200000263201902090671802103]]></transaction_id>\n" +
                    "</xml>";
            NotifyBean bean = XmlBeanUtil.XMLToBean(a, NotifyBean.class);
            System.out.println(bean.getCoupon_fee_0());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
