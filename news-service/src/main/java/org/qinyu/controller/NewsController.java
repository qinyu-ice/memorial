package org.qinyu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.qinyu.entity.News;
import org.qinyu.service.NewsService;
import org.qinyu.tool.Result;
import org.qinyu.vo.PageVO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api/news", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping(value = "/{id}")
    public Result<News> findById(@PathVariable Integer id) {
        return Result.ok("成功获取id为" + id + "的热点资讯", newsService.getById(id));
    }

    @GetMapping(value = "/{page}/{pageSize}")
    public Result<PageVO<News>> findByPage(
            @PathVariable Integer page, @PathVariable Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String title
    ) {
        Page<News> paged = newsService.lambdaQuery()
                .like(!title.isEmpty(), News::getTitle, title)
                .page(Page.of(page, pageSize));
        if (paged.getRecords().isEmpty()) {
            return Result.ok("暂无相关热点资讯");
        }
        return Result.ok("成功获取第" + page + "页热点资讯", new PageVO<>(paged.getTotal(),
                paged.getRecords()));
    }

    @PostMapping(value = "/add")
    public Result<News> add(@RequestBody News news) {
        newsService.save(news);
        return Result.ok("热点资讯新增成功");
    }

    @PostMapping(value = "/update")
    public Result<News> update(@RequestBody News news) {
        newsService.updateNewsById(news);
        return Result.ok("热点资讯更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<News> delete(@PathVariable Integer id) {
        newsService.removeById(id);
        return Result.ok("热点资讯删除成功");
    }
}
