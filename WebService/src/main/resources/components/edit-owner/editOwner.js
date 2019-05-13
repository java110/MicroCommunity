(function(vc){

    vc.extends({
        data:{
            editOwnerInfo:{
                ownerId:'',
                ownerName:'',
                ownerNum:'',
                remark:'',
                errorInfo:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('editOwner','openEditOwnerModal',function(_owner){
                vc.component.editOwnerInfo.errorInfo="";
                vc.copyObject(_owner,vc.component.editOwnerInfo);
                $('#editOwnerModel').modal('show');
            });
        },
        methods:{
            editOwnerValidate(){
                return vc.validate.validate({
                    editOwnerInfo:vc.component.editOwnerInfo
                },{
                    'editOwnerInfo.ownerName':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"楼名称不能为空"
                        },
                        {
                            limit:"maxin",
                            param:"2,10",
                            errInfo:"楼名称长度必须在2位至10位"
                        },
                    ],
                    'editOwnerInfo.ownerNum':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"楼编号不能为空"
                        },
                        {
                            limit:"num",
                            param:"",
                            errInfo:"不是有效的数字"
                        },
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
                    vc.component.editOwnerInfo.errorInfo = vc.validate.errInfo;
                    return ;
                }

                vc.component.editOwnerInfo.errorInfo = "";

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
                        vc.component.editOwnerInfo.errorInfo = json;

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.editOwnerInfo.errorInfo = errInfo;

                     });
            },
            clearEditOwnerInfo:function(){
                vc.component.editOwnerInfo = {
                                            ownerId:'',
                                            ownerName:'',
                                            ownerNum:'',
                                            remark:'',
                                            errorInfo:''
                                        };
            }
        }
    });

})(window.vc);