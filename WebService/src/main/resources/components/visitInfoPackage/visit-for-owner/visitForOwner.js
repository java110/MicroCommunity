/**
 权限组
 **/
(function(vc){

    vc.extends({
        propTypes: {
            callBackListener:vc.propTypes.string,
            callBackFunction:vc.propTypes.string,
            showCallBackButton:vc.propTypes.string='false'
        },
        data:{
            viewOwnerInfo:{
                flowComponent:'viewOwnerInfo',
                ownerId:"",
                name:"",
                age:"",
                sex:"",
                userName:"",
                remark:"",
                link:""
            },
            showCallBackButton:'true'
        },
        _initMethod:function(){
            vc.component._loadOwnerInfo();
        },
        _initEvent:function(){
            vc.on('visitForOwner','onIndex',function(_index){
                console.log("侦听到visitForOwner的index为  "+_index);
                vc.emit('addVisitSpace', 'notify', _index);
                /*if(_index == 2){
                   vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewOwnerInfo);
                }*/
            });

            vc.on('visitForOwner','ownerInfo',function(_info){
                vc.component.viewOwnerInfo=_info;
                console.log(_info);
                vc.emit('addVisitSpace','ownerId',vc.component.viewOwnerInfo.ownerId);
            });

        },
        methods:{

            _loadOwnerInfo:function(){
                //加载 业主信息
                var _ownerId = vc.getParam('ownerId')

                if(!vc.notNull(_ownerId)){
                    return ;
                }

                var param = {
                    params:{
                        ownerId:_ownerId,
                        page:1,
                        row:1,
                        communityId:vc.getCurrentCommunity().communityId,
                        ownerTypeCd:'1001'
                    }
                }

                //发送get请求
                vc.http.get('viewOwner',
                    'getOwner',
                    param,
                    function(json,res){
                        var listOwnerData =JSON.parse(json);
                        vc.copyObject(listOwnerData.owners[0],vc.component.viewOwnerInfo);
                    },function(errInfo,error){
                        console.log('请求失败处理');
                    }
                );

            },
            _openSearchOwnerModel:function(_ownerId){
                vc.emit('searchOwner','openSearchOwnerModel',{});
            }

        }
    });

})(window.vc);