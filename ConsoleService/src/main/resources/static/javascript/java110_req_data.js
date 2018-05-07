
/**
{
  "meta":{
    "method":"",//主要用于，日志记录
    "requestTime":"",
    "transactionId":"请求流水" //不需要填 有系统统一设置
  },
  "param":{
    //这里写参数
  }
}
**/
function createRequestData(method,reqParam){
    var reqJson = {
                    "meta":{
                      "method":method,//主要用于，日志记录
                      "requestTime":nowtime(),
                    },
                    "param":{
                      //这里写参数
                    }
                  };
    reqJson.param = reqParam;

    return reqJson;
}


function nowtime(){
    var time = new Date();
    return time.getFullYear()+((time.getMonth()+1) > 9 ? (time.getMonth()+1) :'0'+(time.getMonth()+1))+(time.getDate()>9?time.getDate():'0'+time.getDate())+(time.getHours()>9?'':'0')
    +time.getHours()+(time.getMinutes()>9?'':'0')
    +time.getMinutes()+(time.getSeconds()>9?'':'0')+time.getSeconds()
  }

function getResponseInfo(data){
    var dataObj =  eval('(' + data + ')');
//    var dataObj = JSON.parse(data);
    var meta = dataObj.meta;
    var resData = {"code":meta.code,"msg":meta.message,"id":meta.transactionId,"data":dataObj.data}
    return resData;
}