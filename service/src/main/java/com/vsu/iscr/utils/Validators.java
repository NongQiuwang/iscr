package com.vsu.iscr.utils;

import java.util.regex.Pattern;

/**
 * 通用格式校验工具
 */
public final class Validators {

    private Validators() {
    }

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^1[3-9]\\d{9}$");

    /**
     * 是否为空字符串（null 或去除首尾空格后为空）
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * 是否为合法邮箱
     */
    public static boolean isEmail(String value) {
        return value != null && EMAIL_PATTERN.matcher(value.trim()).matches();
    }

    /**
     * 是否为合法手机号（中国大陆 11 位）
     */
    public static boolean isPhone(String value) {
        return value != null && PHONE_PATTERN.matcher(value.trim()).matches();
    }
}
