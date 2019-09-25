/**
 权限组
 **/
(function(vc){

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data:{
            viewVisitInfo:{
                index:0,
                flowComponent:'visit',
                needShowAddAppButton:'true'
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('addVisit','openAddVisitAppModal',function(_app){
                $("#addNewVisitModel").modal("show");
            });

            // vc.on('viewVisitInfo', 'onIndex', function(_index){
            //     vc.component.viewVisitInfo.index = _index;
            // });

        },
        methods:{
            addAppValidate() {
                return vc.validate.validate({
                    viewVisitInfo: vc.component.viewVisitInfo
                }, {
                    'viewVisitInfo.name': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "访客姓名不能为空"
                        }
                    ],
                    'viewVisitInfo.visitGender': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "访客性别不能为空"
                        }
                    ],
                    'viewVisitInfo.phoneNumber': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "访客手机号不能为空"
                        },
                        {
                            limit: "phone",
                            param: "",
                            errInfo: "访客手机号不正确"
                        },
                    ]



                });
            },

            _addNewVisitInfo(){
                if (!vc.component.addAppValidate()) {
                    vc.message(vc.validate.errInfo);

                    return;
                }
                vc.component.addAppInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.emit($props.callBackListener, $props.callBackFunction, vc.component.viewVisitInfo);
                $('#addAppModel').modal('hide');
            },
            _openAddAppInfoModel(){
                vc.emit('addApp','openAddAppModal',{});
            },
            _loadAppInfoData:function(){

            }
        }
    });

})(window.vc);