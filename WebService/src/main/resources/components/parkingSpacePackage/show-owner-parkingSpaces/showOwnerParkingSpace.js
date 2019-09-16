/**
    权限组
**/
(function(vc){

    vc.extends({
        propTypes: {
               deleteOwnerParkingSpaceFlag:vc.propTypes.string='false'
        },
        data:{
            showOwnerParkingSpaceInfo:{
                ownerId:'',
                parkingSpaces:[],
                deleteOwnerParkingSpaceFlag:$props.deleteOwnerParkingSpaceFlag
            }
        },
        _initMethod:function(){
            //加载 业主信息
            var _ownerId = vc.getParam('ownerId')
            if(!vc.notNull(_ownerId)){
                return ;
            }

            vc.component.showOwnerParkingSpaceInfo.ownerId = _ownerId;

            vc.component.loadParkingSpaces();
        },
        _initEvent:function(){
            vc.on('showOwnerParkingSpace','notify',function(_owner){
                vc.component.showOwnerParkingSpaceInfo.ownerId = _owner.ownerId;

                //查询 根据业主查询房屋信息
                vc.component.loadParkingSpaces();
            });

        },
        methods:{

            loadParkingSpaces:function(){
                var param = {
                    params:{
                        communityId:vc.getCurrentCommunity().communityId,
                        ownerId:vc.component.showOwnerParkingSpaceInfo.ownerId
                    }
                };

                //发送get请求
                vc.http.get('showOwnerParkingSpace',
                            'list',
                             param,
                             function(json){
                                var _parkingSpaceInfo = JSON.parse(json);
                                vc.component.showOwnerParkingSpaceInfo.parkingSpaces = _parkingSpaceInfo.parkingSpaces;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },

            ownerExitParkingSpaceModel:function(_psId){
                vc.emit('ownerExitParkingSpace','openExitParkingSpaceModel',{
                    ownerId:vc.component.showOwnerParkingSpaceInfo.ownerId,
                    psId:_psId
                });
            },

            showState:function(_state){
                if(_state == '2001'){
                    return "房屋已售";
                }else if(_state == '2002'){
                    return "房屋未售";
                }else if(_state == '2003'){
                    return "已交定金";
                }
                else if(_state == '2004'){
                    return "已出租";
                }else{
                    return "未知";
                }
            }
        }
    });

})(window.vc);