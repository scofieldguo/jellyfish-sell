package com.jellyfish.sell.support.wechat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WXEmojiFilterUtil {

    private static final String PATTERN_EXPRESSION = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\\x{10000}-\\x{10FFFF}]";

    public static String filterEmoji(String source) {
        if (source == null) {
            return source;
        }
        Pattern emoji = Pattern.compile(PATTERN_EXPRESSION, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher emojiMatcher = emoji.matcher(source);
        if (emojiMatcher.find()) {
            source = emojiMatcher.replaceAll("*");
            return source;
        }
        return source;
    }

    public static void main(String[] args) {
        System.out.println(filterEmoji("🍂落叶知秋"));

    }
}
