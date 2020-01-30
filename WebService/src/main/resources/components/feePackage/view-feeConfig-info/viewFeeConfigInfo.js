/**
    费用项 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewFeeConfigInfo:{
                index:0,
                flowComponent:'viewFeeConfigInfo',
                feeTypeCd:'',
feeName:'',
feeFlag:'',
startTime:'',
endTime:'',
computingFormula:'',
squarePrice:'',
additionalAmount:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadFeeConfigInfoData();
        },
        _initEvent:function(){
            vc.on('viewFeeConfigInfo','chooseFeeConfig',function(_app){
                vc.copyObject(_app, vc.component.viewFeeConfigInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewFeeConfigInfo);
            });

            vc.on('viewFeeConfigInfo', 'onIndex', function(_index){
                vc.component.viewFeeConfigInfo.index = _index;
            });

        },
        methods:{

            _openSelectFeeConfigInfoModel(){
                vc.emit('chooseFeeConfig','openChooseFeeConfigModel',{});
            },
            _openAddFeeConfigInfoModel(){
                vc.emit('addFeeConfig','openAddFeeConfigModal',{});
            },
            _loadFeeConfigInfoData:function(){

            }
        }
    });

})(window.vc);
