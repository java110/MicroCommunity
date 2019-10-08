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
            addVisitInfo:{
                vName:'',
                visitGender:'',
                phoneNumber:''
            }
        },
        _initMethod:function(){

        },
        _initEvent:function(){
            vc.on('addVisit','openAddVisitAppModal',function(_app){
                $("#addNewVisitModel").modal("show");
            });

            vc.on('addVisit', 'onIndex', function(_index){
                // vc.component.newVisitInfo.index = _index;
                vc.emit('addVisitSpace', 'notify', _index);
            });

        },
        methods:{
            addAppValidate() {
                return vc.validate.validate({
                    addVisitInfo: vc.component.addVisitInfo
                }, {
                    'addVisitInfo.vName': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "访客姓名不能为空"
                        }
                    ],
                    'addVisitInfo.visitGender': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "访客性别不能为空"
                        }
                    ],
                    'addVisitInfo.phoneNumber': [
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
                vc.component.addVisitInfo.communityId = vc.getCurrentCommunity().communityId;
                vc.emit("viewVisitInfo", "addNewVisit", vc.component.addVisitInfo);
                $('#addNewVisitModel').modal('hide');
            },
            _openAddAppInfoModel(){
                vc.emit('addApp','openAddAppModal',{});
            },
            _loadAppInfoData:function(){

            }
        }
    });

})(window.vc);