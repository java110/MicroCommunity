(function(vc, vm) {

    vc.extends({
        data: {
            editParkingAreaInfo: {
                paId: '',
                num: '',
                typeCd: '',
                remark: '',

            }
        },
        _initMethod: function() {

},
        _initEvent: function() {
            vc.on('editParkingArea', 'openEditParkingAreaModal',
            function(_params) {
                vc.component.refreshEditParkingAreaInfo();
                $('#editParkingAreaModel').modal('show');
                vc.copyObject(_params, vc.component.editParkingAreaInfo);
                vc.component.editParkingAreaInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods: {
            editParkingAreaValidate: function() {
                return vc.validate.validate({
                    editParkingAreaInfo: vc.component.editParkingAreaInfo
                },
                {
                    'editParkingAreaInfo.num': [{
                        limit: "required",
                        param: "",
                        errInfo: "停车场编号不能为空"
                    },
                    {
                        limit: "maxin",
                        param: "1,12",
                        errInfo: "停车场编号不能超过12位"
                    },
                    ],
                    'editParkingAreaInfo.typeCd': [{
                        limit: "required",
                        param: "",
                        errInfo: "停车场类型不能为空"
                    },
                    {
                        limit: "num",
                        param: "",
                        errInfo: "停车场类型格式错误"
                    },
                    ],
                    'editParkingAreaInfo.remark': [
                    {
                        limit: "maxLength",
                        param: "4000",
                        errInfo: "备注太长"
                    },
                    ],
                    'editParkingAreaInfo.paId': [{
                        limit: "required",
                        param: "",
                        errInfo: "停车场ID不能为空"
                    }]

                });
            },
            editParkingArea: function() {
                vc.component.editParkingAreaInfo.communityId = vc.getCurrentCommunity().communityId;

                if (!vc.component.editParkingAreaValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }

                vc.http.post('editParkingArea', 'update', JSON.stringify(vc.component.editParkingAreaInfo), {
                    emulateJSON: true
                },
                function(json, res) {
                    //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                    if (res.status == 200) {
                        //关闭model
                        $('#editParkingAreaModel').modal('hide');
                        vc.emit('parkingAreaManage', 'listParkingArea', {});
                        return;
                    }
                    vc.message(json);
                },
                function(errInfo, error) {
                    console.log('请求失败处理');

                    vc.message(errInfo);
                });
            },
            refreshEditParkingAreaInfo: function() {
                vc.component.editParkingAreaInfo = {
                    paId: '',
                    num: '',
                    typeCd: '',
                    remark: '',

                }
            }
        }
    });

})(window.vc, window.vc.component);