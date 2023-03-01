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

const TurboType = {
  SEQUENCE_FLOW: 1,
  START_EVENT: 2,
  END_EVENT: 3,
  USER_TASK: 4,
  SERVICE_TASK: 5,
  EXCLUSIVE_GATEWAY: 6,
};

function convertFlowElementToEdge(element) {
  const { incoming, outgoing, properties, key } = element;
  const { text, startPoint, endPoint, pointsList, logicFlowType } = properties;
  const edge = {
    id: key,
    type: logicFlowType,
    sourceNodeId: incoming[0],
    targetNodeId: outgoing[0],
    text,
    startPoint,
    endPoint,
    pointsList,
    properties: {},
  };
  const excludeProperties = ['startPoint', 'endPoint', 'pointsList', 'text', 'logicFlowType'];
  Object.keys(element.properties).forEach((property) => {
    if (excludeProperties.indexOf(property) === -1) {
      edge.properties[property] = element.properties[property];
    }
  });
  return edge;
}

function convertFlowElementToNode(element) {
  const { properties, key } = element;
  const { x, y, text, logicFlowType } = properties;
  const node = {
    id: key,
    type: logicFlowType,
    x,
    y,
    text,
    properties: {},
  };
  const excludeProperties = ['x', 'y', 'text', 'logicFlowType'];
  Object.keys(element.properties).forEach((property) => {
    if (excludeProperties.indexOf(property) === -1) {
      node.properties[property] = element.properties[property];
    }
  });
  return node;
}

export function toLogicFlowData(data) {
  const lfData: {
    // TODO type
    nodes: any[];
    edges: any[];
  } = {
    nodes: [],
    edges: [],
  };
  const list = data.flowElementList;
  list &&
    list.length > 0 &&
    list.forEach((element) => {
      if (element.type === TurboType.SEQUENCE_FLOW) {
        const edge = convertFlowElementToEdge(element);
        lfData.edges.push(edge);
      } else {
        const node = convertFlowElementToNode(element);
        lfData.nodes.push(node);
      }
    });
  return lfData;
}
