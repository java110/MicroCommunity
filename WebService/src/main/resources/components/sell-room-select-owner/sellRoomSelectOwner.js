/**
    权限组
**/
(function(vc){

    vc.extends({
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

        },
        _initEvent:function(){
            vc.on('sellRoomSelectOwner','chooseOwner',function(_owner){
                vc.component.ownerInfo = _owner;
            });

        },
        methods:{

            openSearchOwnerModel(){
                vc.emit('searchOwner','openSearchOwnerModel',{});
            }
        }
    });

})(window.vc);