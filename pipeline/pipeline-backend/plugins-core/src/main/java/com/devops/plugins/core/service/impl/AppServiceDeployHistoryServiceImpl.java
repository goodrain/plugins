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

package com.devops.plugins.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.devops.plugins.common.pojo.ReqPage;
import com.devops.plugins.core.dao.AppServiceDeployHistoryMapper;
import com.devops.plugins.core.dao.entity.AppServiceDeployHistoryEntity;
import com.devops.plugins.core.pojo.vo.req.appServiceDeployHistory.AppServiceDeployPageReq;
import com.devops.plugins.core.pojo.vo.resp.appServiceDeployHistory.AppServiceDeployPageResp;
import com.devops.plugins.core.service.IAppServiceDeployHistoryService;
import com.devops.plugins.core.utils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Service服务实现类
 * </p>
 *
 * @author sheep
 * @Date 2022-03-03 17:12:42
 */
@Service
@Transactional
public class AppServiceDeployHistoryServiceImpl extends ServiceImpl<AppServiceDeployHistoryMapper, AppServiceDeployHistoryEntity> implements IAppServiceDeployHistoryService {

    @Resource
    private AppServiceDeployHistoryMapper appServiceDeployHistoryMapper;

    @Override
    public Long findCount(AppServiceDeployHistoryEntity query) {
        return appServiceDeployHistoryMapper.findCount(query);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppServiceDeployPageResp> findPage(AppServiceDeployPageReq query, ReqPage<AppServiceDeployPageReq> page) {
        Page<AppServiceDeployPageResp> result = new Page<>();
        Page<AppServiceDeployHistoryEntity> appServiceDeployHistoryEntityPage = appServiceDeployHistoryMapper.findPage(query, ConvertUtils.convertReqPage(page, new Page<>()));
        List<AppServiceDeployHistoryEntity> appServiceDeployHistoryEntities = appServiceDeployHistoryEntityPage.getRecords();
        if (CollectionUtils.isEmpty(appServiceDeployHistoryEntities)) {
            return result;
        }
        appServiceDeployHistoryEntityPage.setRecords(appServiceDeployHistoryEntities.stream().sorted(Comparator.comparing(AppServiceDeployHistoryEntity::getCreateTime).reversed()).collect(Collectors.toList()));
        return ConvertUtils.convertPage(appServiceDeployHistoryEntityPage, AppServiceDeployPageResp.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppServiceDeployHistoryEntity> findList(AppServiceDeployHistoryEntity query) {
        return appServiceDeployHistoryMapper.findList(query);
    }

    @Override
    public boolean updateByPrimaryKeySelective(AppServiceDeployHistoryEntity entity) {
        return SqlHelper.retBool(appServiceDeployHistoryMapper.updateByPrimaryKeySelective(entity));
    }
}
