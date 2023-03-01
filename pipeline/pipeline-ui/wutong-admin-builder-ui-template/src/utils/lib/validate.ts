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
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path);
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ['admin', 'editor'];
  return valid_map.indexOf(str.trim()) >= 0;
}

/**
 * @param {string} url
 * @returns {Boolean}
 */
export function validURL(url) {
  const reg =
    /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/;
  return reg.test(url);
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validLowerCase(str) {
  const reg = /^[a-z]+$/;
  return reg.test(str);
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUpperCase(str) {
  const reg = /^[A-Z]+$/;
  return reg.test(str);
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validAlphabets(str) {
  const reg = /^[A-Za-z]+$/;
  return reg.test(str);
}

/**
 * @param {string} email
 * @returns {Boolean}
 */
export function validEmail(email) {
  const reg =
    /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return reg.test(email);
}

/**
 * @param {string} phone
 * @returns {Boolean}
 */
export function validPhone(phone) {
  const reg = /^1[3-9][0-9]{9}$/;

  return reg.test(phone);
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function isString(str) {
  if (typeof str === 'string' || str instanceof String) {
    return true;
  }
  return false;
}

/**
 * @param {Array} arg
 * @returns {Boolean}
 */
export function isArray(arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]';
  }
  return Array.isArray(arg);
}
// [修改]-新增-开始
/**
 * 英文验证
 * @param min
 * @param max
 * @param value
 */
export function english(value: string, min = 6, max = 12): boolean {
  return new RegExp('^[a-z|A-Z]{' + min + ',' + max + '}$').test(value);
}

/**
 * 中文验证
 * @param min
 * @param max
 * @param value
 */
export function chinese(value: string, min = 2, max = 12): boolean {
  return new RegExp('^[\u4e00-\u9fa5]{' + min + ',' + max + '}$').test(value);
}
/**
 * 非中文
 * @param value 内容
 * @returns boolean
 */
export function notChinese(value: string): boolean {
  return !/[\u4e00-\u9fa5]/.test(value);
}
/**
 * 必需数字
 * @param min
 * @param max
 * @param value
 */
export function number(value: string, min = 1, max = 20): boolean {
  return new RegExp('^d{' + min + ',' + max + '}$').test(value);
}
/**
 * 必需小数点最大值
 * @param min
 * @param max
 * @param value
 */
export function precision(value: string, max = 8, precision = 8): boolean {
  return new RegExp(
    '(^[0-9]{1,' + max + '}$)|(^[0-9]{1,' + max + '}[.]{1}[0-9]{1,' + precision + '}$)',
  ).test(value);
}
/**
 * 复杂密码验证
 * @param value
 */
export function pwd(value: string): boolean {
  if (value && value.length > 15) {
    const en = /[a-z]/.test(value);
    const num = /[0-9]/.test(value);
    const daxie = /[A-Z]/.test(value);
    const teshu = /[~!@#$%^&*()_+=-\[\]\\,.\/;':{}]/.test(value);
    return en && num && daxie && teshu;
  }
  return false;
}
// [修改]-新增-结束
