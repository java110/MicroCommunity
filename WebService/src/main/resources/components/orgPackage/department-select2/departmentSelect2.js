(function (vc) {
    vc.extends({
        propTypes: {
            parentModal: vc.propTypes.string
        },
        data: {
            departmentSelect2Info: {
                orgs: [],
                orgId: '-1',
                orgName: '',
                companyId:'',
                departmentId:'',
                staffId: '',
                departmentSelector: {}
            }
        },
        watch: {
            departmentSelect2Info: {
                deep: true,
                handler: function () {
                    vc.emit($namespace, 'staffSelect2', "setStaff", this.departmentSelect2Info);
                }
            }
        },
        _initMethod: function () {
            this._initDepartmentSelect2();
        },
        _initEvent: function () {
            vc.on('departmentSelect2', 'setDepartment', function (_param) {
                vc.copyObject(_param, this.departmentSelect2Info);
                console.log("收到部门信息："+_param.orgName);
                var option = new Option(_param.orgName, _param.orgId, true, true);
                this.departmentSelect2Info.departmentSelector.append(option);
            });

            vc.on('departmentSelect2', 'clearDepartment', function (_param) {
                this.departmentSelect2Info = {
                    orgs: [],
                    orgId: '-1',
                    orgName: '',
                    companyId:'',
                    staffId: '',
                    departmentId:'',
                    departmentSelector: {}
                };
            });
        },
        methods: {
            _initDepartmentSelect2: function () {
                console.log("调用_initDepartmentSelect2 方法");
                $.fn.modal.Constructor.prototype.enforceFocus = function () {
                };
                $.fn.select2.defaults.set('width', '100%');
                this.departmentSelect2Info.departmentSelector = $('#departmentSelector').select2({
                    placeholder: '必填，请选择公司',
                    allowClear: true,//允许清空
                    escapeMarkup: function (markup) {
                        return markup;
                    }, // 自定义格式化防止xss注入
                    ajax: {
                        url: "/callComponent/orgManage/list",
                        dataType: 'json',
                        delay: 250,
                        data: function (params) {
                            console.log("param", params);
                            var _term = "";
                            if (params.hasOwnProperty("term")) {
                                _term = params.term;
                            }
                            return {
                                orgName: _term,
                                orgLevel:'3',
                                page: 1,
                                row: 10,
                                parentOrgId: this.departmentSelect2Info.orgId,
                                communityId: vc.getCurrentCommunity().communityId
                            };
                        },
                        processResults: function (data) {
                            console.log(data, this._filterOrgData(data.orgs));
                            return {
                                results: this._filterOrgData(data.orgs)
                            };
                        },
                        cache: true
                    }
                });
                $('#departmentSelector').on("select2:select", function (evt) {
                    //这里是选中触发的事件
                    //evt.params.data 是选中项的信息
                    console.log('select', evt);
                    this.departmentSelect2Info.orgId = evt.params.data.id;
                    this.departmentSelect2Info.departmentId = evt.params.data.id;
                    this.departmentSelect2Info.orgName = evt.params.data.text;
                });

                $('#departmentSelector').on("select2:unselect", function (evt) {
                    //这里是取消选中触发的事件
                    //如配置allowClear: true后，触发
                    console.log('unselect', evt);
                    this.departmentSelect2Info.orgId = '-1';
                    this.departmentSelect2Info.orgName = '';

                });
            },
            _filterOrgData: function (_Orgs) {
                var _tmpOrgs = [];
                for (var i = 0; i < _Orgs.length; i++) {
                    var _tmpOrg = {
                        id: _Orgs[i].orgId,
                        text: _Orgs[i].orgName
                    };
                    _tmpOrgs.push(_tmpOrg);
                }
                return _tmpOrgs;
            }
        }
    });
})(window.vc);
