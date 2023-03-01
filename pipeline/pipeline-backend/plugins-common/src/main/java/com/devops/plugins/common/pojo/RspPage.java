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


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

/**
 * 分页查询结果
 *
 * @param <T> 查询实体对象的类型
 */
@Data
@ToString
@ApiModel(description = "分页查询结果")
public class RspPage<T> {

    @ApiModelProperty(notes = "查询结果")
    protected List<T> records;
    @ApiModelProperty(notes = "总条数", example = "999")
    protected long total;
    @ApiModelProperty(notes = "当前页码", example = "1")
    private long current;
    @ApiModelProperty(notes = "分页大小", example = "10")
    private long size;

    public RspPage() {
        this.records = Collections.emptyList();
        this.total = 0L;
        this.current = 1L;
        this.size = 10L;
    }

    @ApiModelProperty(notes = "是否有上一页", example = "true")
    public boolean hasPrevious() {
        return this.current > 1L;
    }

    @ApiModelProperty(notes = "是否有下一页", example = "true")
    public boolean hasNext() {
        return this.current < this.getPages();
    }

    /**
     * 当前分页总页数
     */
    @ApiModelProperty(notes = "总分页数", example = "3")
    public long getPages() {
        if (getSize() == 0) {
            return 0L;
        }
        long pages = getTotal() / getSize();
        if (getTotal() % getSize() != 0) {
            pages++;
        }
        return pages;
    }


    /**
     * 内部什么也不干
     * <p>只是为了 json 反序列化时不报错</p>
     */
    public void setPages(long pages) {
        // to do nothing
    }
}
