(function(vc){

    vc.extends({
        propTypes: {
               callBackListener:vc.propTypes.string, //父组件名称
               callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            auditInfo:{
                state:'',
                remark:''
            }
        },
        watch:{
            "auditInfo.state":{//深度监听，可监听到对象、数组的变化
                handler(val, oldVal){
                    if(vc.notNull(val) && vc.component.auditInfo.state == '1100'){
                        vc.component.auditInfo.remark = "同意";
                    }else{
                        vc.component.auditInfo.remark = "";
                    }

                },
                deep:true
            }
         },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('audit','openAuditModal',function(){
                $('#auditModel').modal('show');
            });
        },
        methods:{
            auditValidate(){
                return vc.validate.validate({
                    auditInfo:vc.component.auditInfo
                },{
                    'auditInfo.state':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"审核状态不能为空"
                        },
                        {
                            limit:"num",
                            param:"",
                            errInfo:"审核状态错误"
                        },
                    ],
                    'auditInfo.remark':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"原因内容不能为空"
                        },
                        {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"原因内容不能超过200"
                        },
                    ]
                });
            },
            _auditSubmit:function(){
                if(!vc.component.auditValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }
                //不提交数据将数据 回调给侦听处理
                if(vc.notNull($props.callBackListener)){
                    vc.emit($props.callBackListener,$props.callBackFunction,vc.component.auditInfo);
                    $('#auditModel').modal('hide');

                    vc.component.clearAddBasePrivilegeInfo();
                    return ;
                }


            },
            clearAddBasePrivilegeInfo:function(){
                vc.component.auditInfo={
                             state:'',
                             remark:''
                }
            }
        }
    });

})(window.vc);
