package org.franco.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorySheetVo {

    @ExcelProperty("Name")
    private String name;
    @ExcelProperty("Description")
    private String description;
    @ExcelProperty("Status")
    private String status;
}
