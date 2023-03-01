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

import { defineComponent, Transition, TransitionGroup } from 'vue';
import { getSlot } from '/@/utils/helper/tsxHelper';

type Mode = 'in-out' | 'out-in' | 'default' | undefined;

export function createSimpleTransition(name: string, origin = 'top center 0', mode?: Mode) {
  return defineComponent({
    name,
    props: {
      group: {
        type: Boolean as PropType<boolean>,
        default: false,
      },
      mode: {
        type: String as PropType<Mode>,
        default: mode,
      },
      origin: {
        type: String as PropType<string>,
        default: origin,
      },
    },
    setup(props, { slots, attrs }) {
      const onBeforeEnter = (el: HTMLElement) => {
        el.style.transformOrigin = props.origin;
      };

      return () => {
        const Tag = !props.group ? Transition : TransitionGroup;
        return (
          <Tag name={name} mode={props.mode} {...attrs} onBeforeEnter={onBeforeEnter}>
            {() => getSlot(slots)}
          </Tag>
        );
      };
    },
  });
}
export function createJavascriptTransition(
  name: string,
  functions: Recordable,
  mode: Mode = 'in-out',
) {
  return defineComponent({
    name,
    props: {
      mode: {
        type: String as PropType<Mode>,
        default: mode,
      },
    },
    setup(props, { attrs, slots }) {
      return () => {
        return (
          <Transition
            name={name}
            mode={props.mode}
            {...attrs}
            onBeforeEnter={functions.beforeEnter}
            onEnter={functions.enter}
            onLeave={functions.leave}
            onAfterLeave={functions.afterLeave}
            onLeaveCancelled={functions.afterLeave}
          >
            {() => getSlot(slots)}
          </Transition>
        );
      };
    },
  });
}
