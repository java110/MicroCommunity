(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data:{
            listOwnerInfo:{
                owners:[],
                total:0,
                records:1,
                _currentOwnerId:'',
                conditions:{
                    ownerTypeCd:'1001',
                    ownerId:'',
                    name:'',
                    link:''
                }
            }
        },
        _initMethod:function(){
            //加载 业主信息
            var _ownerId = vc.getParam('ownerId')

            if(vc.notNull(_ownerId)){
                vc.component.listOwnerInfo.conditions.ownerId = _ownerId;
            }
            vc.component._listOwnerData(DEFAULT_PAGE,DEFAULT_ROWS);
        },
        _initEvent:function(){
            vc.on('listOwner','listOwnerData',function(){
                vc.component._listOwnerData(DEFAULT_PAGE,DEFAULT_ROWS);
            });
            vc.on('pagination','page_event',function(_currentPage){
                vc.component._listOwnerData(_currentPage,DEFAULT_ROWS);
            });

            vc.on('listOwner','chooseRoom',function(_room){
                 vc.jumpToPage("/flow/propertyFeeFlow?ownerId="+_currentOwnerId+"&roomId="+_room.roomId);
            });
        },
        methods:{
            _listOwnerData:function(_page,_row){

                vc.component.listOwnerInfo.conditions.page= _page;
                vc.component.listOwnerInfo.conditions.row= _row;
                vc.component.listOwnerInfo.conditions.communityId= vc.getCurrentCommunity().communityId;
                var param = {
                    params:vc.component.listOwnerInfo.conditions
                }

               //发送get请求
               vc.http.get('listOwner',
                            'list',
                             param,
                             function(json,res){
                                var listOwnerData =JSON.parse(json);

                                vc.component.listOwnerInfo.total = listOwnerData.total;
                                vc.component.listOwnerInfo.records = listOwnerData.records;
                                vc.component.listOwnerInfo.owners = listOwnerData.owners;

                                vc.emit('pagination','init',{
                                    total:vc.component.listOwnerInfo.records,
                                    currentPage:_page
                                });
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );

            },
            _openAddOwnerModal:function(){ //打开添加框
                vc.emit('addOwner','openAddOwnerModal',-1);
                //vc.jumpToPage("/flow/addOwnerBindingFlow");
            },
            _openDelOwnerModel:function(_owner){ // 打开删除对话框
                vc.emit('deleteOwner','openOwnerModel',_owner);
            },
            _openEditOwnerModel:function(_owner){
                vc.emit('editOwner','openEditOwnerModal',_owner);
            },
            _queryOwnerMethod:function(){
                vc.component._listOwnerData(DEFAULT_PAGE,DEFAULT_ROWS);
            },
            _openAddOwnerRoom:function(_owner){
                vc.jumpToPage("/flow/addOwnerRoomBindingFlow?ownerId="+_owner.ownerId);
            },
            _openHireParkingSpace:function(_owner){
                vc.jumpToPage("/flow/hireParkingSpaceFlow?ownerId="+_owner.ownerId);
            },
            _openSellParkingSpace:function(_owner){
                vc.jumpToPage("/flow/sellParkingSpaceFlow?ownerId="+_owner.ownerId);
            },
            _openOwnerDetailModel:function(_owner){
                vc.jumpToPage("/flow/ownerDetailFlow?ownerId="+_owner.ownerId);
            },
            _openDeleteOwnerRoom:function(_owner){
                vc.jumpToPage("/flow/deleteOwnerRoomFlow?ownerId="+_owner.ownerId);
            },
            _openPayPropertyFee:function(_owner){
                //查看 业主是否有多套房屋，如果有多套房屋，则提示对话框选择，只有一套房屋则直接跳转至交费页面缴费

                vc.component.listOwnerInfo._currentOwnerId = _owner.ownerId; // 暂存如果有多个房屋是回调回来时 ownerId 会丢掉
                var param = {
                    params:{
                        communityId:vc.getCurrentCommunity().communityId,
                        ownerId:_owner.ownerId
                    }
                }
               vc.http.get('listOwner',
                            'getRooms',
                             param,
                             function(json,res){
                                var listRoomData =JSON.parse(json);

                                vc.component.listRoomData.total = listRoomData.total;
                                vc.component.listRoomData.records = listRoomData.records;
                                vc.component.listRoomData.rooms = listRoomData.rooms;
                                if(rooms.length == 1){
                                      vc.jumpToPage("/flow/propertyFeeFlow?ownerId="+_owner.ownerId+"&roomId="+rooms[0].roomId);
                                }else if(rooms.length == 0){
                                    vc.message("当前业主未查询到房屋信息");
                                }else{
                                    vc.emit('listRoom','',listRoomData);
                                }
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            }
        }
    })
})(window.vc);