package com.java110;

import static org.junit.Assert.assertTrue;

import com.java110.core.factory.PlutusFactory;
import com.java110.utils.util.DateUtil;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

import java.util.Calendar;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void should()
    {
//解密
        String content = "IzQSUo0cFXLp1cSE/MlTlrmm9IHA5gO7RHFYJkD6djQDvFhuuQFBIKgTxHSZqc+IZkXvKH41qXXU7Qo1GbhUFmsfDVyiX8kGxfenxZ1dvSZyT6zf3l/BmeEeFQvjVveEQhRDXfH4A5PKPG5KRQHdNj+CAylsaEtncRfFHAizGbz2dKZyEG6HakCM+p+QSoRuWZEc3Ok8KSKBUruLFBGTLTlEmmMxYI438wf7ypuv5JsMBVyKHgXlBYmojULmbbiewcRy5QZ+x+oDc3KMGw37VwX5slEXFfWxQynHiKon0yLEtQSU7QRRm3PhKo9RHnE7v8P8E7Tork/Ks5xrAgHRJEDIhmLdrm0aBIqqnrLirXbMinJybPH3gDkM9BumJr8hn7yPR1ZvWzI4SOL6nhoBfg8jlna7DntH5rACrzDkvdvVd3NGK8oZVz+0iLybJwJLCva1qOd1mGNvJR6/Id8tPTniYui7Rab6bLfVXV3zWaLbtIV82MsxJ7lwFrYcW+jaAv8cUmp4QAlST+z1BR0cTK0qp4LewiIIU5HTWf33Raosk7I7tF90e2DuEddTNzCyNrL1CqhAsxJ5olL/lEXq4ghvKB1Oe0gfSIIWMJXzDtCwk4GF5EoGxGUvdpO+DFOH95T5nFXFy3cIDpaN79bLUwYrs5Nt4d4yVpIm/8pcrdArEM1/KxSIAO0jS4TVJnFN5MD4mep49FcHyAf3kLLMmA==";
        byte[] bb = PlutusFactory.decrypt(Base64.decode(content), "4a787546596c417046725248626d5551");
        //服务器返回内容
        String paramOut = new String(bb);
        System.out.println(paramOut);
    }
}
