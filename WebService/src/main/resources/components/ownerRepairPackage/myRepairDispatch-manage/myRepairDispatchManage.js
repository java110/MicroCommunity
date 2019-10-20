/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            myRepairDispatchInfo:{
                ownerRepairs:[],
                total:0,
                records:1,
                moreCondition:false,
                repairName:'',
                conditions:{
                    pageFlag:'myRepairDispatch',
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
            //vc.component._listOwnerRepairs(DEFAULT_PAGE, DEFAULT_ROWS);
            vc.component._validateParam();
        },
        _initEvent:function(){
            
            vc.on('myRepairDispatch','listOwnerRepair',function(_param){
                  vc.component._listOwnerRepairs(DEFAULT_PAGE, DEFAULT_ROWS);
            });
             vc.on('pagination','page_event',function(_currentPage){
                vc.component._listOwnerRepairs(_currentPage,DEFAULT_ROWS);
            });
        },
        methods:{
            _validateParam:function(){

                var param={
                    params:{
                        roomId:vc.component.myRepairDispatchInfo.conditions.roomId,
                        communityId:vc.getCurrentCommunity().communityId,
                        page:1,
                        row:1
                    }
                };
                //查询房屋信息 业主信息
               vc.http.get('myRepairDispatch',
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
                                    vc.component.myRepairDispatchInfo.conditions.roomName= _roomInfo.floorNum+"号楼 "+_roomInfo.unitNum+"单元 "+_roomInfo.roomNum + "室";
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
                vc.component.myRepairDispatchInfo.conditions.page = _page;
                vc.component.myRepairDispatchInfo.conditions.row = _rows;
                vc.component.myRepairDispatchInfo.conditions.communityId = vc.getCurrentCommunity().communityId;
                var param = {
                    params:vc.component.myRepairDispatchInfo.conditions
               };

               //发送get请求
               vc.http.get('myRepairDispatch',
                            'list',
                             param,
                             function(json,res){
                                var _myRepairDispatchInfo=JSON.parse(json);
                                vc.component.myRepairDispatchInfo.total = _myRepairDispatchInfo.total;
                                vc.component.myRepairDispatchInfo.records = _myRepairDispatchInfo.records;
                                vc.component.myRepairDispatchInfo.ownerRepairs = _myRepairDispatchInfo.ownerRepairs;
                                vc.emit('pagination','init',{
                                     total:vc.component.myRepairDispatchInfo.records,
                                     currentPage:_page
                                 });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openDealRepair:function(){
                vc.emit('addOwnerRepair','openAddOwnerRepairModal',vc.component.myRepairDispatchInfo.conditions);
            },
            _moreCondition:function(){
                if(vc.component.myRepairDispatchInfo.moreCondition){
                    vc.component.myRepairDispatchInfo.moreCondition = false;
                }else{
                    vc.component.myRepairDispatchInfo.moreCondition = true;
                }
            }

             
        }
    });
})(window.vc);
