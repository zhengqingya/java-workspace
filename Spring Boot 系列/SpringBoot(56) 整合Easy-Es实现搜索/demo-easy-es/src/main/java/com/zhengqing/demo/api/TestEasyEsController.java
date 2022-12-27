package com.zhengqing.demo.api;

import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import cn.easyes.core.conditions.LambdaEsUpdateWrapper;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.zhengqing.demo.mapper.DocumentMapper;
import com.zhengqing.demo.model.Document;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("easy-es")
@RequiredArgsConstructor
@Api(tags = "easy-es")
public class TestEasyEsController {
    private final DocumentMapper documentMapper;

    @PostMapping("insert")
    public void insert(@RequestBody Document document) {
        this.documentMapper.insert(document);
    }

    @PostMapping("insertRandom")
    public void insertRandom() {
        List<Document> list = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            list.add(
                    Document.builder()
                            .id(RandomUtil.randomNumbers(10))
                            .title("zhengqingya:" + RandomUtil.randomString("奶茶店里的小帅", 5))
                            .content(RandomUtil.randomString("奶茶店里的小帅很好看哦，希望你多去喝一杯", 20))
                            .build()
            );
        }
        this.documentMapper.insertBatch(list);
    }

    @GetMapping("search")
    public List<Document> search(@RequestParam String title) {
        List<Document> documentList = this.documentMapper.selectList(
                new LambdaEsQueryWrapper<Document>().eq(Document::getTitle, title)
        );
        log.info(JSONUtil.toJsonStr(documentList));
        return documentList;
    }

    @GetMapping("searchKeyword")
    public List<Document> searchKeyword(@RequestParam String keyword) {
        List<Document> documentList = this.documentMapper.selectList(
                new LambdaEsQueryWrapper<Document>().match(Document::getContent, keyword)
        );
        log.info(JSONUtil.toJsonStr(documentList));
        return documentList;
    }

    @PutMapping("update")
    public void update(@RequestParam String oldTitle,
                       @RequestParam String newTitle) {
        this.documentMapper.update(
                Document.builder()
                        .title(newTitle)
                        .content(RandomUtil.randomString(6))
                        .build(),
                new LambdaEsUpdateWrapper<Document>()
                        .eq(Document::getTitle, oldTitle)
        );
    }

    @DeleteMapping("delete")
    public void delete(@RequestParam String title) {
        this.documentMapper.delete(
                new LambdaEsQueryWrapper<Document>().eq(Document::getTitle, title)
        );
    }

}
