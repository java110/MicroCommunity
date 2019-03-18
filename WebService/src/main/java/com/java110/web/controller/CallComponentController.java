package com.java110.web.controller;

import com.java110.common.factory.ApplicationContextFactory;
import com.java110.common.util.Assert;
import com.java110.core.base.controller.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@RestController
public class CallComponentController extends BaseController {

    /**
     * 调用组件方法
     * @return
     */

    @RequestMapping(path="/callComponent/{componentCode}/{componentMethod}",
            method = RequestMethod.POST)
    public ResponseEntity<String> callComponent(
            @PathVariable String componentCode,
            @PathVariable String componentMethod,
            @RequestBody String info,
            HttpServletRequest request){
        ResponseEntity<String> responseEntity = null;
        try{
            Assert.hasLength(componentCode,"参数错误，未传入组件编码");
            Assert.hasLength(componentMethod,"参数错误，未传入调用组件方法");

            Object componentInstance = ApplicationContextFactory.getBean(componentCode);

            Assert.notNull(componentInstance,"未找到组件对应的处理类，请确认 "+componentCode);

            Method cMethod = componentInstance.getClass().getDeclaredMethod(componentMethod,String.class);

            Assert.notNull(cMethod,"未找到组件对应处理类的方法，请确认 "+componentCode+"方法："+componentMethod);

             responseEntity = (ResponseEntity<String>)cMethod.invoke(componentInstance,info);

        }catch (Exception e){
            responseEntity = new ResponseEntity<>("调用组件失败"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            return responseEntity;
        }
    }


}
