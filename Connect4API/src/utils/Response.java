package utils;

/**
 * The Response class encapsulates a standardized response structure
 * for the Connect4 game API, including a status code and optional
 * data. This class is useful for handling API responses in a
 * consistent manner, allowing clients to easily interpret the
 * results of their requests.
 *
 * @param <T> the type of the response data
 */
public class Response<T>{
    // The status code indicating the result of the request
    private int statusCode;
    // The data returned with the response, if any
    private T data;

    /**
     * Constructs a Response object with the specified status code and data.
     *
     * @param statusCode the status code indicating the result of the request
     * @param data the data returned with the response
     */
    public Response(int statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    /**
     * Constructs a Response object with the specified status code.
     * No data is included in this response.
     *
     * @param statusCode the status code indicating the result of the request
     */
    public Response(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Gets the status code of the response.
     *
     * @return the status code indicating the result of the request
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the status code of the response.
     *
     * @param statusCode the new status code indicating the result of the request
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Gets the data associated with the response.
     *
     * @return the data returned with the response, or null if no data was provided
     */
    public T getData() {
        return data;
    }

    /**
     * Sets the data associated with the response.
     *
     * @param data the new data to be returned with the response
     */
    public void setData(T data) {
        this.data = data;
    }

    /**** Static Method ****/
    /**
     * Creates a successful response with the specified status code and data.
     *
     * @param code the status code indicating success
     * @param data the data to be included in the response
     * @param <T> the type of the response data
     * @return a new Response object indicating success
     */
    public static <T> Response<T> success(int code, T data){
        return new Response<>(code, data);
    }

    /**
     * Creates an error response with the specified status code.
     * No data is included in this response.
     *
     * @param statusCode the status code indicating an error
     * @param <T> the type of the response data
     * @return a new Response object indicating an error
     */
    public static <T> Response<T> error(int statusCode){
        return new Response<>(statusCode);
    }
}
