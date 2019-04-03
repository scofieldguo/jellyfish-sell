package com.jellyfish.sell.support.wechat.template;

import java.io.Serializable;

public class Data implements Serializable {
    private static final long serialVersionUID = 1L;

    private KeyWord keyword1;
    private KeyWord keyword2;
    private KeyWord keyword3;
    private KeyWord keyword4;
    private KeyWord keyword5;
    private KeyWord keyword6;



    public Data() {
    }

    public Data(Builder builder) {
        this.keyword1 = builder.keyword1;
        this.keyword2 = builder.keyword2;
        this.keyword3 = builder.keyword3;
        this.keyword4 = builder.keyword4;
        this.keyword5 = builder.keyword5;
        this.keyword6=builder.keyword6;
    }

    public KeyWord getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(KeyWord keyword1) {
        this.keyword1 = keyword1;
    }

    public KeyWord getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(KeyWord keyword2) {
        this.keyword2 = keyword2;
    }

    public KeyWord getKeyword3() {
        return keyword3;
    }

    public void setKeyword3(KeyWord keyword3) {
        this.keyword3 = keyword3;
    }

    public KeyWord getKeyword4() {
        return keyword4;
    }

    public void setKeyword4(KeyWord keyword4) {
        this.keyword4 = keyword4;
    }

    public KeyWord getKeyword5() {
        return keyword5;
    }

    public void setKeyword5(KeyWord keyword5) {
        this.keyword5 = keyword5;
    }

    public KeyWord getKeyword6() {
        return keyword6;
    }

    public void setKeyword6(KeyWord keyword6) {
        this.keyword6 = keyword6;
    }

    public static class Builder{
        private KeyWord keyword1;
        private KeyWord keyword2;
        private KeyWord keyword3;
        private KeyWord keyword4;
        private KeyWord keyword5;
        private KeyWord keyword6;



        public Builder setKeyword1(String value) {
            this.keyword1 = new KeyWord(value);
            return this;
        }

        public Builder setKeyword2(String value) {
            this.keyword2 =  new KeyWord(value);
            return this;
        }


        public Builder setKeyword3(String value) {
            this.keyword3 =  new KeyWord(value);
            return this;

        }

        public Builder setKeyword4(String value) {
            this.keyword4 =  new KeyWord(value);
            return this;
        }
        public Builder setKeyword5(String value) {
            this.keyword5 =  new KeyWord(value);
            return this;
        }
        public Builder setKeyword6(String value) {
            this.keyword6 =  new KeyWord(value);
            return this;
        }
        public Data build() {
            return new Data(this);
        }
    }
}
