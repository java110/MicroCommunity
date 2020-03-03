(function(vc){
    vc.extends({
        propTypes: {
           emitListener:vc.propTypes.string,
           emitFunction:vc.propTypes.string
        },
        data:{
            addInspectionRoutePointInfo:{
                inspectionRouteId:'',
                inspectionPoints:[],
                inspectionName:'',
                selectInspectionPoints:[]
            }
        },
        watch: { // 监视双向绑定的数据数组
            checkData: {
                handler(){ // 数据数组有变化将触发此函数
                    if(vc.component.addInspectionRoutePointInfo.selectInspectionPoints.length == vc.component.addInspectionRoutePointInfo.inspectionPoints.length){
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
            vc.on('addInspectionRoutePoint','openAddInspectionRoutePointModal',function(_param){
                vc.component._refreshInspectionPointsInfo();
                $('#addInspectionRoutePointModel').modal('show');
                vc.copyObject(_param,vc.component.addInspectionRoutePointInfo);
                vc.component._loadAllCommunityInfo(1,10,'');
            });

            vc.on('addInspectionRoutePoint','paginationPlus', 'page_event', function (_currentPage) {
                vc.component._listInspectionRoutePoints(_currentPage, DEFAULT_ROWS);
            });
        },
        methods:{
            _loadInspectionPointInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        name:_name,
                        inspectionRouteId:vc.component.addInspectionRoutePointInfo.inspectionRouteId,
                        communityId:vc.getCurrentCommunity().communityId
                    }
                };

                //发送get请求
               vc.http.get('addInspectionRoutePoint',
                            'list',
                             param,
                             function(json){
                                var _inspectionPointInfo = JSON.parse(json);
                                vc.component.addInspectionRoutePointInfo.inspectionPoints = _inspectionPointInfo.inspectionPoints;
                                vc.emit('addInspectionRoutePoint','paginationPlus', 'init', {
                                    total: _inspectionPointInfo.records,
                                    currentPage: _page
                                });
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            addInspectionRoutePoint:function(_org){
                var _selectInspectionPoints = vc.component.addInspectionRoutePointInfo.selectInspectionPoints;
                var _tmpCommunitys = vc.component.addInspectionRoutePointInfo.inspectionPoints;
                if(_selectInspectionPoints.length <1){
                    vc.toast("请选择隶属小区");
                    return ;
                }
                var _inspectionPoints = [];
                for(var _selectIndex = 0 ;_selectIndex <_selectInspectionPoints.length ;_selectIndex ++){
                    for(var _communityIndex =0; _communityIndex < _tmpCommunitys.length;_communityIndex++){
                        if(_selectInspectionPoints[_selectIndex] == _tmpCommunitys[_communityIndex].communityId){
                            _inspectionPoints.push({
                                communityId:_tmpCommunitys[_communityIndex].communityId,
                                communityName:_tmpCommunitys[_communityIndex].name
                            });
                        }
                    }
                }
                var _objData = {
                    orgId:vc.component.addInspectionRoutePointInfo.orgId,
                    orgName:vc.component.addInspectionRoutePointInfo.orgName,
                    inspectionPoints:_inspectionPoints
                }
                vc.http.post('addInspectionRoutePoint',
                    'save',
                    JSON.stringify(_objData),
                    {
                        emulateJSON: true
                    },
                 function(json,res){
                    $('#addInspectionRoutePointModel').modal('hide');
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
                $('#addInspectionRoutePointModel').modal('hide');
            },
            queryInspectionPoints:function(){
                vc.component._loadInspectionPointInfo(1,10,vc.component.addInspectionRoutePointInfo.inspectionName);
            },
            _refreshInspectionPointsInfo:function(){
                vc.component.addInspectionRoutePointInfo={
                    inspectionPoints:[],
                    inspectionName:'',
                    selectInspectionPoints:[]
                };
            },
            checkAll:function(e){
                    var checkObj = document.querySelectorAll('.checkItem'); // 获取所有checkbox项
                    if(e.target.checked){ // 判定全选checkbox的勾选状态
                        for(var i=0;i<checkObj.length;i++){
                            if(!checkObj[i].checked){ // 将未勾选的checkbox选项push到绑定数组中
                                vc.component.addInspectionRoutePointInfo.selectInspectionPoints.push(checkObj[i].value);
                            }
                        }
                    }else { // 如果是去掉全选则清空checkbox选项绑定数组
                        vc.component.addInspectionRoutePointInfo.selectInspectionPoints = [];
                    }
            }
        }

    });
})(window.vc);