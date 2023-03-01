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

export interface Options {
  show?: boolean;
  imageList: string[];
  index?: number;
  scaleStep?: number;
  defaultWidth?: number;
  maskClosable?: boolean;
  rememberState?: boolean;
  onImgLoad?: ({ index: number, url: string, dom: HTMLImageElement }) => void;
  onImgError?: ({ index: number, url: string, dom: HTMLImageElement }) => void;
}

export interface Props {
  show: boolean;
  instance: Props;
  imageList: string[];
  index: number;
  scaleStep: number;
  defaultWidth: number;
  maskClosable: boolean;
  rememberState: boolean;
}

export interface PreviewActions {
  resume: () => void;
  close: () => void;
  prev: () => void;
  next: () => void;
  setScale: (scale: number) => void;
  setRotate: (rotate: number) => void;
}

export interface ImageProps {
  alt?: string;
  fallback?: string;
  src: string;
  width: string | number;
  height?: string | number;
  placeholder?: string | boolean;
  preview?:
    | boolean
    | {
        visible?: boolean;
        onVisibleChange?: (visible: boolean, prevVisible: boolean) => void;
        getContainer: string | HTMLElement | (() => HTMLElement);
      };
}

export type ImageItem = string | ImageProps;
