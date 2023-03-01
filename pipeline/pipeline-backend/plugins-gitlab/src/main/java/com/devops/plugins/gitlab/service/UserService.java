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

package com.devops.plugins.gitlab.service;

import org.gitlab4j.api.models.ImpersonationToken;
import org.gitlab4j.api.models.User;

import java.util.Date;
import java.util.List;

public interface UserService {

    /**
     * 获得当前用户信息
     *
     * @return User
     */
    User queryCurrentUser(String url, String token, String username, String password);

    /**
     * 创建用户
     *
     * @param user          User
     * @return User
     */
    User createUser(User user, String password);

    /**
     * 根据用户ID获得用户信息
     *
     * @param userId 用户id
     * @return User
     */
    User queryUserByUserId(Integer userId, String url, String token, String username, String password);

    /**
     * 根据用户名获得用户信息
     *
     * @param userName 用户名
     * @return User
     */
    User queryUserByUsername(String userName);

    /**
     * 查找所有用户
     *
     * @param perPage 每页大小
     * @param page    页数
     * @param active  是否有效
     * @return List
     */
    List<User> listUser(Integer perPage, Integer page, Boolean active);

    /**
     * 根据用户Id删除用户
     *
     * @param userId 用户Id
     */
    void deleteUserByUserId(Integer userId);


    /**
     * 创建用户的Aceess_Token
     *
     * @param userId 用户Id
     * @return ImpersonationToken
     */
    ImpersonationToken createUserAccessToken(Integer userId, String tokenName, Date date);

    /**
     * 删除用户的Aceess_Token
     *
     * @param userId
     * @param tokenId
     */
    void revokeImpersonationToken(Integer userId, Integer tokenId);

    /**
     * 获取用户的Aceess_Token
     *
     * @param userId 用户Id
     * @return List
     */
    List<ImpersonationToken> listUserAccessToken(Integer userId);

    /**
     * 根据用户Id启用用户
     *
     * @param userId 用户Id
     */
    void enabledUserByUserId(Integer userId);

    /**
     * 根据用户Id禁用用户
     *
     * @param userId 用户Id
     */
    void disEnabledUserByUserId(Integer userId);

    /**
     * 根据用户email 是否已经存在
     *
     * @param email 用户邮箱
     */
    Boolean checkEmailIsExist(String email);

    /**
     * 判断用户是否是admin
     *
     * @param userId gitlab用户id
     * @return true表示是
     */
    Boolean checkIsAdmin(Integer userId);

    /**
     * 为用户添加admin权限
     *
     * @param userId gitlab用户id
     * @return true表示加上了
     */
    Boolean setAdmin(Integer userId);

    /**
     * 删除用户admin权限
     *
     * @param userId gitlab用户id
     * @return true表示删除了
     */
    Boolean deleteAdmin(Integer userId);
}
