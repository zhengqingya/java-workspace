package com.zhengqing.demo.api;

import cn.easyes.core.biz.EsPageInfo;
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

@Slf4j
@RestController
@RequestMapping("/simple")
@RequiredArgsConstructor
@Api(tags = "simple")
public class SimpleController {

    private final DocumentMapper documentMapper;

    @PostMapping("insert")
    public void insert(@RequestBody Document document) {
        this.documentMapper.insert(document);
    }

    @GetMapping("search")
    public Object search(@RequestParam String title) {
        EsPageInfo<Document> page = this.documentMapper.pageQuery(
                new LambdaEsQueryWrapper<Document>().like(Document::getTitle, title), 1, 5
        );
        log.info(JSONUtil.toJsonStr(page.getList()));
        return page;
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
                new LambdaEsQueryWrapper<Document>().like(Document::getTitle, title)
        );
    }

}
