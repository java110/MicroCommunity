package com.java110.store.dao;

import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 商户组件内部之间使用，没有给外围系统提供服务能力
 * 商户服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IStoreServiceDao {

    /**
     * 保存 商户信息
     *
     * @param businessStoreInfo 商户信息 封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessStoreInfo(Map businessStoreInfo) throws DAOException;

    /**
     * 保存商户属性
     *
     * @param businessStoreAttr 商户属性信息封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessStoreAttr(Map businessStoreAttr) throws DAOException;


    /**
     * 保存商户照片信息
     *
     * @param businessStorePhoto 商户照片
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessStorePhoto(Map businessStorePhoto) throws DAOException;

    /**
     * 保存商户证件信息
     *
     * @param businessStoreCerdentials 商户证件
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessStoreCerdentials(Map businessStoreCerdentials) throws DAOException;

    /**
     * 查询商户信息（business过程）
     * 根据bId 查询商户信息
     *
     * @param info bId 信息
     * @return 商户信息
     * @throws DAOException
     */
    public Map getBusinessStoreInfo(Map info) throws DAOException;

    /**
     * 查询商户属性信息（business过程）
     *
     * @param info bId 信息
     * @return 商户属性
     * @throws DAOException
     */
    public List<Map> getBusinessStoreAttrs(Map info) throws DAOException;

    /**
     * 查询商户照片
     *
     * @param info bId 信息
     * @return 商户照片
     * @throws DAOException
     */
    public List<Map> getBusinessStorePhoto(Map info) throws DAOException;

    /**
     * 查询商户证件信息
     *
     * @param info bId 信息
     * @return 商户照片
     * @throws DAOException
     */
    public List<Map> getBusinessStoreCerdentials(Map info) throws DAOException;

    /**
     * 保存 商户信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException
     */
    public void saveStoreInfoInstance(Map info) throws DAOException;

    /**
     * 保存 商户属性信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException
     */
    public void saveStoreAttrsInstance(Map info) throws DAOException;

    /**
     * 保存 商户照片信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException
     */
    public void saveStorePhotoInstance(Map info) throws DAOException;

    /**
     * 保存 商户证件信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException
     */
    public void saveStoreCerdentialsInstance(Map info) throws DAOException;

    /**
     * 查询商户信息（instance过程）
     * 根据bId 查询商户信息
     *
     * @param info bId 信息
     * @return 商户信息
     * @throws DAOException
     */
    public Map getStoreInfo(Map info) throws DAOException;

    /**
     * 查询商户属性信息（instance过程）
     *
     * @param info bId 信息
     * @return 商户属性
     * @throws DAOException
     */
    public List<Map> getStoreAttrs(Map info) throws DAOException;

    /**
     * 查询商户照片（instance 过程）
     *
     * @param info bId 信息
     * @return 商户照片
     * @throws DAOException
     */
    public List<Map> getStorePhoto(Map info) throws DAOException;

    /**
     * 查询商户证件信息（instance 过程）
     *
     * @param info bId 信息
     * @return 商户照片
     * @throws DAOException
     */
    public List<Map> getStoreCerdentials(Map info) throws DAOException;

    /**
     * 修改商户信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateStoreInfoInstance(Map info) throws DAOException;

    /**
     * 修改商户属性信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateStoreAttrInstance(Map info) throws DAOException;

    /**
     * 修改商户照片信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateStorePhotoInstance(Map info) throws DAOException;

    /**
     * 修改商户证件信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateStoreCerdentailsInstance(Map info) throws DAOException;

    /**
     * 商户成员加入信息
     *
     * @param businessMemberStore 商户成员信息 封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessMemberStore(Map businessMemberStore) throws DAOException;

    /**
     * 成员加入 保存信息至instance
     *
     * @param info
     * @throws DAOException
     */
    public void saveMemberStoreInstance(Map info) throws DAOException;

    /**
     * 查询商户成员加入信息（business过程）
     * 根据bId 查询商户信息
     *
     * @param info bId 信息
     * @return 商户信息
     * @throws DAOException
     */
    public Map getBusinessMemberStore(Map info) throws DAOException;

    /**
     * 查询商户成员加入信息（instance过程）
     * 根据bId 查询商户信息
     *
     * @param info bId 信息
     * @return 商户信息
     * @throws DAOException
     */
    public Map getMemberStore(Map info) throws DAOException;

    /**
     * 修改商户成员加入信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateMemberStoreInstance(Map info) throws DAOException;

    /**
     * 保存商户用户信息
     *
     * @param info
     * @throws DAOException
     */
    public void saveBusinessStoreUser(Map info) throws DAOException;

    /**
     * 查询物业用户信息
     *
     * @param info bId 信息
     * @return 物业照片
     * @throws DAOException
     */
    public List<Map> getBusinessStoreUser(Map info) throws DAOException;

    /**
     * 保存 物业用户信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException
     */
    public void saveStoreUserInstance(Map info) throws DAOException;

    /**
     * 查询物业用户信息（instance 过程）
     *
     * @param info bId 信息
     * @return 物业照片
     * @throws DAOException
     */
    public List<Map> getStoreUser(Map info) throws DAOException;

    /**
     * 修改物业用户信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    public void updateStoreUserInstance(Map info) throws DAOException;

    /**
     * 查询商户信息（instance过程）
     * 根据bId 查询商户信息
     *
     * @param info bId 信息
     * @return 商户信息
     * @throws DAOException
     */
    public List<Map> getStores(Map info) throws DAOException;

    public int getStoreCount(Map info) throws DAOException;

    /**
     * 查询员工和员工所属商户信息
     *
     * @param info
     * @return
     * @throws DAOException
     */
    public List<Map> getStoreUserInfo(Map info) throws DAOException;

    /**
     * 查询商户员工
     *
     * @param beanCovertMap
     * @return
     */
    List<Map> getStoreStaffs(Map beanCovertMap) throws DAOException;

    /**
     * 查询商户员工数
     *
     * @param beanCovertMap
     * @return
     */
    int getStoreStaffCount(Map beanCovertMap) throws DAOException;

    /**
     * 修改商户信息
     *
     * @param info
     * @return
     */
    int updateStore(Map info) throws DAOException;
}