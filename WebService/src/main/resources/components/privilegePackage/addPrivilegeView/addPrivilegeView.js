(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            addPrivilegeViewInfo:{
                flowComponent:'addPrivilegeView',
                name:'',
                domain:'',
                description:'',

            }
        },
        watch:{
            addPrivilegeViewInfo:{
                deep: true,
                handler:function(){
                    vc.component.saveAddPrivilegeInfo();
                }
             }
        },
         _initMethod:function(){

         },
         _initEvent:function(){

            vc.on('addPrivilegeViewInfo','syncData',function(_obj){
                vc.copyObject(_obj,vc.component.addPrivilegeViewInfo);
            });

            vc.on('addPrivilegeViewInfo', 'onIndex', function(_index){
                vc.component.addPrivilegeViewInfo.index = _index;
            });
        },
        methods:{
            addPrivilegeValidate(){
                return vc.validate.validate({
                    addPrivilegeViewInfo:vc.component.addPrivilegeViewInfo
                },{
                    'addPrivilegeViewInfo.name':[
{
                            limit:"required",
                            param:"",
                            errInfo:"权限名称不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,10",
                            errInfo:"权限名称必须在2至10字符之间"
                        },
                    ],
'addPrivilegeViewInfo.domain':[
{
                            limit:"required",
                            param:"",
                            errInfo:"商户类型不能为空"
                        },
 {
                            limit:"maxin",
                            param:"1,12",
                            errInfo:"商户类型错误"
                        },
                    ],
'addPrivilegeViewInfo.description':[
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注内容不能超过200"
                        },
                    ],

                });
            },
            saveAddPrivilegeInfo:function(){
                if(vc.component.addPrivilegeValidate()){
                    //侦听回传
                    vc.emit($props.callBackListener,$props.callBackFunction, vc.component.addPrivilegeViewInfo);
                    return ;
                }
            }
        }
    });

})(window.vc);
