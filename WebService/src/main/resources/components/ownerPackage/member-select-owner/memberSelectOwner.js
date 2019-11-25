/**
    权限组
**/
(function(vc){
    var _fileUrl = '/callComponent/download/getFile/fileByObjId';
    vc.extends({
        data:{
            ownerInfo:{
                ownerId:"",
                name:"",
                age:"",
                sex:"",
                userName:"",
                remark:"",
                idCard:"",
                link:"",
                ownerPhoto:"/img/noPhoto.gif",
                sexName:''
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('memberSelectOwner','chooseOwner',function(_owner){
                vc.copyObject(_owner,vc.component.ownerInfo);
                vc.component.ownerInfo.sexName = (vc.component.ownerInfo.sex == 0 ? '男' : '女');
                vc.component._loadOwnerPhoto();
            });

        },
        methods:{

            openSearchOwnerModel(){
                vc.emit('searchOwner','openSearchOwnerModel',{});
            },
            openAddMemberModel(){
                vc.emit('addOwner','openAddOwnerModal',vc.component.ownerInfo.ownerId
                );
            },
            _loadOwnerPhoto:function(){
                vc.component.ownerInfo.ownerPhoto = _fileUrl+"?objId="+
                               vc.component.ownerInfo.ownerId +"&communityId="+vc.getCurrentCommunity().communityId+"&fileTypeCd=10000&time="+new Date();
            },
            errorLoadImg:function(){
                vc.component.ownerInfo.ownerPhoto="/img/noPhoto.gif";
            }
        }
    });

})(window.vc);