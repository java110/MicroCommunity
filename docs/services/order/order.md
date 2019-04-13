### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2018-07-24|wuxw

### 本页内容

1. [协议说明](#协议说明)
2. [下游系统交互说明](#下游系统交互说明)
3. [订单类型说明](#订单类型说明)
4. [加密说明](#加密说明)
5. [状态说明](#状态说明)
6. [数据格式约定](#数据格式约定)

### 协议说明
1. 协议结构

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
-|orders|1|Object|-|订单节点|-
-|business|1|Array|-|业务节点|-

2. 订单节点结构

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
-|orders|1|Object|-|订单节点|-
orders|appId|1|String|10|系统ID|由中心服务提供
orders|transactionId|1|String|30|交互流水|appId+'00'+YYYYMMDD+10位序列
orders|userId|1|String|30|用户ID|已有用户ID
orders|orderTypeCd|1|String|4|订单类型|查看订单类型说明
orders|requestTime|1|String|14|请求时间|YYYYMMDDhhmmss
orders|remark|1|String|200|备注|备注
orders|sign|?|String|64|签名|查看加密说明
orders|attrs|?|Array|-|订单属性|-
attrs|specCd|1|String|12|规格编码|由中心服务提供
attrs|value|1|String|50|属性值|
orders|response|1|Object|-|返回结果节点|-
response|code|1|String|4|返回状态|查看状态说明
response|message|1|String|200|返回状态描述|-
3. 业务节点结构

父元素名称|参数名称|约束|类型|长度|描述|取值说明
 :-: | :-: | :-: | :-: | :-: | :-: | :-:
 -|business|?|Array|-|业务节点|-
 business|serviceCode|1|String|50|业务编码|由中心服务提供
 business|serviceName|1|String|50|业务名称|由中心服务提供
 business|remark|1|String|200|备注|
 business|datas|1|Object|-|数据节点|不同的服务下的节点不一样
 business|attrs|?|Array|-|业务属性|-
 attrs|specCd|1|String|12|规格编码|由中心服务提供
 attrs|value|1|String|50|属性值|
 business|response|1|Object|-|返回结果节点|-
 response|code|1|String|4|返回状态|查看状态说明
 response|message|1|String|200|返回状态描述|-

 4. 报文样例

请求报文：
 ```
 {
   "orders": {
     "appId": "1234567890",
     "transactionId": "123456789000201804090123456789",
     "userId": "1234567890",
     "orderTypeCd": "Q",
     "requestTime": "20180409224736",
     "remark": "备注",
     "sign": "这个服务是否要求MD5签名",
     "attrs": [{
       "specCd": "配置的字段ID",
       "value": "具体值"
     }]
   },
   "business": [{
     "serviceCode": "服务编码",
     "serviceName": "服务编码名称",
     "remark": "备注",
     "datas": {

     },
     "attrs": [{
       "specCd": "配置的字段ID",
       "value": "具体值"
     }]
   }]
 }
 ```
 返回报文：
 ```
 {
   "orders": {
     "transactionId": "123456789000201804090123456789",
     "responseTime": "20180409224736",
     "sign": "这个服务是否要求MD5签名",
     "response": {//这个是centerOrder 返回的状态结果
       "code": "1999",
       "message": "具体值"
     }
   },
   "business":[{//这个是相应的业务系统返回的结果，（受理为空，查询时不为空）
     "response": {
       "code": "0000",
       "message": "具体值"
     }
   }]
 }
 ```

### 下游系统交互说明

与下游系统交互主要分为三个过程，分别为 Business过程，Instance过程，作废过程

1. Business过程指先将数据存放至中间表中（叫做business表），表明动作 是新增（ADD）还是删除（DEL）。

请求协议为：
```
{
	"orders": {
		"transactionId": "100000000020180409224736000001",
		"requestTime": "20180409224736",
		"orderTypeCd": "订单类型,查询,受理",
		"dataFlowId": "20020180000001",
		"businessType": "Q"
	},
	"business": {
		"bId": "12345678",
		"serviceCode": "querycustinfo",
		"serviceName": "查询客户",
		"remark": "备注",
		"datas": {
			"params": {
			}
		}
	}
}
```
返回协议为：
```
{
	"transactionId": "100000000020180409224736000001",
	"responseTime": "20180409224736",
	"businessType": "B",
	"bId": "12345678",
	"orderTypeCd": "",
	"dataFlowId": "",
	"serviceCode": "",
	"response": {
		"code": "1999",
		"message": "具体值"
	}
}
```

2. Instance过程指将中间表中的数据根据动作分析增加删除或修改业务表中的数据。

请求协议为：

```
{
	"orders": {
		"transactionId": "100000000020180409224736000001",
		"requestTime": "20180409224736",
		"orderTypeCd": "订单类型,查询,受理",
		"dataFlowId": "20020180000001",
		"businessType": "I"
	},
	"business": {
		"bId": "12345678",
		"serviceCode": "save.user.userInfo"
	}
}
```
返回协议为：

```
{
	"transactionId": "100000000020180409224736000001",
	"responseTime": "20180409224736",
	"businessType": "I",
	"bId": "12345678",
	"orderTypeCd": "",
	"dataFlowId": "",
	"serviceCode": "",
	"response": {
		"code": "1999",
		"message": "具体值"
	}
}
```

3. 作废过程指 当某个下游系统失败的情况下，判断是Business过程失败还是Instance过程失败
如果是Business过程失败，则放弃发送Instance过程直接返回，如果Instance过程失败，则发起作废业务数据过程。

请求协议为：

```
{
	"orders": {
		"transactionId": "100000000020180409224736000001",
		"requestTime": "20180409224736",
		"orderTypeCd": "订单类型,查询,受理",
		"dataFlowId": "20020180000001",
		"businessType": "DO"
	},
	"business": {
		"bId": "12345678",
		"serviceCode": "save.user.userInfo"
	}
}
```
返回协议为：
```
{
	"transactionId": "100000000020180409224736000001",
	"responseTime": "20180409224736",
	"businessType": "DO",
	"bId": "12345678",
	"orderTypeCd": "",
	"dataFlowId": "",
	"serviceCode": "",
	"response": {
		"code": "1999",
		"message": "具体值"
	}
}
```

### 订单类型说明

订单类型|说明
:-:|:-:
Q|查询类

### 加密说明
1. 请求sign说明

外系统请求centerService 服务时，sign 的生成：
```
inStr = transactionId + appId+business（内容）+security_code(系统分配);
DigestUtils.md5Hex(inStr.getBytes("UTF-8"));
```

2. CenterService服务返回时 sign生成：

```
inStr = transactionId + responseTime+business（内容）+security_code(系统分配);
DigestUtils.md5Hex(inStr.getBytes("UTF-8"));
```
注意：当传入AppId 不正确，或者请求报文解密失败的情况下，返回sign 不做加密处理，其值为空

3. 请求报文加密：

如果http post 请求时header 中有 ENCRYPT 并且值为ON，时启用密文传输方式，即请求报文和返回报文都为密文，如果没有 ENCRYPT 或 值不为ON 则明文传输。
加密代码参考：
```
/**
 * 加密
 * @param data
 * @param publicKey
 * @param keySize
 * @return
 * @throws Exception
 */
public static byte[] encrypt(byte[] data, PublicKey publicKey, int keySize)
        throws Exception
{
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING", "BC");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);

    int blockSize = (keySize >> 3) - 11;

    int inputLen = data.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    int i = 0;
    while (inputLen - offSet > 0) {
        byte[] buf;
        if (inputLen - offSet > blockSize) {
            buf = cipher.doFinal(data, offSet, blockSize);
        }else {
            buf = cipher.doFinal(data, offSet, inputLen - offSet);
        }
        out.write(buf, 0, buf.length);
        ++i;
        offSet = i * blockSize;
    }
    byte[] result = out.toByteArray();

    return result;
}

/**
 * 加载公钥
 * @param keyData
 * @return
 * @throws Exception
 */
public static PublicKey loadPubKey(String keyData)
        throws Exception
{
    return loadPemPublicKey(keyData, "RSA");
}

/**
 * 加载公钥
 * @param privateKeyPem
 * @param algorithm
 * @return
 * @throws Exception
 */
public static PrivateKey loadPrivateKeyPkcs8(String privateKeyPem, String algorithm)
        throws Exception
{
    String privateKeyData = privateKeyPem.replace("-----BEGIN PRIVATE KEY-----", "");
    privateKeyData = privateKeyData.replace("-----END PRIVATE KEY-----", "");
    privateKeyData = privateKeyData.replace("\n", "");
    privateKeyData = privateKeyData.replace("\r", "");

    byte[] decoded = Base64.getDecoder().decode(privateKeyData.getBytes());

    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(decoded);
    KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

    return keyFactory.generatePrivate(pkcs8KeySpec);
}
//加密
reqJson = new String(encrypt(resJson.getBytes("UTF-8"), loadPubKey(“公钥”)
        , 2048)),"UTF-8");
```

其中 keySize 如果设置要重新设置则 http post header 中传ENCRYPT_KEY_SIZE来设置 不传则去默认值，默认值配置在映射表中，key为 KEY_DEFAULT_DECRYPT_KEY_SIZE

4. 返回报文解密

如果http post 请求时header 中有 ENCRYPT 并且值为ON，时启用密文传输方式。
解密代码参考

```
/**
 * 解密
 * @param data
 * @param privateKey
 * @param keySize
 * @return
 * @throws Exception
 */
public static byte[] decrypt(byte[] data, PrivateKey privateKey, int keySize)
        throws Exception
{
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING", "BC");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    int blockSize = keySize >> 3;

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    byte[] buf = new byte[blockSize];
    int len = 0;
    while ((len = byteArrayInputStream.read(buf)) > 0) {
        byteArrayOutputStream.write(cipher.doFinal(buf, 0, len));
    }

    return byteArrayOutputStream.toByteArray();
}
/**
 * 加载私钥

 * @param keyData
 * @return
 * @throws Exception
 */
public static PrivateKey loadPrivateKey(String keyData) throws Exception {
    return loadPrivateKeyPkcs8(keyData, "RSA");
}
/**
 * 加载私钥
 * @param privateKeyPem
 * @param algorithm
 * @return
 * @throws Exception
 */
public static PrivateKey loadPrivateKeyPkcs8(String privateKeyPem, String algorithm)
        throws Exception
{
    String privateKeyData = privateKeyPem.replace("-----BEGIN PRIVATE KEY-----", "");
    privateKeyData = privateKeyData.replace("-----END PRIVATE KEY-----", "");
    privateKeyData = privateKeyData.replace("\n", "");
    privateKeyData = privateKeyData.replace("\r", "");

    byte[] decoded = Base64.getDecoder().decode(privateKeyData.getBytes());

    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(decoded);
    KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

    return keyFactory.generatePrivate(pkcs8KeySpec);
}
//解密
resJson = new String(decrypt(reqJson.getBytes("UTF-8"), “私钥”
        , 2048)),"UTF-8");
```
<font color=red size=4 face="黑体">说明：加密和解密的公钥和私钥，由centerService提供。</font>

### 状态说明

状态编码|说明
:-:|:-:
0000|成功
1999|未知失败
1998|系统内部错误
1997|调用下游系统超时

### 数据格式约定

格式符号|说明
:-:|:-:
?|0..1
星 | 0..n
+|1..n
1|1


[>回到首页](home)
