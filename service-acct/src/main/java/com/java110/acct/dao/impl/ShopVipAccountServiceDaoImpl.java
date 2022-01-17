package com.java110.acct.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.dao.IShopVipAccountServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 会员账户服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("shopVipAccountServiceDaoImpl")
//@Transactional
public class ShopVipAccountServiceDaoImpl extends BaseServiceDao implements IShopVipAccountServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ShopVipAccountServiceDaoImpl.class);


    /**
     * 保存会员账户信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveShopVipAccountInfo(Map info) throws DAOException {
        logger.debug("保存会员账户信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("shopVipAccountServiceDaoImpl.saveShopVipAccountInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存会员账户信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询会员账户信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getShopVipAccountInfo(Map info) throws DAOException {
        logger.debug("查询会员账户信息 入参 info : {}", info);

        List<Map> businessShopVipAccountInfos = sqlSessionTemplate.selectList("shopVipAccountServiceDaoImpl.getShopVipAccountInfo", info);

        return businessShopVipAccountInfos;
    }


    /**
     * 修改会员账户信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateShopVipAccountInfo(Map info) throws DAOException {
        logger.debug("修改会员账户信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("shopVipAccountServiceDaoImpl.updateShopVipAccountInfo", info);

        return saveFlag;
    }

    /**
     * 查询会员账户数量
     *
     * @param info 会员账户信息
     * @return 会员账户数量
     */
    @Override
    public int queryShopVipAccountsCount(Map info) {
        logger.debug("查询会员账户数据 入参 info : {}", info);

        List<Map> businessShopVipAccountInfos = sqlSessionTemplate.selectList("shopVipAccountServiceDaoImpl.queryShopVipAccountsCount", info);
        if (businessShopVipAccountInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessShopVipAccountInfos.get(0).get("count").toString());
    }


}
