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

// 代码质量
// bugs 等级
export function bugGrade(val) {
  return (
    {
      0: 'A',
      1: 'A',
      2: 'B',
      3: 'C',
      4: 'D',
      5: 'E',
      6: 'E',
      7: 'E',
    }[parseInt(val)] || ''
  );
}
// bug展示颜色
export function bugGradeColor(val) {
  return (
    {
      0: '#06C77F',
      1: '#06C77F',
      2: '#40a9ff',
      3: '#ffbf77',
      4: '#DC5050',
      5: '#ff4d4f',
      6: '#ff4d4f',
      7: '#ff4d4f',
      //6: "error",
    }[parseInt(val)] || 'error'
  );
}

export function dateFilter(num) {
  const day = 8 * 60; // 1天8小时，
  const value = (num / day).toFixed(2);
  return value;
}
