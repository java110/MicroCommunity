(function (vc) {

    var photoUrl = '/callComponent/download/getFile/file';

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            uploadImageInfo: {
                photos: [],
            }
        },
        watch: {
            uploadImageInfo: {
                deep: true,
                handler: function () {
                    vc.emit($props.callBackListener, $props.callBackFunction, this.uploadImageInfo.photos);
                }
            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('uploadImage', 'openAddApplicationKeyModal', function () {

            });
            vc.on('uploadImage', 'clearImage', function () {
                this.uploadImageInfo = {
                    photos: []
                }
            });

            vc.on('uploadImage', 'notifyPhotos', function (_photos) {
                this.uploadImageInfo = {
                    photos: []
                };
                _photos.forEach(function (_photo) {
                    //?objId=772019092507000013&communityId=7020181217000001&fileTypeCd=10000
                    vc.urlToBase64(photoUrl + "?fileId=" + _photo + "&communityId=" + vc.getCurrentCommunity().communityId + "&time=" + new Date(), function (_base64Data) {
                        this.uploadImageInfo.photos.push(_base64Data);
                    })
                });
            });

        },
        methods: {
            _uploadPhoto: function (event) {
                $("#uploadImage").trigger("click")
            },
            _choosePhoto: function (event) {
                var photoFiles = event.target.files;
                if (photoFiles && photoFiles.length > 0) {
                    // 获取目前上传的文件
                    var file = photoFiles[0];// 文件大小校验的动作
                    if (file.size > 1024 * 1024 * 1) {
                        vc.toast("图片大小不能超过 2MB!")
                        return false;
                    }
                    var reader = new FileReader(); //新建FileReader对象
                    reader.readAsDataURL(file); //读取为base64
                    reader.onloadend = function (e) {
                        this.uploadImageInfo.photos.push(reader.result);
                    }
                }
                event.target.value = null;
            },
            _removeImage: function (_photo) {
                var _tmpPhotos = this.uploadImageInfo.photos;
                this.uploadImageInfo.photos = [];
                for (var _photoIndex = 0; _photoIndex < _tmpPhotos.length; _photoIndex++) {
                    if (_tmpPhotos[_photoIndex] != _photo) {
                        this.uploadImageInfo.photos.push(_tmpPhotos[_photoIndex]);
                    }
                }
            }
        }
    });

})(window.vc);