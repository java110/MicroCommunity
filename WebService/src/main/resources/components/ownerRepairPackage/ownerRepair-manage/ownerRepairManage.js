/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            ownerRepairManageInfo:{
                ownerRepairs:[],
                total:0,
                records:1,
                moreCondition:false,
                repairName:'',
                conditions:{
                    repairId:'',
                    repairName:'',
                    tel:'',
                    repairType:'',
                    roomId:'',
                    roomName:'',
                    ownerId:''
                }
            }
        },
        _initMethod:function(){
            //vc.component._listOwnerRepairs(DEFAULT_PAGE, DEFAULT_ROWS);
            vc.component._validateParam();
        },
        _initEvent:function(){
            
            vc.on('ownerRepairManage','listOwnerRepair',function(_param){
                  vc.component._listOwnerRepairs(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listOwnerRepairs(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _validateParam:function(){
                var _ownerId = vc.getParam('ownerId')
                var _roomId = vc.getParam('roomId')

                if(!vc.notNull(_roomId)){
                    vc.message("非法操作，未找到房屋信息");
                    vc.jumpToPage('/flow/ownerFlow');
                    return ;
                }
                vc.component.ownerRepairManageInfo.conditions.roomId = _roomId;
                vc.component.ownerRepairManageInfo.conditions.ownerId = _ownerId;
                var param={
                    params:{
                        roomId:vc.component.ownerRepairManageInfo.conditions.roomId,
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
                                         vc.message("非法操作，未找到房屋信息");
                                         vc.jumpToPage('/flow/ownerFlow');
                                         return ;
                                    }
                                    var _roomInfo = _roomInfos.rooms[0];
                                    vc.component.ownerRepairManageInfo.conditions.roomName= _roomInfo.floorNum+"号楼 "+_roomInfo.unitNum+"单元 "+_roomInfo.roomNum + "室";
                                    vc.component._listOwnerRepairs(DEFAULT_PAGE, DEFAULT_ROWS);
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
            },
            _listOwnerRepairs:function(_page, _rows){
                vc.component.ownerRepairManageInfo.conditions.page = _page;
                vc.component.ownerRepairManageInfo.conditions.row = _rows;
                vc.component.ownerRepairManageInfo.conditions.communityId = vc.getCurrentCommunity().communityId;
                var param = {
                    params:vc.component.ownerRepairManageInfo.conditions
               };

               //发送get请求
               vc.http.get('ownerRepairManage',
                            'list',
                             param,
                             function(json,res){
                                var _ownerRepairManageInfo=JSON.parse(json);
                                vc.component.ownerRepairManageInfo.total = _ownerRepairManageInfo.total;
                                vc.component.ownerRepairManageInfo.records = _ownerRepairManageInfo.records;
                                vc.component.ownerRepairManageInfo.ownerRepairs = _ownerRepairManageInfo.ownerRepairs;
                                vc.emit('pagination','init',{
                                     total:vc.component.ownerRepairManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddOwnerRepairModal:function(){
                vc.emit('addOwnerRepair','openAddOwnerRepairModal',vc.component.ownerRepairManageInfo.conditions);
            },
            _openEditOwnerRepairModel:function(_ownerRepair){
                _ownerRepair.roomName = vc.component.ownerRepairManageInfo.conditions.roomName;
                _ownerRepair.roomId = vc.component.ownerRepairManageInfo.conditions.roomId;
                vc.emit('editOwnerRepair','openEditOwnerRepairModal',_ownerRepair);
            },
            _openDeleteOwnerRepairModel:function(_ownerRepair){
                vc.emit('deleteOwnerRepair','openDeleteOwnerRepairModal',_ownerRepair);
            },
            _queryOwnerRepairMethod:function(){
                vc.component._listOwnerRepairs(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition:function(){
                if(vc.component.ownerRepairManageInfo.moreCondition){
                    vc.component.ownerRepairManageInfo.moreCondition = false;
                }else{
                    vc.component.ownerRepairManageInfo.moreCondition = true;
                }
            }

             
        }
    });
})(window.vc);
