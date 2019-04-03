package com.jellyfish.sell.support.sms;

import com.jellyfish.sell.support.HttpClientUtils;
import com.jellyfish.sell.support.xml.XmlBeanUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class MDSendMobileCode {
    private static final Logger logger = LoggerFactory.getLogger(MDSendMobileCode.class);
    private static final String URL = "http://47.92.166.212:8088/sms.aspx?userid=43";
    private static final String CONTENT_PREFIX = "【指得】尊敬的客户：您的验证码是";
    private static final String CONTENT_SUFFIX = "，三分钟内有效。";

    public static void main(String[] args) {
        sendMobileCode("18868821772", "454524");
    }


    public static Boolean sendMobileCode(String mobileNumber, String code) {
        try {
            String sendMsgUrl = getSendMsgUrl(mobileNumber, code);

            CloseableHttpClient httpClient = HttpClientUtils.getConnection(HttpClientUtils.getPoolingHttpClientConnectionManager(200, 200), HttpClientUtils.getRequestConfig(1000, 1000, 1000, false));
            String result = HttpClientUtils.httpPost(sendMsgUrl,null,httpClient);
            System.out.println(result);
            Returnsms returnsms = XmlBeanUtil.XMLToBean(result, Returnsms.class);
            if (returnsms == null) {
                logger.info("sendMobileCode returnsms is error====" + returnsms);
                return false;
            }
            String message = returnsms.getReturnstatus();
            if ("Success".equals(message)) {
                return true;
            }
            logger.info("SendMobileCode{mobileNum: " + mobileNumber + " is error !! code: " + code + " message: " + returnsms.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getSendMsgUrl(String mobileNumber, String code) {
        StringBuffer sb = new StringBuffer();
        sb.append(URL).append("&mobile=").append(mobileNumber).append("&account=jxlb01&password=a1234567&content=")
                .append(CONTENT_PREFIX).append(code).append(CONTENT_SUFFIX).append("&sendTime&action=send&extno");
        return sb.toString();
    }


    @XmlRootElement(name = "returnsms")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Returnsms {
        @XmlElement(name = "returnstatus")
        private String returnstatus;
        @XmlElement(name = "message")
        private String message;
        @XmlElement(name = "remainpoint")
        private String remainpoint;
        @XmlElement(name = "taskID")
        private String taskID;
        @XmlElement(name = "successCounts")
        private String successCounts;

        public String getReturnstatus() {
            return returnstatus;
        }

        public void setReturnstatus(String returnstatus) {
            this.returnstatus = returnstatus;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRemainpoint() {
            return remainpoint;
        }

        public void setRemainpoint(String remainpoint) {
            this.remainpoint = remainpoint;
        }

        public String getTaskID() {
            return taskID;
        }

        public void setTaskID(String taskID) {
            this.taskID = taskID;
        }

        public String getSuccessCounts() {
            return successCounts;
        }

        public void setSuccessCounts(String successCounts) {
            this.successCounts = successCounts;
        }
    }

}
