package com.java110.api.components;


import com.java110.core.context.IPageData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component(value="vue_test")
public class VueTestComponent {


    /**
     * 测试版本号
     * @return
     */
    public ResponseEntity<String> getTestVersion(IPageData pd){

        return new ResponseEntity<String>(pd.getReqData()+ " vue test v0.0.1", HttpStatus.OK);
    }

}
