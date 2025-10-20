package bard.model;

public class CurrencyRequest {
    private Long id;
    private String name; // измените fullName на name
    private String code;
    private String sign;

    public CurrencyRequest(String name, String code, String sign) {
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public CurrencyRequest() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; } // измените getFullName на getName
    public void setName(String name) { this.name = name; } // измените setFullName на setName

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getSign() { return sign; }
    public void setSign(String sign) { this.sign = sign; }
}