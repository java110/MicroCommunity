(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            listParkingSpaceCreateFeeInfo:{
                fees:[],
                parkingSpaceName:'',
                psId:'',
                total: 0,
                records: 1,
            }
        },
        _initMethod:function(){
            if(vc.notNull(vc.getParam("num"))){
                  vc.component.listParkingSpaceCreateFeeInfo.parkingSpaceName = vc.getParam('areaNum')+"号停车场"+vc.getParam('num')+"号车位";
                  vc.component.listParkingSpaceCreateFeeInfo.psId = vc.getParam('psId');
            };
            vc.component._loadListParkingSpaceCreateFeeInfo(1,10);
        },
        _initEvent:function(){
            vc.on('listParkingSpaceFee','notify',function(_param){
                vc.component._loadListParkingSpaceCreateFeeInfo(DEFAULT_PAGE, DEFAULT_ROWS);
            });
            vc.on('pagination', 'page_event',
                function(_currentPage) {
                    vc.component._loadListParkingSpaceCreateFeeInfo(_currentPage, DEFAULT_ROWS);
                });
        },
        methods:{
            _loadListParkingSpaceCreateFeeInfo:function(_page,_row){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        payerObjId:vc.component.listParkingSpaceCreateFeeInfo.psId
                    }
                };

                //发送get请求
               vc.http.get('listParkingSpaceFee',
                            'list',
                             param,
                             function(json){
                                var _feeConfigInfo = JSON.parse(json);
                                vc.component.listParkingSpaceCreateFeeInfo.total = _feeConfigInfo.total;
                                vc.component.listParkingSpaceCreateFeeInfo.records = _feeConfigInfo.records;
                                vc.component.listParkingSpaceCreateFeeInfo.fees = _feeConfigInfo.fees;
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
            _refreshListParkingSpaceCreateFeeInfo:function(){
                vc.component.listParkingSpaceCreateFeeInfo._currentFeeConfigName = "";
            }
        }

    });
})(window.vc);
