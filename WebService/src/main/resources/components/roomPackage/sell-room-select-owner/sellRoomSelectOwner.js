/**
    权限组
**/
(function(vc){

    vc.extends({
        propTypes: {
            callBackComponent:vc.propTypes.string
        },
        data:{
            ownerInfo:{
                ownerId:"",
                name:"",
                age:"",
                sex:"",
                userName:"",
                remark:"",
                link:"",
            }
        },
        _initMethod:function(){
            //根据请求参数查询 查询 业主信息
            vc.component.loadOwnerData();
        },
        _initEvent:function(){
            vc.on('sellRoomSelectOwner','chooseOwner',function(_owner){
                vc.component.ownerInfo = _owner;
                vc.emit($props.callBackComponent,'notify',_owner);
            });

        },
        methods:{

            openSearchOwnerModel(){
                vc.emit('searchOwner','openSearchOwnerModel',{});
            },
            loadOwnerData:function(){
               vc.component.ownerInfo.ownerId = vc.getParam("ownerId");
               vc.component.ownerInfo.name = vc.getParam("name");
               vc.component.ownerInfo.age = vc.getParam("age");
               vc.component.ownerInfo.sex = vc.getParam("sex");
               vc.component.ownerInfo.userName = vc.getParam("userName");
               vc.component.ownerInfo.link = vc.getParam("link");

               if(vc.component.ownerInfo.ownerId != ''){
                  vc.emit($props.callBackComponent,'notify',vc.component.ownerInfo);
               }
            }
        }
    });

})(window.vc);