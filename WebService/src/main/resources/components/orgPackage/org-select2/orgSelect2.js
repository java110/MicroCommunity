(function (vc) {
    vc.extends({
        propTypes: {
            parentModal: vc.propTypes.string
        },
        data: {
            orgSelect2Info: {
                orgs: [],
                orgId: '-1',
                orgName: '',
                companyId:'',
                orgSelector: {}
            }
        },
        watch: {
            orgSelect2Info: {
                deep: true,
                handler: function () {
                    vc.emit($namespace, 'departmentSelect2', "setDepartment", this.orgSelect2Info);
                }
            }
        },
        _initMethod: function () {
            this._initOrgSelect2();
        },
        _initEvent: function () {
            vc.on('orgSelect2', 'setOrg', function (_param) {
                vc.copyObject(_param, this.orgSelect2Info);
                var option = new Option(_param.orgName, _param.orgId, true, true);
                this.orgSelect2Info.orgSelector.append(option);
            });

            vc.on('orgSelect2', 'clearOrg', function (_param) {
                this.orgSelect2Info = {
                    orgs: [],
                    orgId: '-1',
                    orgName: '',
                    orgSelector: {}
                };
            });
        },
        methods: {
            _initOrgSelect2: function () {
                console.log("调用_initOrgSelect2方法");
                $.fn.modal.Constructor.prototype.enforceFocus = function () {};
                $.fn.select2.defaults.set('width', '100%');
                this.orgSelect2Info.orgSelector = $('#orgSelector').select2({
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
                                orgLevel:'2',
                                page: 1,
                                row: 10,
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
                $('#orgSelector').on("select2:select", function (evt) {
                    //这里是选中触发的事件
                    //evt.params.data 是选中项的信息
                    console.log('select', evt);
                    this.orgSelect2Info.orgId = evt.params.data.id;
                    this.orgSelect2Info.orgName = evt.params.data.text;
                    this.orgSelect2Info.companyId = evt.params.data.id;
                });

                $('#orgSelector').on("select2:unselect", function (evt) {
                    //这里是取消选中触发的事件
                    //如配置allowClear: true后，触发
                    console.log('unselect', evt);
                    this.orgSelect2Info.orgId = '-1';
                    this.orgSelect2Info.orgName = '';

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
