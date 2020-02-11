(function(vc){

    vc.extends({
        data:{
            ownerRepairDetailInfo:{
                repairId:'',
                repairType:'',
                repairName:'',
                tel:'',
                roomId:'',
                roomName:'',
                appointmentTime:'',
                context:'',

            }
        },
         _initMethod:function(){
            
         },
         _initEvent:function(){
            vc.on('ownerRepairDetail','openOwnerRepairDetailModal',function(_ownerInfo){
                vc.component.clearOwnerRepairDetailInfo();
                vc.copyObject(_ownerInfo,vc.component.ownerRepairDetailInfo);
                $('#ownerRepairDetailModel').modal('show');
            });
        },
        methods:{
            clearOwnerRepairDetailInfo:function(){
                vc.component.ownerRepairDetailInfo = {
                        repairId:'',
                        repairType:'',
                        repairName:'',
                        tel:'',
                        roomId:'',
                        roomName:'',
                        appointmentTime:'',
                        context:'',
                    };
            }
        }
    });

})(window.vc);
