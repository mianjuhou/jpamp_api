package com.example.jpampapi.fs;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cpit.udip.common.db.*;
import com.cpit.udip.common.utils.core.result.PageResult;
import com.cpit.udip.common.utils.core.util.ReflectUtil;
import com.cpit.udip.common.utils.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class FsMpService<M extends BaseMapper<T>, T extends BaseEntity<T>> extends BaseMpService<M, T> {
    private static final Logger log = LoggerFactory.getLogger(FsMpService.class);
    @Autowired
    private M dao;

    public List<T> batchLogicDeleteOnTransaction(List<Long> ids) {
        return ids.stream().map(id -> logicDelete(id)).collect(Collectors.toList());
    }

    public List<T> batchDeleteOnTransaction(List<Long> ids) {
        return ids.stream().map(id -> delete(id)).collect(Collectors.toList());
    }

    @Override
    public PageResult<T> queryByQueryCondition(Class<T> clazz, QueryCondition queryCondition) {
        T entity = ReflectUtil.newInstance(clazz, new Object[0]);
        Object deleteFlagObj = Reflections.getFieldValue(queryCondition, "deleteFlag");
        if (deleteFlagObj != null) {
            NumberQueryCondition deleteFlag = (NumberQueryCondition) deleteFlagObj;
            if (deleteFlag.getOriginalValue() != null || deleteFlag.getInValue() != null || (deleteFlag.getMaxValue() != null && deleteFlag.getMinValue() != null)) {
                entity.setDeleteFlag(null);
            }
        }
        QueryWrapper<T> queryWrapper = new QueryWrapper(entity);
        setQueryWrapperByQueryCondition(queryCondition, entity, queryWrapper);
        if (queryCondition instanceof PageQueryCondition) {
            PageQueryCondition pageQueryCondition = (PageQueryCondition) queryCondition;
            if (pageQueryCondition.getPageIndex() != null && pageQueryCondition.getPageSize() != null && pageQueryCondition.getPageIndex() >= 1 && pageQueryCondition.getPageSize() >= 1) {
                Page<T> page = new Page();
                PageQueryParam pageQueryParam = new PageQueryParam(pageQueryCondition.getPageIndex(), pageQueryCondition.getPageSize(), pageQueryCondition.getSortOrders(), pageQueryCondition.getSortProperties());
                ServiceUtil.fillPagePropertiesByPageQueryParam(clazz, page, pageQueryParam);
                if (log.isDebugEnabled()) {
                    log.debug("$$$ CustomSqlSegment = [{}]", queryWrapper.getCustomSqlSegment());
                }
                IPage<T> selectPage = dao.selectPage(page, queryWrapper);
                return new PageResult(selectPage.getTotal(), selectPage.getRecords());
            } else {
                PageQueryParam pageQueryParam = new PageQueryParam(pageQueryCondition.getSortOrders(), pageQueryCondition.getSortProperties());
                ServiceUtil.handleQueryWrapperOrderFields(queryWrapper, pageQueryParam);
                if (log.isDebugEnabled()) {
                    log.debug("$$$ CustomSqlSegment = [{}]", queryWrapper.getCustomSqlSegment());
                }

                List<T> list = this.dao.selectList(queryWrapper);
                return new PageResult((long) list.size(), list);
            }
        } else {
            List<T> list = this.dao.selectList(queryWrapper);
            return new PageResult((long) list.size(), list);
        }
    }

    private void setQueryWrapperByQueryCondition(QueryCondition queryCondition, BaseEntity<T> entity, QueryWrapper<T> queryWrapper) {
        List<Field> queryConditionFieldList = Reflections.getAllFieldsList(queryCondition.getClass());
        Iterator var5 = queryConditionFieldList.iterator();
        List<KeywordQueryCondition> keywordQueryConditions = new ArrayList<>();
        while (true) {
            while (true) {
                Field field;
                String fieldName;
                Object fieldValue;
                Field entityField;
                do {
                    do {
                        if (!var5.hasNext()) {
                            if (!keywordQueryConditions.isEmpty()) {
                                handleKeywordCondition(queryWrapper, keywordQueryConditions);
                            }
                            return;
                        }
                        field = (Field) var5.next();
                        fieldName = field.getName();
                        fieldValue = Reflections.getFieldValue(queryCondition, fieldName);
                    } while (fieldValue == null);
                    if (fieldValue.getClass().equals(KeywordQueryCondition.class)) {
                        entityField = Reflections.getAccessibleField(queryCondition, fieldName);
                    } else {
                        entityField = Reflections.getAccessibleField(entity, fieldName);
                    }
                } while (entityField == null);

                String columnName = ServiceUtil.getFieldNameFromBaseEntityByPropertyName(entity, fieldName);
                Class entityFieldType = entityField.getType();
                Class conditionFieldType = field.getType();
                if (entityFieldType.equals(Date.class) && conditionFieldType.equals(DateQueryCondition.class)) {
                    this.handleDateCondition(queryWrapper, columnName, fieldValue);
                } else if (Number.class.isAssignableFrom(entityFieldType) && conditionFieldType.equals(NumberQueryCondition.class)) {
                    handleNumberCondition(queryWrapper, columnName, fieldValue);
                } else if (conditionFieldType.equals(KeywordQueryCondition.class)) {
                    keywordQueryConditions.add((KeywordQueryCondition) fieldValue);
                } else if (conditionFieldType.equals(entityFieldType)) {
                    if (conditionFieldType.equals(String.class)) {
                        String strValue = StrUtil.trim((String) fieldValue);
                        if (StrUtil.isNotEmpty(strValue)) {
                            handleStringCondition(queryWrapper, columnName, strValue);
                        }
                    } else {
                        Reflections.setFieldValue(entity, fieldName, fieldValue);
                    }
                }
            }
        }
    }

    private void handleKeywordCondition(QueryWrapper<T> queryWrapper, List<KeywordQueryCondition> keywordQueryConditions) {
        queryWrapper.and(tQueryWrapper -> {
            for (int i = 0; i < keywordQueryConditions.size(); i++) {
                KeywordQueryCondition keywordQueryCondition = keywordQueryConditions.get(i);
                String keyValue = (String) Reflections.getFieldValue(keywordQueryCondition, "keyValue");
                String columnNames = (String) Reflections.getFieldValue(keywordQueryCondition, "columnNames");
                if (columnNames.startsWith("[") && columnNames.endsWith("]")) {
                    List<String> columnNameList = ServiceUtil.convertStringCollectionToList(columnNames);
                    for (int j = 0; j < columnNameList.size(); j++) {
                        String columnName = columnNameList.get(j);
                        if (keyValue.startsWith("%") && keyValue.endsWith("%")) {
                            tQueryWrapper.like(columnName, keyValue.substring(1, keyValue.length() - 1));
                        } else if (keyValue.startsWith("%")) {
                            tQueryWrapper.likeLeft(columnName, keyValue.substring(1));
                        } else if (keyValue.endsWith("%")) {
                            tQueryWrapper.likeRight(columnName, keyValue.substring(0, keyValue.length() - 1));
                        } else if (keyValue.contains("%")) {
                            tQueryWrapper.like(columnName, keyValue);
                        } else {
                            tQueryWrapper.eq(columnName, keyValue);
                        }
                        if (j < columnNameList.size() - 1) {
                            tQueryWrapper.or();
                        }
                    }
                }
                if (i < keywordQueryConditions.size() - 1) {
                    tQueryWrapper.or();
                }
            }
        });

    }

    private void handleStringCondition(QueryWrapper<T> queryWrapper, String columnName, String fieldValue) {
        if (fieldValue.startsWith("[") && fieldValue.endsWith("]")) {
            List<String> valList = ServiceUtil.convertStringCollectionToList(fieldValue);
            queryWrapper.in(columnName, valList);
        } else if (fieldValue.startsWith("%") && fieldValue.endsWith("%")) {
            queryWrapper.like(columnName, fieldValue.substring(1, fieldValue.length() - 1));
        } else if (fieldValue.startsWith("%")) {
            queryWrapper.likeLeft(columnName, fieldValue.substring(1));
        } else if (fieldValue.endsWith("%")) {
            queryWrapper.likeRight(columnName, fieldValue.substring(0, fieldValue.length() - 1));
        } else if (fieldValue.contains("%")) {
            queryWrapper.like(columnName, fieldValue);
        } else {
            queryWrapper.eq(columnName, fieldValue);
        }

    }

    private void handleNumberCondition(QueryWrapper<T> queryWrapper, String columnName, Object fieldValue) {
        NumberQueryCondition numberQueryCondition = (NumberQueryCondition) fieldValue;
        if (numberQueryCondition.getOriginalValue() != null) {
            queryWrapper.eq(columnName, numberQueryCondition.getOriginalValue());
            numberQueryCondition.setInValue((String) null);
            numberQueryCondition.setMaxValue((Number) null);
            numberQueryCondition.setMinValue((Number) null);
            log.info("处理数值型条件: 字段 {} 设置相等值 {}", columnName, numberQueryCondition.getOriginalValue());
        } else if (numberQueryCondition.getMaxValue() == null && numberQueryCondition.getMinValue() == null) {
            if (StrUtil.isNotBlank(numberQueryCondition.getInValue())) {
                List<String> valList = ServiceUtil.convertStringCollectionToList(numberQueryCondition.getInValue().trim());
                Iterator var6 = valList.iterator();

                while (var6.hasNext()) {
                    String val = (String) var6.next();

                    try {
                        Double.parseDouble(val);
                    } catch (NumberFormatException var9) {
                        log.error("in 集合 {} 中存在非法数字: {}", valList, val);
                        return;
                    }
                }

                log.info("处理数值型条件: 字段 {} 设置集合值 {}", columnName, valList);
                queryWrapper.in(columnName, valList);
            }
        } else {
            if (numberQueryCondition.getMaxValue() != null) {
                queryWrapper.le(columnName, numberQueryCondition.getMaxValue());
                numberQueryCondition.setInValue((String) null);
                log.info("处理数值型条件: 字段 {} 设置最大值 {}", columnName, numberQueryCondition.getMaxValue());
            }

            if (numberQueryCondition.getMinValue() != null) {
                queryWrapper.ge(columnName, numberQueryCondition.getMinValue());
                numberQueryCondition.setInValue((String) null);
                log.info("处理数值型条件: 字段 {} 设置最小值 {}", columnName, numberQueryCondition.getMinValue());
            }
        }

    }

    private void handleDateCondition(QueryWrapper<T> queryWrapper, String columnName, Object fieldValue) {
        DateQueryCondition dateQueryCondition = (DateQueryCondition) fieldValue;
        if (!dateQueryCondition.isTimeNotLimit()) {
            if (dateQueryCondition.isQueryTimeScope()) {
                log.info("{}设置时间范围：{} - {}", new Object[]{columnName, dateQueryCondition.getStartTime(), dateQueryCondition.getEndTime()});
                Date start = dateQueryCondition.getStartTime();
                if (start != null) {
                    queryWrapper.ge(columnName, start);
                }

                Date end = dateQueryCondition.getEndTime();
                if (end != null) {
                    queryWrapper.le(columnName, end);
                }

                dateQueryCondition.setCurrentTime((Date) null);
            } else {
                log.info("{}设置精确时间条件：{} ", columnName, dateQueryCondition.getCurrentTime());
                queryWrapper.eq(columnName, dateQueryCondition.getCurrentTime());
            }

        }
    }

    public List<T> queryIds(List<Long> ids) {
        List<T> dicts = dao.selectBatchIds(ids);
        return dicts;
    }

    public List<T> queryByIdsLogic(List<Long> ids) {
        List<T> datas = dao.selectBatchIds(ids);
        return datas.stream().filter(data -> data.getDeleteFlag() == 0).collect(Collectors.toList());
    }
}
