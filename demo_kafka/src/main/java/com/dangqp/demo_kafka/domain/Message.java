package com.dangqp.demo_kafka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title:com.dangqp.demo_kafka.domain
 * Description:
 * Copyright: Copyright (c) 2018
 *
 * @author dangqp
 * @version 1.0
 * @created 2018/06/07  15:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    private String id;
    private String msgName;
    private String msg;
}
