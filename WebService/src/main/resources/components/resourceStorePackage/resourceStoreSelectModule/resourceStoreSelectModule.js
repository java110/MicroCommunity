(function(vc){
    vc.extends({
        propTypes: {
            emitListener:vc.propTypes.string,
            emitFunction:vc.propTypes.string
        },
        data:{
            addOrgCommunityInfo:{
                communitys:[],
                communityName:'',
                orgId:'',
                orgName:'',
                selectCommunitys:[],
                index:0,
            }
        },
        watch: { // 监视双向绑定的数据数组
            checkData: {
                handler(){ // 数据数组有变化将触发此函数
                    if(vc.component.addOrgCommunityInfo.selectCommunitys.length == vc.component.addOrgCommunityInfo.communitys.length){
                        document.querySelector('#quan').checked = true;
                    }else {
                        document.querySelector('#quan').checked = false;
                    }
                },
                deep: true // 深度监视
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('addOrgCommunity','openAddOrgCommunityModal',function(_param){
                vc.component._refreshChooseOrgInfo();
                $('#addOrgCommunityModel').modal('show');
                vc.copyObject(_param,vc.component.addOrgCommunityInfo);
                vc.component._loadAllCommunityInfo(1,10,'');
            });

            vc.on('addOrgCommunity','paginationPlus', 'page_event', function (_currentPage) {
                vc.component._listOrgCommunitys(_currentPage, DEFAULT_ROWS);
            });

            vc.on('resourceStoreSelectModule', 'onIndex', function(_index){
                vc.component.viewStaffInfo.index = _index;
            });
        },
        methods:{
            _loadAllCommunityInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        name:_name,
                        orgId:vc.component.addOrgCommunityInfo.orgId
                    }
                };

                //发送get请求
                vc.http.get('addOrgCommunity',
                    'list',
                    param,
                    function(json){
                        var _communityInfo = JSON.parse(json);
                        vc.component.addOrgCommunityInfo.communitys = _communityInfo.communitys;
                        vc.emit('addOrgCommunity','paginationPlus', 'init', {
                            total: _communityInfo.records,
                            currentPage: _page
                        });
                    },function(){
                        console.log('请求失败处理');
                    }
                );
            },
            addOrgCommunity:function(_org){
                var _selectCommunitys = vc.component.addOrgCommunityInfo.selectCommunitys;
                var _tmpCommunitys = vc.component.addOrgCommunityInfo.communitys;
                if(_selectCommunitys.length <1){
                    vc.toast("请选择隶属小区");
                    return ;
                }
                var _communitys = [];
                for(var _selectIndex = 0 ;_selectIndex <_selectCommunitys.length ;_selectIndex ++){
                    for(var _communityIndex =0; _communityIndex < _tmpCommunitys.length;_communityIndex++){
                        if(_selectCommunitys[_selectIndex] == _tmpCommunitys[_communityIndex].communityId){
                            _communitys.push({
                                communityId:_tmpCommunitys[_communityIndex].communityId,
                                communityName:_tmpCommunitys[_communityIndex].name
                            });
                        }
                    }
                }
                var _objData = {
                    orgId:vc.component.addOrgCommunityInfo.orgId,
                    orgName:vc.component.addOrgCommunityInfo.orgName,
                    communitys:_communitys
                }
                vc.http.post('addOrgCommunity',
                    'save',
                    JSON.stringify(_objData),
                    {
                        emulateJSON: true
                    },
                    function(json,res){
                        $('#addOrgCommunityModel').modal('hide');
                        if(res.status == 200){
                            vc.emit($props.emitListener,$props.emitFunction,{
                            });
                            return ;
                        }
                        vc.toast(json);
                    },function(){
                        console.log('请求失败处理');
                    }
                );
                $('#addOrgCommunityModel').modal('hide');
            },
            queryCommunitys:function(){
                vc.component._loadAllCommunityInfo(1,10,vc.component.addOrgCommunityInfo.communityName);
            },
            _refreshChooseOrgInfo:function(){
                vc.component.addOrgCommunityInfo={
                    communitys:[],
                    communityName:'',
                    orgId:'',
                    orgName:'',
                    selectCommunitys:[]
                };
            },
            checkAll:function(e){
                var checkObj = document.querySelectorAll('.checkItem'); // 获取所有checkbox项
                if(e.target.checked){ // 判定全选checkbox的勾选状态
                    for(var i=0;i<checkObj.length;i++){
                        if(!checkObj[i].checked){ // 将未勾选的checkbox选项push到绑定数组中
                            vc.component.addOrgCommunityInfo.selectCommunitys.push(checkObj[i].value);
                        }
                    }
                }else { // 如果是去掉全选则清空checkbox选项绑定数组
                    vc.component.addOrgCommunityInfo.selectCommunitys = [];
                }
            }
        }

    });
})(window.vc);
