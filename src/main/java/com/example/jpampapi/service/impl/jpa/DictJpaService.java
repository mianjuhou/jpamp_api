package com.example.jpampapi.service.impl.jpa;

import com.example.jpampapi.dao.repository.DictRepository;
import com.example.jpampapi.entity.Dict;
import com.example.jpampapi.fs.FsJpaService;
import com.example.jpampapi.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DictJpaService extends FsJpaService<Dict, DictRepository> implements DictService {

    @Autowired
    private DictRepository repository;

}
