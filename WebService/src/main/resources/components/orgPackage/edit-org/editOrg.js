(function (vc, vm) {

    vc.extends({
        data: {
            editOrgInfo: {
                orgId: '',
                orgName: '',
                orgLevel: '',
                parentOrgId: '',
                belongCommunityId: '',
                description: '',
                parentOrg: [],
                belongCommunitys:[]

            }
        },
        watch: {
            "editOrgInfo.orgLevel": {//深度监听，可监听到对象、数组的变化
                handler(val, oldVal) {
                    vc.component._editOrgListParentOrgInfo();
                },
                deep: true
            }
        },
        _initMethod: function () {
            vc.component._editOrgListParentOrgInfo();
        },
        _initEvent: function () {
            vc.on('editOrg', 'openEditOrgModal', function (_params) {
                vc.component.refreshEditOrgInfo();
                vc.component._loadEditEnterCommunitys();
                $('#editOrgModel').modal('show');
                vc.copyObject(_params, vc.component.editOrgInfo);
                //vc.component.editOrgInfo.communityId = vc.component.editOrgInfo.belongCommunityId;
            });
        },
        methods: {
            editOrgValidate: function () {
                return vc.validate.validate({
                    editOrgInfo: vc.component.editOrgInfo
                }, {
                    'editOrgInfo.orgName': [
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
                    'editOrgInfo.orgLevel': [
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
                    'editOrgInfo.parentOrgId': [
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
                    'editOrgInfo.description': [

                        {
                            limit: "maxLength",
                            param: "200",
                            errInfo: "描述不能为空"
                        },
                    ],
                    'editOrgInfo.belongCommunityId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "隶属小区不能为空"
                        },
                        {
                            limit: "num",
                            param: "",
                            errInfo: "隶属小区不正确"
                        },
                    ],
                    'editOrgInfo.orgId': [
                        {
                            limit: "required",
                            param: "",
                            errInfo: "组织ID不能为空"
                        }]

                });
            },
            editOrg: function () {
                if (!vc.component.editOrgValidate()) {
                    vc.toast(vc.validate.errInfo);
                    return;
                }

                vc.http.post(
                    'editOrg',
                    'update',
                    JSON.stringify(vc.component.editOrgInfo),
                    {
                        emulateJSON: true
                    },
                    function (json, res) {
                        //vm.menus = vm.refreshMenuActive(JSON.parse(json),0);
                        if (res.status == 200) {
                            //关闭model
                            $('#editOrgModel').modal('hide');
                            vc.emit('orgManage', 'listOrg', {});
                            return;
                        }
                        vc.message(json);
                    },
                    function (errInfo, error) {
                        console.log('请求失败处理');

                        vc.message(errInfo);
                    });
            },
            refreshEditOrgInfo: function () {
                vc.component.editOrgInfo = {
                    orgId: '',
                    orgName: '',
                    orgLevel: '',
                    parentOrgId: '',
                    description: '',
                    belongCommunityId: '',
                    communityId: '',
                    parentOrg: [],
                    belongCommunitys:[]
                }
            },
            _editOrgListParentOrgInfo: function () {


                var _tmpOrgLevel = vc.component.editOrgInfo.orgLevel;

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
                vc.http.get('editOrg',
                    'list',
                    param,
                    function (json, res) {
                        var _orgManageInfo = JSON.parse(json);
                        vc.component.editOrgInfo.parentOrg = _orgManageInfo.orgs;
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            _loadEditEnterCommunitys: function () {
                //belongCommunitys
                var param = {
                    params: {
                        _uid: 'mmmllnnjhhjjh'
                    }
                }

                //发送get请求
                vc.http.get('editOrg',
                    'listEnterCommunitys',
                    param,
                    function (json, res) {
                        var _enterCommunitys = JSON.parse(json);
                        vc.component.editOrgInfo.belongCommunitys = _enterCommunitys;
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            }
        }
    });

})(window.vc, window.vc.component);
