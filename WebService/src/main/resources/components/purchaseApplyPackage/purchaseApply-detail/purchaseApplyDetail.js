/**
 入驻小区
 **/
(function(vc){
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 1;
    vc.extends({
        data:{
            purchaseApplyDetailInfo:{
                    resourceNames:'',
                    state:'',
                    totalPrice:'',
                    applyOrderId:'',
                    description:'',
                    createTime:'',
                    userName:'',
                    stateName:'',
                    purchaseApplyDetailVo:[]
            }
        },
        _initMethod:function(){
            var _applyOrderId = vc.getParam('applyOrderId');
            if(!vc.notNull(_applyOrderId)){
                return ;
            }
            vc.component.purchaseApplyDetailInfo.applyOrderId = _applyOrderId;
            vc.component._listPurchaseApply(DEFAULT_PAGE, DEFAULT_ROWS);
        },
        _initEvent:function(){

        },
        methods:{
            _listPurchaseApply:function(_page, _rows){
                var param = {
                    params:{
                        page:_page,
                        row:_rows,
                        applyOrderId:vc.component.purchaseApplyDetailInfo.applyOrderId,
                        resOrderType:'10000'
                    }
                };

                //发送get请求
                vc.http.get('purchaseApplyManage',
                    'list',
                    param,
                    function(json,res){
                        var _purchaseApplyDetailInfo=JSON.parse(json);

                        var _purchaseApply = _purchaseApplyDetailInfo.purchaseApplys;
                        vc.component.purchaseApplyDetailInfo = _purchaseApply[0];
                    },function(errInfo,error){
                        console.log('请求失败处理');
                    }
                );
            },
            _openEditPurchaseApplyDetailModel:function (resourceStore) {

            },
            _openDeletePurchaseApplyDetailModel:function (resourceStore) {

            }

        }
    });
})(window.vc);
