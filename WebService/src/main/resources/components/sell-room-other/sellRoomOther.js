/**
    权限组
**/
(function(vc){

    vc.extends({
        data:{
            otherInfo:{
                ownerId:"",
                state:"",
                remark:""
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('sellRoomOther','listOwnerData',function(_owner){
                //vc.component.ownerInfo = _owner;
                vc.component.otherInfo.ownerId = ownerId;
            });

        },
        methods:{


        }
    });

})(window.vc);