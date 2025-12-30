package cn.iocoder.yudao.module.system.service.datasync.converter;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

import java.math.BigDecimal;

public class BooleanConverter implements Converter<Boolean> {

    @Override
    public Boolean convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (cellData.getData() ==  null) return Boolean.FALSE;
        return BigDecimal.ONE.compareTo(cellData.getNumberValue()) == 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public WriteCellData<?> convertToExcelData(Boolean value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return value ? new WriteCellData(BigDecimal.ONE) : new WriteCellData(BigDecimal.ZERO);
    }
}
