(function(vc){

    vc.extends({
        data:{
            ownerRepairDetailInfo:{
                repairId:'',
                repairType:'',
                repairTypeName:'',
                repairName:'',
                tel:'',
                roomId:'',
                roomName:'',
                appointmentTime:'',
                context:'',
                stateName:'',
                roomId:''

            }
        },
         _initMethod:function(){
            
         },
         _initEvent:function(){
            vc.on('ownerRepairDetail','openOwnerRepairDetailModal',function(_ownerInfo){
                vc.component.clearOwnerRepairDetailInfo();
                vc.copyObject(_ownerInfo,vc.component.ownerRepairDetailInfo);
                vc.component._getRoom();
                $('#ownerRepairDetailModel').modal('show');
            });
        },
        methods:{
            clearOwnerRepairDetailInfo:function(){
                vc.component.ownerRepairDetailInfo = {
                        repairId:'',
                        repairType:'',
                        repairTypeName:'',
                        repairName:'',
                        tel:'',
                        roomId:'',
                        roomName:'',
                        appointmentTime:'',
                        context:'',
                        stateName:'',
                        roomId:''
                    };
            },
            _getRoom:function(){
                var param={
                    params:{
                        roomId:vc.component.ownerRepairDetailInfo.roomId,
                        communityId:vc.getCurrentCommunity().communityId,
                        page:1,
                        row:1
                    }
                };
                //查询房屋信息 业主信息
               vc.http.get('ownerRepairManage',
                            'getRoom',
                             param,
                             function(json,res){
                                if(res.status == 200){
                                    var _roomInfos=JSON.parse(json);
                                    if(!_roomInfos.hasOwnProperty("rooms")){
                                         vc.toast("非法操作，未找到房屋信息");
                                         //vc.jumpToPage('/flow/ownerFlow');
                                         return ;
                                    }
                                    var _roomInfo = _roomInfos.rooms[0];
                                    vc.component.ownerRepairManageInfo.roomName= _roomInfo.floorNum+"号楼 "+_roomInfo.unitNum+"单元 "+_roomInfo.roomNum + "室";
                                }else{
                                     vc.message("非法操作，未找到房屋信息");
                                     vc.jumpToPage('/flow/ownerFlow');
                                }
                             },function(errInfo,error){
                                console.log('请求失败处理');
                                vc.message("非法操作，未找到房屋信息");
                                vc.jumpToPage('/flow/ownerFlow');
                             }
                 );
            }
        }
    });

})(window.vc);
