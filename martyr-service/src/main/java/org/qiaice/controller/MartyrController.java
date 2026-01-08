package org.qiaice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.qiaice.entity.Result;
import org.qiaice.entity.dto.MartyrDTO;
import org.qiaice.entity.table.Martyr;
import org.qiaice.entity.vo.PageVO;
import org.qiaice.entity.vo.SimpleMartyrVO;
import org.qiaice.entity.vo.SimpleSmartMartyrVO;
import org.qiaice.service.MartyrService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/martyr", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class MartyrController {

    private final MartyrService martyrService;

    @GetMapping(value = "/{mid}")
    public Result<Martyr> findById(@PathVariable Integer mid) {
        Martyr martyr = martyrService.getById(mid);
        martyr.setPhoto("https://www.sctyjrsw.com/image" + martyr.getPhoto());
        return Result.ok("成功获取id为" + mid + "的烈士信息", martyr);
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

//    @GetMapping(value = "smart/{page}/{pageSize}")
//    public Result<PageVO<SimpleMartyrVO>> smartFindByPage() {
//        Page<Martyr> paged = martyrService.page(Page.of(page, pageSize));
//        return Result.ok("成功获取第" + page + "页烈士名称", new PageVO<>(paged.getTotal(),
//                paged.getRecords().stream().map(SimpleMartyrVO::new).toList()));
//    }


    @GetMapping(value = "/simple/{mid}")
    public Result<SimpleMartyrVO> findSimpleById(@PathVariable Integer mid) {
        Martyr martyr = martyrService.getById(mid);
        return Result.ok("成功获取mid为" + mid + "的简要烈士信息", new SimpleMartyrVO(martyr));
    }

    @GetMapping(value = "/smartSearch/{page}/{pageSize}")
    public Result<PageVO<SimpleSmartMartyrVO>> smartSearch(
            MartyrDTO martyrDTO,
            @PathVariable Integer page, @PathVariable Integer pageSize
    ) throws IOException {
        System.out.println("martyrDTO:"+martyrDTO);
        PageVO<SimpleSmartMartyrVO> pageVO = martyrService.smartSearch(martyrDTO, page, pageSize);
        return Result.ok("成功获取第" + page + "页烈士名称", pageVO);
    }
}
