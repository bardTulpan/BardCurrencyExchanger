package bard.model;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Currency {
    private Long id;
    @NotBlank(message = "Code is required")
    @Size(min = 3, max = 3, message = "Code must be exactly 3 characters")
    private String code;
    @NotBlank(message = "Full name is required")
    private String fullName;
    @NotBlank(message = "Sign is required")
    private String sign;

    public Currency(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public Currency() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}