package org.qinyu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.qinyu.tool.Result;
import org.qinyu.dto.MartyrDTO;
import org.qinyu.entity.Martyr;
import org.qinyu.util.AliOssUtil;
import org.qinyu.vo.PageVO;
import org.qinyu.vo.SimpleMartyrVO;
import org.qinyu.vo.SimpleSmartMartyrVO;
import org.qinyu.service.MartyrService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/martyr", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class MartyrController {

    private final MartyrService martyrService;

    private AliOssUtil aliOssUtil;

    @GetMapping(value = "/{id}")
    public Result<Martyr> findById(@PathVariable Integer id) {
        Martyr martyr = martyrService.getById(id);
        if (martyr.getPhoto().contains("https://memorial-dazhou.oss-cn-chengdu.aliyuncs.com")) {
            martyr.setPhoto("https://memorial-dazhou.oss-cn-chengdu.aliyuncs.com" + martyr.getPhoto());
        } else {
            martyr.setPhoto("https://www.sctyjrsw.com" + martyr.getPhoto());
        }
        return Result.ok("成功获取" + martyr.getName() + "烈士的信息", martyr);
    }

    @GetMapping(value = "/{page}/{pageSize}")
    public Result<PageVO<SimpleMartyrVO>> findByPage(
            @PathVariable Integer page, @PathVariable Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String name
    ) {
        Page<Martyr> paged = martyrService.lambdaQuery()
                .like(!name.isEmpty(), Martyr::getName, name)
                .page(Page.of(page, pageSize));
        if (paged.getRecords().isEmpty()) {
            return Result.ok("暂无相关烈士信息", new PageVO<>(paged.getTotal(), paged.getRecords().stream().map(SimpleMartyrVO::new).toList()));
        }
        return Result.ok("成功获取第" + page + "页烈士名称", new PageVO<>(paged.getTotal(),
                paged.getRecords().stream().map(SimpleMartyrVO::new).toList()));
    }

    @GetMapping(value = "/all/{page}/{pageSize}")
    public Result<PageVO<Martyr>> findAllByPage(
            @PathVariable Integer page, @PathVariable Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String name
    ) {
        Page<Martyr> paged = martyrService.lambdaQuery()
                .like(!name.isEmpty(), Martyr::getName, name)
                .page(Page.of(page, pageSize));
        // 遍历查询结果，处理每个烈士的photo
        paged.getRecords().forEach(martyr -> {
            // 先判断photo是否为空，避免空指针异常
            if (martyr.getPhoto() != null && !martyr.getPhoto().isEmpty()) {
                if (martyr.getPhoto().contains("/memorial/martyr")) {
                    martyr.setPhoto("https://memorial-dazhou.oss-cn-chengdu.aliyuncs.com" + martyr.getPhoto());
                } else {
                    martyr.setPhoto("https://www.sctyjrsw.com" + martyr.getPhoto());
                }
            }
        });
        if (paged.getRecords().isEmpty()) {
            return Result.ok("暂无相关烈士信息", new PageVO<>(paged.getTotal(), paged.getRecords()));
        }
        return Result.ok("成功获取第" + page + "页烈士信息", new PageVO<>(paged.getTotal(),
                paged.getRecords()));
    }

    @PostMapping("/add")
    public Result<Object> add(@RequestBody Martyr martyr) {
        martyrService.save(martyr);
        return Result.ok("烈士信息新增成功");
    }

    @PostMapping("/update")
    public Result<Object> update(@RequestBody Martyr martyr) {
        martyrService.updateMartyrById(martyr);
        return Result.ok("烈士信息更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Integer id) {
        martyrService.removeById(id);
        return Result.ok("烈士信息删除成功");
    }

//    @GetMapping(value = "smart/{page}/{pageSize}")
//    public Result<PageVO<SimpleMartyrVO>> smartFindByPage() {
//        Page<Martyr> paged = martyrService.page(Page.of(page, pageSize));
//        return Result.ok("成功获取第" + page + "页烈士名称", new PageVO<>(paged.getTotal(),
//                paged.getRecords().stream().map(SimpleMartyrVO::new).toList()));
//    }


    @GetMapping(value = "/simple/{id}")
    public Result<SimpleMartyrVO> findSimpleById(@PathVariable Integer id) {
        Martyr martyr = martyrService.getById(id);
        return Result.ok("成功获取id为" + id + "的简要烈士信息", new SimpleMartyrVO(martyr));
    }

    @GetMapping(value = "/smartSearch/{page}/{pageSize}")
    public Result<PageVO<SimpleSmartMartyrVO>> smartSearch(
            MartyrDTO martyrDTO,
            @PathVariable Integer page, @PathVariable Integer pageSize
    ) throws IOException {
        System.out.println("martyrDTO:" + martyrDTO);
        PageVO<SimpleSmartMartyrVO> pageVO = martyrService.smartSearch(martyrDTO, page, pageSize);
        return Result.ok("成功获取第" + page + "页烈士名称", pageVO);
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
        String objectName = "memorial/martyr/" + originalFilename;

        //文件的请求路径
        //参数：  byte数组，文件对象转成的数组     传上去的图片在阿里云存储空间里面的名字
        String filePath = aliOssUtil.upload(file.getBytes(), objectName);
        return Result.ok("上传成功", filePath);
    }
}
