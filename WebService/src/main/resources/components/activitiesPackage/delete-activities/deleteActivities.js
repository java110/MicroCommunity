(function(vc,vm){

    vc.extends({
        data:{
            deleteActivitiesInfo:{

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('deleteActivities','openDeleteActivitiesModal',function(_params){

                vc.component.deleteActivitiesInfo = _params;
                $('#deleteActivitiesModel').modal('show');

            });
        },
        methods:{
            deleteActivities:function(){
                vc.component.deleteActivitiesInfo.communityId=vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteActivities',
                    'delete',
                    JSON.stringify(vc.component.deleteActivitiesInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteActivitiesModel').modal('hide');
                            vc.emit('activitiesManage','listActivities',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(json);

                     });
            },
            closeDeleteActivitiesModel:function(){
                $('#deleteActivitiesModel').modal('hide');
            }
        }
    });

})(window.vc,window.vc.component);
