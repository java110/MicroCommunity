(function(vc,vm){

    vc.extends({
        data:{
            editCommunityInfo:{
                communityId:'',
name:'',
address:'',
nearbyLandmarks:'',
cityCode:'0971',
mapX:'101.33',
mapY:'101.33',

            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
             vc.on('editCommunity','openCommunityModel',function(_params){
                vc.component.refreshEditCommunityInfo();
                $('#editCommunityModel').modal('show');
                vc.component.editCommunityInfo = _params;
                vc.component.editCommunityInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods:{
            editCommunityValidate:function(){
                        return vc.validate.validate({
                            editCommunityInfo:vc.component.editCommunityInfo
                        },{
                            @@addTemplateCodeValidate@@
                        });
             },
            editCommunity:function(){
                if(!vc.component.editCommunityValidate()){
                    vc.message(vc.validate.errInfo);
                    return ;
                }

                vc.http.post(
                    'editCommunity',
                    'update',
                    JSON.stringify(vc.component.editCommunityInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editCommunityModel').modal('hide');
                             vc.emit('communityManage','listCommunity',{});
                            return ;
                        }
                        vc.message(json);
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.message(errInfo);
                     });
            },
            refreshEditCommunityInfo:function(){
                vc.component.editCommunityInfo= {
                  communityId:'',
name:'',
address:'',
nearbyLandmarks:'',
cityCode:'0971',
mapX:'101.33',
mapY:'101.33',

                }
            }
        }
    });

})(window.vc,window.vc.component);
