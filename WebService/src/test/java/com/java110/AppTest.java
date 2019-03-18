package com.java110;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
//        Resource r = resourcePatternResolver.getResource(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
//                + "components/login/login.js");
        Reader reader = null;
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("components/login/login.js");
         reader = new InputStreamReader(inputStream);
        int tempChar;
        StringBuffer b = new StringBuffer();
        try {
            while ((tempChar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉r，或者屏蔽n。否则，将会多出很多空行。
                if (((char) tempChar) != '\r') {
                    b.append((char) tempChar);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String tb = b.toString();
        System.out.printf("tb:" +tb);
    }
}
