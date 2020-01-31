/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROW = 10;
    vc.extends({
        data:{
            roomUnits:[],
            roomCreateFeeInfo:{
                rooms:[],
                total:0,
                records:1,
                floorId:'',
                unitId:'',
                state:'',
                roomNum:'',
                moreCondition:false,
                conditions:{
                    floorId:'',
                    floorName:'',
                    unitId:'',
                    roomNum:'',
                    roomId:'',
                    state:'',
                    section:''
                }
            }
        },
        _initMethod:function(){
            vc.component.roomCreateFeeInfo.conditions.floorId = vc.getParam("floorId");
            vc.component.roomCreateFeeInfo.conditions.floorName = vc.getParam("floorName");
            vc.component.listRoom(DEFAULT_PAGE,DEFAULT_ROW);
        },
        _initEvent:function(){
            vc.on('room','chooseFloor',function(_param){
                vc.component.roomCreateFeeInfo.conditions.floorId = _param.floorId;
                vc.component.roomCreateFeeInfo.conditions.floorName = _param.floorName;
                vc.component.loadUnits(_param.floorId);

            });
            vc.on('pagination','page_event',function(_currentPage){
                vc.component.listRoom(_currentPage,DEFAULT_ROW);
            });
        },
        methods:{

            listRoom:function(_page,_row){

                vc.component.roomCreateFeeInfo.conditions.page=_page;
                vc.component.roomCreateFeeInfo.conditions.row=_row;
                vc.component.roomCreateFeeInfo.conditions.communityId = vc.getCurrentCommunity().communityId;
                var param = {
                    params:vc.component.roomCreateFeeInfo.conditions
                };

               //发送get请求
               vc.http.get('roomCreateFee',
                            'listRoom',
                             param,
                             function(json,res){
                                var listRoomData =JSON.parse(json);

                                vc.component.roomCreateFeeInfo.total = listRoomData.total;
                                vc.component.roomCreateFeeInfo.records = listRoomData.records;
                                vc.component.roomCreateFeeInfo.rooms = listRoomData.rooms;

                                vc.emit('pagination','init',{
                                    total:vc.component.roomCreateFeeInfo.records,
                                    dataCount: vc.component.roomCreateFeeInfo.total,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openRoomCreateFeeAddModal:function(_room,_isMore){
                vc.emit('roomCreateFeeAdd', 'openRoomCreateFeeAddModal',{
                    isMore:_isMore,
                    room:_room
                });
            },
            _openEditRoomModel:function(_room){
                //_room.floorId = vc.component.roomCreateFeeInfo.conditions.floorId;
                vc.emit('editRoom','openEditRoomModal',_room);
            },
            _openDelRoomModel:function(_room){
                 //_room.floorId = vc.component.roomCreateFeeInfo.conditions.floorId;
                 vc.emit('deleteRoom','openRoomModel',_room);
            },
            /**
                根据楼ID加载房屋
            **/
            loadUnits:function(_floorId){
                vc.component.addRoomUnits = [];
                var param = {
                    params:{
                        floorId:_floorId,
                        communityId:vc.getCurrentCommunity().communityId
                    }
                }
                vc.http.get(
                    'roomCreateFee',
                    'loadUnits',
                     param,
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            var tmpUnits = JSON.parse(json);
                            vc.component.roomUnits = tmpUnits;

                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });
            },
            _queryRoomMethod:function(){
                vc.component.listRoom(DEFAULT_PAGE,DEFAULT_ROW);
            },
            _loadDataByParam: function(){
                vc.component.roomCreateFeeInfo.conditions.floorId = vc.getParam("floorId");
                vc.component.roomCreateFeeInfo.conditions.floorId = vc.getParam("floorName");
                //如果 floodId 没有传 则，直接结束
               /* if(!vc.notNull(vc.component.roomCreateFeeInfo.conditions.floorId)){
                    return ;
                }*/

                var param = {
                    params:{
                        communityId:vc.getCurrentCommunity().communityId,
                        floorId:vc.component.roomCreateFeeInfo.conditions.floorId
                    }
                }

                vc.http.get(
                    'roomCreateFee',
                    'loadFloor',
                     param,
                     function(json,res){
                        if(res.status == 200){
                            var _floorInfo = JSON.parse(json);
                            var _tmpFloor = _floorInfo.apiFloorDataVoList[0];
                            /*vc.emit('roomSelectFloor','chooseFloor', _tmpFloor);
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
                if(vc.component.roomCreateFeeInfo.moreCondition){
                    vc.component.roomCreateFeeInfo.moreCondition = false;
                }else{
                    vc.component.roomCreateFeeInfo.moreCondition = true;
                }
            },
            _openChooseFloorMethod:function(){
                vc.emit('searchFloor','openSearchFloorModel',{});
            }
        }
    });
})(window.vc);