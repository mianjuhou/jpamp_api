package com.example.jpampapi.controller;

import com.cpit.udip.common.db.ModifyResultData;
import com.cpit.udip.common.db.NumberQueryCondition;
import com.cpit.udip.common.db.Reflections;
import com.cpit.udip.common.db.ServiceUtil;
import com.cpit.udip.common.utils.core.result.PageResult;
import com.example.jpampapi.entity.Dict;
import com.example.jpampapi.entity.DictQueryCondition;
import com.example.jpampapi.fs.DataCondition;
import com.example.jpampapi.fs.FsResult;
import com.example.jpampapi.fs.KeywordQueryCondition;
import com.example.jpampapi.service.impl.jpa.DictJpaService;
import com.example.jpampapi.service.impl.mp.DictMpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * (Dict)表控制层
 *
 * @author makejava
 * @since 2020-09-29 14:27:21
 */
@Slf4j
@RestController
@RequestMapping("dict")
public class DictController {
    @Resource
    private DictMpService mpService;

    @Autowired
    private DictJpaService jpaService;

    /**
     * 物理新增3个
     */
    @PostMapping("")
    public FsResult<Dict> create(@RequestBody Dict data) {
        ServiceUtil.setSnowflakeValue2NullProperty(data, Dict.KEY_PROPERTY);
        return FsResult.success("创建成功", mpService.create(data));
    }

    @PostMapping("/batchTransaction")
    public FsResult<Dict> batchCreateWithTransaction(@RequestBody List<Dict> datas) {
        try {
            return FsResult.success("批量创建全部成功", jpaService.batchCreateOnTransaction(datas));
        } catch (Exception e) {
            e.printStackTrace();
            return FsResult.error("批量创建全部失败");
        }
    }

    @PostMapping("/batchEffort")
    public FsResult batchCreateWithEffort(@RequestBody List<Dict> datas) {
        List<Dict> retDatas = mpService.batchCreateWithBestEffort(datas);
        if (datas.size() > retDatas.size()) {
            return FsResult.error("批量创建部分失败", retDatas);
        } else {
            return FsResult.success("批量创建全部成功", retDatas);
        }
    }

    /**
     * 逻辑新增 3个
     */
    @PostMapping("/logic")
    public FsResult<Dict> createOnLogic(@RequestBody Dict data) {
        return FsResult.success("逻辑创建成功", jpaService.logicCreate(data));
    }

    @PostMapping("/logic/batchTransaction")
    public FsResult<Dict> batchCreateWithTransactionOnLogic(@RequestBody List<Dict> datas) {
        try {
            return FsResult.success("批量逻辑创建全部成功", jpaService.batchCreateWithTransactionOnLogic(datas));
        } catch (Exception e) {
            e.printStackTrace();
            return FsResult.error("批量逻辑创建全部失败");
        }
    }

    @PostMapping("/logic/batchEffort")
    public FsResult<Dict> batchCreateWithEffortOnLogic(@RequestBody List<Dict> datas) {
        List<Dict> retDatas = mpService.batchCreateWithBestEffort(datas);
        if (datas.size() > retDatas.size()) {
            return FsResult.error("逻辑批量创建部分失败", retDatas);
        } else {
            return FsResult.success("逻辑批量创建全部成功", retDatas);
        }
    }

    /**
     * 逻辑删除 7个
     */
    @DeleteMapping("/{id}")
    public FsResult<Dict> deleteById(@PathVariable Long id) {
        return FsResult.success("逻辑删除成功", mpService.logicDelete(id));
    }

    @DeleteMapping("/batchTransaction")
    public FsResult<Dict> batchDeleteWithTransaction(@RequestBody List<Long> ids) {
        try {
            return FsResult.success("批量逻辑删除全部成功", mpService.batchLogicDeleteOnTransaction(ids));
        } catch (Exception e) {
            e.printStackTrace();
            return FsResult.error("批量逻辑删除全部失败");
        }
    }

    @DeleteMapping("/batchEffort")
    public FsResult<Dict> batchDeleteWithEffort(@RequestBody List<Long> ids) {
        List retDatas = mpService.batchLogicDeleteWithBestEffort(ids);
        if (ids.size() > retDatas.size()) {
            return FsResult.error("批量逻辑删除部分失败", retDatas);
        } else {
            return FsResult.success("批量逻辑删除全部成功", retDatas);
        }
    }

    @DeleteMapping("/conditionTransaction")
    public FsResult<Dict> batchDeleteWithConditionTransaction(DictQueryCondition condition) {
        PageResult<Dict> pageResult = mpService.queryByQueryCondition(Dict.class, condition);
        List<Long> ids = pageResult.getItems().stream().map(dict -> dict.getId()).collect(Collectors.toList());
        return batchDeleteWithTransaction(ids);
    }

    @DeleteMapping("/conditionEffort")
    public FsResult<Dict> batchDeleteWithConditionEffort(DictQueryCondition condition) {
        PageResult<Dict> pageResult = mpService.queryByQueryCondition(Dict.class, condition);
        List<Long> ids = pageResult.getItems().stream().map(dict -> dict.getId()).collect(Collectors.toList());
        return batchDeleteWithEffort(ids);
    }

    @DeleteMapping("/exampleTransaction")
    public FsResult<Dict> batchDeleteWithExampleTransaction(Dict example) {
        List<Dict> result = jpaService.findByExample(example);
        List<Long> ids = result.stream().map(dict -> dict.getId()).collect(Collectors.toList());
        return batchDeleteWithTransaction(ids);
    }

    @DeleteMapping("/exampleEffort")
    public FsResult<Dict> batchDeleteWithExampleEffort(Dict example) {
        List<Dict> result = jpaService.findByExample(example);
        List<Long> ids = result.stream().map(dict -> dict.getId()).collect(Collectors.toList());
        return batchDeleteWithEffort(ids);
    }

    /**
     * 物理删除 7个
     */
    @DeleteMapping("/physical/{id}")
    public FsResult<Dict> deleteOnPhysical(@PathVariable Long id) {
        return FsResult.success("物理删除成功", mpService.delete(id));
    }

    @DeleteMapping("/physical/batchTransaction")
    public FsResult<Dict> batchDeleteWithTransactionPhysical(@RequestBody List<Long> ids) {
        try {
            return FsResult.success("批量物理删除全部成功", mpService.batchDeleteOnTransaction(ids));
        } catch (Exception e) {
            e.printStackTrace();
            return FsResult.error("批量物理删除全部失败");
        }
    }

    @DeleteMapping("/physical/batchEffort")
    public FsResult<Dict> batchDeleteWithEffortPhysical(@RequestBody List<Long> ids) {
        List retDatas = mpService.batchDeleteWithBestEffort(ids);
        if (ids.size() > retDatas.size()) {
            return FsResult.error("批量逻辑删除部分失败", retDatas);
        } else {
            return FsResult.success("批量逻辑删除全部成功", retDatas);
        }
    }

    @DeleteMapping("/physical/conditionTransaction")
    public FsResult<Dict> batchDeleteWithConditionTransactionPhysical(DictQueryCondition condition) {
        PageResult<Dict> pageResult = mpService.queryByQueryCondition(Dict.class, condition);
        List<Long> ids = pageResult.getItems().stream().map(dict -> dict.getId()).collect(Collectors.toList());
        return batchDeleteWithTransactionPhysical(ids);
    }

    @DeleteMapping("/physical/conditionEffort")
    public FsResult<Dict> batchDeleteWithConditionEffortPhysical(DictQueryCondition condition) {
        PageResult<Dict> pageResult = mpService.queryByQueryCondition(Dict.class, condition);
        List<Long> ids = pageResult.getItems().stream().map(dict -> dict.getId()).collect(Collectors.toList());
        return batchDeleteWithTransactionPhysical(ids);
    }

    @DeleteMapping("/physical/exampleTransaction")
    public FsResult<Dict> batchDeleteWithExampleTransactionPhysical(Dict example) {
        List<Dict> result = jpaService.findByExample(example);
        List<Long> ids = result.stream().map(dict -> dict.getId()).collect(Collectors.toList());
        return batchDeleteWithTransactionPhysical(ids);
    }

    @DeleteMapping("/physical/exampleEffort")
    public FsResult<Dict> batchDeleteWithExampleEffortPhysical(Dict example) {
        List<Dict> result = jpaService.findByExample(example);
        List<Long> ids = result.stream().map(dict -> dict.getId()).collect(Collectors.toList());
        return batchDeleteWithTransactionPhysical(ids);
    }

    /**
     * 全量更新 7个
     */
    @PutMapping("/{id}")
    public FsResult<Dict> update(@PathVariable Long id, @RequestBody Dict data) {
        Reflections.setFieldValue(data, Dict.KEY_PROPERTY, id);
        try {
            ModifyResultData<Dict> retData = mpService.modify(data);
            return FsResult.success("修改成功", (Dict) retData.getOldData());
        } catch (Exception e) {
            e.printStackTrace();
            return FsResult.error("修改失败");
        }
    }

    @PutMapping("/batchTransaction")
    public FsResult<Dict> batchUpdateByTransaction(@RequestBody List<Dict> datas) {
        try {
            mpService.batchUpdateWithTransaction(datas);
            return FsResult.success("批量修改全部成功");
        } catch (Exception e) {
            e.printStackTrace();
            return FsResult.error("批量修改全部失败");
        }
    }

    @PutMapping("/batchEffort")
    public FsResult<Dict> batchUpdateByEffort(@RequestBody List<Dict> datas) {
        List<ModifyResultData<Dict>> retDatas = mpService.batchUpdateWithBestEffort(datas);
        List<Dict> retDict = retDatas.stream().map(m -> (Dict) m.getOldData()).collect(Collectors.toList());
        if (datas.size() > retDict.size()) {
            return FsResult.error("批量修改部分失败", retDict);
        } else {
            return FsResult.success("批量修改全部成功", retDict);
        }
    }

    @PutMapping("/conditionTransaction")
    public FsResult<Dict> conditionUpdateByTransaction(@RequestBody DataCondition<Dict, DictQueryCondition> dataCondition) {
        PageResult<Dict> retDatas = mpService.queryByQueryCondition(Dict.class, dataCondition.getCondition());
        try {
            mpService.batchUpdateWithTransaction(retDatas.getItems());
            return FsResult.success("复杂条件修改全部成功");
        } catch (Exception e) {
            e.printStackTrace();
            return FsResult.error("复杂条件修改全部失败");
        }
    }

    @PutMapping("/conditionEffort")
    public FsResult<Dict> conditionUpdateByEffort(@RequestBody DataCondition<Dict, DictQueryCondition> dataCondition) {
        PageResult<Dict> retPage = mpService.queryByQueryCondition(Dict.class, dataCondition.getCondition());
        List<ModifyResultData<Dict>> retDatas = mpService.batchUpdateWithBestEffort(retPage.getItems());
        List<Dict> retDict = retDatas.stream().map(m -> (Dict) m.getOldData()).collect(Collectors.toList());
        if (retPage.getItems().size() > retDict.size()) {
            return FsResult.error("复杂条件修改部分失败", retDict);
        } else {
            return FsResult.success("复杂条件修改全部成功", retDict);
        }
    }

    @PutMapping("/exampleTransaction")
    public FsResult<Dict> exampleUpdateByTransaction(@RequestBody DataCondition<Dict, Dict> dataExample) {
        List<Dict> retDatas = jpaService.findByExample(dataExample.getCondition());
        try {
            mpService.batchUpdateWithTransaction(retDatas);
            return FsResult.success("简单条件修改全部成功");
        } catch (Exception e) {
            e.printStackTrace();
            return FsResult.error("简单条件修改全部失败");
        }
    }

    @PutMapping("/exampleEffort")
    public FsResult<Dict> exampleUpdateByEffort(@RequestBody DataCondition<Dict, Dict> dataExample) {
        List<Dict> retDatas = jpaService.findByExample(dataExample.getCondition());
        List<ModifyResultData<Dict>> retModify = mpService.batchUpdateWithBestEffort(retDatas);
        List<Dict> retDict = retModify.stream().map(m -> (Dict) m.getOldData()).collect(Collectors.toList());
        if (retDatas.size() > retDict.size()) {
            return FsResult.error("简单条件修改部分失败", retDict);
        } else {
            return FsResult.success("简单条件修改全部成功", retDict);
        }
    }

    /**
     * 非空更新 7个
     */
    @PatchMapping("/{id}")
    public Dict updateNoNull(@RequestBody Dict data) {
        return null;
    }

    @PatchMapping("/batchTransaction")
    public List<Dict> batchUpdateNoNullByTransaction(@RequestBody List<Dict> datas) {
        return null;
    }

    @PatchMapping("/batchEffort")
    public List<Dict> batchUpdateNoNullByEffort(@RequestBody List<Dict> datas) {
        return null;
    }

    @PatchMapping("/conditionTransaction")
    public List<Dict> conditionUpdateNoNullByTransaction(@RequestBody DataCondition<Dict, DictQueryCondition> dataCondition) {
        return null;
    }

    @PatchMapping("/conditionEffort")
    public List<Dict> conditionUpdateNoNullByEffort(@RequestBody DataCondition<Dict, DictQueryCondition> dataCondition) {
        return null;
    }

    @PatchMapping("/exampleTransaction")
    public List<Dict> exampleUpdateNoNullByTransaction(@RequestBody DataCondition<Dict, Dict> dataExample) {
        return null;
    }

    @PatchMapping("/exampleEffort")
    public List<Dict> exampleUpdateNoNullByEffort(@RequestBody DataCondition<Dict, Dict> dataExample) {
        return null;
    }

    /**
     * 物理查询 6个
     */
    @GetMapping("/physical/{id}")
    public FsResult<Dict> findByIdPhysical(@PathVariable Long id) {
        Dict dict = jpaService.findByIdPhysical(id);
        return FsResult.success("查询成功", dict);
    }

    @GetMapping("/physicalAll")
    public FsResult<Dict> findAllPhysical() {
        return FsResult.success("查询成功", jpaService.findAll());
    }

    @GetMapping("/physicalIds")
    public FsResult<Dict> queryByIdsPhysical(String ids) {
        List<Long> idList = Stream.of(ids.split(",")).map(sid -> Long.parseLong(sid)).collect(Collectors.toList());
        return FsResult.success("查询成功", mpService.queryIds(idList));
    }

    @PostMapping("/physicalQueryIds")
    public FsResult<Dict> queryByIdsPhysical(@RequestBody List<Long> ids) {
        return FsResult.success("查询成功", mpService.queryIds(ids));
    }

    @PostMapping("/physicalQueryCondition")
    public FsResult<Dict> queryByConditionPhysical(@RequestBody DictQueryCondition condition) {
        NumberQueryCondition deleteCondition = new NumberQueryCondition();
        deleteCondition.setInValue("[0,1]");
        condition.setDeleteFlag(deleteCondition);
        PageResult<Dict> result = mpService.queryByQueryCondition(Dict.class, condition);
        return FsResult.success("查询成功", result.getItems(), result.getTotal());
    }

    @PostMapping("/physicalQueryExample")
    public FsResult<Dict> queryByExamplePhysical(Dict data, @RequestParam(value = "pageSize", required = false) Integer pageSize, @RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "sort", required = false) String sort) {
        Page<Dict> result = jpaService.queryExample(data, pageSize, pageNum, sort);
        return FsResult.success("查询成功", result.getContent(), result.getTotalElements());
    }

    /**
     * 逻辑查询 6个 考虑删除标志
     */
    @GetMapping("/{id}")
    public FsResult<Dict> findById(@PathVariable Long id) {
        Dict dict = jpaService.findById(id);
        return FsResult.success("查询成功", dict);
    }

    @GetMapping("")
    public FsResult<Dict> findAll() {
        return FsResult.success("查询成功", jpaService.findAllLogic());
    }

    @GetMapping("/ids")
    public FsResult<Dict> queryByIds(String ids) {
        List<Long> idList = Stream.of(ids.split(",")).map(sid -> Long.parseLong(sid)).collect(Collectors.toList());
        return FsResult.success("查询成功", mpService.queryByIdsLogic(idList));
    }

    @PostMapping("/queryIds")
    public FsResult<Dict> queryByIds(@RequestBody List<Long> ids) {
        return FsResult.success("查询成功", mpService.queryByIdsLogic(ids));
    }

    @PostMapping("/queryCondition")
    public FsResult<Dict> queryByCondition(@RequestBody DictQueryCondition condition) {
        KeywordQueryCondition keyword1 = new KeywordQueryCondition();
        keyword1.setKeyValue("%元%");
        keyword1.setColumnNames("[name,value,type,remark]");
        condition.setKeyword1(keyword1);
        KeywordQueryCondition keyword2 = new KeywordQueryCondition();
        keyword2.setKeyValue("%1%");
        keyword2.setColumnNames("[name,value,type,remark]");
        condition.setKeyword2(keyword2);
        PageResult<Dict> result = mpService.queryByQueryCondition(Dict.class, condition);
        return FsResult.success("查询成功", result.getItems(), result.getTotal());
    }

    @PostMapping("/queryExample")
    public FsResult<Dict> queryByExample(Dict data, @RequestParam(value = "pageSize", required = false) Integer pageSize, @RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "sort", required = false) String sort) {
        data.setDeleteFlag(0);
        Page<Dict> result = jpaService.queryExample(data, pageSize, pageNum, sort);
        return FsResult.success("查询成功", result.getContent(), result.getTotalElements());
    }


}