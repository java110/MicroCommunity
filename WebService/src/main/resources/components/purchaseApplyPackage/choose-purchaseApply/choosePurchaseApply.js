(function(vc){
    vc.extends({
        propTypes: {
           emitChoosePurchaseApply:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            choosePurchaseApplyInfo:{
                purchaseApplys:[],
                _currentPurchaseApplyName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('choosePurchaseApply','openChoosePurchaseApplyModel',function(_param){
                $('#choosePurchaseApplyModel').modal('show');
                vc.component._refreshChoosePurchaseApplyInfo();
                vc.component._loadAllPurchaseApplyInfo(1,10,'');
            });
        },
        methods:{
            _loadAllPurchaseApplyInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('choosePurchaseApply',
                            'list',
                             param,
                             function(json){
                                var _purchaseApplyInfo = JSON.parse(json);
                                vc.component.choosePurchaseApplyInfo.purchaseApplys = _purchaseApplyInfo.purchaseApplys;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            choosePurchaseApply:function(_purchaseApply){
                if(_purchaseApply.hasOwnProperty('name')){
                     _purchaseApply.purchaseApplyName = _purchaseApply.name;
                }
                vc.emit($props.emitChoosePurchaseApply,'choosePurchaseApply',_purchaseApply);
                vc.emit($props.emitLoadData,'listPurchaseApplyData',{
                    purchaseApplyId:_purchaseApply.purchaseApplyId
                });
                $('#choosePurchaseApplyModel').modal('hide');
            },
            queryPurchaseApplys:function(){
                vc.component._loadAllPurchaseApplyInfo(1,10,vc.component.choosePurchaseApplyInfo._currentPurchaseApplyName);
            },
            _refreshChoosePurchaseApplyInfo:function(){
                vc.component.choosePurchaseApplyInfo._currentPurchaseApplyName = "";
            }
        }

    });
})(window.vc);
