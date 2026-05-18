package org.qinyu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.qinyu.entity.Permission;
import org.qinyu.entity.PermissionUserBind;
import org.qinyu.service.PermissionService;
import org.qinyu.tool.Result;
import org.qinyu.vo.PageVO;
import org.qinyu.vo.SimplePermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/permission", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping(value = "/{id}")
    public Result<Permission> selectById(@PathVariable String id) {
        return Result.ok("权限查询成功", permissionService.getDetailById(id));
    }

    @GetMapping(value = "/{page}/{pageSize}")
    public Result<PageVO<SimplePermissionVO>> findByPage(
            @PathVariable Integer page, @PathVariable Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String name
    ) {
        Page<Permission> pageParam = Page.of(page, pageSize);
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(Permission::getName, name.trim()).orderByDesc(Permission::getCreateTime);
        }
        Page<Permission> paged = permissionService.page(pageParam, queryWrapper);
        if (paged.getRecords().isEmpty()) {
            return Result.ok("暂无相关权限", new PageVO<>(paged.getTotal(), paged.getRecords().stream().map(SimplePermissionVO::new).toList()));
        }
        return Result.ok("成功获取第" + page + "页权限", new PageVO<>(paged.getTotal(),
                paged.getRecords().stream().map(SimplePermissionVO::new).toList()));
    }

    @PostMapping(value = "/add")
    public Result<Boolean> add(@RequestBody Permission permission) {
        Boolean result = permissionService.add(permission);
        return Result.ok("权限新增成功", result);
    }

    @PostMapping(value = "/update")
    public Result<Boolean> update(@RequestBody Permission permission) {
        Boolean result = permissionService.update(permission);
        return Result.ok("权限更新成功", result);
    }

    @DeleteMapping(value = "/delete/{id}")
    public Result<Boolean> delete(@PathVariable String id) {
        Boolean result = permissionService.delete(id);
        return Result.ok("权限删除成功", result);
    }

    @DeleteMapping(value = "/deleteBatch")
    public Result<Boolean> deleteBatch(@RequestParam List<String> ids) {
        Boolean result = permissionService.deleteBatch(ids);
        return Result.ok("权限批量删除成功", result);
    }

    @PostMapping(value = "/bindBatch")
    public Result<Boolean> bindPermissionList(@RequestBody List<PermissionUserBind> binds) {
        Boolean result = permissionService.bindPermissionList(binds);
        return Result.ok("权限绑定成功", result);
    }

    @PostMapping(value = "unbindBatch")
    public Result<Boolean> unbindPermissionList(@RequestBody List<PermissionUserBind> binds) {
        Boolean result = permissionService.unbindPermissionList(binds);
        return Result.ok("权限解绑成功", result);
    }
}
