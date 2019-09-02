/**
    单元 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewUnitInfo:{
                index:0,
                flowComponent:'viewUnitInfo',
                unitId:'',
                unitNum:'',
                layerCount:'',
                lift:'',
                remark:'',
                floorId:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadUnitInfoData();
        },
        _initEvent:function(){
            vc.on('viewUnitInfo','chooseUnit',function(_app){
                vc.copyObject(_app, vc.component.viewUnitInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewUnitInfo);
            });

            vc.on('viewUnitInfo', 'onIndex', function(_index){
                vc.component.viewUnitInfo.index = _index;
            });

            vc.on('viewUnitInfo','onFloorInfo',function(_param){
                vc.component.viewUnitInfo.floorId = _param.floorId;
            });

        },
        methods:{

            _openSelectUnitInfoModel(){
                vc.emit('chooseUnit','openChooseUnitModel',{});
            },
            _openAddUnitInfoModel(){
                vc.emit('addUnit','openAddUnitModal',{});
            },
            _loadUnitInfoData:function(){

            }
        }
    });

})(window.vc);
