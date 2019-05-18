(function(vc){

    vc.extends({
        data:{
            editOwnerInfo:{
                ownerId:'',
                memberId:'',
                name:'',
                age:'',
                link:'',
                sex:'',
                remark:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('editOwner','openEditOwnerModal',function(_owner){
                vc.copyObject(_owner,vc.component.editOwnerInfo);
                $('#editOwnerModel').modal('show');
            });
        },
        methods:{
            editOwnerValidate(){
                return vc.validate.validate({
                    editOwnerInfo:vc.component.editOwnerInfo
                },{
                   'editOwnerInfo.name':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"名称不能为空"
                        },
                        {
                            limit:"maxin",
                            param:"2,10",
                            errInfo:"名称长度必须在2位至10位"
                        },
                    ],
                    'editOwnerInfo.age':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"年龄不能为空"
                        },
                        {
                            limit:"num",
                            param:"",
                            errInfo:"年龄不是有效的数字"
                        },
                    ],
                    'editOwnerInfo.sex':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"性别不能为空"
                        }
                    ],
                    'editOwnerInfo.link':[
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
                    'editOwnerInfo.remark':[

                        {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注长度不能超过200位"
                        }
                    ]

                });
            },
            editOwnerMethod:function(){

                if(!vc.component.editOwnerValidate()){
                    vc.message(vc.validate.errInfo);

                    return ;
                }

                vc.component.editOwnerInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'editOwner',
                    'changeOwner',
                    JSON.stringify(vc.component.editOwnerInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editOwnerModel').modal('hide');
                            vc.component.clearEditOwnerInfo();
                            vc.emit('listOwner','listOwnerData',{});

                            return ;
                        }
                        vc.message(json);

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(errInfo);
                     });
            },
            clearEditOwnerInfo:function(){
                vc.component.editOwnerInfo = {
                    ownerId:'',
                    memberId:'',
                    name:'',
                    age:'',
                    link:'',
                    sex:'',
                    remark:''
                };
            }
        }
    });

})(window.vc);