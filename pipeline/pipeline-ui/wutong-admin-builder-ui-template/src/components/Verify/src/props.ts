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
export const basicProps = {
  value: {
    type: Boolean as PropType<boolean>,
    default: false,
  },

  isSlot: {
    type: Boolean as PropType<boolean>,
    default: false,
  },

  text: {
    type: [String] as PropType<string>,
    default: t('component.verify.dragText'),
  },
  successText: {
    type: [String] as PropType<string>,
    default: t('component.verify.successText'),
  },
  height: {
    type: [Number, String] as PropType<number | string>,
    default: 40,
  },

  width: {
    type: [Number, String] as PropType<number | string>,
    default: 220,
  },

  circle: {
    type: Boolean as PropType<boolean>,
    default: false,
  },

  wrapStyle: {
    type: Object as PropType<any>,
    default: {},
  },
  contentStyle: {
    type: Object as PropType<any>,
    default: {},
  },
  barStyle: {
    type: Object as PropType<any>,
    default: {},
  },
  actionStyle: {
    type: Object as PropType<any>,
    default: {},
  },
};

export const rotateProps = {
  ...basicProps,
  src: {
    type: String as PropType<string>,
  },

  imgWidth: {
    type: Number as PropType<number>,
    default: 260,
  },

  imgWrapStyle: {
    type: Object as PropType<any>,
    default: {},
  },

  minDegree: {
    type: Number as PropType<number>,
    default: 90,
  },

  maxDegree: {
    type: Number as PropType<number>,
    default: 270,
  },

  diffDegree: {
    type: Number as PropType<number>,
    default: 20,
  },
};
