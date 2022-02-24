package com.java110.acct.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.dao.IShopVipAccountDetailServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 会员账户交易服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("shopVipAccountDetailServiceDaoImpl")
//@Transactional
public class ShopVipAccountDetailServiceDaoImpl extends BaseServiceDao implements IShopVipAccountDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ShopVipAccountDetailServiceDaoImpl.class);


    /**
     * 保存会员账户交易信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveShopVipAccountDetailInfo(Map info) throws DAOException {
        logger.debug("保存会员账户交易信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("shopVipAccountDetailServiceDaoImpl.saveShopVipAccountDetailInfo", info);

        return saveFlag;
    }


    /**
     * 查询会员账户交易信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getShopVipAccountDetailInfo(Map info) throws DAOException {
        logger.debug("查询会员账户交易信息 入参 info : {}", info);

        List<Map> businessShopVipAccountDetailInfos = sqlSessionTemplate.selectList("shopVipAccountDetailServiceDaoImpl.getShopVipAccountDetailInfo", info);

        return businessShopVipAccountDetailInfos;
    }


    /**
     * 修改会员账户交易信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateShopVipAccountDetailInfo(Map info) throws DAOException {
        logger.debug("修改会员账户交易信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("shopVipAccountDetailServiceDaoImpl.updateShopVipAccountDetailInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改会员账户交易信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询会员账户交易数量
     *
     * @param info 会员账户交易信息
     * @return 会员账户交易数量
     */
    @Override
    public int queryShopVipAccountDetailsCount(Map info) {
        logger.debug("查询会员账户交易数据 入参 info : {}", info);

        List<Map> businessShopVipAccountDetailInfos = sqlSessionTemplate.selectList("shopVipAccountDetailServiceDaoImpl.queryShopVipAccountDetailsCount", info);
        if (businessShopVipAccountDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessShopVipAccountDetailInfos.get(0).get("count").toString());
    }


}
