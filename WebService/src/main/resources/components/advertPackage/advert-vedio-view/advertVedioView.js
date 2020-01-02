(function (vc) {
    var curr = 0;
    var _imagePlayTime = 10 * 1000;
    var myvideo = null;
    vc.extends({
        data: {
            advertVedioViewInfo: {
                imgUrl: '',
                notices: '',
                imgsAndVideos: [],
                words: '',
            }
        },
        _initMethod: function () {
            vc.component._loadAdvertVedioData();
            var h = document.documentElement.clientHeight;//获取页面可见高度
            $("#videoView").height(h + "px");//掐头去尾，减去100px
        },
        _initEvent: function () {

        },
        methods: {
            _loadAdvertVedioData: function () {
                var _machineCode = vc.getParam("machineCode");
                if (!vc.notNull(_machineCode)) {
                    vc.toast("请求参数中未包含设备编码");
                    return;
                }

                var _communityId = vc.getParam("communityId");
                if (!vc.notNull(_communityId)) {
                    vc.toast("请求参数中未包含小区信息");
                    return;
                }

                var param = {
                    params: {
                        machineCode: _machineCode,
                        communityId: _communityId
                    }
                };

                //发送get请求
                vc.http.get('advertVedioView',
                    'list',
                    param,
                    function (json) {
                        var _advertInfo = JSON.parse(json);
                        _advertInfo = vc.component._refreshAdvertActive(_advertInfo);
                        vc.component.startInterval(_advertInfo);
                    }, function () {
                        console.log('请求失败处理');
                    }
                );
            },
            startInterval: function (data) {
                vc.component.advertVedioViewInfo.imgsAndVideos = data;
                myvideo = document.getElementById("videoView")
                vc.component.handleImgVideoUrl(vc.component.advertVedioViewInfo.imgsAndVideos);
            },

            handleImgVideoUrl: function (_imgsAndVideos) {
                // 设置图片和视频播放
                // let iavSize = imgsAndVideos.length;
                var vList = [];
                // var vList = ['0:https://www.runoob.com/try/demo_source/movie.mp4','1:http://www.runoo.png']; // 播放列表
                for (var index = 0; index < _imgsAndVideos.length; index++) {
                    vList.push(_imgsAndVideos[index].suffix + ":" + _imgsAndVideos[index].url);
                }

                var vLen = vList.length;
                console.log("当前url", vList[curr]);
                if (vList[curr].indexOf('VIDEO:') >= 0) {
                    $("#imgView").hide();
                    $("#videoView").show();
                    var url = vList[curr].replace('VIDEO:', '');
                    $('#videoView').attr('src', url);
                    myvideo.load();
                    myvideo.play();
                    //curr++;
                } else {
                    var url = vList[curr].replace('JPEG:', '');
                    $("#imgView").attr("src", url);
                    $("#videoView").hide();
                    $("#imgView").show();
                    curr++;
                    // console.log("当前图片地址：1" );
                    setTimeout(function () {
                        if (curr >= vLen) {
                            curr = 0; //重新循环播放
                            vc.component._loadAdvertVedioData();
                            return;
                        }
                        console.log("当前图片地址：2" + vList[curr]);
                        vc.component.handleImgVideoUrl(_imgsAndVideos);
                    }, _imagePlayTime);
                }

                //视频播放完执行的方法
                myvideo.onended = function () {
                    if (vList[curr].indexOf('VIDEO:') >= 0) {
                        curr++;
                        if (curr >= vLen) {
                            curr = 0; //重新循环播放
                        }
                        vc.component.handleImgVideoUrl(_imgsAndVideos);
                    } else {
                        // 图片
                        // let imgUrl = prefixUrl.substring(2);
                        var url = vList[curr].replace('JPEG:', '');
                        $("#imgView").attr("src", url);
                        $("#videoView").hide();
                        $("#imgView").show();
                        //$('#videoView').get(0).pause();
                        console.log("当前图片地址：" + url);
                        curr++;

                        setTimeout(function () {
                            if (curr >= vLen) {
                                curr = 0; //重新循环播放
                                vc.component._loadAdvertVedioData();
                                return;
                            }
                            vc.component.handleImgVideoUrl(_imgsAndVideos);
                        }, _imagePlayTime);

                    }

                };
            },
            /**
             *
             * [{"suffix":"VIDEO","url":"/video/57a04507-2e3a-4409-8cc3-407383a36b8c.mp4","seq":"1"},{"suffix":"JPEG","url":"/callComponent/download/getFile/file?fileId=812019121575170004&communityId=702019120393220007","seq":"1"},{"suffix":"VIDEO","url":"/video/10481024-4d0d-4ff1-8c0d-d692159cd137.mp4","seq":"1"},{"suffix":"JPEG","url":"/callComponent/download/getFile/file?fileId=812019121565750002&communityId=702019120393220007","seq":"1"},{"suffix":"JPEG","url":"/callComponent/download/getFile/file?fileId=812019121517060006&communityId=702019120393220007","seq":"1"},{"suffix":"VIDEO","url":"/video/616286ed-6d16-40b5-9426-758ca43ca387.mp4","seq":"1"}]
             * @param _advertInfo
             * @returns {*}
             */
            _refreshAdvertActive: function (_advertInfo) {
                _advertInfo.sort(function (_child, _newChild) {
                    return _child.seq - _newChild.seq
                });

                return _advertInfo;
            },

        }

    })
    ;
})(window.vc);
