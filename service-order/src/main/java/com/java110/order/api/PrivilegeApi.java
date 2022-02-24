package com.java110.order.api;

import com.java110.core.base.controller.BaseController;
import com.java110.order.smo.IPrivilegeSMO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限API 处理类
 * Created by Administrator on 2019/4/1.
 */
@RestController
@RequestMapping(path = "/privilegeApi")
public class PrivilegeApi extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(PrivilegeApi.class);


    @Autowired
    private IPrivilegeSMO privilegeSMOImpl;

    @RequestMapping(path = "/saveUserDefaultPrivilege",method= RequestMethod.POST)
    @ApiOperation(value="添加用户默认权限", notes="test: 返回 200 表示服务受理成功，其他表示失败")
    @ApiImplicitParam(paramType="query", name = "privilegeInfo", value = "权限信息", required = true, dataType = "String")
    public ResponseEntity<String> saveUserDefaultPrivilege(@RequestBody String privilegeInfo, HttpServletRequest request){

        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = privilegeSMOImpl.saveUserDefaultPrivilege(privilegeInfo);
        }catch (Exception e){
            logger.error("请求订单异常",e);
            responseEntity =  new ResponseEntity<String>("请求中心服务发生异常，"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            logger.debug("订单服务返回报文为: {}",responseEntity);
        }
        return responseEntity;
    }

    @RequestMapping(path = "/deleteUserAllPrivilege",method= RequestMethod.POST)
    @ApiOperation(value="删除用户所有权限", notes="test: 返回 200 表示服务受理成功，其他表示失败")
    @ApiImplicitParam(paramType="query", name = "privilegeInfo", value = "权限信息", required = true, dataType = "String")
    public ResponseEntity<String> deleteUserAllPrivilege(@RequestBody String privilegeInfo,HttpServletRequest request){
        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = privilegeSMOImpl.deleteUserAllPrivilege(privilegeInfo);
        }catch (Exception e){
            logger.error("请求订单异常",e);
            responseEntity =  new ResponseEntity<String>("请求中心服务发生异常，"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            logger.debug("订单服务返回报文为: {}",responseEntity);
        }
        return responseEntity;
    }


    @RequestMapping(path = "/savePrivilegeGroup",method= RequestMethod.POST)
    @ApiOperation(value="保存权限组", notes="test: 返回 200 表示服务受理成功，其他表示失败")
    @ApiImplicitParam(paramType="query", name = "privilegeGroupInfo", value = "权限信息", required = true, dataType = "String")
    public ResponseEntity<String> savePrivilegeGroup(@RequestBody String privilegeGroupInfo,HttpServletRequest request){
        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = privilegeSMOImpl.savePrivilegeGroup(privilegeGroupInfo);
        }catch (Exception e){
            logger.error("请求订单异常",e);
            responseEntity =  new ResponseEntity<String>("请求中心服务发生异常，"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            logger.debug("订单服务返回报文为: {}",responseEntity);
        }
        return responseEntity;
    }

    @RequestMapping(path = "/editPrivilegeGroup",method= RequestMethod.POST)
    @ApiOperation(value="编辑权限组", notes="test: 返回 200 表示服务受理成功，其他表示失败")
    @ApiImplicitParam(paramType="query", name = "privilegeGroupInfo", value = "权限信息", required = true, dataType = "String")
    public ResponseEntity<String> editPrivilegeGroup(@RequestBody String privilegeGroupInfo,HttpServletRequest request){
        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = privilegeSMOImpl.editPrivilegeGroup(privilegeGroupInfo);
        }catch (Exception e){
            logger.error("请求订单异常",e);
            responseEntity =  new ResponseEntity<String>("请求中心服务发生异常，"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            logger.debug("订单服务返回报文为: {}",responseEntity);
        }
        return responseEntity;
    }

    @RequestMapping(path = "/deletePrivilegeGroup",method= RequestMethod.POST)
    @ApiOperation(value="删除权限组", notes="test: 返回 200 表示服务受理成功，其他表示失败")
    @ApiImplicitParam(paramType="query", name = "privilegeGroupInfo", value = "权限信息", required = true, dataType = "String")
    public ResponseEntity<String> deletePrivilegeGroup(@RequestBody String privilegeGroupInfo,HttpServletRequest request){
        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = privilegeSMOImpl.deletePrivilegeGroup(privilegeGroupInfo);
        }catch (Exception e){
            logger.error("请求订单异常",e);
            responseEntity =  new ResponseEntity<String>("请求中心服务发生异常，"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            logger.debug("订单服务返回报文为: {}",responseEntity);
        }
        return responseEntity;
    }

    @RequestMapping(path = "/addPrivilegeToPrivilegeGroup",method= RequestMethod.POST)
    @ApiOperation(value="添加权限到权限组", notes="test: 返回 200 表示服务受理成功，其他表示失败")
    @ApiImplicitParam(paramType="query", name = "privilegeInfo", value = "权限信息", required = true, dataType = "String")
    public ResponseEntity<String> addPrivilegeToPrivilegeGroup(@RequestBody String privilegeInfo,HttpServletRequest request){
        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = privilegeSMOImpl.addPrivilegeToPrivilegeGroup(privilegeInfo);
        }catch (Exception e){
            logger.error("请求订单异常",e);
            responseEntity =  new ResponseEntity<String>("请求中心服务发生异常，"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            logger.debug("订单服务返回报文为: {}",responseEntity);
        }
        return responseEntity;
    }


    @RequestMapping(path = "/deletePrivilegeFromPrivilegeGroup",method= RequestMethod.POST)
    @ApiOperation(value="从权限组删除权限", notes="test: 返回 200 表示服务受理成功，其他表示失败")
    @ApiImplicitParam(paramType="query", name = "privilegeInfo", value = "权限信息", required = true, dataType = "String")
    public ResponseEntity<String> deletePrivilegeFromPrivilegeGroup(@RequestBody String privilegeInfo,HttpServletRequest request){
        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = privilegeSMOImpl.deletePrivilegeToPrivilegeGroup(privilegeInfo);
        }catch (Exception e){
            logger.error("请求订单异常",e);
            responseEntity =  new ResponseEntity<String>("请求中心服务发生异常，"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            logger.debug("订单服务返回报文为: {}",responseEntity);
        }
        return responseEntity;
    }


    @RequestMapping(path = "/addStaffPrivilegeOrPrivilegeGroup",method= RequestMethod.POST)
    @ApiOperation(value="用户添加权限或权限组", notes="test: 返回 200 表示服务受理成功，其他表示失败")
    @ApiImplicitParam(paramType="query", name = "privilegeInfo", value = "权限信息", required = true, dataType = "String")
    public ResponseEntity<String> addStaffPrivilegeOrPrivilegeGroup(@RequestBody String privilegeInfo,HttpServletRequest request){
        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = privilegeSMOImpl.addStaffPrivilegeOrPrivilegeGroup(privilegeInfo);
        }catch (Exception e){
            logger.error("请求订单异常",e);
            responseEntity =  new ResponseEntity<String>("请求中心服务发生异常，"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            logger.debug("订单服务返回报文为: {}",responseEntity);
        }
        return responseEntity;
    }


    @RequestMapping(path = "/deleteStaffPrivilegeOrPrivilegeGroup",method= RequestMethod.POST)
    @ApiOperation(value="删除用户权限或权限组", notes="test: 返回 200 表示服务受理成功，其他表示失败")
    @ApiImplicitParam(paramType="query", name = "privilegeInfo", value = "权限信息", required = true, dataType = "String")
    public ResponseEntity<String> deleteStaffPrivilegeOrPrivilegeGroup(@RequestBody String privilegeInfo,HttpServletRequest request){
        ResponseEntity<String> responseEntity = null;

        try {
            responseEntity = privilegeSMOImpl.deleteStaffPrivilegeOrPrivilegeGroup(privilegeInfo);
        }catch (Exception e){
            logger.error("请求订单异常",e);
            responseEntity =  new ResponseEntity<String>("请求中心服务发生异常，"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            logger.debug("订单服务返回报文为: {}",responseEntity);
        }
        return responseEntity;
    }



    public IPrivilegeSMO getPrivilegeSMOImpl() {
        return privilegeSMOImpl;
    }

    public void setPrivilegeSMOImpl(IPrivilegeSMO privilegeSMOImpl) {
        this.privilegeSMOImpl = privilegeSMOImpl;
    }
}
