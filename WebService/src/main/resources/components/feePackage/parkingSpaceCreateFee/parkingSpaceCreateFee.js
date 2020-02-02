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
                parkingSpaceNum:'',
                moreCondition:false,
                conditions:{
                    floorId:'',
                    floorName:'',
                    unitId:'',
                    parkingSpaceNum:'',
                    psId:'',
                    state:'',
                    section:''
                }
            }
        },
        _initMethod:function(){
            vc.component.parkingSpaceCreateFeeInfo.conditions.floorId = vc.getParam("floorId");
            vc.component.parkingSpaceCreateFeeInfo.conditions.floorName = vc.getParam("floorName");
            vc.component.listParkingSpace(DEFAULT_PAGE,DEFAULT_ROW);
        },
        _initEvent:function(){
            vc.on('parkingSpace','chooseFloor',function(_param){
                vc.component.parkingSpaceCreateFeeInfo.conditions.floorId = _param.floorId;
                vc.component.parkingSpaceCreateFeeInfo.conditions.floorName = _param.floorName;
                vc.component.loadUnits(_param.floorId);

            });
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
            /**
                根据楼ID加载房屋
            **/
            loadUnits:function(_floorId){
                vc.component.addParkingSpaceUnits = [];
                var param = {
                    params:{
                        floorId:_floorId,
                        communityId:vc.getCurrentCommunity().communityId
                    }
                }
                vc.http.get(
                    'parkingSpaceCreateFee',
                    'loadUnits',
                     param,
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            var tmpUnits = JSON.parse(json);
                            vc.component.parkingSpaceUnits = tmpUnits;

                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });
            },
            _queryParkingSpaceMethod:function(){
                vc.component.listParkingSpace(DEFAULT_PAGE,DEFAULT_ROW);
            },
            _loadDataByParam: function(){
                vc.component.parkingSpaceCreateFeeInfo.conditions.floorId = vc.getParam("floorId");
                vc.component.parkingSpaceCreateFeeInfo.conditions.floorId = vc.getParam("floorName");
                //如果 floodId 没有传 则，直接结束
               /* if(!vc.notNull(vc.component.parkingSpaceCreateFeeInfo.conditions.floorId)){
                    return ;
                }*/

                var param = {
                    params:{
                        communityId:vc.getCurrentCommunity().communityId,
                        floorId:vc.component.parkingSpaceCreateFeeInfo.conditions.floorId
                    }
                }

                vc.http.get(
                    'parkingSpaceCreateFee',
                    'loadFloor',
                     param,
                     function(json,res){
                        if(res.status == 200){
                            var _floorInfo = JSON.parse(json);
                            var _tmpFloor = _floorInfo.apiFloorDataVoList[0];
                            /*vc.emit('parkingSpaceSelectFloor','chooseFloor', _tmpFloor);
                            */

                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });

            },
            _moreCondition:function(){
                if(vc.component.parkingSpaceCreateFeeInfo.moreCondition){
                    vc.component.parkingSpaceCreateFeeInfo.moreCondition = false;
                }else{
                    vc.component.parkingSpaceCreateFeeInfo.moreCondition = true;
                }
            },
            _openChooseFloorMethod:function(){
                vc.emit('searchFloor','openSearchFloorModel',{});
            }
        }
    });
})(window.vc);