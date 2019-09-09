/**
    入驻小区
**/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROW = 10;
    vc.extends({
        data:{
            roomUnits:[],
            roomInfo:{
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
            //根据 参数查询相应数据
            vc.component._loadDataByParam();
        },
        _initEvent:function(){
            vc.on('room','chooseFloor',function(_param){
                vc.component.roomInfo.conditions.floorId = _param.floorId;
                vc.component.roomInfo.conditions.floorName = _param.floorName;
                vc.component.loadUnits(_param.floorId);

            });
            vc.on('room','listRoom',function(_param){
                  vc.component.listRoom(DEFAULT_PAGE,DEFAULT_ROW);
            });
            vc.on('room','loadData',function(_param){
                  vc.component.listRoom(DEFAULT_PAGE,DEFAULT_ROW);
            });
            vc.on('pagination','page_event',function(_currentPage){
                vc.component.listRoom(_currentPage,DEFAULT_ROW);
            });
        },
        methods:{

            listRoom:function(_page,_row){

                vc.component.roomInfo.conditions.page=_page;
                vc.component.roomInfo.conditions.row=_row;
                vc.component.roomInfo.conditions.communityId = vc.getCurrentCommunity().communityId;
                var param = {
                    params:vc.component.roomInfo.conditions
                };

               //发送get请求
               vc.http.get('room',
                            'listRoom',
                             param,
                             function(json,res){
                                var listRoomData =JSON.parse(json);

                                vc.component.roomInfo.total = listRoomData.total;
                                vc.component.roomInfo.records = listRoomData.records;
                                vc.component.roomInfo.rooms = listRoomData.rooms;

                                vc.emit('pagination','init',{
                                    total:vc.component.roomInfo.records,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            _openAddRoom:function(){
                vc.jumpToPage("/flow/addRoomBindingFlow");
            },
            _openEditRoomModel:function(_room){
                _room.floorId = vc.component.roomInfo.conditions.floorId;
                vc.emit('editRoom','openEditRoomModal',_room);
            },
            _openDelRoomModel:function(_room){
                 _room.floorId = vc.component.roomInfo.conditions.floorId;
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
                    'room',
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
            showState:function(_state){
                if(_state == '2001'){
                    return "房屋已售";
                }else if(_state == '2002'){
                    return "房屋未售";
                }else if(_state == '2003'){
                    return "已交定金";
                }
                else if(_state == '2004'){
                    return "已出租";
                }else{
                    return "未知";
                }
            },
            _loadDataByParam: function(){
                vc.component.roomInfo.conditions.floorId = vc.getParam("floorId");
                //如果 floodId 没有传 则，直接结束
                /*if(!vc.notNull(vc.component.roomInfo.conditions.floorId)){
                    return ;
                }*/

                var param = {
                    params:{
                        communityId:vc.getCurrentCommunity().communityId,
                        floorId:vc.component.roomInfo.conditions.floorId
                    }
                }

                vc.http.get(
                    'room',
                    'loadFloor',
                     param,
                     function(json,res){
                        if(res.status == 200){
                            var _floorInfo = JSON.parse(json);
                            var _tmpFloor = _floorInfo.apiFloorDataVoList[0];
                            vc.emit('roomSelectFloor','chooseFloor', _tmpFloor);
                            vc.component._loadData({
                                floorId: _tmpFloor.floorId
                            });
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
                if(vc.component.roomInfo.moreCondition){
                    vc.component.roomInfo.moreCondition = false;
                }else{
                    vc.component.roomInfo.moreCondition = true;
                }
            },
            _openChooseFloorMethod:function(){
                vc.emit('searchFloor','openSearchFloorModel',{});
            }
        }
    });
})(window.vc);