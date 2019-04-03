package com.jellyfish.sell.support.wechat.template;

import java.io.Serializable;

public class KeyWord implements Serializable{
    private static final long serialVersionUID = 1L;

    public KeyWord() {
    }

    public KeyWord(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
