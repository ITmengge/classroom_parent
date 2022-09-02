package com.simon.classroom.vod.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.vod.Subject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author simon
 * @since 2022-08-29
 */
public interface SubjectService extends IService<Subject> {

    /**
     * 根据parent_id查询课程分类
     * @param id
     * @return
     */
    List<Subject> selectList(Long id);

    /**
     * 课程分类导出
     * @param response
     */
    void exportData(HttpServletResponse response);

    /**
     * 课程分类导入
     * @param file
     */
    void importData(MultipartFile file);

    /**
     * 通过parent_id查询对应的subject
     * @param subjectParentId
     * @return
     */
    Subject getByParentId(Long subjectParentId);
}
