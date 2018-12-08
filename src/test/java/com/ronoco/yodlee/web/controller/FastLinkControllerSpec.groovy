package com.ronoco.yodlee.web.controller

import com.ronoco.yodlee.service.YodleeService
import spock.lang.Specification

import java.security.Principal

class FastLinkControllerSpec extends Specification {

    YodleeService yodleeService = Mock(YodleeService.class)
    Principal principal = Mock(Principal.class)
    FastLinkController fastLinkController
    String cobSession = 'cobSession'
    String username = 'user1'
    String userSession = 'userSession'
    String accessToken = 'accessToken'

    def 'FastLinkController()'() {
        given:
        String cobrandName = 'testCobrand'
        String fastLinkUrl = 'http://fast.com'

        when:
        fastLinkController = new FastLinkController(cobrandName, fastLinkUrl, yodleeService)

        then:
        fastLinkController
    }

    def 'getFinanceFastLink()'() {
        given:
        String cobrandName = 'testCobrand'
        String fastLinkUrl = 'http://fast.com'
        fastLinkController = new FastLinkController(cobrandName, fastLinkUrl, yodleeService)

        when:
        String data = fastLinkController.getFinanceFastLink(principal)

        then:
        1 * principal.getName() >> username
        1 * yodleeService.getCobSession() >> cobSession
        1 * yodleeService.getUserSession(cobSession, username) >> userSession
        1 * yodleeService.getAccessTokens(cobSession, userSession) >> accessToken
        data.contains(accessToken)
    }

    def 'getInsuranceFastLink()'() {
        given:
        String cobrandName = 'testCobrand'
        String fastLinkUrl = 'http://fast.com'
        fastLinkController = new FastLinkController(cobrandName, fastLinkUrl, yodleeService)

        when:
        String data = fastLinkController.getInsuranceFastLink(principal)

        then:
        1 * principal.getName() >> username
        1 * yodleeService.getCobSession() >> cobSession
        1 * yodleeService.getUserSession(cobSession, username) >> userSession
        1 * yodleeService.getAccessTokens(cobSession, userSession) >> accessToken
        data.contains(accessToken)
    }
}
