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
        recordService.save(new Record(null, dto.getUserId(), dto.getMartyrId(),
                dto.getIgnite(), dto.getFlower(), dto.getMessage(), null));
        return Result.ok("userId为" + dto.getUserId() + "的用户悼念了martyrId为" + dto.getMartyrId() + "的烈士");
    }

    @GetMapping(value = "/{page}/{pageSize}")
    public Result<List<SimpleRecordVO>> findByPage(
            @PathVariable Integer page, @PathVariable Integer pageSize
    ) {
        return Result.ok("成功获取前" + pageSize + "条留言", recordService.findByPage(page, pageSize));
    }
}
