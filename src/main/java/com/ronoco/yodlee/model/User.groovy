package com.ronoco.yodlee.model

import groovy.transform.CompileStatic

@CompileStatic
class User {

    String loginName
    String password
    String locale
    Session session
    List<AccessToken> accessTokens

}
