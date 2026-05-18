package org.qinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.entity.Permission;

import java.util.List;

public interface PermissionService extends IService<Permission> {

    Permission getDetailById(String id);

    Boolean add(Permission permission);

    Boolean update(Permission permission);

    Boolean delete(String id);

    Boolean deleteBatch(List<String> ids);
}
