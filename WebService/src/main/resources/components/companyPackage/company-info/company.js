/**
    初始化 公司信息

**/

(function(vc){
    vc.extends({
        data:{
            step:1,
            companyInfo:{
                errorInfo:""
            }
        },
         _initMethod:function(){
             //vc.component.initStoreType();
         },
         _initEvent:function(){
              //监听 公司基本信息
              vc.component.$on('companyBaseEvent',function(companyBase){
                     for(var companyBaseKey in companyBase){
                          vc.component.companyInfo[companyBaseKey] = companyBase[companyBaseKey];
                      }
                 });
              // 扩展信息
              vc.component.$on('companyExtendEvent',function(companyExtend){
                   for(var companyExtendKey in companyExtend){
                        vc.component.companyInfo[companyExtendKey] = companyExtend[companyExtendKey];
                    }
               });
               // 证件信息
             vc.component.$on('companyCerdentialsEvent',function(companyCerdentials){
                  for(var companyCerdentialsKey in companyCerdentials){
                       vc.component.companyInfo[companyCerdentialsKey] = companyCerdentials[companyCerdentialsKey];
                   }
              });

         },
        watch:{

        },
        methods:{

            next:function(){
                //第一步
                if(vc.component.step == 1 && !vc.component.validateBase()){
                    vc.component.companyInfo.errorInfo = vc.validate.errInfo;
                    return ;
                }
                //第二步
                if(vc.component.step == 2 && !vc.component.validateExtend()){
                    vc.component.companyInfo.errorInfo = vc.validate.errInfo;
                    return ;
                }
                //第三步
                if(vc.component.step == 3 && !vc.component.validateCerdentials()){
                    vc.component.companyInfo.errorInfo = vc.validate.errInfo;
                    return ;
                }
                if(vc.component.step<4){
                    vc.component.companyInfo.errorInfo = '';
                    vc.component.step = vc.component.step+1;
                }

                //校验字段是否填写
            },
            previous:function(){
                if(vc.component.step>1){
                    vc.component.step = vc.component.step-1;;
                }
            },
            finish:function(){
                //这里写提交代码
                console.log("提交审核",vc.component.companyInfo);
                vc.http.post(
                            'company',
                            'saveCompanyInfo',
                            JSON.stringify(vc.component.companyInfo),
                            {
                                emulateJSON:true
                             },
                             function(json,res){
                                //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                                if(res.status == 200){
                                    vc.jumpToPage("/");
                                    return ;
                                }
                                vc.component.companyInfo.errorInfo = json;
                             },
                             function(errInfo,error){
                                console.log('请求失败处理');

                                vc.component.companyInfo.errorInfo = errInfo;
                             });

            }
        }

    });

})(window.vc);

