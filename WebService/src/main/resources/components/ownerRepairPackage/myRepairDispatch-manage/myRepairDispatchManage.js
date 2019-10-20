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
                currentRepairId:'',
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
            vc.on('myRepairDispatch','notifyData',function(_param){
                vc.component._closeRepairDispatchOrder(_param);
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
            _openDealRepair:function(_ownerRepair){
                vc.component.myRepairDispatchInfo.currentRepairId = _ownerRepair.repairId;
                vc.emit('closeOrder','openCloseOrderModal',{});
            },
            _moreCondition:function(){
                if(vc.component.myRepairDispatchInfo.moreCondition){
                    vc.component.myRepairDispatchInfo.moreCondition = false;
                }else{
                    vc.component.myRepairDispatchInfo.moreCondition = true;
                }
            },
            _closeRepairDispatchOrder:function(_orderInfo){
                var _repairDispatchParam = {
                    repairId:vc.component.myRepairDispatchInfo.currentRepairId,
                    context:_orderInfo.remark,
                    communityId:vc.getCurrentCommunity().communityId
                };
                if(_orderInfo.state == '1100'){
                    _repairDispatchParam.state = '10002';
                }else{
                    _repairDispatchParam.state = '10003';
                }

               vc.http.post(
                   'myRepairDispatch',
                   'closeOrder',
                   JSON.stringify(_repairDispatchParam),
                   {
                       emulateJSON:true
                    },
                    function(json,res){
                       //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                       if(res.status == 200){
                           //关闭model
                           vc.component._listOwnerRepairs(DEFAULT_PAGE, DEFAULT_ROWS);
                           return ;
                       }
                       vc.message(json);

                    },
                    function(errInfo,error){
                       console.log('请求失败处理');

                       vc.message(errInfo);

                    });

            }

             
        }
    });
})(window.vc);
