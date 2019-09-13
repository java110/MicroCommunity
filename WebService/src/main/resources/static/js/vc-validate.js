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
        errInfo:'',

        setState:function(_state,_errInfo){
            this.state = _state;
            if(!this.state){
                this.errInfo = _errInfo
                throw "校验失败:"+_errInfo;
            }
        },

        /**
            校验手机号
        **/
        phone:function(text){
             var regPhone =/^0?1[3|4|5|6|7|8][0-9]\d{8}$/;
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
            var regNum = /^[1-9][0-9]*$/;
            return regNum.test(text);
        },
        date:function(str) {
            var regDate = /^(\d{4})-(\d{2})-(\d{2})$/;
            return regDate.test(str);
        },
        dateTime:function(str){
            var reDateTime = /^(?:19|20|30)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$/;
            return reDateTime.test(reDateTime);
        },
        /**
            金额校验
        **/
        money:function(text){
            var regMoney = /^\d+\.?\d{0,2}$/;
            return regMoney.test(text);
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
     *      "name":[
                    {
                       limit:"required",
                       param:"",
                       errInfo:'用户名为必填'
                    },
                    {
                        limit:"maxin",
                       param:"1,10",
                       errInfo:'用户名必须为1到10个字之间'
                    }]
     * }
     *
     */
    validate.validate = function(dataObj,dataConfig){

        try{
            // 循环配置（每个字段）
            for(var key in dataConfig){
                //配置信息
                var tmpDataConfigValue = dataConfig[key];
                //对key进行处理
                var keys = key.split(".");
                console.log("keys :",keys);
                var tmpDataObj = dataObj;
                //根据配置获取 数据值
                keys.forEach(function(tmpKey){
                     console.log('tmpDataObj:',tmpDataObj);
                     tmpDataObj = tmpDataObj[tmpKey]
                });
//                for(var tmpKey in keys){
//                    console.log('tmpDataObj:',tmpDataObj);
//                    tmpDataObj = tmpDataObj[tmpKey]
//                }

                tmpDataConfigValue.forEach(function(configObj){
                    if(configObj.limit == "required"){
                        validate.setState(validate.required(tmpDataObj),configObj.errInfo);
                    }

                    if(configObj.limit == 'phone'){
                        validate.setState(validate.phone(tmpDataObj),configObj.errInfo);
                    }

                    if(configObj.limit == 'email'){
                        validate.setState(validate.email(tmpDataObj),configObj.errInfo);
                    }

                    if(configObj.limit == 'maxin'){
                        var tmpParam = configObj.param.split(",")
                        validate.setState(validate.maxin(tmpDataObj,tmpParam[0],tmpParam[1]),configObj.errInfo);
                    }

                    if(configObj.limit == 'maxLength'){
                        validate.setState(validate.maxLength(tmpDataObj,configObj.param),configObj.errInfo);

                    }

                    if(configObj.limit == 'minLength'){
                        validate.setState(validate.minLength(tmpDataObj,configObj.param),configObj.errInfo);
7
                    }

                    if(configObj.limit == 'num'){
                        validate.setState(validate.num(tmpDataObj),configObj.errInfo);
                    }

                    if(configObj.limit == 'date'){
                        validate.setState(validate.date(tmpDataObj),configObj.errInfo);
                    }
                    if(configObj.limit == 'dateTime'){
                        validate.setState(validate.dateTime(tmpDataObj),configObj.errInfo);
                    }

                    if(configObj.limit == 'money'){
                        validate.setState(validate.money(tmpDataObj),configObj.errInfo);
                    }
                });

            }
        }catch(error){
            console.log("数据校验失败",validate.state,validate.errInfo,error);
            return false;
        }

        return true;
    }

})(window.vc.validate);


/**
对 validate 进行二次封装
**/
(function(vc){
    vc.check = function(dataObj,dataConfig){
        return vc.validate.validate(dataObj, dataConfig);
    }
})(window.vc)