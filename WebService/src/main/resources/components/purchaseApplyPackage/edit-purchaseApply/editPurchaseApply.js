(function(vc,vm){

    vc.extends({
        data:{
            editPurchaseApplyInfo:{
                applyOrderId:'',
state:'',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('editPurchaseApply','openEditPurchaseApplyModal',function(_params){
                vc.component.refreshEditPurchaseApplyInfo();
                $('#editPurchaseApplyModel').modal('show');
                vc.copyObject(_params, vc.component.editPurchaseApplyInfo );
                vc.component.editPurchaseApplyInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            editPurchaseApplyValidate:function(){
                        return vc.validate.validate({
                            editPurchaseApplyInfo:vc.component.editPurchaseApplyInfo
                        },{
                            'editPurchaseApplyInfo.state':[
{
                            limit:"required",
                            param:"",
                            errInfo:"订单状态不能为空"
                        },
 {
                            limit:"num",
                            param:"",
                            errInfo:"计算公式格式错误"
                        },
                    ],
'editPurchaseApplyInfo.applyOrderId':[
{
                            limit:"required",
                            param:"",
                            errInfo:"订单号不能为空"
                        }]

                        });
             },
            editPurchaseApply:function(){
                if(!vc.component.editPurchaseApplyValidate()){
                    vc.toast(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'editPurchaseApply',
                    'update',
                    JSON.stringify(vc.component.editPurchaseApplyInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editPurchaseApplyModel').modal('hide');
                             vc.emit('purchaseApplyManage','listPurchaseApply',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });
            },
            refreshEditPurchaseApplyInfo:function(){
                vc.component.editPurchaseApplyInfo= {
                  applyOrderId:'',
state:'',

                }
            }
        }
    });

})(window.vc,window.vc.component);
