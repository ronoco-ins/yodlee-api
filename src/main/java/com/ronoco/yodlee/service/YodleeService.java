package com.ronoco.yodlee.service;

import com.ronoco.yodlee.model.Account;

import java.util.List;

public interface YodleeService {

    String getCobSession();

    String getUserSession(String cobSession, String username);

    String registerUser(String cobSession, String username, String password, String email);

    String getAccessTokens(String cobSession, String userSession);

    List<Account> getInvestmentAccounts(String cobSession, String userSession);

}
