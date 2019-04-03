package com.jellyfish.sell.wechat.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendCustomerMessage implements Serializable {

    public enum  MsgEnum{
        text,image,link,miniprogrampage;
    }

    private String touser;
    private String msgtype;
    private String content;
    private Image image;
    private Link link;
    private Miniprogrampage miniprogrampage;

    public SendCustomerMessage(Builder builder){
        this.touser =builder.touser;
        this.content = builder.content;
        this.image = builder.image;
        this.link = builder.link;
        this.msgtype =builder.msgtype;
        this.miniprogrampage = builder.miniprogrampage;
    }

    public SendCustomerMessage(){

    }

    public static class Builder{
        private String touser;
        private String msgtype;
        private String content;
        private Image image;
        private Link link;
        private Miniprogrampage miniprogrampage;

        public Builder buildContentMessage(String touser,String content){
            this.content = content;
            this.touser = touser;
            this.msgtype = MsgEnum.text.name();
            return this;
        }

        public Builder buildImageMessage(String touser,Image image){
            this.image = image;
            this.touser = touser;
            this.msgtype = MsgEnum.image.name();
            return this;
        }

        public Builder buildLinkMessage(String touser,Link link){
            this.link = link;
            this.touser = touser;
            this.msgtype = MsgEnum.link.name();
            return this;
        }

        public Builder buildMiniprogrampageMessage(String touser,Miniprogrampage miniprogrampage){
            this.miniprogrampage = miniprogrampage;
            this.touser = touser;
            this.msgtype = MsgEnum.miniprogrampage.name();
            return this;
        }

        public SendCustomerMessage build(){
          return  new SendCustomerMessage(this);
        }
    }
}
