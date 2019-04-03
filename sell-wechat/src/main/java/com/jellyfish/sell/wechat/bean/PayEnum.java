package com.jellyfish.sell.wechat.bean;

public class PayEnum {
    public enum CANPAY {
        CLOSED("CLOSED", "已关闭"), REVOKED("REVOKED", "已撤销"), PAYERROR("PAYERROR", "支付失败"),REFUND("REFUND","转入退款"),NOTPAY("NOTPAY", "未支付");
        private String message;
        private String desc;

        CANPAY(String name, String desc) {
            this.message = name;
            this.desc = desc;
        }

        public static boolean iscan(String message) {
            for (CANPAY s : CANPAY.values()) {
                if (s.message.equals(message)) {
                    return true;
                }
            }
            return false;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public enum NOTPAY {
        USERPAYING("USERPAYING", "用户支付中");
        private String message;
        private String desc;

        NOTPAY(String name, String desc) {
            this.message = name;
            this.desc = desc;
        }

        public static boolean iscan(String message) {
            for (NOTPAY s : NOTPAY.values()) {
                if (s.message.equals(message)) {
                    return true;
                }
            }
            return false;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
