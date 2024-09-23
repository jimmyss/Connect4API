package utils;

public class Response<T>{
    private int statusCode;
    private String msg;
    private T data;

    public Response(int statusCode, String info, T data) {
        this.statusCode = statusCode;
        this.msg = info;
        this.data = data;
    }

    public Response(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**** Static Method ****/
    public static <T> Response<T> success(T data){
        return new Response<>(200, "Success", data);
    }

    public static <T> Response<T> error(int statusCode, String msg){
        return new Response<>(statusCode, msg);
    }
}
