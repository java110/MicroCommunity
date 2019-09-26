(function(vc){

    vc.extends({
        propTypes: {
               notifyLoadDataComponentName:vc.propTypes.string,
               componentTitle:vc.propTypes.string // 组件名称
        },
        data:{
            addOwnerInfo:{
                componentTitle:$props.componentTitle,
                name:'',
                age:'',
                link:'',
                sex:'',
                remark:'',
                ownerId:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('addOwner','openAddOwnerModal',function(_ownerId){
                if(_ownerId != null || _ownerId != -1){
                    vc.component.addOwnerInfo.ownerId = _ownerId;
                }
                $('#addOwnerModel').modal('show');
            });
        },
        methods:{
            addOwnerValidate(){
                return vc.validate.validate({
                    addOwnerInfo:vc.component.addOwnerInfo
                },{
                    'addOwnerInfo.name':[
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
                    'addOwnerInfo.age':[
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
                    'addOwnerInfo.sex':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"性别不能为空"
                        }
                    ],
                    'addOwnerInfo.link':[
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
                    'addOwnerInfo.remark':[

                        {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注长度不能超过200位"
                        }
                    ]

                });
            },
            saveOwnerInfo:function(){
                if(!vc.component.addOwnerValidate()){
                    vc.message(vc.validate.errInfo);

                    return ;
                }

                vc.component.addOwnerInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'addOwner',
                    'saveOwner',
                    JSON.stringify(vc.component.addOwnerInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addOwnerModel').modal('hide');
                            vc.component.clearAddOwnerInfo();
                            vc.emit($props.notifyLoadDataComponentName,'listOwnerData',{});

                            return ;
                        }
                        vc.message(json);

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);

                     });
            },
            clearAddOwnerInfo:function(){
                vc.component.addOwnerInfo = {
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