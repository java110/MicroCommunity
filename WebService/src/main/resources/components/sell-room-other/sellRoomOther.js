/**
    权限组
**/
(function(vc){

    vc.extends({
        data:{
            otherInfo:{
                otherState:"",
                otherRemark:""
            }
        },
        watch:{
            otherInfo:{
                deep: true,
                handler:function(){
                    vc.emit('sellRoom','notify',vc.component.otherInfo);

                }
             }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('sellRoomOther','listOwnerData',function(_owner){
                //vc.component.ownerInfo = _owner;
                vc.component.otherInfo.ownerId = _owner.ownerId;
            });

        },
        methods:{


        }
    });

})(window.vc);