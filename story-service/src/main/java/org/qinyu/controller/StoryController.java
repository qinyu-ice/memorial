package org.qinyu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.qinyu.tool.Result;
import org.qinyu.entity.Story;
import org.qinyu.vo.PageVO;
import org.qinyu.vo.SimpleStoryVO;
import org.qinyu.service.StoryService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/add")
    public Result<Story> add(@RequestBody Story story) {
        storyService.save(story);
        return Result.ok("寻亲故事新增成功");
    }

    @PostMapping(value = "/update")
    public Result<Story> update(@RequestBody Story story) {
        storyService.updateById(story);
        return Result.ok("寻亲故事更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Story> delete(@PathVariable Integer id) {
        storyService.removeById(id);
        return Result.ok("寻亲故事删除成功");
    }
}
