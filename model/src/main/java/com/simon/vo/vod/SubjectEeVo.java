package com.simon.vo.vod;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * <p>
 * Dict
 * </p>
 *
 * @author qy
 */
@Data
public class SubjectEeVo {

	// 设置excel表头，index=0代表是第一列
	@ExcelProperty(value = "id" ,index = 0)
	private Long id;

	@ExcelProperty(value = "课程分类名称" ,index = 1)
	private String title;

	@ExcelProperty(value = "上级id" ,index = 2)
	private Long parentId;

	@ExcelProperty(value = "排序" ,index = 3)
	private Integer sort;


}

