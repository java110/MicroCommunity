(function(vc){
    vc.extends({
        propTypes: {
                notifyLoadDataComponentName:vc.propTypes.string
        },
        data:{
            deleteBusinessTypeInfo:{}
        },
        _initEvent:function(){
             vc.on('deleteBusinessType','openBusinessTypeModel',function(_BusinessTypeInfo){
                    vc.component.deleteBusinessTypeInfo = _BusinessTypeInfo;
                    $('#deleteBusinessTypeModel').modal('show');
                });
        },
        methods:{
            closeDeleteOwnerModel:function(){
                $('#deleteBusinessTypeModel').modal('hide');
            },
            deleteOwner:function(){

                vc.component.deleteBusinessTypeInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.http.post(
                    'deleteBusinessType',
                    'delete',
                    JSON.stringify(vc.component.deleteBusinessTypeInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#deleteBusinessTypeModel').modal('hide');
                            vc.emit($props.notifyLoadDataComponentName,'listBusinessTypeData',{});
                            return ;
                        }
                        vc.component.deleteBusinessTypeInfo.errorInfo = json;
                     },
                     function(errInfo,error){
                        console.log('请求失败处理');

                        vc.component.deleteBusinessTypeInfo.errorInfo = errInfo;
                     });
            }
        }
    });
})(window.vc);