package com.android.wassally;

public final class Constants {

    public static final String BASE_URL = "https://wassally.herokuapp.com/api/";
    public static final String AUTH_TOKEN ="auth_token";
    public static final String USER_ID = "user_id";
    public static final String FULL_NAME = "full_name";

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String CLOSE_ALL ="CLOSE_ALL" ;
    public static final int GET_FROM_GALLERY = 4;
    public static final int USER_ADDRESS_REQUEST = 5;
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
    public static final String USER_EXTRA = PACKAGE_NAME +".USER_EXTRA" ;
    public static final String GET_USER_ADDRESS =PACKAGE_NAME+".GERT_USER_ADDRESS" ;



}
