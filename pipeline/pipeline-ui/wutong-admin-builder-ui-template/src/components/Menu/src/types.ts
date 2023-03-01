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

// import { ComputedRef } from 'vue';
// import { ThemeEnum } from '/@/enums/appEnum';
// import { MenuModeEnum } from '/@/enums/menuEnum';
export interface MenuState {
  // 默认选中的列表
  defaultSelectedKeys: string[];

  // 模式
  // mode: MenuModeEnum;

  // // 主题
  // theme: ComputedRef<ThemeEnum> | ThemeEnum;

  // 缩进
  inlineIndent?: number;

  // 展开数组
  openKeys: string[];

  // 当前选中的菜单项 key 数组
  selectedKeys: string[];

  // 收缩状态下展开的数组
  collapsedOpenKeys: string[];
}
