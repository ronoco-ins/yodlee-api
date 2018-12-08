package com.ronoco.yodlee.model

import com.fasterxml.jackson.annotation.JsonProperty

class Account {

    String accountName
    String description
    String type
    Long id
    Double amount
    Date updateDate
    @JsonProperty("CONTAINER")
    String container
    Balance balance

}
