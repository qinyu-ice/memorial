package org.qinyu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.qinyu.util.Result;
import org.qinyu.entity.Place;
import org.qinyu.vo.PageVO;
import org.qinyu.vo.SimplePlaceVO;
import org.qinyu.service.PlaceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = "/{pid}")
    public Result<Place> findById(@PathVariable Integer pid) {
        Place place = placeService.getById(pid);
        place.setImg("https://www.sctyjrsw.com/image" + place.getImg());
        return Result.ok("成功获取id为" + pid + "的悼念地点信息", place);
    }

    @GetMapping(value = "/ip")
    public Result<String> findByIP(String ip) throws IOException {
        String address = placeService.getAddressByIp(ip);
        return Result.ok("成功获取地址信息" + address,address);
    }

    @GetMapping(value = "/{page}/{pageSize}")
    public Result<PageVO<SimplePlaceVO>> findByPage(
            @PathVariable Integer page, @PathVariable Integer pageSize
    ) {
        Page<Place> paged = placeService.page(Page.of(page, pageSize));
        return Result.ok("成功获取第" + page + "页悼念地点信息", new PageVO<>(paged.getTotal(),
                paged.getRecords().stream().map(SimplePlaceVO::new).toList()));
    }
}
