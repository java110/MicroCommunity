(function(vc,vm){

    vc.extends({
        data:{
            addRoomInfo:{
                floorId:'',
                roomNum:'',
                layerCount:'',
                lift:'',
                remark:'',
                communityId:''
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('addRoom','addRoomModel',function(_params){
                vc.component.refreshAddRoomInfo();
                $('#addRoomModel').modal('show');
                vc.component.addRoomInfo.floorId = _params.floorId;
                vc.component.addRoomInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            addRoomValidate:function(){
                        return vc.validate.validate({
                            addRoomInfo:vc.component.addRoomInfo
                        },{
                            'addRoomInfo.floorId':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"小区楼不能为空"
                                }
                            ],
                            'addRoomInfo.roomNum':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"单元编号不能为空"
                                },
                                {
                                    limit:"maxLength",
                                    param:"12",
                                    errInfo:"单元编号长度不能超过12位"
                                },
                            ],
                            'addRoomInfo.layerCount':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"单元楼层高度不能为空"
                                },
                                {
                                    limit:"num",
                                    param:"",
                                    errInfo:"单元楼层高度必须为数字"
                                }
                            ],
                            'addRoomInfo.lift':[
                                {
                                    limit:"required",
                                    param:"",
                                    errInfo:"必须选择单元是否电梯"
                                }
                            ],
                            'addRoomInfo.remark':[
                                {
                                    limit:"maxLength",
                                    param:"200",
                                    errInfo:"备注长度不能超过200位"
                                },
                            ]

                        });
             },
            addRoom:function(){
                if(!vc.component.addRoomValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'addRoom',
                    'save',
                    JSON.stringify(vc.component.addRoomInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#addRoomModel').modal('hide');
                            vc.emit('room','loadRoom',{
                                floorId:vc.component.addRoomInfo.floorId
                            });
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });
            },
            refreshAddRoomInfo:function(){
                vc.component.addRoomInfo= {
                  floorId:'',
                  roomNum:'',
                  layerCount:'',
                  lift:'',
                  remark:'',
                  communityId:''
                }
            }
        }
    });

})(window.vc,window.vc.component);