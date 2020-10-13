package com.example.jpampapi.entity;

import com.cpit.udip.common.db.NumberQueryCondition;
import com.cpit.udip.common.db.PageQueryCondition;
import com.example.jpampapi.fs.KeywordQueryCondition;
import lombok.Data;

@Data
public class DictQueryCondition extends PageQueryCondition {
    private String name;
    private String value;
    private NumberQueryCondition deleteFlag;
    private KeywordQueryCondition keyword1;
    private KeywordQueryCondition keyword2;
}
