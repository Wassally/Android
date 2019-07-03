package com.android.wassally;

import retrofit2.http.PUT;

public final class Constants {

    public static final String BASE_URL = "https://wassally.herokuapp.com/api/";
    public static final String AUTH_TOKEN ="auth_token";
    public static final String USER_ID = "user_id";
    public static final String FULL_NAME = "full_name";



    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    private static final String PACKAGE_NAME = "com.android.wassally";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_LAT_EXTRA = PACKAGE_NAME +
            ".LOCATION_lAT_EXTRA";
    public static final String LOCATION_LNG_EXTRA = PACKAGE_NAME +
            ".LOCATION_LNG_EXTRA";

    public static final String SELECTED_ADDRESS = PACKAGE_NAME +
            ".SELECTED_ADDRESS";
    public static final String LOCATION_REQUEST_KEY = PACKAGE_NAME+
            ".LOCATION_REQUEST_KEY";
    public static final int RESULT_OK = 5;
    public static final int PICKUP_LOCATION_REQUEST = 1;
    public static final int DESTINATION_LOCATION_REQUEST = 2;

    public static final String TRANSPORT_WAY = "wassally";

    public static final String SALARY_EXTRA = PACKAGE_NAME + ".SALARY";
    public static final String ORDER_EXTRA = PACKAGE_NAME + ".ORDER";










}
