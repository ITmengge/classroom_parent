package com.simon.classroom.vod.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.simon.classroom.vod.mapper.SubjectMapper;
import com.simon.model.vod.Subject;
import com.simon.vo.vod.SubjectEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubjectListener extends AnalysisEventListener<SubjectEeVo> {

    @Autowired
    private SubjectMapper subjectMapper;

    /**
     * 一行一行解析excel内容，把每行内容封装到 user 对象
     * 默认从excel第二行开始读取，因为第一行是表头
     * @param subjectEeVo
     * @param analysisContext
     */
    @Override
    public void invoke(SubjectEeVo subjectEeVo, AnalysisContext analysisContext) {
        // 将每一行的数据添加到数据库中
        Subject subject = new Subject();
        BeanUtils.copyProperties(subjectEeVo,subject);
        subjectMapper.insert(subject);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
