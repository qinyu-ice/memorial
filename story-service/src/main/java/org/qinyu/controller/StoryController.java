package org.qinyu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.qinyu.util.Result;
import org.qinyu.entity.Story;
import org.qinyu.vo.PageVO;
import org.qinyu.vo.SimpleStoryVO;
import org.qinyu.service.StoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/story", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @GetMapping(value = "/{id}")
    public Result<Story> findById(@PathVariable Integer id) {
        return Result.ok("成功获取id为" + id + "的寻亲故事信息", storyService.getById(id));
    }

    @GetMapping(value = "/{page}/{pageSize}")
    public Result<PageVO<SimpleStoryVO>> findAll(
            @PathVariable Integer page, @PathVariable Integer pageSize
    ) {
        Page<Story> paged = storyService.lambdaQuery()
                .orderByDesc(Story::getTime)
                .page(Page.of(page, pageSize));
        return Result.ok("成功获取第" + page + "页简要寻亲故事信息", new PageVO<>(paged.getTotal(),
                paged.getRecords().stream().map(SimpleStoryVO::new).toList()));
    }
}
