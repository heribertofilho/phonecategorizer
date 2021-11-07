package jumia.heriberto.phonecategorizer.customer.model.dto;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CountryPhoneEnum {
    UNKNOWN("000", ""),
    MOROCCO("212", "\\(212\\)\\ 6\\d{8}$"),
    CAMEROON("237", "\\(237\\)\\ ?[2368]\\d{7,8}$"),
    ETHIOPIA("251", " \\(251\\)\\ ?[1-59]\\d{8}$"),
    UGANDA("256", "\\(256\\)\\ 7\\d{8}$"),
    MOZAMBIQUE("258", "\\(258\\)\\ 8[1-9]\\d{6,7}$");

    private final String code;
    private final String regex;

    CountryPhoneEnum(String code, String regex) {
        this.code = code;
        this.regex = regex;
    }

    public static CountryPhoneEnum getByCode(String code) {
        return Arrays.stream(CountryPhoneEnum.values()).filter(country -> country.code.equals(code)).findFirst().orElse(CountryPhoneEnum.UNKNOWN);
    }
}