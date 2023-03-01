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

import type { PropType, CSSProperties } from 'vue';
import type { ModalWrapperProps } from './typing';
import { ButtonProps } from 'ant-design-vue/es/button/buttonTypes';
import { useI18n } from '/@/hooks/web/useI18n';

const { t } = useI18n();

export const modalProps = {
  visible: { type: Boolean },
  scrollTop: { type: Boolean, default: true },
  height: { type: Number },
  minHeight: { type: Number },
  // open drag
  draggable: { type: Boolean, default: true },
  centered: { type: Boolean },
  cancelText: { type: String, default: t('common.cancelText') },
  okText: { type: String, default: t('common.okText') },

  closeFunc: Function as PropType<() => Promise<boolean>>,
};

export const basicProps = Object.assign({}, modalProps, {
  defaultFullscreen: { type: Boolean },
  // Can it be full screen
  canFullscreen: { type: Boolean, default: true },
  // After enabling the wrapper, the bottom can be increased in height
  wrapperFooterOffset: { type: Number, default: 0 },
  // Warm reminder message
  helpMessage: [String, Array] as PropType<string | string[]>,
  // Whether to setting wrapper
  useWrapper: { type: Boolean, default: true },
  loading: { type: Boolean },
  loadingTip: { type: String },
  /**
   * @description: Show close button
   */
  showCancelBtn: { type: Boolean, default: true },
  /**
   * @description: Show confirmation button
   */
  showOkBtn: { type: Boolean, default: true },

  wrapperProps: Object as PropType<Partial<ModalWrapperProps>>,

  afterClose: Function as PropType<() => Promise<VueNode>>,

  bodyStyle: Object as PropType<CSSProperties>,

  closable: { type: Boolean, default: true },

  closeIcon: Object as PropType<VueNode>,

  confirmLoading: { type: Boolean },

  destroyOnClose: { type: Boolean },

  footer: Object as PropType<VueNode>,

  getContainer: Function as PropType<() => any>,

  mask: { type: Boolean, default: true },

  maskClosable: { type: Boolean, default: true },
  keyboard: { type: Boolean, default: true },

  maskStyle: Object as PropType<CSSProperties>,

  okType: { type: String, default: 'primary' },

  okButtonProps: Object as PropType<ButtonProps>,

  cancelButtonProps: Object as PropType<ButtonProps>,

  title: { type: String },

  visible: { type: Boolean },

  width: [String, Number] as PropType<string | number>,

  wrapClassName: { type: String },

  zIndex: { type: Number },
});
