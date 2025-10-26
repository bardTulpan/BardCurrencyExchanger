package bard.model;

public class CurrencyResponce {
    private final String code;
    private final String fullName;
    private final String sign;
    private Long id;

    public CurrencyResponce(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }
}
