package com.nonobank.testcase.service;

import java.util.List;

public interface TestDataService {

    List<String> getAllProvince();

    List<String> getCityList(String province);

    List<String> getDistrictList(String provice, String city);

    String getIdCardByEnvIsRegisteredProvinceCityDistrict(String env, boolean isRegistered, String province, String city, String district) throws Exception;

}
