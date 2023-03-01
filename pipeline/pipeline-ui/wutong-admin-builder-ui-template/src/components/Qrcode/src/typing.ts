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

import type { QRCodeSegment, QRCodeRenderersOptions } from 'qrcode';

export type ContentType = string | QRCodeSegment[];

export type { QRCodeRenderersOptions };

export type LogoType = {
  src: string;
  logoSize: number;
  borderColor: string;
  bgColor: string;
  borderSize: number;
  crossOrigin: string;
  borderRadius: number;
  logoRadius: number;
};

export interface RenderQrCodeParams {
  canvas: any;
  content: ContentType;
  width?: number;
  options?: QRCodeRenderersOptions;
  logo?: LogoType | string;
  image?: HTMLImageElement;
  downloadName?: string;
  download?: boolean | Fn;
}

export type ToCanvasFn = (options: RenderQrCodeParams) => Promise<unknown>;

export interface QrCodeActionType {
  download: (fileName?: string) => void;
}

export interface QrcodeDoneEventParams {
  url: string;
  ctx?: CanvasRenderingContext2D | null;
}
