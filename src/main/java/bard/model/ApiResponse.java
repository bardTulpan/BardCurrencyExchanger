package bard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private String code;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(T data, String s, String number, ArrayList<Currency> currencies, LocalDateTime now, String path) {
        return new ApiResponse<>(true, "Success", "200", data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, "SUCCESS", data, LocalDateTime.now());
    }

    // Error static methods
    public static <T> ApiResponse<T> error(String message, String code) {
        return new ApiResponse<>(false, message, code, null, LocalDateTime.now());
    }
}
