(function (vc) {

    vc.extends({
        propTypes: {
            callBackListener: vc.propTypes.string, //父组件名称
            callBackFunction: vc.propTypes.string //父组件监听方法
        },
        data: {
            addOrgInfo: {
                orgId: '',
                orgName: '',
                orgLevel: '',
                parentOrgId: '',
                description: '',
                belongCommunityId: '',
                parentOrg:[],
                belongCommunitys:[]

            }
        },
        watch: {
            "addOrgInfo.orgLevel":
                {//深度监听，可监听到对象、数组的变化
                    handler(val, oldVal) {
                        vc.component._addOrgListParentOrgInfo();
                    }
                    ,
                    deep: true
                }
        }
        ,
        _initMethod: function () {

        }
        ,
        _initEvent: function () {
            vc.on('addOrg', 'openAddOrgModal', function (_param) {
                if (_param.hasOwnProperty('parentOrgId')) {
                    vc.component.addOrgInfo.parentOrgId = _param.parentOrgId;
                    vc.component.addOrgInfo.orgLevel = _param.orgLevel;
                    if(_param.orgLevel == 3){ // 部门是不能改小区的，是依赖分公司的小区信息
                        vc.component.addOrgInfo.belongCommunityId = _param.belongCommunityId;
                    }
                }
                //查询入驻的小区
                vc.component._loadAddEnterCommunitys();
                $('#addOrgModel').modal('show');
            });
        }
        ,
        methods: {
            addOrgValidate() {
                return vc.validate.validate({
                    addOrgInfo: vc.component.addOrgInfo
                }, {
                    'addOrgInfo.orgName': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "组织名称不能为空"
                        },
                        {
                            limit: "maxin",
                            param: "2,50",
                            errInfo: "组织名称长度为2至50"
                        },
                    ],
                    'addOrgInfo.orgLevel': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "组织级别不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "组织级别错误"
                        },
                    ],
                    'addOrgInfo.parentOrgId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "上级ID不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "上级ID不正确"
                        },
                    ],
                    'addOrgInfo.description': [

                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "描述不能为空"
                        },
                    ],


                });
            }
            ,
            saveOrgInfo: function () {
                if (!vc.component.addOrgValidate()) {
                    vc.toast(vc.validate.errInfo);

                    return;
                }

                vc.component.addOrgInfo.communityId = vc.getCurrentCommunity().communityId;
                //不提交数据将数据 回调给侦听处理
                if (vc.notNull($props.callBackListener)) {
                    vc.emit($props.callBackListener, $props.callBackFunction, vc.component.addOrgInfo);
                    $('#addOrgModel').modal('hide');
                    return;
                }

                vc.http.post(
                    'addOrg',
                    'save',
                    JSON.stringify(vc.component.addOrgInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#addOrgModel').modal('hide');
                            vc.component.clearAddOrgInfo();
                            vc.emit('orgManage', 'listOrg', {});

                            return;
                        }
                        vc.message(json);

                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);

                    });
            }
            ,
            clearAddOrgInfo: function () {
                vc.component.addOrgInfo = {
                    orgName: '',
                    orgLevel: '',
                    parentOrgId: '',
                    description: '',
                    parentOrg: [],
                    belongCommunityId: '',
                    communityId: '',
                };
            }
            ,
            _addOrgListParentOrgInfo: function () {


                var _tmpOrgLevel = vc.component.addOrgInfo.orgLevel;

                if (_tmpOrgLevel > 1) {
                    _tmpOrgLevel = _tmpOrgLevel - 1;
                }

                var param = {
                    params: {
                        orgLevel: _tmpOrgLevel,
                        page: 1,
                        row: 30,
                    }
                };

                //发送get请求
                vc.http.get('addOrg',
                    'list',
                    param,
                    function (json, res) {
                        var _orgManageInfo = JSON.parse(json);
                        vc.component.addOrgInfo.parentOrg = _orgManageInfo.orgs;
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _loadAddEnterCommunitys:function () {
                //belongCommunitys
                var param = {
                    params:{
                        _uid:'mmmllnnjhhjjh'
                    }
                }

                //发送get请求
                vc.http.get('addOrg',
                    'listEnterCommunitys',
                    param,
                    function (json, res) {
                        var _enterCommunitys = JSON.parse(json);
                        vc.component.addOrgInfo.belongCommunitys = _enterCommunitys;
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            }
        }
    })
    ;

})(window.vc);
