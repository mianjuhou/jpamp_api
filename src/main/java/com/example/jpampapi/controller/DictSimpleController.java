package com.example.jpampapi.controller;

import com.example.jpampapi.service.impl.jpa.DictJpaService;
import com.example.jpampapi.service.impl.mp.DictMpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("dict")
public class DictSimpleController {
    @Resource
    private DictMpService mpService;

    @Autowired
    private DictJpaService jpaService;

//    /**
//     * 新增
//     */
//    @PostMapping("")
//    public FsResult<Dict> create(@RequestBody Dict data) {
//        ServiceUtil.setSnowflakeValue2NullProperty(data, Dict.KEY_PROPERTY);
//        return FsResult.success("创建成功", mpService.create(data));
//    }
//
//    @PostMapping("/batch")
//    public FsResult<Dict> batchCreate(@RequestBody List<Dict> datas) {
//        try {
//            return FsResult.success("批量创建全部成功", jpaService.batchCreateOnTransaction(datas));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return FsResult.error("批量创建全部失败");
//        }
//    }
//
//    /**
//     * 删除
//     */
//    @DeleteMapping("/{id}")
//    public FsResult<Dict> deleteById(@PathVariable Long id) {
//        return FsResult.success("逻辑删除成功", mpService.logicDelete(id));
//    }
//
//    @DeleteMapping("/batch")
//    public FsResult<Dict> batchDelete(@RequestBody List<Long> ids) {
//        try {
//            return FsResult.success("批量逻辑删除全部成功", mpService.batchLogicDeleteOnTransaction(ids));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return FsResult.error("批量逻辑删除全部失败");
//        }
//    }
//
//    @DeleteMapping("/condition")
//    public FsResult<Dict> batchDeleteWithCondition(DictQueryCondition condition) {
//        PageResult<Dict> pageResult = mpService.queryByQueryCondition(Dict.class, condition);
//        List<Long> ids = pageResult.getItems().stream().map(dict -> dict.getId()).collect(Collectors.toList());
//        try {
//            return FsResult.success("批量逻辑删除全部成功", mpService.batchLogicDeleteOnTransaction(ids));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return FsResult.error("批量逻辑删除全部失败");
//        }
//    }
//
//    @DeleteMapping("/example")
//    public FsResult<Dict> batchDeleteWithExample(Dict example) {
//        List<Dict> result = jpaService.findByExample(example);
//        List<Long> ids = result.stream().map(dict -> dict.getId()).collect(Collectors.toList());
//        try {
//            return FsResult.success("批量逻辑删除全部成功", mpService.batchLogicDeleteOnTransaction(ids));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return FsResult.error("批量逻辑删除全部失败");
//        }
//    }
//
//    /**
//     * 更新
//     */
//    @PatchMapping("/{id}")
//    public Dict update(@RequestBody Dict data) {
//        return null;
//    }
//
//    @PatchMapping("/batch")
//    public List<Dict> batchUpdate(@RequestBody List<Dict> datas) {
//        return null;
//    }
//
//    @PatchMapping("/condition")
//    public List<Dict> conditionUpdate(@RequestBody DataCondition<Dict, DictQueryCondition> dataCondition) {
//        return null;
//    }
//
//    @PatchMapping("/example")
//    public List<Dict> exampleUpdate(@RequestBody DataCondition<Dict, Dict> dataExample) {
//        return null;
//    }
//
//
//    /**
//     * 查询
//     */
//    @GetMapping("/{id}")
//    public FsResult<Dict> findById(@PathVariable Long id) {
//        Dict dict = jpaService.findByIdPhysical(id);
//        return FsResult.success("查询成功", dict);
//    }
//
//    @GetMapping("")
//    public FsResult<Dict> findAll() {
//        return FsResult.success("查询成功", jpaService.findAll());
//    }
//
//    @GetMapping("/ids")
//    public FsResult<Dict> queryByIds(String ids) {
//        List<Long> idList = Stream.of(ids.split(",")).map(sid -> Long.parseLong(sid)).collect(Collectors.toList());
//        return FsResult.success("查询成功", mpService.queryIds(idList));
//    }
//
//    @PostMapping("/queryIds")
//    public FsResult<Dict> queryByIds(@RequestBody List<Long> ids) {
//        return FsResult.success("查询成功", mpService.queryIds(ids));
//    }
//
//    @PostMapping("/queryCondition")
//    public FsResult<Dict> queryByCondition(@RequestBody DictQueryCondition condition) {
//        NumberQueryCondition deleteCondition = new NumberQueryCondition();
//        deleteCondition.setInValue("[0,1]");
//        condition.setDeleteFlag(deleteCondition);
//        PageResult<Dict> result = mpService.queryByQueryCondition(Dict.class, condition);
//        return FsResult.success("查询成功", result.getItems(), result.getTotal());
//    }
//
//    @PostMapping("/queryExample")
//    public FsResult<Dict> queryByExample(Dict data, @RequestParam(value = "pageSize", required = false) Integer pageSize, @RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "sort", required = false) String sort) {
//        Page<Dict> result = jpaService.queryExample(data, pageSize, pageNum, sort);
//        return FsResult.success("查询成功", result.getContent(), result.getTotalElements());
//    }
}
