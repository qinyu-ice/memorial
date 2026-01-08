package org.qinyu.controller;

import lombok.AllArgsConstructor;
import org.qinyu.util.Result;
import org.qinyu.dto.RecordDTO;
import org.qinyu.entity.Record;
import org.qinyu.vo.SimpleRecordVO;
import org.qinyu.service.RecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/record", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    public Result<Object> save(@RequestBody RecordDTO dto) {
        recordService.save(new Record(null, dto.getUid(), dto.getMid(),
                dto.getIgnite(), dto.getFlower(), dto.getMessage(), null));
        return Result.ok("uid为" + dto.getUid() + "的用户悼念了mid为" + dto.getMid() + "的烈士");
    }

    @GetMapping(value = "/{page}/{pageSize}")
    public Result<List<SimpleRecordVO>> findByPage(
            @PathVariable Integer page, @PathVariable Integer pageSize
    ) {
        return Result.ok("成功获取前" + pageSize + "条留言", recordService.findByPage(page, pageSize));
    }
}
