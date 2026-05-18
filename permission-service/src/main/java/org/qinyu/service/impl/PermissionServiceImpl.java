package org.qinyu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qinyu.entity.Permission;
import org.qinyu.mapper.PermissionMapper;
import org.qinyu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

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
}
