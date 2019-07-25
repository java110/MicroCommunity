/**
    编辑员工
**/
(function(vc){
    vc.extends({
        data:{
            editStaffInfo:{
                userId:'',
                username:'',
                email:'',
                tel:'',
                sex:'',
                address:'',
                errorInfo:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
             vc.component.$on('edit_staff_event',function(_staffInfo){
                    vc.component.refreshEditStaffInfo(_staffInfo);
                    $('#editStaffModel').modal('show');
                });
        },
        methods:{
            refreshEditStaffInfo(_staffInfo){
                vc.component.editStaffInfo.userId = _staffInfo.userId;
                vc.component.editStaffInfo.username = _staffInfo.name;
                vc.component.editStaffInfo.email = _staffInfo.email;
                vc.component.editStaffInfo.tel = _staffInfo.tel;
                vc.component.editStaffInfo.sex = _staffInfo.sex;
                vc.component.editStaffInfo.address = _staffInfo.address;
            },
            editStaffValidate(){
                return vc.validate.validate({
                    editStaffInfo:vc.component.editStaffInfo
                },{
                    'editStaffInfo.username':[
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
                    'editStaffInfo.email':[
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
                    'editStaffInfo.tel':[
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
                    'editStaffInfo.sex':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"性别不能为空"
                        }
                    ],
                    'editStaffInfo.address':[
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
            editStaffSubmit:function(){
                 if(!vc.component.editStaffValidate()){
                    vc.component.editStaffInfo.errorInfo = vc.validate.errInfo;
                    return ;
                }

                vc.component.editStaffInfo.errorInfo = "";
                vc.http.post(
                    'editStaff',
                    'modifyStaff',
                    JSON.stringify(vc.component.editStaffInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editStaffModel').modal('hide');
                            vc.component.$emit('editStaff_reload_event',{});
                            return ;
                        }
                        vc.component.editStaffInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.editStaffInfo.errorInfo = errInfo;
                     });
            }
        },
    });
})(window.vc);