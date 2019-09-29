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
                sexName:''
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('memberSelectOwner','chooseOwner',function(_owner){
                vc.copyObject(_owner,vc.component.ownerInfo);
                vc.component.ownerInfo.sexName = (vc.component.ownerInfo.sex == 0 ? '男' : '女');
            });

        },
        methods:{

            openSearchOwnerModel(){
                vc.emit('searchOwner','openSearchOwnerModel',{});
            },
            openAddMemberModel(){
                vc.emit('addOwner','openAddOwnerModal',vc.component.ownerInfo.ownerId
                );
            }
        }
    });

})(window.vc);