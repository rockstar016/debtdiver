package com.rock.debitdiver.ApiUtils;


import com.rock.debitdiver.BaseActivity;

public class ServerURLs {
    public static final String BASE_URL = "http://192.168.1.107/debit_diver/api/";
    public static final String REGISTER_URL = BASE_URL + "auth/register";
    public static final String LOGIN_URL = BASE_URL + "auth/login";
    public static final String ADD_DEBT_URL = BASE_URL + "debt/insertdebt";
    public static final String EDIT_DEBT_URL = BASE_URL + "debt/update";
    public static final String GET_DEBTLIST_URL = BASE_URL + "debt/debtlist";
    public static final String INSERT_PAY_URL = BASE_URL + "debt/insertpay";
    public static final String GET_HISTORY = BASE_URL + "debt/payhistory";
}
