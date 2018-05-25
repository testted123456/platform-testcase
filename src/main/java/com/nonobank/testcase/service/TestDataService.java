package com.nonobank.testcase.service;

import java.util.List;

public interface TestDataService {

    String getIdCardByEnvIsRegistered(String env, boolean isRegistered) throws Exception;

    List<String> getAllProvince();

    List<String> getCityList(String province);

    List<String> getDistrictList(String provice, String city);

}
