/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROW = 10;
    vc.extends({
        data:{
            parkingSpaceUnits:[],
            parkingSpaceCreateFeeInfo:{
                parkingSpaces:[],
                total:0,
                records:1,
                floorId:'',
                unitId:'',
                state:'',
                num:'',
                moreCondition:false,
                conditions:{
                    psId:'',
                    num:'',
                    carNum:''
                }
            }
        },
        _initMethod:function(){
            vc.component.listParkingSpace(DEFAULT_PAGE,DEFAULT_ROW);
        },
        _initEvent:function(){

            vc.on('pagination','page_event',function(_currentPage){
                vc.component.listParkingSpace(_currentPage,DEFAULT_ROW);
            });
        },
        methods:{

            listParkingSpace:function(_page,_row){

                vc.component.parkingSpaceCreateFeeInfo.conditions.page=_page;
                vc.component.parkingSpaceCreateFeeInfo.conditions.row=_row;
                vc.component.parkingSpaceCreateFeeInfo.conditions.communityId = vc.getCurrentCommunity().communityId;
                var param = {
                    params:vc.component.parkingSpaceCreateFeeInfo.conditions
                };

               //发送get请求
               vc.http.get('parkingSpaceCreateFee',
                            'listParkingSpace',
                             param,
                             function(json,res){
                                var listParkingSpaceData =JSON.parse(json);

                                vc.component.parkingSpaceCreateFeeInfo.total = listParkingSpaceData.total;
                                vc.component.parkingSpaceCreateFeeInfo.records = listParkingSpaceData.records;
                                vc.component.parkingSpaceCreateFeeInfo.parkingSpaces = listParkingSpaceData.parkingSpaces;

                                vc.emit('pagination','init',{
                                    total:vc.component.parkingSpaceCreateFeeInfo.records,
                                    dataCount: vc.component.parkingSpaceCreateFeeInfo.total,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openParkingSpaceCreateFeeAddModal:function(_parkingSpace,_isMore){
                vc.emit('parkingSpaceCreateFeeAdd', 'openParkingSpaceCreateFeeAddModal',{
                    isMore:_isMore,
                    parkingSpace:_parkingSpace
                });
            },
            _openViewParkingSpaceCreateFee:function(_parkingSpace){
                 vc.jumpToPage("/flow/listParkingSpaceFeeFlow?"+vc.objToGetParam(_parkingSpace));
            },
            _queryParkingSpaceMethod:function(){
                vc.component.listParkingSpace(DEFAULT_PAGE,DEFAULT_ROW);
            },

            _moreCondition:function(){
                if(vc.component.parkingSpaceCreateFeeInfo.moreCondition){
                    vc.component.parkingSpaceCreateFeeInfo.moreCondition = false;
                }else{
                    vc.component.parkingSpaceCreateFeeInfo.moreCondition = true;
                }
            }

        }
    });
})(window.vc);