/**
    权限组
**/
(function(vc){

    vc.extends({
        data:{
            roomInfo:{
                roomId:"",
                roomNum:"",
                unitNum:"",
                layer:"",
                section:"",
                apartment:"",
                builtUpArea:"",
                unitPrice:"",
                userName:"",
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('memberSelectOwner','chooseOwner',function(_owner){
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