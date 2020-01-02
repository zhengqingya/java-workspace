package com.zhengqing.demo.modules.system.repository;

import com.zhengqing.demo.modules.system.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends ElasticsearchRepository<User, Long> {

}
