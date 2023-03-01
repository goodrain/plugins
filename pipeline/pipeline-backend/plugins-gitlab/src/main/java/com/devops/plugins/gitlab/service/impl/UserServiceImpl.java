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

package com.devops.plugins.gitlab.service.impl;

import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.result.ABizCode;
import com.devops.plugins.gitlab.config.Gitlab4jClient;
import com.devops.plugins.gitlab.service.UserService;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.UserApi;
import org.gitlab4j.api.models.ImpersonationToken;
import org.gitlab4j.api.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private Gitlab4jClient gitlab4jclient;

    public UserServiceImpl(Gitlab4jClient gitlab4jclient) {
        this.gitlab4jclient = gitlab4jclient;
    }

    @Override
    public User createUser(User user, String password) {
        try {
            return gitlab4jclient.getGitLabApi()
                    .getUserApi()
                    .createUser(user, password, false);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public List<User> listUser(Integer perPage, Integer page, Boolean active) {
        try {
            UserApi userApi = gitlab4jclient.getGitLabApi().getUserApi();
            return active
                    ? userApi.getActiveUsers(page, perPage)
                    : userApi.getUsers(page, perPage);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public User queryCurrentUser(String url, String token, String username, String password) {
        try {
            return gitlab4jclient.getGitLabApi(url, token, username, password, null).getUserApi().getCurrentUser();
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public User queryUserByUserId(Integer userId, String url, String token, String username, String password) {
        try {
            return gitlab4jclient.getGitLabApi(url, token, username, password, null)
                    .getUserApi().getUser(userId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL,e.getMessage(), e);
        }
    }

    @Override
    public User queryUserByUsername(String userName) {
        try {
            return gitlab4jclient.getGitLabApi()
                    .getUserApi()
                    .getUser(userName);
        } catch (GitLabApiException e) {
            return null;
        }
    }

    @Override
    public void deleteUserByUserId(Integer userId) {
        try {
            UserApi userApi = gitlab4jclient.getGitLabApi().getUserApi();
            userApi.deleteUser(userId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }


    @Override
    public void enabledUserByUserId(Integer userId) {
        UserApi userApi = gitlab4jclient.getGitLabApi().getUserApi();
        try {
            userApi.unblockUser(userId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL,e.getMessage(), e);
        }
    }

    @Override
    public void disEnabledUserByUserId(Integer userId) {
        UserApi userApi = gitlab4jclient.getGitLabApi().getUserApi();
        try {
            userApi.blockUser(userId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public Boolean checkEmailIsExist(String email) {
        try {
            List<User> users = gitlab4jclient.getGitLabApi().getUserApi().findUsers(email);
            if (!users.isEmpty()) {
                return true;
            }
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Boolean checkIsAdmin(Integer userId) {
        try {
            User user = gitlab4jclient.getGitLabApi().getUserApi().getUser(userId);
            return user != null && Boolean.TRUE.equals(user.getIsAdmin());
        } catch (GitLabApiException e) {
            LOGGER.info("Exception occurred when querying the user with id {}", userId);
            LOGGER.info("The exception is {}", e);
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean setAdmin(Integer userId) {
        if (userId == null) {
            LOGGER.warn("The id for the user to be set as admin is null. Abort.");
            return Boolean.FALSE;
        }
        User user = new User();
        user.setId(userId);
        user.setIsAdmin(Boolean.TRUE);
        try {
            gitlab4jclient.getGitLabApi().getUserApi().modifyUser(user, null, null);
        } catch (GitLabApiException e) {
            LOGGER.info("Exception occurred when setting the user with id {} to be admin", userId);
            LOGGER.info("The exception is {}", e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteAdmin(Integer userId) {
        if (userId == null) {
            LOGGER.warn("The id for the user whose admin role is to be deleted is null. Abort.");
            return Boolean.FALSE;
        }
        User user = new User();
        user.setId(userId);
        user.setIsAdmin(Boolean.FALSE);
        try {
            gitlab4jclient.getGitLabApi().getUserApi().modifyUser(user, null, null);
        } catch (GitLabApiException e) {
            LOGGER.info("Exception occurred when deleting the admin role for the user with id {}", userId);
            LOGGER.info("The exception is {}", e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public ImpersonationToken createUserAccessToken(Integer userId, String tokenName, Date date) {
        UserApi userApi = gitlab4jclient.getGitLabApi().getUserApi();
        try {
            User user = userApi.getUser(userId);
            if (user == null) {
                throw new BusinessRuntimeException(ABizCode.FAIL, "user not exist");
            }
            if (StringUtils.isEmpty(tokenName)) {
                tokenName = "myToken";
            }
            return userApi.createImpersonationToken(
                        user.getId(),
                        tokenName,
                        date,
                        ImpersonationToken.Scope.values());
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL,e.getMessage(), e);
        }
    }

    @Override
    public void revokeImpersonationToken(Integer userId, Integer tokenId) {
        UserApi userApi = gitlab4jclient.getGitLabApi().getUserApi();
        try {
            User user = userApi.getUser(userId);
            if (user == null) {
                throw new BusinessRuntimeException(ABizCode.FAIL, "user not exist");
            }
            userApi.revokeImpersonationToken(
                    user.getId(),
                    tokenId);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }

    @Override
    public List<ImpersonationToken> listUserAccessToken(Integer userId) {
        UserApi userApi = gitlab4jclient.getGitLabApi().getUserApi();
        try {
            User user = userApi.getUser(userId);
            if (user == null) {
                throw new BusinessRuntimeException(ABizCode.FAIL, "user not exist");
            }
            return userApi.getImpersonationTokens(user.getId(), Constants.ImpersonationState.ACTIVE);
        } catch (GitLabApiException e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, e.getMessage(), e);
        }
    }
}
