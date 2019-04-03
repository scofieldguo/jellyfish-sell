package com.jellyfish.sell.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jellyfish.sell.user.entity.WechatUser;
import com.jellyfish.sell.user.mapper.WechatUserMapper;
import com.jellyfish.sell.user.service.IWechatUserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("wechatUserService")
public class WechatUserServiceImpl extends ServiceImpl<WechatUserMapper,WechatUser> implements IWechatUserService {
	
	@Override
	public WechatUser findByUnionID(String unionID) {
		QueryWrapper<WechatUser> ew = new QueryWrapper<WechatUser>();
		ew.eq("union_id", unionID);
		List<WechatUser> wechatUsers = baseMapper.selectList(ew);
		if(wechatUsers!=null&&wechatUsers.size()>0){
			return wechatUsers.get(0);
		}
		return null;
	}

	@Override
	public WechatUser findByOpenID(String openID) {
		return baseMapper.selectById(openID);
	}

	@Override
	public void insertWeChatUser(WechatUser user) {
		baseMapper.insert(user);
	}

	@Override
	public void updateWeChatUser(WechatUser user) {
		baseMapper.updateById(user);
	}

}
