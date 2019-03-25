/**
vc 校验 工具类 -method
(1)、required:true               必输字段
(2)、remote:"remote-valid.jsp"   使用ajax方法调用remote-valid.jsp验证输入值
(3)、email:true                  必须输入正确格式的电子邮件
(4)、url:true                    必须输入正确格式的网址
(5)、date:true                   必须输入正确格式的日期，日期校验ie6出错，慎用
(6)、dateISO:true                必须输入正确格式的日期(ISO)，例如：2009-06-23，1998/01/22 只验证格式，不验证有效性
(7)、number:true                 必须输入合法的数字(负数，小数)
(8)、digits:true                 必须输入整数
(9)、creditcard:true             必须输入合法的信用卡号
(10)、equalTo:"#password"        输入值必须和#password相同
(11)、accept:                    输入拥有合法后缀名的字符串（上传文件的后缀）
(12)、maxlength:5                输入长度最多是5的字符串(汉字算一个字符)
(13)、minlength:10               输入长度最小是10的字符串(汉字算一个字符)
(14)、rangelength:[5,10]         输入长度必须介于 5 和 10 之间的字符串")(汉字算一个字符)
(15)、range:[5,10]               输入值必须介于 5 和 10 之间
(16)、max:5                      输入值不能大于5
(17)、min:10                     输入值不能小于10
**/
(function(vc){
    var validate = {

        state:true,
        /**
            校验手机号
        **/
        phone:function(text){
             var reg =/^0?1[3|4|5|6|7|8][0-9]\d{8}$/;
             return regPhone.test(text);
        },
        /**
            校验邮箱
        **/
        email:function(text){
            var regEmail = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); //正则表达式
            return regEmail.test(text);
        },
        /**
         * 必填
         * @param {参数} text
         */
        required:function(text){
            if(text == undefined || text == null || text == "" ){
                return false;
            }

            return true;
        },
        /**
         * 校验长度
         * @param {校验文本} text
         * @param {最小长度} minLength
         * @param {最大长度} maxLength
         */
        maxin:function(text,minLength,maxLength){
            if(text.length <minLength || text.length > maxLength){
                return false;
            }

            return true;
        },
        /**
         * 校验长度
         * @param {校验文本} text
         * @param {最大长度} maxLength
         */
        maxLength:function(text,maxLength){
            if(text.length > maxLength){
                return false;
            }

            return true;
        },
        /**
         * 校验最小长度
         * @param {校验文本} text
         * @param {最小长度} minLength
         */
        minLength:function(text,minLength){
            if(text.length < minLength){
                return false;
            }
            return true;
        },
        /**
         * 全是数字
         * @param {校验文本} text
         */
        num:function(text){
            var regNum = /^[0-9]+$/;
            return regNum.test(text);
        }

    };
    vc.validate = validate;

})(window.vc);

/**
 * 校验 -core
 */
(function(validate){

    /**
     * 根据配置校验
     *
     * eg:
     * dataObj:
     * {
     *      name:"wuxw",
     *      age:"19",
     *      emailInfo:{
     *          email:"928255095@qq.com"
     *      }
     * }
     *
     * dataConfig:
     * {
     *      "name":"required|maxin(1,10)",
     *      "age":"required|num",
     *      "emailInfo.email":"required|email"
     * }
     *
     */
    validate.validate = function(dataObj,dataConfig){
        for(var key in dataConfig){
            var keys = key.split(".");
            dataConfig[key];
        }
    }

})(window.vc.validate);