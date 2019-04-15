/**
    入驻小区
**/
(function(vc){
    vc.extends({
        data:{
            communityInfo:{
                enterCommunityInfo:[],
            }
        },
        _initMethod:function(){
            vc.component.listMyCommunity();
        },
        _initEvent:function(){

        },
        methods:{
            listMyCommunity:function(){
                var param = {
                    params:{
                        msg:this.message
                    }

               }
               //发送get请求
               vc.http.get('enterCommunity',
                            'listMyCommunity',
                             param,
                             function(json,res){
                                vc.component.communityInfo.enterCommunityInfo=JSON.parse(json);
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );
            },
            openEnterCommunity:function(){

            },
            _openDeleteStaffPrivilegeModel:function(_community){

            }
        }
    });
})(window.vc);