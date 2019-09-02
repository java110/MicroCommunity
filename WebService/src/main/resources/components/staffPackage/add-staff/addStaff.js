(function(vc){

    vc.extends({
        data:{
            addStaffInfo:{
                username:'',
                email:'',
                tel:'',
                sex:'',
                address:'',
                errorInfo:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){

        },
        methods:{
            addStaffValidate(){
                return vc.validate.validate({
                    addStaffInfo:vc.component.addStaffInfo
                },{
                    'addStaffInfo.username':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"用户名不能为空"
                        },
                        {
                            limit:"maxin",
                            param:"2,10",
                            errInfo:"用户名长度必须在2位至10位"
                        },
                    ],
                    'addStaffInfo.email':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"密码不能为空"
                        },
                        {
                            limit:"email",
                            param:"",
                            errInfo:"不是有效的邮箱"
                        },
                    ],
                    'addStaffInfo.tel':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"手机号不能为空"
                        },
                        {
                            limit:"phone",
                            param:"",
                            errInfo:"不是有效的手机号"
                        }
                    ],
                    'addStaffInfo.sex':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"性别不能为空"
                        }
                    ],
                    'addStaffInfo.address':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"地址不能为空"
                        },
                        {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"地址长度不能超过200位"
                        },
                    ]

                });
            },
            saveStaffInfo:function(){
                if(!vc.component.addStaffValidate()){
                    vc.component.addStaffInfo.errorInfo = vc.validate.errInfo;
                    return ;
                }

                vc.component.addStaffInfo.errorInfo = "";
                vc.http.post(
                    'addStaff',
                    'saveStaff',
                    JSON.stringify(vc.component.addStaffInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addStaffModel').modal('hide');
                            vc.component.clearAddStaffInfo();
                            vc.component.$emit('addStaff_reload_event',{});
                            return ;
                        }
                        vc.component.addStaffInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.addStaffInfo.errorInfo = errInfo;
                     });
            },
            clearAddStaffInfo:function(){
                vc.component.addStaffInfo = {
                                            username:'',
                                            email:'',
                                            tel:'',
                                            sex:'',
                                            address:'',
                                            errorInfo:''
                                        };
            }
        }
    });

})(window.vc);