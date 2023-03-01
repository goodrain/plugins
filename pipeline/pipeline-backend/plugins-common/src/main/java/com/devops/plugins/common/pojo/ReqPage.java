/*
 *
 * Copyright 2023 Talkweb Co., Ltd.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * /
 */

package com.devops.plugins.common.pojo;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * api通用分页对象
 *
 * @param <T> 查询实体对象
 */
@ApiModel(description = "分页查询参数")
public class ReqPage<T> {

    @Valid
    @ApiModelProperty(notes = "查询条件", position = 1)
    private T queryVO;
    @ApiModelProperty(notes = "排序条件", position = 2)
    private List<OrderItem> orders;
    @ApiModelProperty(notes = "查询页码", example = "1", position = 3)
    private long current;
    @ApiModelProperty(notes = "分页大小", example = "10", position = 4)
    private long size;

    public ReqPage() {
        this.queryVO = null;
        this.orders = Collections.emptyList();
        this.current = 1L;
        this.size = 10L;
    }

    public T getQueryVO() {
        return queryVO;
    }

    public void setQueryVO(T queryVO) {
        this.queryVO = queryVO;
    }

    public List<OrderItem> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderItem> orders) {
        this.orders = orders;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
