package io.github.aparnachaudhary;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aparna
 * @since 06.01.2017
 */
public class CountryService {

    Map<String, Integer> countryCodeMap = new HashMap<>();

    public CountryService() {
        countryCodeMap.put("NL", 1);
        countryCodeMap.put("BE", 2);
        countryCodeMap.put("DE", 3);
        countryCodeMap.put("FR", 4);
        countryCodeMap.put("AT", 5);
        countryCodeMap.put("IN", 6);

    }

    public Integer getIdByCode(final String code) {
        return countryCodeMap.get(code);
    }

    public Collection<String> getAllCountryCodes() {
        return countryCodeMap.keySet();
    }

}
