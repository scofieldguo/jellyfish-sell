package com.jellyfish.sell.common.api.service;

public interface IUserFormService {

    public static final String USER_FORM_KEY="user_form_key_";

    /**
     * 添加用户FORMID
     * @param userId
     * @param formId
     */
    void addUserFormId(Long userId, String formId);


    /**
     * 获取用户formId
     * @param userId
     * @return
     */
    String findUserFormId(Long userId);
}
