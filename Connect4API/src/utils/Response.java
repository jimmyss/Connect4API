package utils;

public class Response<T>{
    private int statusCode;
    private String info;
    private T data;

    public Response(int statusCode, String info, T data) {
        this.statusCode = statusCode;
        this.info = info;
        this.data = data;
    }

    public Response(int statusCode, String info) {
        this.statusCode = statusCode;
        this.info = info;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**** Static Method ****/
    public static <T> Response<T> success(int code, T data){
        return new Response<>(code, "Success", data);
    }

    public static <T> Response<T> error(int statusCode, String info){
        return new Response<>(statusCode, info);
    }
}
