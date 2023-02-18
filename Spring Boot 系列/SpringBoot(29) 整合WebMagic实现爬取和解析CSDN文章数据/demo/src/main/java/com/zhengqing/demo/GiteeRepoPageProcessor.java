package com.zhengqing.demo;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * <p> 获取Gitee项目名 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/2/18 4:30 PM
 */
public class GiteeRepoPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        List<Selectable> projectList = html.xpath(
                "//div[@class='ui two cards']" +
                        "//a[@class='popular-project-title']" +
                        "//text()"
        ).nodes();
        System.err.println(projectList);
    }

    @Override
    public Site getSite() {
        return this.site;
    }

    public static void main(String[] args) {
        Spider.create(new GiteeRepoPageProcessor()).addUrl("https://gitee.com/zhengqingya").thread(5).run();
    }
}