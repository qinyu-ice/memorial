package org.qinyu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.qinyu.tool.Result;
import org.qinyu.entity.Place;
import org.qinyu.vo.PageVO;
import org.qinyu.vo.SimplePlaceVO;
import org.qinyu.service.PlaceService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/place", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public Result<List<SimplePlaceVO>> findAll() {
        return Result.ok("成功获取所有悼念地点信息",
                placeService.list().stream().map(SimplePlaceVO::new).toList());
    }

    @GetMapping(value = "/{id}")
    public Result<Place> findById(@PathVariable Integer id) {
        Place place = placeService.getById(id);
        place.setImg("https://www.sctyjrsw.com/image" + place.getImg());
        return Result.ok("成功获取id为" + id + "的悼念地点信息", place);
    }

    @GetMapping(value = "/ip")
    public Result<String> findByIP(String ip) throws IOException {
        String address = placeService.getAddressByIp(ip);
        return Result.ok("成功获取地址信息" + address, address);
    }

    @GetMapping(value = "/{page}/{pageSize}")
    public Result<PageVO<SimplePlaceVO>> findByPage(
            @PathVariable Integer page, @PathVariable Integer pageSize
    ) {
        Page<Place> paged = placeService.page(Page.of(page, pageSize));
        return Result.ok("成功获取第" + page + "页悼念地点信息", new PageVO<>(paged.getTotal(),
                paged.getRecords().stream().map(SimplePlaceVO::new).toList()));
    }

    @PostMapping("/add")
    public Result<Object> add(@RequestBody Place place) {
        placeService.save(place);
        return Result.ok("悼念地点新增成功");
    }

    @PostMapping("/update")
    public Result<Object> update(@RequestBody Place place) {
        placeService.updateById(place);
        return Result.ok("悼念地点" + place.getName() + "更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Integer id) {
        placeService.removeById(id);
        return Result.ok("悼念地点删除成功");
    }
}
