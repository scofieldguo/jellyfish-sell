package com.jellyfish.sell.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.user.entity.UserData;

import java.util.List;
import java.util.Map;

public interface IUserDataService extends IService<UserData> {

    public static final String MOBILECODE_KEY = "mobileCode_key_";
    public static final String BINGMOBILE = "bingMobile_";


    /**
     * 增加新用户
     *
     * @param userData
     * @return
     */
    UserData addUserData(UserData userData);

    /**
     * 根据openId查找用户
     *
     * @param openId
     * @return
     */
    UserData findUserDataByOpenId(String openId);


    /**
     * 根据用户openId更新用户
     *
     * @param userData
     */
    void updateUserDataByOpenId(UserData userData);

    UserData findUserDataById(Long id);


    /**
     * 分页查询会员列表
     *
     * @param page
     * @param params
     * @return
     */
    IPage pageList(IPage page, Map<String, Object> params);

    /**
     * 批量更新用户状态
     *
     * @param
     */
    boolean upStatustById(UserData userDataEntity);


    /**
     * 查找用户根据unionId
     *
     * @param unionId
     * @return
     */
    UserData findUserDataByUnionId(String unionId);

    IPage<UserData> findSubSignUsers(IPage<UserData> page);

    IPage<UserData> findCallBackUsersByMasterId(Long masterId, IPage<UserData> page);

    Integer countCallbackPerson(Long masterId);

    Integer selCountChannel(Map<String, Object> params);

    Boolean bingMobile(Long userId, String mobileNum);

    Boolean mobileNumIsExist(String moblieNum);

    Boolean insertMobileCode(Long userId, String mobile, String code);

    Boolean deleteMobileCode(Long userId, String mobile);

    Boolean userIsBingMobile(Long userId);

    String findMobileCode(Long userId, String mobile);

    Integer userPrenticeNum(Long masterId);
}
