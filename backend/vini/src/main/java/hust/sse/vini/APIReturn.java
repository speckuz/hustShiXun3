package hust.sse.vini;

public class APIReturn {
    private Integer statusCode;

    private String errMsg;

    private Object result;

    public static APIReturn apiError(Integer statusCode, String errMsg) {
        APIReturn res=new APIReturn();
        res.statusCode = statusCode;
        res.errMsg = errMsg;
        res.result=null;
        return res;
    }

    public static APIReturn successfulResult(Object result){
        APIReturn res=new APIReturn();
        res.statusCode=200;
        res.errMsg=null;
        res.result=result;
        return res;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
