package com.ronoco.yodlee.web.controller;

import com.ronoco.yodlee.service.YodleeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@RequestMapping("/yodlee/fastlink")
@Controller
public class FastLinkController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FastLinkController.class);

    private static final String EXTRA_PARAMS = "dataset=%5B%7B%0A%09%22name%22%3A%20%22BASIC_AGG_DATA%22%2C%0A%09%22attribute%22%3A%20%5B%7B%0A%09%09%09%22name%22%3A%20%22ACCOUNT_DETAILS%22%2C%0A%09%09%09%22container%22%3A%20%5B%0A%09%09%09%09%22insurance%22%0A%09%09%09%5D%0A%09%09%7D%2C%0A%09%09%7B%0A%09%09%09%22name%22%3A%20%22HOLDINGS%22%2C%0A%09%09%09%22container%22%3A%20%5B%0A%09%09%09%09%22insurance%22%0A%09%09%09%5D%0A%09%09%7D%2C%0A%09%09%7B%0A%09%09%09%22name%22%3A%20%22STATEMENTS%22%2C%0A%09%09%09%22container%22%3A%20%5B%0A%09%09%09%09%22insurance%22%0A%09%09%09%5D%0A%09%09%7D%2C%0A%09%09%7B%0A%09%09%09%22name%22%3A%20%22TRANSACTIONS%22%2C%0A%09%09%09%22container%22%3A%20%5B%0A%09%09%09%09%22insurance%22%0A%09%09%09%5D%0A%09%09%7D%0A%09%5D%0A%7D%5D";
    private static final String APP_ID = "10003600";
    private static final String FORM_HTML_CONTENT = "<div style=\"display:none;\">"
            + "<form action='${NODE_URL}' method='post' id='rsessionPost'>"
            + "<input type='text' name='rsession' value='${RSESSION}'/><br/>"
            + "<input type='text' name='app' value='${FINAPP_ID}'/><br/>"
            + "<input type='text' name='redirectReq' value='true'/><br/>"
            + "<input type='text' name='token' value='${TOKEN}'/><br/>"
            + "<input type='text' name='extraParams' value='${EXTRA_PARAMS}'/><br/>"
            + "</form></div><script>document.getElementById('rsessionPost').submit();</script>";

    private final String cobrandName;
    private final String fastLinkUrl;
    private final YodleeService yodleeService;
    private final String nodeUrl;

    @Autowired
    public FastLinkController(@Value("${yodlee.cobrand.name}") String cobrandName,
                              @Value("${yodlee.fastlink.url}") String fastLinkUrl,
                              YodleeService yodleeService) {
        this.cobrandName = cobrandName;
        this.fastLinkUrl = fastLinkUrl;
        this.yodleeService = yodleeService;
        this.nodeUrl = fastLinkUrl + "/authenticate/" + cobrandName + "/?channelAppName=usyichnnel";
    }

    @GetMapping("/insurance")
    @ResponseBody
    public String getInsuranceFastLink(Principal principal) {
        String username = principal.getName();
        return getFastLinkPage(username, EXTRA_PARAMS);
    }

    @GetMapping("/finance")
    @ResponseBody
    public String getFinanceFastLink(Principal principal) {
        String username = principal.getName();
        return getFastLinkPage(username, "");
    }

    String getFastLinkPage(String username, String extraParams) {
        String cobSession = yodleeService.getCobSession();
        String userSession = yodleeService.getUserSession(cobSession, username);
        String accessToken = yodleeService.getAccessTokens(cobSession, userSession);

        String data = FORM_HTML_CONTENT.replace("${NODE_URL}", nodeUrl)
                .replace("${RSESSION}", userSession)
                .replace("${TOKEN}", accessToken)
                .replace("${EXTRA_PARAMS}", extraParams)
                .replace("${FINAPP_ID}", APP_ID);

        return data;
    }

}
