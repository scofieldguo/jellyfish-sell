package com.jellyfish.sell.support.wechat.template;

import java.io.Serializable;

public class WeixinTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    private String touser;
    private String template_id;
    private String page;
    private String form_id;
    private Data data;
    private String emphasis_keyword;

    public WeixinTemplate() {
    }

    public WeixinTemplate(Builder builder) {
        this.touser = builder.touser;
        this.template_id = builder.template_id;
        this.page = builder.page;
        this.form_id = builder.form_id;
        this.data =builder.data;
        this.emphasis_keyword = builder.emphasis_keyword;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getEmphasis_keyword() {
        return emphasis_keyword;
    }

    public void setEmphasis_keyword(String emphasis_keyword) {
        this.emphasis_keyword = emphasis_keyword;
    }

    public static class Builder{
        private String touser;
        private String template_id;
        private String page;
        private String form_id;
        private Data data;
        private String emphasis_keyword;

        public Builder setTouser(String touser) {
            this.touser = touser;
            return this;
        }

        public Builder setTemplate_id(String template_id) {
            this.template_id = template_id;
            return this;
        }


        public Builder setPage(String page) {
            this.page = page;
            return this;
        }


        public Builder setForm_id(String form_id) {
            this.form_id = form_id;
            return this;
        }


        public Builder setData(Data data) {
            this.data = data;
            return this;
        }


        public Builder setEmphasis_keyword(String emphasis_keyword) {
            this.emphasis_keyword = emphasis_keyword;
            return this;
        }
        public WeixinTemplate build(){
            return new WeixinTemplate(this);
        }
    }
}
