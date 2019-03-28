/**
    初始化 公司信息

**/

(function(vc){
    vc.extends({
        data:{
            step:1,
            companyInfo:{
                tel:""
            }
        },
         _initMethod:function(){
             //vc.component.initStoreType();
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

            next:function(){
                if(vc.component.step<4){
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
                console.log("提交审核");
            }
        }

    });

})(window.vc);

