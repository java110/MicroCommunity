(function(vc){

    vc.extends({
        data:{
            editFloorInfo:{
                floorId:'',
                floorName:'',
                floorNum:'',
                remark:'',
                errorInfo:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('editFloor','openEditFloorModal',function(_floor){
                vc.component.editFloorInfo.errorInfo="";
                vc.copyObject(_floor,vc.component.editFloorInfo);
                $('#editFloorModel').modal('show');
            });
        },
        methods:{
            editFloorValidate(){
                return vc.validate.validate({
                    editFloorInfo:vc.component.editFloorInfo
                },{
                    'editFloorInfo.floorName':[
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
                    'editFloorInfo.floorNum':[
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
                    'editFloorInfo.remark':[

                        {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注长度不能超过200位"
                        }
                    ]

                });
            },
            editFloorMethod:function(){

                if(!vc.component.editFloorValidate()){
                    vc.component.editFloorInfo.errorInfo = vc.validate.errInfo;
                    return ;
                }

                vc.component.editFloorInfo.errorInfo = "";

                vc.component.editFloorInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'editFloor',
                    'changeFloor',
                    JSON.stringify(vc.component.editFloorInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editFloorModel').modal('hide');
                            vc.component.clearEditFloorInfo();
                            vc.emit('listFloor','listFloorData',{});

                            return ;
                        }
                        vc.component.editFloorInfo.errorInfo = json;

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.editFloorInfo.errorInfo = errInfo;

                     });
            },
            clearEditFloorInfo:function(){
                vc.component.editFloorInfo = {
                                            floorId:'',
                                            floorName:'',
                                            floorNum:'',
                                            remark:'',
                                            errorInfo:''
                                        };
            }
        }
    });

})(window.vc);