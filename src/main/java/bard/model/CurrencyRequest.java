package bard.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CurrencyRequest {
    private String name;
    private String code;
    private String sign;

    public CurrencyRequest(String name, String code, String sign) {
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public CurrencyRequest() {
    }

}