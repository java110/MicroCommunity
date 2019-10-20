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
            vc.component._listOwnerRepairs(DEFAULT_PAGE, DEFAULT_ROWS);
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
