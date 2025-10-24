package bard.model;

public class CurrencyResponce {
    private Long id;
    private final String code;
    private final String fullName;
    private final String sign;

    public CurrencyResponce(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }
}
