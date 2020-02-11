/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            repairDispatchManageInfo:{
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
                    ownerId:'',
                    state:''
                }
            }
        },
        _initMethod:function(){
            vc.component._listOwnerRepairs(DEFAULT_PAGE, DEFAULT_ROWS);
            //vc.component._validateParam();
        },
        _initEvent:function(){
            
            vc.on('repairDispatchManage','listOwnerRepair',function(_param){
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
                vc.component.repairDispatchManageInfo.conditions.roomId = _roomId;
                vc.component.repairDispatchManageInfo.conditions.ownerId = _ownerId;
                var param={
                    params:{
                        roomId:vc.component.repairDispatchManageInfo.conditions.roomId,
                        communityId:vc.getCurrentCommunity().communityId,
                        page:1,
                        row:1
                    }
                };
                //查询房屋信息 业主信息
               vc.http.get('repairDispatchManage',
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
                                    vc.component.repairDispatchManageInfo.conditions.roomName= _roomInfo.floorNum+"号楼 "+_roomInfo.unitNum+"单元 "+_roomInfo.roomNum + "室";
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
                vc.component.repairDispatchManageInfo.conditions.page = _page;
                vc.component.repairDispatchManageInfo.conditions.row = _rows;
                vc.component.repairDispatchManageInfo.conditions.communityId = vc.getCurrentCommunity().communityId;
                var param = {
                    params:vc.component.repairDispatchManageInfo.conditions
               };

               //发送get请求
               vc.http.get('repairDispatchManage',
                            'list',
                             param,
                             function(json,res){
                                var _repairDispatchManageInfo=JSON.parse(json);
                                vc.component.repairDispatchManageInfo.total = _repairDispatchManageInfo.total;
                                vc.component.repairDispatchManageInfo.records = _repairDispatchManageInfo.records;
                                vc.component.repairDispatchManageInfo.ownerRepairs = _repairDispatchManageInfo.ownerRepairs;
                                vc.emit('pagination','init',{
                                     total:vc.component.repairDispatchManageInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _queryOwnerRepairMethod:function(){
                vc.component._listOwnerRepairs(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _openDispatchRepair:function(_ownerRepair){
                 vc.jumpToPage('/flow/repairDispatchStepFlow?repairId=' + _ownerRepair.repairId);

            },
            _openDispatchRepairDetail:function(_ownerRepair){
                vc.emit('ownerRepairDetail','openOwnerRepairDetailModal',_ownerRepair);
            },
            _moreCondition:function(){
                if(vc.component.repairDispatchManageInfo.moreCondition){
                    vc.component.repairDispatchManageInfo.moreCondition = false;
                }else{
                    vc.component.repairDispatchManageInfo.moreCondition = true;
                }
            }

             
        }
    });
})(window.vc);
