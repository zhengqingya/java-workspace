# EasyExcel

- https://github.com/alibaba/easyexcel
- https://easyexcel.opensource.alibaba.com

EasyExcel是一个基于Java的简单、省内存的读写Excel的开源项目。在尽可能节约内存的情况下支持读写百M的Excel。

### 导出

excel支持的单sheet页最大行数为1048575行

解决：超过最大行数后新建一个sheet页进行存储

```shell
java.lang.IllegalArgumentException: Invalid row number (1048576) outside allowable range (0..1048575)

	at org.apache.poi.xssf.streaming.SXSSFSheet.createRow(SXSSFSheet.java:123)
	at org.apache.poi.xssf.streaming.SXSSFSheet.createRow(SXSSFSheet.java:65)
```