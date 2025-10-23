package bard.model;

public class  CurrencyResponce {
    private Long id;
    private String code;
    private String fullName;
    private String sign;

    public CurrencyResponce(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }
}
