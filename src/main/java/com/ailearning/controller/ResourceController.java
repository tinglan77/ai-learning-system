package com.ailearning.controller;

import com.ailearning.common.Result;
import com.ailearning.entity.Resource;
import com.ailearning.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/list")
    public Result<List<Resource>> getAllResources() {
        return Result.success(resourceService.findAll());
    }

    @GetMapping("/chapter/{chapterId}")
    public Result<List<Resource>> getResourcesByChapterId(@PathVariable Integer chapterId) {
        return Result.success(resourceService.findByChapterId(chapterId));
    }

    @GetMapping("/{id}")
    public Result<Resource> getResourceById(@PathVariable Integer id) {
        Resource resource = resourceService.findById(id);
        if (resource != null) {
            resourceService.incrementViewCount(id);
            return Result.success(resource);
        }
        return Result.error("资源不存在");
    }

    @PostMapping("/create")
    public Result<String> createResource(@RequestBody Resource resource) {
        boolean success = resourceService.create(resource);
        if (success) {
            return Result.success("创建成功");
        }
        return Result.error("创建失败");
    }

    @PutMapping("/update")
    public Result<String> updateResource(@RequestBody Resource resource) {
        boolean success = resourceService.update(resource);
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
}
