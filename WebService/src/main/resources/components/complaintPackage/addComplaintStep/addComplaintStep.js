/**
 入驻小区
 **/
(function (vc) {
    vc.extends({
        data: {
            addComplaintStepInfo: {
                $step: {},
                index: 0,
                infos: []
            }
        },
        _initMethod: function () {
            vc.component._initStep();
        },
        _initEvent: function () {
            vc.on("addComplaintStep", "notify", function (_info) {
                vc.component.addComplaintStepInfo.infos[vc.component.addComplaintStepInfo.index] = _info;
            });

        },
        methods: {
            _initStep: function () {
                vc.component.addComplaintStepInfo.$step = $("#step");
                vc.component.addComplaintStepInfo.$step.step({
                    index: 0,
                    time: 500,
                    title: ["选择楼栋", "选择房屋", "投诉建议"]
                });
                vc.component.addComplaintStepInfo.index = vc.component.addComplaintStepInfo.$step.getIndex();
            },
            _prevStep: function () {
                vc.component.addComplaintStepInfo.$step.prevStep();
                vc.component.addComplaintStepInfo.index = vc.component.addComplaintStepInfo.$step.getIndex();

                vc.emit('viewFloorInfo', 'onIndex', vc.component.addComplaintStepInfo.index);
                vc.emit('sellRoomSelectRoom', 'onIndex', vc.component.addComplaintStepInfo.index);
                vc.emit('addComplain', 'onIndex', vc.component.addComplaintStepInfo.index);

            },
            _nextStep: function () {
                var _currentData = vc.component.addComplaintStepInfo.infos[vc.component.addComplaintStepInfo.index];
                if (_currentData == null || _currentData == undefined) {
                    vc.message("请选择或填写必选信息");
                    return;
                }
                vc.component.addComplaintStepInfo.$step.nextStep();
                vc.component.addComplaintStepInfo.index = vc.component.addComplaintStepInfo.$step.getIndex();

                vc.emit('viewFloorInfo', 'onIndex', vc.component.addComplaintStepInfo.index);
                vc.emit('sellRoomSelectRoom', 'onIndex', vc.component.addComplaintStepInfo.index);
                vc.emit('addComplain', 'onIndex', vc.component.addComplaintStepInfo.index);

            },
            _finishStep: function () {


                var _currentData = vc.component.addComplaintStepInfo.infos[vc.component.addComplaintStepInfo.index];
                if (_currentData == null || _currentData == undefined) {
                    vc.message("请选择或填写必选信息");
                    return;
                }

                var param = {
                    data: vc.component.addComplaintStepInfo.infos
                }

                vc.http.post(
                    'addComplaintStepBinding',
                    'binding',
                    JSON.stringify(param),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        if (res.status == 200) {

                            vc.message('处理成功', true);
                            //关闭model
                            vc.jumpToPage("/flow/complaintFlow?" + vc.objToGetParam(JSON.parse(json)));
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            }
        }
    });
})(window.vc);
