package com.example.jpampapi.service.impl.mp;

import com.example.jpampapi.dao.mapper.DictMapper;
import com.example.jpampapi.entity.Dict;
import com.example.jpampapi.fs.FsMpService;
import com.example.jpampapi.service.DictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DictMpService extends FsMpService<DictMapper, Dict> implements DictService {

    @Resource
    private DictMapper dictMapper;

}
