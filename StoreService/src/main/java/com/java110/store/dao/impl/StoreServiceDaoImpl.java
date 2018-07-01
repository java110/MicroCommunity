package com.java110.store.dao.impl;

import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;
import com.java110.store.dao.IStoreServiceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商户服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("storeServiceDaoImpl")
@Transactional
public class StoreServiceDaoImpl extends BaseServiceDao implements IStoreServiceDao {



}
