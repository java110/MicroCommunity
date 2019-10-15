(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            addStaffViewInfo:{
                flowComponent:'addStaffView',
                username:'',
                sex:'',
                email:'',
                tel:'',
                address:'',
            }
        },
        watch:{
            addStaffViewInfo:{
                deep: true,
                handler:function(){
                    vc.component.saveAddStaffInfo();
                }
             }
        },
         _initMethod:function(){

         },
         _initEvent:function(){

            vc.on('addStaffViewInfo', 'onIndex', function(_index){
                vc.component.addStaffViewInfo.index = _index;
            });
        },
        methods:{
            addStaffValidate(){
                return vc.validate.validate({
                    addStaffViewInfo:vc.component.addStaffViewInfo
                },{
                    'addStaffViewInfo.username':[
{
                            limit:"required",
                            param:"",
                            errInfo:"员工名称不能为空"
                        },
 {
                            limit:"maxin",
                            param:"2,10",
                            errInfo:"员工名称长度必须在2位至10位"
                        },
                    ],
'addStaffViewInfo.sex':[
{
                            limit:"required",
                            param:"",
                            errInfo:"员工性别不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"员工性别不能为空"
                        },
                    ],
'addStaffViewInfo.email':[
{
                            limit:"required",
                            param:"",
                            errInfo:"员工邮箱不能为空"
                        },
 {
                            limit:"email",
                            param:"",
                            errInfo:"员工邮箱不是有效邮箱"
                        },
                    ],
'addStaffViewInfo.tel':[
 {
                            limit:"photo",
                            param:"",
                            errInfo:"联系方式不是有效手机"
                        },
                    ],
'addStaffViewInfo.address':[
{
                            limit:"required",
                            param:"",
                            errInfo:"家庭住址不能为空"
                        },
 {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"家庭住址不能超过200位"
                        },
                    ],

                });
            },
            saveAddStaffInfo:function(){
                if(vc.component.addStaffValidate()){
                    //侦听回传
                    vc.emit($props.callBackListener,$props.callBackFunction, vc.component.addStaffViewInfo);
                    return ;
                }
            }
        }
    });

})(window.vc);
