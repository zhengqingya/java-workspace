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
//        list.add(Document.builder().id(null).title("zhengqingya"  ).content("人生是单行路，只有奋斗了，才会有光明的前途。").build());
//        list.add(Document.builder().id(null).title("zhengqingya"  ).content("过去的永远不再来，未来的还需要努力。").build());
//        list.add(Document.builder().id(null).title("zhengqingya"  ).content("请不要在该吃苦的年纪选择安逸，走自己的路，为自己的梦想好好去奋斗，即使有人亏待你，时间也不会亏待你，人生更不会亏待你。").build());
//        list.add(Document.builder().id(null).title("zhengqingya"  ).content("从零开始需要勇气，而我从来不缺少勇气，成长是逼迫自己努力的过程。").build());
//        list.add(Document.builder().id(null).title("zhengqingya"  ).content("努力是一种生活态度，和年龄无关。生活要有激情，只要你有前进的方向和目标，什么时候开始都不晚。").build());
//        list.add(Document.builder().id(null).title("zhengqingya"  ).content("生活，不是用来妥协的；日子，不是用来将就的。退缩得越多，表现得越卑微，幸福就会离得越远。").build());
//        list.add(Document.builder().id(null).title("zhengqingya"  ).content("为努力工作的自我加油，提醒虚荣的自我，它不再被抛弃。相信梦想，梦想会相信你，有一个鸿沟，你不配你的野心，也辜负了苦难。").build());
//        list.add(Document.builder().id(null).title("zhengqingya"  ).content("别总是很快的否定自己,你是优秀的,值得拥有最特别的。").build());
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
