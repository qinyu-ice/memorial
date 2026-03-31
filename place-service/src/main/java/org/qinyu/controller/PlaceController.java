package org.qinyu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.qinyu.entity.News;
import org.qinyu.tool.Result;
import org.qinyu.entity.Place;
import org.qinyu.util.AliOssUtil;
import org.qinyu.vo.PageVO;
import org.qinyu.vo.SimplePlaceVO;
import org.qinyu.service.PlaceService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/place", produces = "application/json; charset=utf-8")
@AllArgsConstructor
@Slf4j
public class PlaceController {

    private final PlaceService placeService;

    private AliOssUtil aliOssUtil;

    @GetMapping
    public Result<List<SimplePlaceVO>> findAll() {
        return Result.ok("成功获取所有烈士纪念设施",
                placeService.list().stream().map(SimplePlaceVO::new).toList());
    }

    @GetMapping(value = "/{id}")
    public Result<Place> findById(@PathVariable Integer id) {
        Place place = placeService.getById(id);
        if (place == null) {
            return Result.ok("暂无id为" + id + "的烈士纪念设施");
        }
        place.setImg("https://memorial-dazhou.oss-cn-chengdu.aliyuncs.com" + place.getImg());
        return Result.ok("成功获取id为" + id + "的烈士纪念设施", place);
    }

    @GetMapping(value = "/ip")
    public Result<String> findByIP(String ip) throws IOException {
        String address = placeService.getAddressByIp(ip);
        return Result.ok("成功获取烈士纪念设施" + address, address);
    }

    @GetMapping(value = "/{page}/{pageSize}")
    public Result<PageVO<SimplePlaceVO>> findByPage(
            @PathVariable Integer page, @PathVariable Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String name
    ) {
        // 1. 创建分页对象
        Page<Place> pageParam = Page.of(page, pageSize);

        // 2. 构建查询条件
        LambdaQueryWrapper<Place> queryWrapper = new LambdaQueryWrapper<>();
        // 如果name参数不为空且不是空字符串，添加模糊查询条件
        if (name != null && !name.trim().isEmpty()) {
            queryWrapper.like(Place::getName, name.trim()).orderByDesc(Place::getCreateTime);
        }

        Page<Place> paged = placeService.page(pageParam, queryWrapper);
        if (paged.getRecords().isEmpty()) {
            return Result.ok("暂无相关烈士纪念设施", new PageVO<>(paged.getTotal(), paged.getRecords().stream().map(SimplePlaceVO::new).toList()));
        }
        return Result.ok("成功获取第" + page + "页烈士纪念设施", new PageVO<>(paged.getTotal(),
                paged.getRecords().stream().map(SimplePlaceVO::new).toList()));
    }

    @PostMapping("/add")
    public Result<Object> add(@RequestBody Place place) {
        placeService.save(place);
        return Result.ok("烈士纪念设施新增成功");
    }

    @PostMapping("/update")
    public Result<Object> update(@RequestBody Place place) {
        placeService.updateById(place);
        return Result.ok("烈士纪念设施" + place.getName() + "更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Integer id) {
        placeService.removeById(id);
        return Result.ok("烈士纪念设施删除成功");
    }

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return Result.no("上传失败：文件不能为空");
        }
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //截取原始文件名的后缀
//        String extension = null;
//        if (originalFilename != null) {
//            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        }
        //构造新文件名称
        String objectName = "memorial/facilities/" + originalFilename;

        //文件的请求路径
        //参数：  byte数组，文件对象转成的数组     传上去的图片在阿里云存储空间里面的名字
        String filePath = aliOssUtil.upload(file.getBytes(), objectName);
        return Result.ok("上传成功", filePath);
    }
}
