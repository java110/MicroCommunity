(function (vc, vm) {

    vc.extends({
        data: {
            editComplaintInfo: {
                complaintId: '',
                typeCd: '',
                complaintName: '',
                tel: '',
                context: '',

            }
        },
        _initMethod: function () {

        },
        _initEvent: function () {
            vc.on('editComplaint', 'openEditComplaintModal', function (_params) {
                vc.component.refreshEditComplaintInfo();
                $('#editComplaintModel').modal('show');
                vc.copyObject(_params, vc.component.editComplaintInfo);
                vc.component.editComplaintInfo.communityId = vc.getCurrentCommunity().communityId;
            });
        },
        methods: {
            editComplaintValidate: function () {
                return vc.validate.validate({
                    editComplaintInfo: vc.component.editComplaintInfo
                }, {

                    'editComplaintInfo.typeCd': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "投诉类型不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "投诉类型格式错误"
                        },
                    ],

                    'editComplaintInfo.complaintName': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "投诉人不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "投诉人不能大于200位"
                        },
                    ],
                    'editComplaintInfo.tel': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "投诉电话不能为空"
                        },
                        {
                            limit: "phone",
                            param: "",
                            errInfo: "投诉电话格式错误"
                        },
                    ],
                    'editComplaintInfo.context': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "投诉内容不能为空"
                        },
                        {
                            limit: "maxLength",
                            param: "4000",
                            errInfo: "投诉状态超过4000位"
                        },
                    ],
                    'editComplaintInfo.complaintId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "投诉ID不能为空"
                        }]

                });
            },
            editComplaint: function () {
                if (!vc.component.editComplaintValidate()) {
                    vc.message(vc.validate.errInfo);
                    return;
                }

                vc.http.post(
                    'editComplaint',
                    'update',
                    JSON.stringify(vc.component.editComplaintInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#editComplaintModel').modal('hide');
                            vc.emit('complaintManage', 'listComplaint', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            },
            refreshEditComplaintInfo: function () {
                vc.component.editComplaintInfo = {
                    complaintId: '',
                    typeCd: '',
                    complaintName: '',
                    tel: '',
                    context: '',

                }
            }
        }
    });

})(window.vc, window.vc.component);
