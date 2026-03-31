package org.qinyu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.qinyu.entity.Info;
import org.qinyu.entity.News;
import org.qinyu.service.InfoService;
import org.qinyu.tool.Result;
import org.qinyu.vo.PageVO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api/info", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class InfoController {

    private final InfoService infoService;

    @GetMapping(value = "/{id}")
    public Result<Info> findById(@PathVariable Integer id) {
        Info info = infoService.getById(id);
        if (info == null) {
            return Result.ok("暂无id为" + id + "的寻亲信息");
        }
        return Result.ok("成功获取id为" + id + "的寻亲信息", info);
    }

    @GetMapping(value = "/{page}/{pageSize}")
    public Result<PageVO<Info>> findByPage(
            @PathVariable Integer page, @PathVariable Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String title
    ) {
        Page<Info> paged = infoService.lambdaQuery()
                .like(!title.isEmpty(), Info::getTitle, title)
                .orderByDesc(Info::getTime)
                .page(Page.of(page, pageSize));
        if (paged.getRecords().isEmpty()) {
            return Result.ok("暂无相关寻亲信息", new PageVO<>(paged.getTotal(), paged.getRecords()));
        }
        return Result.ok("成功获取第" + page + "页寻亲信息", new PageVO<>(paged.getTotal(),
                paged.getRecords()));
    }

    @PostMapping(value = "/add")
    public Result<Info> add(@RequestBody Info info) {
        info.setTime(LocalDateTime.now());
        infoService.save(info);
        return Result.ok("寻亲信息新增成功");
    }

    @PostMapping(value = "/update")
    public Result<Info> update(@RequestBody Info info) {
        infoService.updateInfoById(info);
        return Result.ok("寻亲信息更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Info> delete(@PathVariable Integer id) {
        infoService.removeById(id);
        return Result.ok("寻亲信息删除成功");
    }
}
