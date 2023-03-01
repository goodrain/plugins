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

import type { PropType } from 'vue';

import { useI18n } from '/@/hooks/web/useI18n';
const { t } = useI18n();

export const footerProps = {
  confirmLoading: { type: Boolean },
  /**
   * @description: Show close button
   */
  showCancelBtn: { type: Boolean, default: true },
  cancelButtonProps: Object as PropType<Recordable>,
  cancelText: { type: String, default: t('common.cancelText') },
  /**
   * @description: Show confirmation button
   */
  showOkBtn: { type: Boolean, default: true },
  okButtonProps: Object as PropType<Recordable>,
  okText: { type: String, default: t('common.okText') },
  okType: { type: String, default: 'primary' },
  showFooter: { type: Boolean },
  footerHeight: {
    type: [String, Number] as PropType<string | number>,
    default: 60,
  },
};
export const basicProps = {
  isDetail: { type: Boolean },
  title: { type: String, default: '' },
  loadingText: { type: String },
  showDetailBack: { type: Boolean, default: true },
  visible: { type: Boolean },
  loading: { type: Boolean },
  maskClosable: { type: Boolean, default: true },
  getContainer: {
    type: [Object, String] as PropType<any>,
  },
  closeFunc: {
    type: [Function, Object] as PropType<any>,
    default: null,
  },
  destroyOnClose: { type: Boolean },
  ...footerProps,
};
