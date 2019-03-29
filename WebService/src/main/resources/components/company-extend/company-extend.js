/**
    初始化 公司信息

**/

(function(vc){
    vc.extends({
        data:{
            storeTypes:[],
            companyExtendInfo:{
                corporation:"",
                registeredCapital:"",
                foundingTime:"",
                registrationAuthority:"",
                scope:""
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
//              vc.component.$on('errorInfoEvent',function(_errorInfo){
//                     vc.component.registerInfo.errorInfo = _errorInfo;
//                     console.log('errorInfoEvent 事件被监听',_errorInfo)
//                 });

         },
        watch:{
            companyExtendInfo:{
                deep: true,
                handler:function(){
                    vc.component.$emit('companyExtendEvent',vc.component.companyExtendInfo);
                }
             }
        },
        methods:{
            validateExtend:function(){
                return vc.validate.validate({
                                                       companyExtendInfo:vc.component.companyExtendInfo
                                                   },{
                                                       'companyExtendInfo.corporation':[
                                                           {
                                                               limit:"required",
                                                               param:"",
                                                               errInfo:"法人不能为空"
                                                           },
                                                           {
                                                               limit:"maxLength",
                                                               param:"50",
                                                               errInfo:"法人长度必须在50位之内"
                                                           },
                                                       ],
                                                       'companyExtendInfo.registeredCapital':[
                                                           {
                                                               limit:"required",
                                                               param:"",
                                                               errInfo:"注册资本不能为空"
                                                           },
                                                           {
                                                               limit:"num",
                                                               param:"50",
                                                               errInfo:"注册资本必须是数字"
                                                           },
                                                       ],
                                                       'companyExtendInfo.foundingTime':[
                                                           {
                                                               limit:"required",
                                                               param:"",
                                                               errInfo:"成立时间不能为空"
                                                           },
                                                           {
                                                               limit:"date",
                                                               param:"",
                                                               errInfo:"不是有效的日期，例如：2019-03-29"
                                                           }
                                                       ],
                                                       'companyExtendInfo.registrationAuthority':[
                                                           {
                                                               limit:"required",
                                                               param:"",
                                                               errInfo:"登记机关不能为空"
                                                           },
                                                           {
                                                               limit:"maxLength",
                                                               param:"50",
                                                               errInfo:"登记机关长度必须在50位之内"
                                                           }
                                                       ],
                                                       'companyExtendInfo.scope':[
                                                           {
                                                               limit:"required",
                                                               param:"",
                                                               errInfo:"经营范围不能为空"
                                                           },
                                                           {
                                                               limit:"maxLength",
                                                               param:"50",
                                                               errInfo:"经营范围长度必须在50位之内"
                                                           }
                                                       ],

                                                   });
            }

        }

    });

})(window.vc);

