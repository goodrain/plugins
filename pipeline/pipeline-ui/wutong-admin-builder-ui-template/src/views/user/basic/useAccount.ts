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

/**
 * 账户信息
 */
import { ref } from 'vue';
import { useUserStore } from '/@/store/modules/user';
import { useMessage } from '/@/hooks/web/useMessage';
import { getUserInfo, updateUser as _updateUser } from '/@/api/user/userApi';

const { createMessage } = useMessage();

export const loading = ref(false);
export const userInfo: any = ref({});

export function getUser() {
  const userStore = useUserStore();
  const user = userStore.getUserInfo;
  userInfo.value = user;
  loading.value = true;
  return new Promise((reslove) => {
    getUserInfo(user.id)
      .then((data) => {
        userInfo.value = data;
        reslove(userInfo);
      })
      .finally(() => {
        loading.value = false;
      });
  });
}
export function updateUser(user) {
  loading.value = true;
  _updateUser(user)
    .then(() => {
      createMessage.success('更新成功！');
      getUser();
    })
    .finally(() => {
      loading.value = false;
    });
}
