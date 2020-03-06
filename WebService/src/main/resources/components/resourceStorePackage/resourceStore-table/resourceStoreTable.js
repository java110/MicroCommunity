(function (vc) {
    var DEFAULT_PAGE = 1;
    var DEFAULT_ROWS = 10;
    vc.extends({
        data: {
            resourceStoreManageInfo: {
                resourceStores: [],
                total: 0,
                records: 1,
                moreCondition: false,
                resName: '',
                conditions: {
                    resId: '',
                    resName: '',
                    resCode: '',
                    page:'',
                    row:''
                },
                data:{
                    total:'',
                    rows:''
                }
            }
        },
        _initMethod: function () {
            vc.component._listResourceStores(DEFAULT_PAGE,DEFAULT_ROWS);
            //vc.component._initResourceStoreTable();
            // console.log("table加载本地数据："+vc.component.resourceStoreManageInfo.data.rows);
            // console.log("table加载本地数据："+vc.component.resourceStoreManageInfo.data.total);
            // $('#resourceStore_table').bootstrapTable('load', vc.component.resourceStoreManageInfo.data);
        },
        _initEvent: function () {
            vc.on('resourceStoreTable', 'openResourceStoreTableModal', function () {
                $('#resourceBootstropTable').modal('show');
            });
        },
        methods: {
            _listResourceStores: function (_page, _rows) {
                vc.component.resourceStoreManageInfo.conditions.page = _page;
                vc.component.resourceStoreManageInfo.conditions.row = _rows;
                var param = {
                    params: vc.component.resourceStoreManageInfo.conditions
                };

                //发送get请求
                vc.http.get('resourceStoreManage',
                    'list',
                    param,
                    function (json, res) {
                        var _resourceStoreManageInfo = JSON.parse(json);
                        vc.component.resourceStoreManageInfo.total = _resourceStoreManageInfo.total;
                        vc.component.resourceStoreManageInfo.records = _resourceStoreManageInfo.records;
                        vc.component.resourceStoreManageInfo.resourceStores = _resourceStoreManageInfo.resourceStores;

                        vc.component.resourceStoreManageInfo.data.total = _resourceStoreManageInfo.total;
                        vc.component.resourceStoreManageInfo.data.rows = _resourceStoreManageInfo.resourceStores;
                        vc.emit('pagination', 'init', {
                            total: vc.component.resourceStoreManageInfo.records,
                            currentPage: _page
                        });
                    }, function (errInfo, error) {
                        console.log('请求失败处理');
                    }
                );
            },
            //初始化表格
            _initResourceStoreTable: function () {
                $('#resourceStore_table').bootstrapTable({
                    data:vc.component.resourceStoreManageInfo.data,
                    // url:'/callComponent/resourceStoreManage/list',
                    // method: 'get',                      //请求方式（*）
                    // pagination: true,                   //是否显示分页（*）
                    // pageNumber: 1,                       //初始化加载第一页，默认第一页
                    // pageSize: 10,                       //每页的记录行数（*）
                    // pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
                    // showColumns: false,                 //是否显示所有的列
                    // showRefresh: false,                 //是否显示刷新按钮
                    clickToSelect: true,                //是否启用点击选中行
                    height: 350,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
                    // showToggle: false,                   //是否显示详细视图和列表视图的切换按钮
                    // cardView: false,                    //是否显示详细视图
                    // detailView: false,                   //是否显示父子表
                    columns: [
                        {
                            field: 'resId',
                            title: '资源ID',
                        },
                        {
                            field: 'resName',
                            title: '资源名称',
                        }, {
                            field: 'resCode',
                            title: '物品编码',
                        }, {
                            field: 'price',
                            title: '单价',
                        }, {
                            field: 'stock',
                            title: '库存'},
                        // }, {
                        //     field: 'quantity',
                        //     title: '采购数量'
                        // },
                        {
                            field: 'remark',
                            title: '备注'
                        }
                    ],
                });
            },
            _openAddResourceStoreModal: function () {
                vc.emit('addResourceStore', 'openAddResourceStoreModal', {});
            },
            _openEditResourceStoreModel: function (_resourceStore) {
                vc.emit('editResourceStore', 'openEditResourceStoreModal', _resourceStore);
            },
            _openDeleteResourceStoreModel: function (_resourceStore) {
                vc.emit('deleteResourceStore', 'openDeleteResourceStoreModal', _resourceStore);
            },
            _queryResourceStoreMethod: function () {
                vc.component._listResourceStores(DEFAULT_PAGE, DEFAULT_ROWS);

            },
            _moreCondition: function () {
                if (vc.component.resourceStoreManageInfo.moreCondition) {
                    vc.component.resourceStoreManageInfo.moreCondition = false;
                } else {
                    vc.component.resourceStoreManageInfo.moreCondition = true;
                }
            }


        }
    });
})(window.vc);
