package com.java110.core.factory;

import com.java110.dto.community.CommunitySettingDto;
import com.java110.intf.community.ICommunitySettingInnerServiceSMO;
import com.java110.po.communitySetting.CommunitySettingPo;
import com.java110.utils.cache.BaseCache;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.SerializeUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import com.java110.utils.cache.Jedis;

import java.util.List;

public class CommunitySettingFactory extends BaseCache {

    //日志
    private static Logger logger = LoggerFactory.getLogger(CommunitySettingFactory.class);

    public static final String KEY_FEE_SCALE = "SCALE";
    public static final int DEFAULE_FEE_SCALE = 2;


    /**
     * 查询设置值
     *
     * @param communityId
     * @param key
     * @return
     */
    public static String getValue(String communityId, String key) {
        Jedis redis = null;
        CommunitySettingDto communitySettingDto = null;
        try {
            redis = getJedis();
            Object object = SerializeUtil.unserialize(redis.get((communityId + "_" + key + "_community_setting").getBytes()));
            if (object == null) {//这里存在并发问题，但是 等于查询了多次 然后多次写缓存，作者认为 这种应该比加全局锁效率高些
                communitySettingDto = getCommunitySettingFromDb(communityId, key, redis);
            } else {
                communitySettingDto = (CommunitySettingDto) object;
            }
        } finally {
            if (redis != null) {
                redis.close();
            }
        }

        if (communitySettingDto == null) {
            return null;
        }

        return communitySettingDto.getSettingValue();
    }

    /**
     * 查询设置值
     *
     * @param communityId
     * @param key
     * @return
     */
    public static String getRemark(String communityId, String key) {
        Jedis redis = null;
        CommunitySettingDto communitySettingDto = null;
        try {
            redis = getJedis();
            Object object = SerializeUtil.unserialize(redis.get((communityId + "_" + key + "_community_setting").getBytes()));
            if (object == null) {//这里存在并发问题，但是 等于查询了多次 然后多次写缓存，作者认为 这种应该比加全局锁效率高些
                communitySettingDto = getCommunitySettingFromDb(communityId, key, redis);
            } else {
                communitySettingDto = (CommunitySettingDto) object;
            }
        } finally {
            if (redis != null) {
                redis.close();
            }
        }

        if (communitySettingDto == null) {
            return null;
        }

        return communitySettingDto.getRemark();
    }

    public static CommunitySettingDto getCommunitySettingFromDb(String communityId, String key) {
        Jedis redis = null;
        try {
            redis = getJedis();
            return getCommunitySettingFromDb(communityId, key, redis);
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    private static CommunitySettingDto getCommunitySettingFromDb(String communityId, String key, Jedis redis) {
        ICommunitySettingInnerServiceSMO communitySettingInnerServiceSMOImpl = null;
        try {
            communitySettingInnerServiceSMOImpl = ApplicationContextFactory.getBean(ICommunitySettingInnerServiceSMO.class.getName(), ICommunitySettingInnerServiceSMO.class);
        } catch (NoSuchBeanDefinitionException e) {
            communitySettingInnerServiceSMOImpl = ApplicationContextFactory.getBean("communitySettingInnerServiceSMOImpl", ICommunitySettingInnerServiceSMO.class);
        }
        CommunitySettingDto communitySettingDto = new CommunitySettingDto();
        communitySettingDto.setCommunityId(communityId);
        communitySettingDto.setSettingKey(key);
        List<CommunitySettingDto> communitySettingDtos = communitySettingInnerServiceSMOImpl.queryCommunitySettings(communitySettingDto);
        if (communitySettingDtos == null || communitySettingDtos.size() < 1) {
            return null;
        }

        redis.set((communityId + "_" + key + "_community_setting").getBytes(), SerializeUtil.serialize(communitySettingDtos.get(0)));
        return communitySettingDtos.get(0);
    }

    /**
     * 手工保存数据
     *
     * @param communitySettingDto
     */
    public static void saveCommunitySetting(CommunitySettingDto communitySettingDto) {
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.set((communitySettingDto.getCommunityId() + "_" + communitySettingDto.getSettingKey() + "_community_setting").getBytes(), SerializeUtil.serialize(communitySettingDto));
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 手工保存数据
     *
     * @param communitySettingDto
     */
    public static void deleteCommunitySetting(CommunitySettingPo communitySettingPo) {
        Jedis redis = null;
        try {
            redis = getJedis();
            redis.del((communitySettingPo.getCommunityId() + "_" + communitySettingPo.getSettingKey() + "_community_setting").getBytes());
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * 查询设置值
     *
     * @param communityId
     * @param key
     * @return
     */
    public static CommunitySettingDto getCommunitySetting(String communityId, String key) {
        Jedis redis = null;
        CommunitySettingDto communitySettingDto = null;
        try {
            redis = getJedis();
            Object object = SerializeUtil.unserialize(redis.get((communityId + "_" + key + "_community_setting").getBytes()));
            if (object == null) { //这里存在并发问题，但是 等于查询了多次 然后多次写缓存，作者认为 这种应该比加全局锁效率高些
                communitySettingDto = getCommunitySettingFromDb(communityId, key, redis);
            } else {
                communitySettingDto = (CommunitySettingDto) object;
            }
        } finally {
            if (redis != null) {
                redis.close();
            }
        }

        if (communitySettingDto == null) {
            return null;
        }

        return communitySettingDto;
    }

    /**
     * 查询小数点 位数
     *
     * @param communityId
     * @return
     */
    public static int getFeeScale(String communityId) {
        String scale = getValue(communityId, KEY_FEE_SCALE);
        if (StringUtil.isEmpty(scale)) {
            //防止每次都需要 查询数据库 增加 压力，这里像缓存中写入默认值
            CommunitySettingDto communitySettingDto = new CommunitySettingDto();
            communitySettingDto.setCommunityId(communityId);
            communitySettingDto.setSettingKey(KEY_FEE_SCALE);
            communitySettingDto.setSettingName("小数点位数");
            communitySettingDto.setCsId("-1");
            communitySettingDto.setSettingValue(DEFAULE_FEE_SCALE + "");
            communitySettingDto.setSettingType(CommunitySettingDto.SETTING_TYPE_FEE);
            communitySettingDto.setRemark("费用计算小数点位数,0至4整数");
            saveCommunitySetting(communitySettingDto);
            return DEFAULE_FEE_SCALE;
        }

        if (!StringUtil.isInteger(scale)) {
            return DEFAULE_FEE_SCALE;
        }

        int scaleInt = Integer.parseInt(scale);
        if (scaleInt > 4 || scaleInt < 0) {
            return DEFAULE_FEE_SCALE;
        }
        return scaleInt;
    }
}
