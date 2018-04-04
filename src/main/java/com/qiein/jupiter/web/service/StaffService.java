package com.qiein.jupiter.web.service;

import com.github.pagehelper.Page;

import java.util.List;

/**
 * 员工
 * @param <T>
 */
public interface StaffService<T> {

    Page<T> get();

}
