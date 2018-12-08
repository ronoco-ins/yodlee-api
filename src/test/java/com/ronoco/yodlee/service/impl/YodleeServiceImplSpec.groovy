package com.ronoco.yodlee.service.impl

import com.ronoco.yodlee.model.Account
import com.ronoco.yodlee.service.YodleeService
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class YodleeServiceImplSpec extends Specification {

    String cobrandLogin = 'sbCobdb7aac01b2ec074d4d4e879f1f9d51cc0a'
    String cobrandPassword = '8ed13c0b-4a52-4216-983e-627f1bbef24f'
    String cobrandName = 'restserver'
    String locale = 'en_US'
    String username = 'sbMemdb7aac01b2ec074d4d4e879f1f9d51cc0a2'
    String password = 'sbMemdb7aac01b2ec074d4d4e879f1f9d51cc0a2#123'
    String url = 'https://developer.api.yodlee.com/ysl/'
    RestTemplate restTemplate = new RestTemplate();
    YodleeService yodleeService = new YodleeServiceImpl(cobrandLogin, cobrandPassword, locale, password, cobrandName,
            url, restTemplate)

    def 'getCobSession()'() {
        when:
        String cobSession = yodleeService.getCobSession();

        then:
        cobSession
    }

    def 'getUserSession()'() {
        given:
        String cobSession = yodleeService.getCobSession();

        when:
        String userSession = yodleeService.getUserSession(cobSession, username);

        then:
        userSession
    }

    def 'getAccessTokens()'() {
        given:
        String cobSession = yodleeService.getCobSession();
        String userSession = yodleeService.getUserSession(cobSession, username);

        when:
        String accessToken = yodleeService.getAccessTokens(cobSession, userSession);

        then:
        accessToken

    }

    def 'getInvestmentAccounts()'() {
        given:
        String cobSession = yodleeService.getCobSession();
        String userSession = yodleeService.getUserSession(cobSession, username);

        when:
        List<Account> accountList = yodleeService.getInvestmentAccounts(cobSession, userSession);

        then:
        accountList != null
    }
}
