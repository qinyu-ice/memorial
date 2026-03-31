package org.qinyu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.qinyu.entity.MartyrStory;
import org.qinyu.entity.News;
import org.qinyu.service.MartyrStoryService;
import org.qinyu.tool.Result;
import org.qinyu.vo.PageVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/martyrStory", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class MartyrStoryController {

    private MartyrStoryService martyrStoryService;

    @GetMapping(value = "/{page}/{pageSize}")
    public Result<PageVO<MartyrStory>> findMartyrStory(
            @PathVariable Integer page, @PathVariable Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String title
    ) {
        Page<MartyrStory> paged = martyrStoryService.lambdaQuery()
                .like(!title.isEmpty(), MartyrStory::getTitle, title)
                .orderByDesc(MartyrStory::getTime)
                .page(Page.of(page, pageSize));
        if (paged.getRecords().isEmpty()) {
            return Result.ok("暂无相关英烈故事", new PageVO<>(paged.getTotal(), paged.getRecords()));
        }
        return Result.ok("成功获取第" + page + "页英烈故事", new PageVO<>(paged.getTotal(),
                paged.getRecords()));
    }

    @GetMapping(value = "/{id}")
    public Result<MartyrStory> findById(@PathVariable Integer id) {
        MartyrStory martyrStory = martyrStoryService.getById(id);
        if (martyrStory == null) {
            return Result.ok("暂无id为" + id + "的英烈故事");
        }
        return Result.ok("成功获取id为" + id + "的英烈故事", martyrStory);
    }

    @PostMapping(value = "/add")
    public Result<Object> add(@RequestBody MartyrStory martyrStory) {
        martyrStoryService.save(martyrStory);
        return Result.ok("英烈故事新增成功");
    }

    @PostMapping("/update")
    public Result<Object> update(@RequestBody MartyrStory martyrStory) {
        martyrStoryService.updateMartyrStoryById(martyrStory);
        return Result.ok("英烈故事更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Integer id) {
        martyrStoryService.removeById(id);
        return Result.ok("英烈故事删除成功");
    }
}
