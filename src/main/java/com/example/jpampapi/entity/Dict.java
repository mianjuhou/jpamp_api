package com.example.jpampapi.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cpit.udip.common.db.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * (Dict)表实体类
 *
 * @author fangdean
 * @since 2020-09-29 14:27:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@Entity
@Table(name = "dict")
@TableName("dict")
public class Dict extends BaseEntity<Dict> {

    /**
     * 显示名称
     */
    public static final String ENTITY_DISPLAY_NAME = "字典数据";

    /**
     * 主键属性
     */
    public static final String KEY_PROPERTY = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Column(name = "name")
    @TableField("name")
    private String name;

    @Column(name = "value")
    @TableField("value")
    private String value;

    @Column(name = "type")
    @TableField("type")
    private Integer type;

    @Column(name = "remark")
    @TableField("remark")
    private String remark;
}