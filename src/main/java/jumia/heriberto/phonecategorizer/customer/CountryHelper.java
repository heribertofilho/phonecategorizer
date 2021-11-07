package jumia.heriberto.phonecategorizer.customer;

import jumia.heriberto.phonecategorizer.customer.exception.CountryCodeNotFoundException;
import jumia.heriberto.phonecategorizer.customer.model.dto.CountryPhoneEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountryHelper {
    private static final Pattern patternCountryCode = Pattern.compile("\\(([^)]+)\\)");

    public static CountryPhoneEnum getCountry(String phone) throws CountryCodeNotFoundException {
        Matcher matcher = patternCountryCode.matcher(phone);

        if (!matcher.find()) {
            throw new CountryCodeNotFoundException();
        }
        return CountryPhoneEnum.getByCode(matcher.group(1));
    }
}
