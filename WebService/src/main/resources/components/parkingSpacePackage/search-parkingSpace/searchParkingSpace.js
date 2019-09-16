(function(vc){
    vc.extends({
        propTypes: {
           emitChooseParkingSpace:vc.propTypes.string,
           emitLoadData:vc.propTypes.string,
           parkingSpaceFlag:vc.propTypes.string, // 如果 S 表示查询售卖停车位 H 出租停车位 SH 查询出租和出售车位 F 表示查询未售卖未出租停车位
           showSearchCondition:vc.propTypes.string='true'
        },
        data:{
            searchParkingSpaceInfo:{
                parkingSpaces:[],
                total:0,
                records:1,
                num:'',
                carNum:'',
                psFlag:$props.parkingSpaceFlag,
                showSearchCondition:$props.showSearchCondition
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('searchParkingSpace','openSearchParkingSpaceModel',function(_param){
                console.log("打开业主成员界面")
                $('#searchParkingSpaceModel').modal('show');
                vc.component._refreshSearchParkingSpaceData();
                vc.component._loadAllParkingSpaceInfo(1,10);
            });

            vc.on('searchParkingSpace','showOwnerParkingSpaces',function(_parkingSpaces){
                $('#searchParkingSpaceModel').modal('show');
                vc.component.searchParkingSpaceInfo.parkingSpaces=_parkingSpaces;
            });
        },
        methods:{
            _loadAllParkingSpaceInfo:function(_page,_row){

                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        num:vc.component.searchParkingSpaceInfo.num,
                        carNum:vc.component.searchParkingSpaceInfo.carNum,
                        state:$props.parkingSpaceFlag
                    }
                };

                //发送get请求
               vc.http.get('searchParkingSpace',
                            'listParkingSpace',
                             param,
                             function(json){
                                var _parkingSpaceInfo = JSON.parse(json);
                                vc.component.searchParkingSpaceInfo.parkingSpaces = _parkingSpaceInfo.parkingSpaces;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseParkingSpace:function(_parkingSpace){
                vc.emit($props.emitChooseParkingSpace,'chooseParkingSpace',_parkingSpace);
                vc.emit($props.emitLoadData,'listParkingSpaceData',{
                    psId:_parkingSpace.psId
                });
                $('#searchParkingSpaceModel').modal('hide');
            },
            searchParkingSpaces:function(){
                vc.component._loadAllParkingSpaceInfo(1,15);
            },
            _refreshSearchParkingSpaceData:function(){
                vc.component.searchParkingSpaceInfo.num = "";
            },
            _viewParkingSpaceState:function(state){
                if(state == 'F'){
                    return "空闲";
                }else if(state == 'S'){
                    return "已售卖";
                }else if(state == 'H'){
                    return "已出租";
                }else{
                    return "未知";
                }
            },
        }

    });
})(window.vc);