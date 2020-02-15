/**
 分页组件
 **/
(function (vc) {
    vc.extends({
        data: {
            paginationPlusInfo: {
                total:0,
                dataCount: 0,
                currentPage: 1,
                pageList: []
            }
        },
        _initEvent: function () {
            vc.on($namespace,'paginationPlus','info_event', function (_paginationPlusInfo) {
                this.paginationPlusInfo.total = _paginationPlusInfo.total;
                if(_paginationPlusInfo.hasOwnProperty("dataCount")){
                    this.paginationPlusInfo.dataCount = _paginationPlusInfo.dataCount;
                }
                this.paginationPlusInfo.currentPage = _paginationPlusInfo.currentPage;
                this._freshPageList();
            });

            vc.on($namespace,'paginationPlus', 'init', function (_paginationPlusInfo) {
                this.paginationPlusInfo.total = _paginationPlusInfo.total;
                if(_paginationPlusInfo.hasOwnProperty("dataCount")) {
                    this.paginationPlusInfo.dataCount = _paginationPlusInfo.dataCount;
                }
                this.paginationPlusInfo.currentPage = _paginationPlusInfo.currentPage;
                this._freshPageList();
            });
        },
        methods: {
            previous: function () {
                // 当前页为 1时 不触发消息
                if (this.paginationPlusInfo.currentPage <= 1) {
                    return;
                }
                this.paginationPlusInfo.currentPage = this.paginationPlusInfo.currentPage - 1;
                vc.emit($namespace,'paginationPlus','page_event', this.paginationPlusInfo.currentPage);
            },
            next: function () {
                if (this.paginationPlusInfo.currentPage >= this.paginationPlusInfo.total) {
                    return;
                }
                this.paginationPlusInfo.currentPage = this.paginationPlusInfo.currentPage + 1;
                vc.emit($namespace,'paginationPlus','page_event', this.paginationPlusInfo.currentPage);

            },
            current: function (_page) {
                if(_page == -1){
                    return;
                }
                if (_page > this.paginationPlusInfo.total) {
                    return;
                }
                this.paginationPlusInfo.currentPage = _page;

                vc.emit($namespace,'paginationPlus','page_event', this.paginationPlusInfo.currentPage);
            },
            _freshPageList: function () {
                var current = this.paginationPlusInfo.currentPage;
                var total = this.paginationPlusInfo.total;
                this.paginationPlusInfo.pageList = [];
                if (total > 6) {
                    //当前页数小于5时显示省略号
                    if (current < 5) {
                        for (var i = 1; i < 6; i++) {
                            if (current == i) {
                                this.paginationPlusInfo.pageList.push({
                                    "page": i,
                                    "pageView": i + "",
                                    "currentPage": true
                                });
                            } else {
                                this.paginationPlusInfo.pageList.push({
                                    "page": i,
                                    "pageView": i + "",
                                    "currentPage": false
                                });
                            }
                        }
                        this.paginationPlusInfo.pageList.push({
                            "page": -1,
                            "pageView": "...",
                            "currentPage": false
                        });
                        this.paginationPlusInfo.pageList.push({
                            "page": total,
                            "pageView": total + "",
                            "currentPage": false
                        });
                    } else {
                        //判断页码在末尾的时候
                        if (current < total - 3) {
                            for (var i = current - 2; i < current + 3; i++) {
                                if (current == i) {
                                    this.paginationPlusInfo.pageList.push({
                                        "page": i,
                                        "pageView": i + "",
                                        "currentPage": true
                                    });
                                } else {
                                    this.paginationPlusInfo.pageList.push({
                                        "page": i,
                                        "pageView": i + "",
                                        "currentPage": false
                                    });
                                }
                            }
                            this.paginationPlusInfo.pageList.push({
                                "page": -1,
                                "pageView": "...",
                                "currentPage": false
                            });
                            this.paginationPlusInfo.pageList.push({
                                "page": total,
                                "pageView": total + "",
                                "currentPage": false
                            });
                            //页码在中间部分时候
                        } else {
                            this.paginationPlusInfo.pageList.push({
                                "page": 1,
                                "pageView": 1 + "",
                                "currentPage": false
                            });
                            this.paginationPlusInfo.pageList.push({
                                "page": -1,
                                "pageView": "...",
                                "currentPage": false
                            });
                            for (var i = total - 4; i < total + 1; i++) {
                                if (current == i) {
                                    this.paginationPlusInfo.pageList.push({
                                        "page": i,
                                        "pageView": i + "",
                                        "currentPage": true
                                    });
                                } else {
                                    this.paginationPlusInfo.pageList.push({
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
                            this.paginationPlusInfo.pageList.push({
                                "page": i,
                                "pageView": i + "",
                                "currentPage": true
                            });
                        } else {
                            this.paginationPlusInfo.pageList.push({
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