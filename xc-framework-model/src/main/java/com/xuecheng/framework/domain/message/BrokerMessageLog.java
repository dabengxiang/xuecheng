package com.xuecheng.framework.domain.message;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * project name : xuecheng
 * Date:2019/1/3
 * Author: yc.guo
 * DESC:
 */

@Data
@ToString
@Document(collection = "broker_message_log")
public class BrokerMessageLog {
    
    @Id
    private String messageId;

    private String message;

    private Integer tryCount = 0;

    private String status;

    private Date nextRetry;

    private Date createTime;

    private Date updateTime;


}
