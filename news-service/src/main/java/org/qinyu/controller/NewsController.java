package org.qinyu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.qinyu.entity.News;
import org.qinyu.service.NewsService;
import org.qinyu.tool.Result;
import org.qinyu.util.AliOssUtil;
import org.qinyu.vo.PageVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/news", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class NewsController {

    private final NewsService newsService;

    private final AliOssUtil aliOssUtil;

    @GetMapping(value = "/{id}")
    public Result<News> findById(@PathVariable Integer id) {
        News news = newsService.getById(id);
        if (news == null) {
            return Result.ok("暂无id为" + id + "的热点资讯");
        }
        return Result.ok("成功获取id为" + id + "的热点资讯", news);
    }

    @GetMapping(value = "/{page}/{pageSize}")
    public Result<PageVO<News>> findByPage(
            @PathVariable Integer page, @PathVariable Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String title
    ) {
        Page<News> paged = newsService.lambdaQuery()
                .like(!title.isEmpty(), News::getTitle, title)
                .page(Page.of(page, pageSize));
        // 遍历查询结果，处理每个烈士的photo
        paged.getRecords().forEach(news -> {
            // 先判断photo是否为空，避免空指针异常
            if (news.getImg() != null && !news.getImg().isEmpty()) {
                if (news.getImg().contains("/memorial/news")) {
                    news.setImg("https://memorial-dazhou.oss-cn-chengdu.aliyuncs.com" + news.getImg());
                } else {
                    news.setImg("https://www.sctyjrsw.com" + news.getImg());
                }
            }
        });
        if (paged.getRecords().isEmpty()) {
            return Result.ok("暂无相关热点资讯", new PageVO<>(paged.getTotal(), paged.getRecords()));
        }
        return Result.ok("成功获取第" + page + "页热点资讯", new PageVO<>(paged.getTotal(),
                paged.getRecords()));
    }

    @GetMapping(value = "/getImg")
    public Result<List<String>> getImg() {
        List<String> imgList = newsService.lambdaQuery()
                .select(News::getImg)
                .last("LIMIT 6")
                .list()
                .stream()
                .map(News::getImg)
                // 处理路径拼接
                .map(img -> {
                    if (img == null || img.isEmpty()) {
                        return img;
                    }
                    if (img.contains("/memorial/news")) {
                        return "https://memorial-dazhou.oss-cn-chengdu.aliyuncs.com" + img;
                    } else {
                        return "https://www.sctyjrsw.com" + img;
                    }
                })
                .toList();
        return Result.ok("成功获取热点资讯图片", imgList);
    }

    @PostMapping(value = "/add")
    public Result<News> add(@RequestBody News news) {
        news.setTime(LocalDateTime.now());
        newsService.save(news);
        return Result.ok("热点资讯新增成功");
    }

    @PostMapping(value = "/update")
    public Result<News> update(@RequestBody News news) {
        newsService.updateNewsById(news);
        return Result.ok("热点资讯更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result<News> delete(@PathVariable Integer id) {
        newsService.removeById(id);
        return Result.ok("热点资讯删除成功");
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
        String objectName = "memorial/news/" + originalFilename;

        //文件的请求路径
        //参数：  byte数组，文件对象转成的数组     传上去的图片在阿里云存储空间里面的名字
        String filePath = aliOssUtil.upload(file.getBytes(), objectName);
        return Result.ok("上传成功", filePath);
    }
}
