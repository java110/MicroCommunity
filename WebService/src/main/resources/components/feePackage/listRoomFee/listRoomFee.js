(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            listRoomCreateFeeInfo:{
                fees:[],
                roomName:'',
                roomId:'',
                total: 0,
                records: 1,
            }
        },
        _initMethod:function(){
            if(vc.notNull(vc.getParam("roomNum"))){
                  vc.component.listRoomCreateFeeInfo.roomName = vc.getParam('floorNum')+"号楼"+vc.getParam('unitNum')+"单元"+vc.getParam("roomNum")+"室";
                  vc.component.listRoomCreateFeeInfo.roomId = vc.getParam('roomId');
            };
            vc.component._loadListRoomCreateFeeInfo(1,10);
        },
        _initEvent:function(){
            vc.on('listRoomFee','notify',function(_param){
                vc.component._loadListRoomCreateFeeInfo(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event',
                function(_currentPage) {
                    vc.component._loadListRoomCreateFeeInfo(_currentPage, DEFAULT_ROWS);
                });
        },
        methods:{
            _loadListRoomCreateFeeInfo:function(_page,_row){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        payerObjId:vc.component.listRoomCreateFeeInfo.roomId
                    }
                };

                //发送get请求
               vc.http.get('listRoomFee',
                            'list',
                             param,
                             function(json){
                                var _feeConfigInfo = JSON.parse(json);
                                vc.component.listRoomCreateFeeInfo.total = _feeConfigInfo.total;
                                vc.component.listRoomCreateFeeInfo.records = _feeConfigInfo.records;
                                vc.component.listRoomCreateFeeInfo.fees = _feeConfigInfo.fees;
                                vc.emit('pagination', 'init', {
                                    total: _feeConfigInfo.records,
                                    currentPage: _page
                                });
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            _payFee:function(_fee){
               vc.jumpToPage('/flow/payFeeOrderFlow?'+vc.objToGetParam(_fee));
            },
            _payFeeHis:function(_fee){
               vc.jumpToPage('/flow/propertyFeeFlow?'+vc.objToGetParam(_fee));
            },
            _deleteFee:function(_fee){

                var dateA = new Date(_fee.startTime);
                var dateB = new Date();
                if(dateA.setHours(0, 0, 0, 0) != dateB.setHours(0, 0, 0, 0)){
                    vc.toast("只能取消当天添加的费用");
                    return;
                }

                vc.emit('deleteFee','openDeleteFeeModal',{
                         communityId:vc.getCurrentCommunity().communityId,
                         feeId:_fee.feeId
                });
            },
            _refreshListRoomCreateFeeInfo:function(){
                vc.component.listRoomCreateFeeInfo._currentFeeConfigName = "";
            }
        }

    });
})(window.vc);
