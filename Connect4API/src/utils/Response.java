package utils;

public class Response<T>{
    private int statusCode;
    private T data;

    public Response(int statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public Response(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**** Static Method ****/
    public static <T> Response<T> success(int code, T data){
        return new Response<>(code, data);
    }

    public static <T> Response<T> error(int statusCode){
        return new Response<>(statusCode);
    }
}
