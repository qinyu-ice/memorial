package org.qinyu.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qinyu.entity.Permission;
import org.qinyu.entity.PermissionUserBind;
import org.qinyu.mapper.PermissionMapper;
import org.qinyu.mapper.PermissionUserBindMapper;
import org.qinyu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private PermissionUserBindMapper permissionUserBindMapper;

    @Override
    public Permission getDetailById(String id) {
        return permissionMapper.selectById(id);
    }

    @Override
    public Boolean add(Permission permission) {
        Permission perm = permissionMapper.selectByName(permission.getName());
        if (perm != null) {
            if (perm.getName().equals(permission.getName()) && perm.getStatus().equals(permission.getStatus()) && perm.getDescription().equals(permission.getDescription())) {
                return false;
            }
        }
        permissionMapper.insert(permission);
        return true;
    }

    @Override
    public Boolean update(Permission permission) {
        Permission perm = permissionMapper.selectById(permission.getId());
        if (perm == null) {
            return false;
        }
        if (perm.getName().equals(permission.getName()) && perm.getStatus().equals(permission.getStatus()) && perm.getDescription().equals(permission.getDescription())) {
            return false;
        }
        permission.setUpdateTime(LocalDateTime.now());
        permissionMapper.updateById(permission);
        return true;
    }

    @Override
    public Boolean delete(String id) {
        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            return false;
        }
        permissionMapper.deleteById(id);
        return true;
    }

    @Override
    public Boolean deleteBatch(List<String> ids) {
        return permissionMapper.deleteBatch(ids);
    }

    @Override
    public Boolean bindPermissionList(List<PermissionUserBind> binds) {
        if (CollectionUtils.isEmpty(binds)) {
            return false;
        }

        // 获取已经存在的权限绑定关系列表
        List<PermissionUserBind> existBinds = permissionUserBindMapper.existBatch(binds);

        // 过滤出不存在的权限绑定关系列表
        List<PermissionUserBind> notExistList = binds.stream()
                .filter(dto -> existBinds.stream()
                        .noneMatch(exist ->
                                dto.getPermId().equals(exist.getPermId())
                                        && dto.getUserId().equals(exist.getUserId())
                        )
                )
                .toList();

        if (CollectionUtils.isEmpty(notExistList)) {
            return false;
        }

        permissionUserBindMapper.insertBatch(notExistList);
        return true;
    }

    @Override
    public Boolean unbindPermissionList(List<PermissionUserBind> binds) {
        if (CollectionUtils.isEmpty(binds)) {
            return false;
        }

        // 获取已经存在的权限绑定关系列表
        List<PermissionUserBind> existBinds = permissionUserBindMapper.existBatch(binds);

        if (CollectionUtils.isEmpty(existBinds)) {
            return false;
        }

        permissionUserBindMapper.deleteBatch(existBinds);
        return true;
    }
}
