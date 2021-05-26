package com.zhengqing.demo.mongodb;

import com.alibaba.fastjson.JSONObject;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@Slf4j
@RestController
@RequestMapping("")
public class TestController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("add")
    public void add() {
        log.info("add");
        mongoTemplate.save(User.builder().id(1L).username("admin").nickname("管理员").build());
        mongoTemplate.save(User.builder().id(2L).username("test").nickname("测试员").build());
    }

    @DeleteMapping("delete")
    public void delete() {
        log.info("delete");
        mongoTemplate.remove(User.builder().id(2L).build());
    }

    @PutMapping("update")
    public void update() {
        log.info("update");
        Query query = new Query(Criteria.where("id").is(1L));
        Update update = new Update();
        update.set("username", "admin01");
        update.set("nickname", "管理员01");
        mongoTemplate.upsert(query, update, User.class);
    }

    @GetMapping("select")
    public void select() {
        log.info("select");
        Query query = new Query(Criteria.where("id").is(1L));
        User user = mongoTemplate.findOne(query, User.class);
        log.info(JSONObject.toJSONString(user));
    }


}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
class User implements Serializable {

    @Id
    private Long id;

    private String username;

    private String nickname;

}
