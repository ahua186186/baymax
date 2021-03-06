package com.tongbanjie.baymax.router.strategy;

import java.util.List;

/**
 * Created by sidawei on 16/3/20.
 *
 * 分区表配置的基本信息
 */
public class PartitionTableMetaData {

    // 以下属性通过配置注入

    /*---------------------------------------------------------------*/

    protected String logicTableName;		    // 逻辑表明

    protected String namePatten;

    protected boolean disableFullScan;		    // 关闭全表扫描

    protected PartitionTableRule rule;          // 分区规则

    protected PartitionTableNodeMapping nodeMapping; //表名到数据库的映射

    // 以下属性通过注入的属性计算得到

    /*---------------------------------------------------------------*/

    protected List<PartitionColumn> columns;    // 分区键

    protected String[] partitionColumns;		// 分区键

    protected String prefix;				    // 物理表明格式化模式trade_order_

    protected int suffixLength;				    // 后缀的位数

    /*---------------------------------------------------------------*/

    public String format(String suffix){
        return prefix + suffix;
    }

    /**
     * 根据int类型的suffix获取固定长度的完整表明后缀。
     * @param suffix
     * @return
     */
    public String getSuffix(int suffix){
        String sfx = String.valueOf(suffix);
        if(sfx.length() > suffixLength){
            throw new RuntimeException("suffix is too long then config "+suffix);
        }
        while(sfx.length() < suffixLength){
            sfx = "0"+sfx;
        }
        return sfx;
    }


    /*--------------------------------init-------------------------------*/

    //trade_order_{00}
    public void initPatten() {
        // TODO 校验
        int start = namePatten.indexOf("{");
        int end = namePatten.indexOf("}");
        this.prefix = namePatten.substring(0, start);
        this.suffixLength = namePatten.substring(start+1, end).length();
    }

    protected void initColumns(){
        if(columns == null || columns.size() == 0){
            throw new RuntimeException(String.format("columns must not be empty! strategy{%s}", logicTableName));
        }
        partitionColumns = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            partitionColumns[i] = columns.get(i).getName();
        }
        if(this.partitionColumns == null || this.partitionColumns.length == 0){
            throw new RuntimeException(String.format("partitionColumns must not be empty! strategy{%s}", this.logicTableName));
        }
    }

    /*---------------------------------get set------------------------------*/
    public String getLogicTableName() {
        return logicTableName;
    }

    public void setLogicTableName(String logicTableName) {
        this.logicTableName = logicTableName;
    }

    public String getNamePatten() {
        return namePatten;
    }

    public void setNamePatten(String namePatten) {
        this.namePatten = namePatten;
        // init
        initPatten();
    }

    public boolean isDisableFullScan() {
        return disableFullScan;
    }

    public void setDisableFullScan(boolean disableFullScan) {
        this.disableFullScan = disableFullScan;
    }

    public String[] getPartitionColumns() {
        return partitionColumns;
    }

    public PartitionTableRule getRule() {
        return rule;
    }

    public List<PartitionColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<PartitionColumn> columns) {
        this.columns = columns;
        initColumns();
    }

    public PartitionTableNodeMapping getNodeMapping() {
        return nodeMapping;
    }

    public void setNodeMapping(PartitionTableNodeMapping nodeMapping) {
        this.nodeMapping = nodeMapping;
    }

    public void setRule(PartitionTableRule rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('\n');
        sb.append(String.format("--[name:%s] [namePatten:%s] [disableFullScan:%s]", logicTableName, namePatten, disableFullScan)).append('\n');
        if (rule == null){
            sb.append(String.format("--[rule:%s] ", "null")).append('\n');
        }else {
            sb.append(String.format("--[rule:%s] ", rule.toString())).append('\n');
        }
        if (nodeMapping == null){
            sb.append(String.format("--[nodeMapping:%s]", "null")).append('\n');
        }else {
            sb.append(String.format("--[nodeMapping:%s]", nodeMapping.toString())).append('\n');
        }
        return sb.toString();
    }
}
