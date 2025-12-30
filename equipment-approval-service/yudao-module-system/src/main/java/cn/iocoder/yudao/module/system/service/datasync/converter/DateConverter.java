package cn.iocoder.yudao.module.system.service.datasync.converter;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.converters.ReadConverterContext;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateConverter implements Converter<LocalDateTime> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    @Override
    public LocalDateTime convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (cellData.getData() == null) {
            return LocalDateTime.now();
        }
        Object data = cellData.getData();
        return LocalDateTime.from(formatter.parse(data.toString()));
    }
}
