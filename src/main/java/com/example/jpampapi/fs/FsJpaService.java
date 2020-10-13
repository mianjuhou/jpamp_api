package com.example.jpampapi.fs;

import com.cpit.udip.common.db.BaseEntity;
import com.cpit.udip.common.db.BaseJpaService;
import com.cpit.udip.common.db.BaseRepository;
import com.cpit.udip.common.db.QueryCondition;
import com.cpit.udip.common.utils.core.result.PageResult;
import com.github.wenhao.jpa.Specifications;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FsJpaService<T extends BaseEntity, R extends BaseRepository> extends BaseJpaService<T, R> {

    @Autowired
    private R repository;

    public T logicCreate(T data) {
        //先拷贝除了ID创时修饰属性外的其他属性
        //再设置删除标志位1
        //再Example查询
        //如果为空物理新增
        //如果非空修改时间和删除标志
        T targetData = (T) new Object();
        BeanUtils.copyProperties(data, targetData, "gmt_created", "gmt_modified", "id");
        targetData.setDeleteFlag(1);
        Example<T> example = Example.of(data);
        List<T> list = repository.findAll(example);
        if (list == null || list.isEmpty()) {
            return create(data);
        } else {
            T dbData = list.get(0);
            dbData.setDeleteFlag(0);
            dbData.setGmtCreated(new Date());
            dbData.setGmtModified(new Date());
            return updateEntity(dbData);
        }
    }

    public List<T> batchCreateWithTransactionOnLogic(List<T> datas) {
        List<T> optDatas = new ArrayList<>();
        for (T data : datas) {
            T targetData = (T) new Object();
            BeanUtils.copyProperties(data, targetData, "gmt_created", "gmt_modified", "id");
            targetData.setDeleteFlag(1);
            Example<T> example = Example.of(data);
            List<T> list = repository.findAll(example);
            if (list == null || list.isEmpty()) {
                data.setDeleteFlag(0);
                data.setGmtCreated(new Date());
                data.setGmtModified(data.getGmtCreated());
                optDatas.add(data);
            } else {
                T dbData = list.get(0);
                dbData.setDeleteFlag(0);
                dbData.setGmtCreated(new Date());
                dbData.setGmtModified(dbData.getGmtCreated());
                optDatas.add(dbData);
            }
        }
        return optDatas.stream().map(data -> (T) repository.save(data)).collect(Collectors.toList());
    }

    public List<T> batchCreateOnTransaction(List<T> dataList) {
        return dataList.stream().map(data -> create(data)).collect(Collectors.toList());
    }

    @Override
    public PageResult<T> queryByQueryCondition(Class<T> clazz, QueryCondition queryCondition) {
        return super.queryByQueryCondition(clazz, queryCondition);
    }

    public Page<T> queryExample(T data, Integer pageSize, Integer pageNum, String sort) {
        Example<T> example = Example.of(data);
        Page<T> pageResult;
        List<Sort.Order> orderList = new ArrayList<>();
        if (!StringUtils.isEmpty(sort)) {
            String[] split = sort.split(",");
            for (int i = 0; i < split.length; i = i + 2) {
                String proper = split[i];
                String direct = i + 1 >= split.length ? "" : split[i + 1].toLowerCase();
                if ("desc".equals(direct)) {
                    orderList.add(Sort.Order.desc(proper));
                } else if ("asc".equals(direct)) {
                    orderList.add(Sort.Order.asc(proper));
                }
            }
        } else {
            orderList.add(Sort.Order.desc("gmtCreated"));
        }
        if (pageSize == null || pageSize <= 0 || pageNum == null || pageNum < 0) {
            List<T> list;
            if (orderList.isEmpty()) {
                list = repository.findAll(example);
            } else {
                list = repository.findAll(example, Sort.by(orderList));
            }
            pageResult = new PageImpl<>(list);
        } else {
            PageRequest pageRequest;
            if (orderList.isEmpty()) {
                pageRequest = PageRequest.of(pageNum, pageSize);
            } else {
                pageRequest = PageRequest.of(pageNum, pageSize, Sort.by(orderList));
            }
            pageResult = repository.findAll(example, pageRequest);
        }
        return pageResult;
    }

    public List<T> findByExample(T example) {
        List<T> list = repository.findAll(Example.of(example));
        return list;
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public List<T> findAllLogic() {
        return repository.findAll(Specifications.and().eq("deleteFlag", 0).build());
    }

    public T findByIdPhysical(Long id) {
        return (T) repository.findById(id).get();
    }
}
