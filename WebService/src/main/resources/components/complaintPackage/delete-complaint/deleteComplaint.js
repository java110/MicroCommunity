(function(vc,vm){

    vc.extends({
        data:{
            deleteComplaintInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteComplaint','openDeleteComplaintModal',function(_params){

                vc.component.deleteComplaintInfo = _params;
                $('#deleteComplaintModel').modal('show');

            });
        },
        methods:{
            deleteComplaint:function(){
                vc.component.deleteComplaintInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteComplaint',
                    'delete',
                    JSON.stringify(vc.component.deleteComplaintInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteComplaintModel').modal('hide');
                            vc.emit('complaintManage','listComplaint',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteComplaintModel:function(){
                $('#deleteComplaintModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
