package com.zhengqing.domain.user.event;

import com.zhengqing.domain.user.core.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgSendEvent implements DomainEvent {

    /**
     * 消息发送id
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 发送者
     */
    private String sender;

    /**
     * 接收者
     */
    private String receiver;

    /**
     * 状态 0：成功 1：失败
     */
    private Integer status;

    /**
     * 发送时间
     */
    private Date sendTime;

}
