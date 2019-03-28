/**
    初始化 公司信息

**/

(function(vc){
    vc.extends({
        data:{
            storeTypes:[],
            step:1,
            companyInfo:{
                tel:""
            }
        },
         _initMethod:function(){
             vc.component.initStoreType();
         },
         _initEvent:function(){
//              vc.component.$on('errorInfoEvent',function(_errorInfo){
//                     vc.component.registerInfo.errorInfo = _errorInfo;
//                     console.log('errorInfoEvent 事件被监听',_errorInfo)
//                 });

         },
        watch:{
            'companyInfo':{
                deep: true,
                handler:function(){
                    console.log('通知号码信息',vc.component.companyInfo.tel);
                    vc.component.$emit('validate_tel_change_event',vc.component.companyInfo);
                }
            }
        },
        methods:{
            initStoreType:function(){
                var param = {
                                    params:{
                                        msg:"123"
                                    }

                               }
                vc.http.get('company','getStoreType',
                             JSON.stringify(param),
                             function(json,res){
                                if(res.status == 200){
                                    vc.component.storeTypes = JSON.parse(json);
                                    return ;
                                }
                                //vc.component.$emit('errorInfoEvent',json);
                             },function(errInfo,error){
                                console.log('请求失败处理',errInfo,error);
                                vc.component.$emit('errorInfoEvent',errInfo);
                             });
            },
            next:function(){
                if(vc.component.step<4){
                    vc.component.step = vc.component.step+1;
                }
            },
            previous:function(){
                if(vc.component.step>1){
                    vc.component.step = vc.component.step-1;;
                }
            },
            finish:function(){
                //这里写提交代码
                console.log("提交审核");
            }
        }

    });

})(window.vc);

