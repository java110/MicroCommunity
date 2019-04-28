(function(vc){

    vc.extends({
        data:{
            addFloorInfo:{
                name:'',
                floorNum:'',
                remark:'',
                errorInfo:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('addFloor','openAddFloorModal',function(){
                $('#addFloorModel').modal('show');
            });
        },
        methods:{
            addFloorValidate(){
                return vc.validate.validate({
                    addFloorInfo:vc.component.addFloorInfo
                },{
                    'addFloorInfo.name':[
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
                    'addFloorInfo.floorNum':[
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
                    'addFloorInfo.remark':[

                        {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注长度不能超过200位"
                        }
                    ]

                });
            },
            saveFloorInfo:function(){
                vc.loading('open');
                if(!vc.component.addFloorValidate()){
                    vc.component.addFloorInfo.errorInfo = vc.validate.errInfo;
                    vc.loading('close');

                    return ;
                }

                vc.component.addFloorInfo.errorInfo = "";

                vc.component.addFloorInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'addFloor',
                    'saveFloor',
                    JSON.stringify(vc.component.addFloorInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addFloorModel').modal('hide');
                            vc.component.clearAddFloorInfo();
                            vc.emit('listFloor','listFloorData',{});
                            vc.loading('close');

                            return ;
                        }
                        vc.component.addFloorInfo.errorInfo = json;
                             vc.loading('close');

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.addFloorInfo.errorInfo = errInfo;
                             vc.loading('close');

                     });
            },
            clearAddFloorInfo:function(){
                vc.component.addFloorInfo = {
                                            name:'',
                                            floorNum:'',
                                            remark:'',
                                            errorInfo:''
                                        };
            }
        }
    });

})(window.vc);