package com.nonobank.testcase.service;

import java.sql.SQLException;
import java.util.List;

public interface TestDataService {

    List<String> getAllProvince();

    List<String> getCityList(String province);

    List<String> getDistrictList(String provice, String city);

    String getIdCardByEnvIsRegisteredProvinceCityDistrict(String env, boolean isRegistered, String province, String city, String district) throws Exception;

    List<String> getAllCNBankName();

    String getBankCardByEnvBanknameIsRegistered(String env, String bankName, boolean isRegistered) throws SQLException, Exception;

    String getMobileNO(String env, boolean isRegistered, String branchId) throws SQLException, Exception;

}
