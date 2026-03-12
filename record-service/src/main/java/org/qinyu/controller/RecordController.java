package org.qinyu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.qinyu.entity.Story;
import org.qinyu.service.client.UserClient;
import org.qinyu.tool.Result;
import org.qinyu.dto.RecordDTO;
import org.qinyu.entity.Record;
import org.qinyu.vo.PageVO;
import org.qinyu.vo.SimpleRecordVO;
import org.qinyu.service.RecordService;
import org.qinyu.vo.SimpleUserVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/record", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class RecordController {

    private final RecordService recordService;
    private final UserClient userClient;

    @PostMapping
    public Result<Object> save(@RequestBody RecordDTO dto) {
        recordService.save(new Record(null, dto.getUserId(), dto.getMartyrId(), dto.getFlower(), dto.getMessage(), null));
        return Result.ok("userId为" + dto.getUserId() + "的用户悼念了martyrId为" + dto.getMartyrId() + "的烈士");
    }

    @GetMapping(value = "/{page}/{pageSize}")
    public Result<PageVO<SimpleRecordVO>> findAllRecord(
            @PathVariable Integer page, @PathVariable Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String searchKeyWord
    ) {
        Integer userId = 0;
        if (!searchKeyWord.isEmpty()) {
            Result<SimpleUserVO> result = userClient.findSimpleByUsername(searchKeyWord);
            if (result.data() != null) {
                userId = result.data().getId();
            } else {
                return Result.no("暂无相关用户的留言");
            }
        }
        Page<SimpleRecordVO> paged = recordService.findByPage(page, pageSize, userId);
        return Result.ok("成功获取第" + page + "页留言", new PageVO<>(paged.getTotal(),
                paged.getRecords()));
    }

    @GetMapping(value = "/all")
    public Result<List<SimpleRecordVO>> getAllRecords() {
        return Result.ok("成功获取所有留言", recordService.findAll());
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Integer id) {
        recordService.removeById(id);
        return Result.ok("留言删除成功");
    }
}
