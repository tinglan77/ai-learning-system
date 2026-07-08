package com.ailearning.service;

import com.ailearning.entity.Resource;
import com.ailearning.mapper.ResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    public Resource findById(Integer id) {
        return resourceMapper.findById(id);
    }

    public List<Resource> findAll() {
        return resourceMapper.findAll();
    }

    public List<Resource> findByChapterId(Integer chapterId) {
        return resourceMapper.findByChapterId(chapterId);
    }

    public boolean create(Resource resource) {
        return resourceMapper.insert(resource) > 0;
    }

    public boolean update(Resource resource) {
        return resourceMapper.update(resource) > 0;
    }

    public boolean incrementViewCount(Integer id) {
        return resourceMapper.incrementViewCount(id) > 0;
    }
}
