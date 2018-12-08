package com.ronoco.yodlee.service.impl;

import com.ronoco.yodlee.model.*;
import com.ronoco.yodlee.service.YodleeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class YodleeServiceImpl implements YodleeService {

    private static final String ACCEPT_CHARSET = "UTF-8";
    private static final String ACCOUNTS_URL = "accounts?status=ACTIVE";
    private static final String API_VERSION = "1.1";
    private static final String API_VERSION_HEADER = "Api-Version";
    private static final String APP_ID = "10003600";
    private static final String COBRAND_HEADER = "Cobrand-Name";
    private static final String COBRAND_LOGIN_URL = "cobrand/login";
    private static final String CONTENT_TYPE = "application/json";
    private static final String INVESTMENT_TYPE = "investment";
    private static final String USER_LOGIN_URL = "user/login";
    private static final String ACCESS_TOKEN_URL = "user/accessTokens?appIds=" + APP_ID;

    private final String cobrandLogin;
    private final String cobrandPassword;
    private final String locale;
    private final String yodleeAccountPassword;
    private final String cobrandName;
    private final String yodleeUrl;
    private final RestTemplate restTemplate;

    public YodleeServiceImpl(@Value("${yodlee.cobrand.login}") String cobrandLogin,
                             @Value("${yodlee.cobrand.password}") String cobrandPassword,
                             @Value("${yodlee.cobrand.locale}") String locale,
                             @Value("${yodlee.api.accountPassword}") String yodleeAccountPassword,
                             @Value("${yodlee.cobrand.name}") String cobrandName,
                             @Value("${yodlee.api.url}") String yodleeUrl,
                             RestTemplate restTemplate) {
        this.cobrandLogin = cobrandLogin;
        this.cobrandPassword = cobrandPassword;
        this.locale = locale;
        this.yodleeAccountPassword = yodleeAccountPassword;
        this.cobrandName = cobrandName;
        this.yodleeUrl = yodleeUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public String getCobSession() {
        HttpHeaders headers = getHttpHeaders();
        CobrandRequest cobrandRequest = new CobrandRequest();
        Cobrand cobrand = new Cobrand();
        cobrandRequest.setCobrand(cobrand);
        cobrand.setCobrandLogin(this.cobrandLogin);
        cobrand.setCobrandPassword(this.cobrandPassword);
        cobrand.setLocale(this.locale);
        HttpEntity<CobrandRequest> httpEntity = new HttpEntity<>(cobrandRequest, headers);
        ResponseEntity<CobrandResponse> responseEntity = restTemplate.exchange(
                yodleeUrl + COBRAND_LOGIN_URL, HttpMethod.POST, httpEntity, CobrandResponse.class);
        CobrandResponse cobrandResponse = responseEntity.getBody();
        return cobrandResponse.getSession().getCobSession();
    }

    @Override
    public String getUserSession(String cobSession, String username) {
        HttpHeaders headers = getHttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(cobSession));
        UserRequest userRequest = new UserRequest();
        User user = new User();
        userRequest.setUser(user);
        user.setLoginName(username);
        user.setPassword(this.yodleeAccountPassword);
        user.setLocale(this.locale);
        HttpEntity<UserRequest> httpEntity = new HttpEntity<>(userRequest, headers);
        String url = yodleeUrl + USER_LOGIN_URL;
        ResponseEntity<UserResponse> responseEntity = restTemplate.exchange(
                yodleeUrl + USER_LOGIN_URL, HttpMethod.POST, httpEntity, UserResponse.class);
        UserResponse userResponse = responseEntity.getBody();
        return userResponse.getUser().getSession().getUserSession();
    }

    @Override
    public String registerUser(String cobSession, String username, String password, String email) {
        return null;
    }

    @Override
    public String getAccessTokens(String cobSession, String userSession) {
        HttpHeaders headers = getHttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(cobSession, userSession));
        HttpEntity<?> httpEntity = new HttpEntity<String>(headers);
        String url = yodleeUrl + ACCESS_TOKEN_URL;
        ResponseEntity<UserAccessTokenResponse> responseEntity = restTemplate.exchange(
                yodleeUrl + ACCESS_TOKEN_URL, HttpMethod.GET, httpEntity, UserAccessTokenResponse.class);
        UserAccessTokenResponse userAccessTokenResponse = responseEntity.getBody();
        List<AccessToken> accessTokenList = userAccessTokenResponse.getUser().getAccessTokens();
        for(AccessToken accessToken : accessTokenList) {
            if(accessToken.getAppId().equals(APP_ID)) {
                return accessToken.getValue();
            }
        }
        return null;
    }

    @Override
    public List<Account> getInvestmentAccounts(String cobSession, String userSession) {
        HttpHeaders headers = getHttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(cobSession, userSession));
        HttpEntity<?> httpEntity = new HttpEntity<String>(headers);
        ResponseEntity<AccountResponse> responseEntity = restTemplate.exchange(
                yodleeUrl + ACCOUNTS_URL, HttpMethod.GET, httpEntity, AccountResponse.class);
        List<Account> accountList = responseEntity.getBody().getAccount();
        List<Account> investmentAccountList = new ArrayList<>();
        for(Account account : accountList) {
            if(INVESTMENT_TYPE.equals(account.getContainer())) {
                investmentAccountList.add(account);
            }
        }
        return investmentAccountList;
    }

    String getAuthorizationHeader(String cobSession) {
        return "cobSession=" + cobSession;
    }

    String getAuthorizationHeader(String cobSession, String userSession) {
        return "cobSession=" + cobSession + ",userSession=" + userSession;
    }

    HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(COBRAND_HEADER, cobrandName);
        headers.add(API_VERSION_HEADER, API_VERSION);
        headers.add(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
        headers.add(HttpHeaders.ACCEPT_CHARSET, ACCEPT_CHARSET);
        return headers;
    }

}
