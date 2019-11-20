(function(vc){
    var _fileUrl = 'https://hc.demo.winqi.cn/callComponent/download/getFile/fileByObjId';
    vc.extends({
        propTypes: {
            notifyLoadDataComponentName:vc.propTypes.string
        },
        data:{
            editOwnerInfo:{
                ownerId:'',
                memberId:'',
                name:'',
                age:'',
                link:'',
                sex:'',
                remark:'',
                ownerPhoto:'',
                videoPlaying:false
            }
        },
         _initMethod:function(){

         },
         _initEvent:function(){
            vc.on('editOwner','openEditOwnerModal',function(_owner){
                vc.copyObject(_owner,vc.component.editOwnerInfo);
                //根据memberId 查询 照片信息
                vc.component.editOwnerInfo.ownerPhoto = _fileUrl+"?objId="+
                   vc.component.editOwnerInfo.memberId +"&communityId="+vc.getCurrentCommunity().communityId+"&fileTypeCd=10000&time="+new Date();
                $('#editOwnerModel').modal('show');
                vc.component._initAddOwnerMediaForEdit();
            });
        },
        methods:{
            editOwnerValidate(){
                return vc.validate.validate({
                    editOwnerInfo:vc.component.editOwnerInfo
                },{
                   'editOwnerInfo.name':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"名称不能为空"
                        },
                        {
                            limit:"maxin",
                            param:"2,10",
                            errInfo:"名称长度必须在2位至10位"
                        },
                    ],
                    'editOwnerInfo.age':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"年龄不能为空"
                        },
                        {
                            limit:"num",
                            param:"",
                            errInfo:"年龄不是有效的数字"
                        },
                    ],
                    'editOwnerInfo.sex':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"性别不能为空"
                        }
                    ],
                    'editOwnerInfo.link':[
                        {
                            limit:"required",
                            param:"",
                            errInfo:"手机号不能为空"
                        },
                        {
                            limit:"phone",
                            param:"",
                            errInfo:"不是有效的手机号"
                        }
                    ],
                    'editOwnerInfo.remark':[

                        {
                            limit:"maxLength",
                            param:"200",
                            errInfo:"备注长度不能超过200位"
                        }
                    ]

                });
            },

            editOwnerMethod:function(){

                if(!vc.component.editOwnerValidate()){
                    vc.message(vc.validate.errInfo);

                    return ;
                }

                vc.component.editOwnerInfo.communityId = vc.getCurrentCommunity().communityId;

                //编辑时 ownerPhoto 中内容不是照片内容，则清空
                if(vc.component.editOwnerInfo.ownerPhoto.indexOf(_fileUrl) == -1){
                    vc.component.editOwnerInfo.ownerPhoto = "";
                }
                vc.http.post(
                    'editOwner',
                    'changeOwner',
                    JSON.stringify(vc.component.editOwnerInfo),
                    {
                        emulateJSON:true
                     },
                     function(json,res){
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if(res.status == 200){
                            //关闭model
                            $('#editOwnerModel').modal('hide');
                            vc.component.clearEditOwnerInfo();
                            vc.emit($props.notifyLoadDataComponentName,'listOwnerData',{});

                            return ;
                        }
                        vc.message(json);

                     },
                     function(errInfo,error){
                        console.log('请求失败处理');
                        vc.message(errInfo);
                     });
            },
            clearEditOwnerInfo:function(){
                vc.component.editOwnerInfo = {
                    ownerId:'',
                    memberId:'',
                    name:'',
                    age:'',
                    link:'',
                    sex:'',
                    remark:'',
                    ownerPhoto:'',
                    videoPlaying:false
                };
            },
            _editUserMedia:function() {
                return navigator.getUserMedia = navigator.getUserMedia ||
                    navigator.webkitGetUserMedia ||
                    navigator.mozGetUserMedia ||
                    navigator.msGetUserMedia || null;
            },
            _initAddOwnerMediaForEdit:function () {
                if(vc.component._editUserMedia()){
                    vc.component.editOwnerInfo.videoPlaying = false;
                    var constraints = {
                        video: true,
                        audio: false
                    };
                    var video = document.getElementById('ownerPhotoForEdit');
                    var media = navigator.getUserMedia(constraints, function (stream) {
                        var url = window.URL || window.webkitURL;
                        //video.src = url ? url.createObjectURL(stream) : stream;
                        try {
                            video.src = url ? url.createObjectURL(stream) : stream;
                        } catch (error) {
                            video.srcObject = stream;
                        }
                        video.play();
                        vc.component.editOwnerInfo.videoPlaying = true;
                    }, function (error) {
                        console.log("ERROR");
                        console.log(error);
                    });
                }else{
                    console.log("初始化视频失败");
                }
            },
            _takePhotoForEdit:function () {
                if (vc.component.editOwnerInfo.videoPlaying) {
                    var canvas = document.getElementById('canvasForEdit');
                    var video = document.getElementById('ownerPhotoForEdit');
                    canvas.width = video.videoWidth;
                    canvas.height = video.videoHeight;
                    canvas.getContext('2d').drawImage(video, 0, 0);
                    var data = canvas.toDataURL('image/webp');
                    vc.component.editOwnerInfo.ownerPhoto = data;
                    //document.getElementById('photo').setAttribute('src', data);
                }
            }
        }
    });

})(window.vc);