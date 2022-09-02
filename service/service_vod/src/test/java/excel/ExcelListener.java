package excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class ExcelListener  extends AnalysisEventListener<User> {

    /**
     * 一行一行解析excel内容，把每行内容封装到 user 对象
     * 默认从excel第二行开始读取，因为第一行是表头
     * @param user
     * @param analysisContext
     */
    @Override
    public void invoke(User user, AnalysisContext analysisContext) {
        System.out.println(user);
    }

    /**
     * 读取表头内容
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头：" + headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
