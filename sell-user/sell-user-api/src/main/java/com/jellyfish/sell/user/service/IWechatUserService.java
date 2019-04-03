package com.jellyfish.sell.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jellyfish.sell.user.entity.WechatUser;

public interface IWechatUserService extends IService<WechatUser> {
	
	void insertWeChatUser(WechatUser user);
	void updateWeChatUser(WechatUser user);
	WechatUser findByOpenID(String openID);
	WechatUser findByUnionID(String unionID);
}
