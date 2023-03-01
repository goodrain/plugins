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

package com.devops.plugins.core.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devops.plugins.common.exception.BusinessRuntimeException;
import com.devops.plugins.common.pojo.ReqPage;
import com.devops.plugins.common.result.ABizCode;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sheep
 */
public class ConvertUtils {
    private ConvertUtils() {
    }

    /**
     * This is a method to convert an object to destination object.
     * It's only for the simple object that can use
     * {@link BeanUtils#copyProperties(Object, Object)} method.
     * And the destination class should have the non-parameter constructor.
     * {@link Class#newInstance()} method is used.
     *
     * @param source           the source object
     * @param destinationClass the destination class
     * @param <S>              the source type
     * @param <D>              the destination type
     * @return destination object
     */
    public static <S, D> D convertObject(S source, Class<D> destinationClass) {
        if (source == null) {
            return null;
        }
        try {
            D destination = destinationClass.newInstance();
            BeanUtils.copyProperties(source, destination);
            return destination;
        } catch (Exception e) {
            throw new BusinessRuntimeException(ABizCode.FAIL, destinationClass.getName());
        }
    }

    /**
     * This is a method to convert an object to destination object.
     * It's only for the simple object that can use
     * {@link BeanUtils#copyProperties(Object, Object)} method.
     * And the destination class should have the non-parameter constructor.
     * {@link Class#newInstance()} method is used.
     *
     * @param source           the source object
     * @param converter converter for list content of page
     * @param <S>              the source type
     * @param <D>              the destination type
     * @return destination object
     */
    public static <S, D> D convertObject(S source, Function<S, D> converter) {
        if (source == null) {
            return null;
        }
        D destination = converter.apply(source);
        return destination;
    }

    /**
     * convert page with special converter
     *
     * @param source    source page
     * @param converter converter for list content of page
     * @param <S>       the source content type
     * @param <D>       the destination content type
     * @return destination page
     */
    public static <S, D> Page<D> convertPage(Page<S> source, Function<S, D> converter) {
        if (source == null) {
            return null;
        }
        Page<D> destination = new Page<>();
        BeanUtils.copyProperties(source, destination, "content");
        if (source.getRecords() != null) {
            destination.setRecords(source.getRecords().stream().map(converter).collect(Collectors.toList()));
        } else {
            destination.setRecords(new ArrayList<>());
        }
        return destination;
    }


    /**
     * convert page with default beanUtils
     *
     * @param source source page
     * @param <S>    the source content type
     * @param <D>    the destination content type
     * @return destination page
     */
    public static <S, D> Page<D> convertPage(Page<S> source, Class<D> destinationClass) {
        if (source == null) {
            return null;
        }
        Page<D> destination = new Page<>();
        BeanUtils.copyProperties(source, destination, "content");
        if (source.getRecords() != null) {
            destination.setRecords(source.getRecords().stream().map(s -> convertObject(s, destinationClass)).collect(Collectors.toList()));
        }
        return destination;
    }

    /**
     * convert page with default beanUtils
     *
     * @param source source page
     * @param <S>    the source content type
     * @param <D>    the destination content type
     * @return destination page
     */
    public static <S, D> Page<D> convertReqPage(ReqPage<S> source, Page<D> destination) {
        if (source == null) {
            return null;
        }
        BeanUtils.copyProperties(source, destination, "content");
        return destination;
    }

    /**
     * convert List with default beanUtils
     *
     * @param source source page
     * @param <S>    the source content type
     * @param <D>    the destination content type
     * @return destination page
     */
    public static <S, D> List<D> convertList(Collection<S> source, Class<D> destinationClass) {
        if (source == null) {
            return new ArrayList<>();
        }
        if (source.isEmpty()) {
            return new ArrayList<>();
        }
        return source.stream().map(s -> convertObject(s, destinationClass)).collect(Collectors.toList());
    }


    /**
     * convert List with special converter
     *
     * @param source source page
     * @param <S>    the source content type
     * @param <D>    the destination content type
     * @return destination page
     */
    public static <S, D> List<D> convertList(Collection<S> source, Function<S, D> converter) {
        if (source == null) {
            return new ArrayList<>();
        }
        if (source.isEmpty()) {
            return new ArrayList<>();
        }
        return source.stream().map(converter).collect(Collectors.toList());
    }
}
