package org.qinyu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.qinyu.tool.Result;
import org.qinyu.dto.MartyrDTO;
import org.qinyu.entity.Martyr;
import org.qinyu.vo.PageVO;
import org.qinyu.vo.SimpleMartyrVO;
import org.qinyu.vo.SimpleSmartMartyrVO;
import org.qinyu.service.MartyrService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/martyr", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class MartyrController {

    private final MartyrService martyrService;

    @GetMapping(value = "/{id}")
    public Result<Martyr> findById(@PathVariable Integer id) {
        Martyr martyr = martyrService.getById(id);
        martyr.setPhoto("https://www.sctyjrsw.com/image" + martyr.getPhoto());
        return Result.ok("成功获取id为" + id + "的烈士信息", martyr);
    }

    @GetMapping(value = "/{page}/{pageSize}")
    public Result<PageVO<SimpleMartyrVO>> findByPage(
            @PathVariable Integer page, @PathVariable Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String name
    ) {
        Page<Martyr> paged = martyrService.lambdaQuery()
                .like(!name.isEmpty(), Martyr::getName, name)
                .page(Page.of(page, pageSize));
        return Result.ok("成功获取第" + page + "页烈士名称", new PageVO<>(paged.getTotal(),
                paged.getRecords().stream().map(SimpleMartyrVO::new).toList()));
    }

    @PostMapping("/add")
    public Result<Object> add(@RequestBody Martyr martyr) {
        martyrService.save(martyr);
        return Result.ok("烈士信息新增成功");
    }

    @PostMapping("/update")
    public Result<Object> update(@RequestBody Martyr martyr) {
        martyrService.updateById(martyr);
        return Result.ok("烈士信息更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Integer id) {
        martyrService.removeById(id);
        return Result.ok("烈士信息删除成功");
    }

//    @GetMapping(value = "smart/{page}/{pageSize}")
//    public Result<PageVO<SimpleMartyrVO>> smartFindByPage() {
//        Page<Martyr> paged = martyrService.page(Page.of(page, pageSize));
//        return Result.ok("成功获取第" + page + "页烈士名称", new PageVO<>(paged.getTotal(),
//                paged.getRecords().stream().map(SimpleMartyrVO::new).toList()));
//    }


    @GetMapping(value = "/simple/{id}")
    public Result<SimpleMartyrVO> findSimpleById(@PathVariable Integer id) {
        Martyr martyr = martyrService.getById(id);
        return Result.ok("成功获取mid为" + id + "的简要烈士信息", new SimpleMartyrVO(martyr));
    }

    @GetMapping(value = "/smartSearch/{page}/{pageSize}")
    public Result<PageVO<SimpleSmartMartyrVO>> smartSearch(
            MartyrDTO martyrDTO,
            @PathVariable Integer page, @PathVariable Integer pageSize
    ) throws IOException {
        System.out.println("martyrDTO:" + martyrDTO);
        PageVO<SimpleSmartMartyrVO> pageVO = martyrService.smartSearch(martyrDTO, page, pageSize);
        return Result.ok("成功获取第" + page + "页烈士名称", pageVO);
    }
}
