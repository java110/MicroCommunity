/**
    采购申请 组件
**/
(function(vc){

    vc.extends({
        propTypes: {
           callBackListener:vc.propTypes.string, //父组件名称
           callBackFunction:vc.propTypes.string //父组件监听方法
        },
        data:{
            viewPurchaseApplyInfo:{
                index:0,
                flowComponent:'viewPurchaseApplyInfo',
                state:'',

            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component._loadPurchaseApplyInfoData();
        },
        _initEvent:function(){
            vc.on('viewPurchaseApplyInfo','choosePurchaseApply',function(_app){
                vc.copyObject(_app, vc.component.viewPurchaseApplyInfo);
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewPurchaseApplyInfo);
            });

            vc.on('viewPurchaseApplyInfo', 'onIndex', function(_index){
                vc.component.viewPurchaseApplyInfo.index = _index;
            });

        },
        methods:{

            _openSelectPurchaseApplyInfoModel(){
                vc.emit('choosePurchaseApply','openChoosePurchaseApplyModel',{});
            },
            _openAddPurchaseApplyInfoModel(){
                vc.emit('addPurchaseApply','openAddPurchaseApplyModal',{});
            },
            _loadPurchaseApplyInfoData:function(){

            }
        }
    });

})(window.vc);
