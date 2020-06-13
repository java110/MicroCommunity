package com.java110.generator.dao;

import java.util.Map;

/**
 * 主键查询操作数据类
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IPrimaryKeyServiceDao {

    /**
     * 根据主键name查询主键生成ID
     * map 中必须包含name字段
     *
     * @param primaryKey 主键name信息
     * @return 返回map中为 targetId 字段
     */
    public Map queryPrimaryKey(Map primaryKey) throws RuntimeException;


}