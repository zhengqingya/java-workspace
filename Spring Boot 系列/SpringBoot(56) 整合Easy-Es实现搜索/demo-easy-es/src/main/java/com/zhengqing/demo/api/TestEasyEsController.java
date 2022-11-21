package com.zhengqing.demo.api;

import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import cn.easyes.core.conditions.LambdaEsUpdateWrapper;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
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

    @GetMapping("search")
    public List<Document> search(@RequestParam String title) {
        List<Document> documentList = this.documentMapper.selectList(
                new LambdaEsQueryWrapper<Document>().eq(Document::getTitle, title)
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
