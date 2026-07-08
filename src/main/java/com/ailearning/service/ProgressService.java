package com.ailearning.service;

import com.ailearning.entity.Progress;
import com.ailearning.mapper.ProgressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProgressService {

    @Autowired
    private ProgressMapper progressMapper;

    public List<Progress> findByUserId(Integer userId) {
        return progressMapper.findByUserId(userId);
    }

    public Progress findByUserIdAndChapterId(Integer userId, Integer chapterId) {
        return progressMapper.findByUserIdAndChapterId(userId, chapterId);
    }

    public boolean updateProgress(Integer userId, Integer chapterId, String status, Integer score, Integer studyTime) {
        Progress progress = progressMapper.findByUserIdAndChapterId(userId, chapterId);
        if (progress == null) {
            progress = new Progress();
            progress.setUserId(userId);
            progress.setChapterId(chapterId);
            progress.setStatus(status);
            progress.setScore(score);
            progress.setStudyTime(studyTime);
            progress.setLastStudyTime(LocalDateTime.now());
            return progressMapper.insert(progress) > 0;
        } else {
            progress.setStatus(status);
            progress.setScore(score);
            progress.setStudyTime(studyTime);
            progress.setLastStudyTime(LocalDateTime.now());
            return progressMapper.update(progress) > 0;
        }
    }

    public boolean updateStudyTime(Integer userId, Integer chapterId, Integer studyTime) {
        Progress progress = progressMapper.findByUserIdAndChapterId(userId, chapterId);
        if (progress == null) {
            progress = new Progress();
            progress.setUserId(userId);
            progress.setChapterId(chapterId);
            progress.setStatus("in_progress");
            progress.setScore(0);
            progress.setStudyTime(studyTime);
            progress.setLastStudyTime(LocalDateTime.now());
            return progressMapper.insert(progress) > 0;
        } else {
            progress.setStudyTime(progress.getStudyTime() + studyTime);
            progress.setLastStudyTime(LocalDateTime.now());
            if ("not_started".equals(progress.getStatus())) {
                progress.setStatus("in_progress");
            }
            return progressMapper.update(progress) > 0;
        }
    }
}
