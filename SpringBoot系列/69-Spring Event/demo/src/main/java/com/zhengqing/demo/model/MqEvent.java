package com.zhengqing.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
@ToString
public class MqEvent extends ApplicationEvent {
    private Integer id;
    private String name;

    public MqEvent(Object source, Integer id, String name) {
        super(source);
        this.id = id;
        this.name = name;
    }
}