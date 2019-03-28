/**
    初始化 公司信息

**/

(function(vc){
    vc.extends({
        data:{
            storeTypes:[],
            companyBaseInfo:{
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
            }
        }

    });

})(window.vc);

