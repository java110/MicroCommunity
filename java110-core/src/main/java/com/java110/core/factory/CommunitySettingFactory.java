package com.java110.core.factory;

import com.java110.dto.communitySetting.CommunitySettingDto;
import com.java110.intf.community.ICommunitySettingInnerServiceSMO;
import com.java110.utils.cache.BaseCache;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;

public class CommunitySettingFactory extends BaseCache {

    //日志
    private static Logger logger = LoggerFactory.getLogger(CommunitySettingFactory.class);


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
        ICommunitySettingInnerServiceSMO communitySettingInnerServiceSMOImpl
                = ApplicationContextFactory.getBean(ICommunitySettingInnerServiceSMO.class.getName(), ICommunitySettingInnerServiceSMO.class);
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
}
