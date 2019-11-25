/**
    权限组
**/
(function(vc){
    var _fileUrl = '/callComponent/download/getFile/fileByObjId';
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
                idCard:"",
                link:"",
                ownerPhoto:"/img/noPhoto.gif",
                showCallBackButton:$props.showCallBackButton
            }
        },
        _initMethod:function(){
            vc.component._loadOwnerInfo();
        },
        _initEvent:function(){
            vc.on('viewOwnerInfo','onIndex',function(_index){
                /*if(_index == 2){
                   vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewOwnerInfo);
                }*/
            });

            vc.on('viewOwnerInfo','callBackOwnerInfo',function(_info){
                vc.emit($props.callBackListener,$props.callBackFunction,vc.component.viewOwnerInfo);
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
                                //加载图片
                                vc.component._loadOwnerPhoto();
                             },function(errInfo,error){
                                console.log('请求失败处理');
                             }
                           );

            },
            _callBackListOwner:function(_ownerId){
                vc.jumpToPage("/flow/ownerFlow?ownerId="+_ownerId);
            },
            _loadOwnerPhoto:function(){
                vc.component.viewOwnerInfo.ownerPhoto = _fileUrl+"?objId="+
                               vc.component.viewOwnerInfo.ownerId +"&communityId="+vc.getCurrentCommunity().communityId+"&fileTypeCd=10000&time="+new Date();
            },
            errorLoadImg:function(){
                vc.component.viewOwnerInfo.ownerPhoto="/img/noPhoto.gif";
            }

        }
    });

})(window.vc);