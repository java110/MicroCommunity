(function(vc,vm){

    vc.extends({
        data:{
            deleteNoticeInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteNotice','openDeleteNoticeModal',function(_params){

                vc.component.deleteNoticeInfo = _params;
                $('#deleteNoticeModel').modal('show');

            });
        },
        methods:{
            deleteNotice:function(){
                vc.component.deleteNoticeInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteNotice',
                    'delete',
                    JSON.stringify(vc.component.deleteNoticeInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteNoticeModel').modal('hide');
                            vc.emit('noticeManage','listNotice',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteNoticeModel:function(){
                $('#deleteNoticeModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
