/**
    楼 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewFloorInfo:{
                index:0,
                flowComponent:'viewFloorInfo',
                floorId:'',
                floorName:'',
                floorNum:'',
                remark:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadFloorInfoData();
        },
        _initEvent:function(){
            vc.on('viewFloorInfo','chooseFloor',function(_app){
                vc.copyObject(_app, vc.component.viewFloorInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewFloorInfo);
                vc.emit('chooseUnit', 'onFloorInfo', {
                    floorId: vc.component.viewFloorInfo.floorId
                });
                vc.emit('viewUnitInfo', 'onFloorInfo', {
                    floorId: vc.component.viewFloorInfo.floorId
                });
            });

            vc.on('viewFloorInfo', 'onIndex', function(_index){
                vc.component.viewFloorInfo.index = _index;
            });

        },
        methods:{

            _openSelectFloorInfoModel(){
                vc.emit('chooseFloor','openChooseFloorModel',{});
            },
            _openAddFloorInfoModel(){
                vc.emit('addFloor','openAddFloorModal',{});
            },
            _loadFloorInfoData:function(){

            }
        }
    });

})(window.vc);
