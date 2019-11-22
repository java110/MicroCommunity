/**
 分页组件
 **/
(function (vc) {
    vc.extends({
        data: {
            paginationInfo: {
                total:0,
                dataCount: 0,
                currentPage: 1,
                pageList: []
            }
        },
        _initEvent: function () {
            vc.component.$on('pagination_info_event', function (_paginationInfo) {
                vc.component.paginationInfo.total = _paginationInfo.total;
                if(_paginationInfo.hasOwnProperty("dataCount")){
                    vc.component.paginationInfo.dataCount = _paginationInfo.dataCount;
                }
                vc.component.paginationInfo.currentPage = _paginationInfo.currentPage;
                vc.component._freshPageList();
            });

            vc.on('pagination', 'init', function (_paginationInfo) {
                vc.component.paginationInfo.total = _paginationInfo.total;
                if(_paginationInfo.hasOwnProperty("dataCount")) {
                    vc.component.paginationInfo.dataCount = _paginationInfo.dataCount;
                }
                vc.component.paginationInfo.currentPage = _paginationInfo.currentPage;
                vc.component._freshPageList();
            });
        },
        methods: {
            previous: function () {
                // 当前页为 1时 不触发消息
                if (vc.component.paginationInfo.currentPage <= 1) {
                    return;
                }
                vc.component.paginationInfo.currentPage = vc.component.paginationInfo.currentPage - 1;
                vc.component.$emit('pagination_page_event', vc.component.paginationInfo.currentPage);
            },
            next: function () {
                if (vc.component.paginationInfo.currentPage >= vc.component.paginationInfo.total) {
                    return;
                }
                vc.component.paginationInfo.currentPage = vc.component.paginationInfo.currentPage + 1;
                vc.component.$emit('pagination_page_event', vc.component.paginationInfo.currentPage);

            },
            current: function (_page) {
                if(_page == -1){
                    return;
                }
                if (_page > vc.component.paginationInfo.total) {
                    return;
                }
                vc.component.paginationInfo.currentPage = _page;

                vc.component.$emit('pagination_page_event', vc.component.paginationInfo.currentPage);
            },
            _freshPageList: function () {
                var current = vc.component.paginationInfo.currentPage;
                var total = vc.component.paginationInfo.total;
                vc.component.paginationInfo.pageList = [];
                if (total > 6) {
                    //当前页数小于5时显示省略号
                    if (current < 5) {
                        for (var i = 1; i < 6; i++) {
                            if (current == i) {
                                vc.component.paginationInfo.pageList.push({
                                    "page": i,
                                    "pageView": i + "",
                                    "currentPage": true
                                });
                            } else {
                                vc.component.paginationInfo.pageList.push({
                                    "page": i,
                                    "pageView": i + "",
                                    "currentPage": false
                                });
                            }
                        }
                        vc.component.paginationInfo.pageList.push({
                            "page": -1,
                            "pageView": "...",
                            "currentPage": false
                        });
                        vc.component.paginationInfo.pageList.push({
                            "page": total,
                            "pageView": total + "",
                            "currentPage": false
                        });
                    } else {
                        //判断页码在末尾的时候
                        if (current < total - 3) {
                            for (var i = current - 2; i < current + 3; i++) {
                                if (current == i) {
                                    vc.component.paginationInfo.pageList.push({
                                        "page": i,
                                        "pageView": i + "",
                                        "currentPage": true
                                    });
                                } else {
                                    vc.component.paginationInfo.pageList.push({
                                        "page": i,
                                        "pageView": i + "",
                                        "currentPage": false
                                    });
                                }
                            }
                            vc.component.paginationInfo.pageList.push({
                                "page": -1,
                                "pageView": "...",
                                "currentPage": false
                            });
                            vc.component.paginationInfo.pageList.push({
                                "page": total,
                                "pageView": total + "",
                                "currentPage": false
                            });
                            //页码在中间部分时候
                        } else {
                            vc.component.paginationInfo.pageList.push({
                                "page": 1,
                                "pageView": 1 + "",
                                "currentPage": false
                            });
                            vc.component.paginationInfo.pageList.push({
                                "page": -1,
                                "pageView": "...",
                                "currentPage": false
                            });
                            for (var i = total - 4; i < total + 1; i++) {
                                if (current == i) {
                                    vc.component.paginationInfo.pageList.push({
                                        "page": i,
                                        "pageView": i + "",
                                        "currentPage": true
                                    });
                                } else {
                                    vc.component.paginationInfo.pageList.push({
                                        "page": i,
                                        "pageView": i + "",
                                        "currentPage": false
                                    });
                                }
                            }
                        }
                    }
                    //页面总数小于6的时候
                } else {
                    for (var i = 1; i < total + 1; i++) {
                        if (current == i) {
                            vc.component.paginationInfo.pageList.push({
                                "page": i,
                                "pageView": i + "",
                                "currentPage": true
                            });
                        } else {
                            vc.component.paginationInfo.pageList.push({
                                "page": i,
                                "pageView": i + "",
                                "currentPage": false
                            });
                        }
                    }
                }
            }
        }
    });
})(window.vc);