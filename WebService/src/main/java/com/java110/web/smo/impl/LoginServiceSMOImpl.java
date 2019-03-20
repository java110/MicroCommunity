package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.web.smo.ILoginServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 登录信息实现类
 * Created by wuxw on 2019/3/20.
 */

@Service("loginServiceSMOImpl")
public class LoginServiceSMOImpl extends BaseServiceSMO implements ILoginServiceSMO {
    private final static Logger logger = LoggerFactory.getLogger(LoginServiceSMOImpl.class);


    private static char[] chs = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final int NUMBER_OF_CHS = 4;
    private static final int IMG_WIDTH = 65;
    private static final int IMG_HEIGHT = 25;
    private static Random r = new Random();
    @Autowired
    private RestTemplate restTemplate;


    /**
     * 登录处理
     * @param pd 页面请求对象
     * @return
     */
    @Override
    public ResponseEntity<String> doLogin(IPageData pd) {

        ResponseEntity<String> responseEntity = null;

        Assert.jsonObjectHaveKey(pd.getReqData(),"username","请求报文格式错误或未包含username信息");
        JSONObject loginInfo = JSONObject.parseObject(pd.getReqData());
        loginInfo.put("passwd", AuthenticationFactory.passwdMd5(loginInfo.getString("passwd")));
        responseEntity = this.callCenterService(restTemplate,pd,loginInfo.toJSONString(),ServiceConstant.SERVICE_API_URL+"/api/user.service.login",HttpMethod.POST);
        if(responseEntity.getStatusCode() == HttpStatus.OK){
            pd.setToken(JSONObject.parseObject(responseEntity.getBody()).getString("token"));
        }
        return responseEntity;
    }

    /**
     * 生成验证码
     * 参考地址：https://www.cnblogs.com/happyfans/p/4486010.html
     * @param pd 页面请求对象
     * @return
     */
    @Override
    public ResponseEntity<String> generateValidateCode(IPageData pd) {



        BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);    // 实例化BufferedImage
                    Graphics g = image.getGraphics();
                    Color c = new Color(200, 200, 255);                                             // 验证码图片的背景颜色
                    g.setColor(c);
                    g.fillRect(0, 0, IMG_WIDTH, IMG_HEIGHT);                                        // 图片的边框

                    StringBuffer sb = new StringBuffer();                                           // 用于保存验证码字符串
                    int index;                                                                      // 数组的下标
                    for (int i = 0; i < NUMBER_OF_CHS; i++) {
                            index = r.nextInt(chs.length);                                              // 随机一个下标
                            g.setColor(new Color(r.nextInt(88), r.nextInt(210), r.nextInt(150)));       // 随机一个颜色
                            g.drawString(chs[index] + "", 15 * i + 3, 18);                              // 画出字符
                            sb.append(chs[index]);                                                      // 验证码字符串
                        }

                    /*request.getSession().setAttribute("piccode", sb.toString());                    // 将验证码字符串保存到session中
                    ImageIO.write(image, "jpg", response.getOutputStream());*/
        return null;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
