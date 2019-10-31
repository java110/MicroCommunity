(function(vc){
    vc.extends({
        propTypes: {
           emitChooseComplaint:vc.propTypes.string,
           emitLoadData:vc.propTypes.string
        },
        data:{
            chooseComplaintInfo:{
                complaints:[],
                _currentComplaintName:'',
            }
        },
        _initMethod:function(){
        },
        _initEvent:function(){
            vc.on('chooseComplaint','openChooseComplaintModel',function(_param){
                $('#chooseComplaintModel').modal('show');
                vc.component._refreshChooseComplaintInfo();
                vc.component._loadAllComplaintInfo(1,10,'');
            });
        },
        methods:{
            _loadAllComplaintInfo:function(_page,_row,_name){
                var param = {
                    params:{
                        page:_page,
                        row:_row,
                        communityId:vc.getCurrentCommunity().communityId,
                        name:_name
                    }
                };

                //发送get请求
               vc.http.get('chooseComplaint',
                            'list',
                             param,
                             function(json){
                                var _complaintInfo = JSON.parse(json);
                                vc.component.chooseComplaintInfo.complaints = _complaintInfo.complaints;
                             },function(){
                                console.log('请求失败处理');
                             }
                           );
            },
            chooseComplaint:function(_complaint){
                if(_complaint.hasOwnProperty('name')){
                     _complaint.complaintName = _complaint.name;
                }
                vc.emit($props.emitChooseComplaint,'chooseComplaint',_complaint);
                vc.emit($props.emitLoadData,'listComplaintData',{
                    complaintId:_complaint.complaintId
                });
                $('#chooseComplaintModel').modal('hide');
            },
            queryComplaints:function(){
                vc.component._loadAllComplaintInfo(1,10,vc.component.chooseComplaintInfo._currentComplaintName);
            },
            _refreshChooseComplaintInfo:function(){
                vc.component.chooseComplaintInfo._currentComplaintName = "";
            }
        }

    });
})(window.vc);
