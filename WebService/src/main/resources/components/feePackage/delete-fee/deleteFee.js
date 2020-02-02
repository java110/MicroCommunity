(function(vc,vm){

    vc.extends({
        data:{
            deleteFeeInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteFee','openDeleteFeeModal',function(_params){

                vc.component.deleteFeeInfo = _params;
                $('#deleteFeeModel').modal('show');

            });
        },
        methods:{
            deleteFee:function(){
                vc.component.deleteFeeInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteFee',
                    'delete',
                    JSON.stringify(vc.component.deleteFeeInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteFeeModel').modal('hide');
                            vc.emit('listRoomFee','notify',{});
                            vc.emit('listParkingSpaceFee','notify',{});
                            vc.toast("删除费用成功");
                            return ;
                        }
                        vc.toast(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.toast(json);
                     });
            },
            closeDeleteFeeModel:function(){
                $('#deleteFeeModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
