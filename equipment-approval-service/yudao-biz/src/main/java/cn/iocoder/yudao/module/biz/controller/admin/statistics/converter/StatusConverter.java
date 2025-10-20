package cn.iocoder.yudao.module.biz.controller.admin.statistics.converter;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

public class StatusConverter implements Converter<Integer> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }



    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (1 == value) {
            return new WriteCellData<>("有效");
        } else if (2 == value) {
            return new WriteCellData<>("已注销");
        } else if (3 == value){
            return new WriteCellData<>("已变更");
        }
        return new WriteCellData<>("未知");
    }
}
