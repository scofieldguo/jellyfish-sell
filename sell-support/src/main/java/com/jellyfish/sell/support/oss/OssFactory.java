package com.jellyfish.sell.support.oss;

public class OssFactory {
    public static CloudStorageService build() {

        return new AliyunStorageService();
    }

}
